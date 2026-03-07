package com.baby_io.baby_io_app.service;

import com.baby_io.baby_io_app.configuration.SleepEventWebSocketHandler;
import com.baby_io.baby_io_app.dto.LullabyPlayerStatusDTO;
import com.baby_io.baby_io_app.dto.SensorStatusDTO;
import com.baby_io.baby_io_app.dto.SensorValueDTO;
import com.baby_io.baby_io_app.dto.SleepEventDTO;
import com.baby_io.baby_io_app.entity.SensorConfiguration;
import com.baby_io.baby_io_app.entity.SleepEvent;
import com.baby_io.baby_io_app.entity.SleepRoutine;
import com.baby_io.baby_io_app.repository.SleepSessionRepository;
import com.baby_io.baby_io_app.types.AlertLevel;
import com.baby_io.baby_io_app.types.SensorType;
import com.baby_io.baby_io_app.types.SleepSessionEventType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class MqttService {

  // MQTT Topics
  public static final String BASE_TOPIC = "babyio";

  public static final String SENSOR_STATUS_REQ = BASE_TOPIC + "/sensor/status/req";
  public static final String SENSOR_STATUS_RES = BASE_TOPIC + "/sensor/status/res";
  public static final String SENSOR_ENABLE = BASE_TOPIC + "/sensor/enable";
  public static final String SENSOR_DISABLE = BASE_TOPIC + "/sensor/disable";
  public static final String SENSOR_RESTART = BASE_TOPIC + "/sensor/restart";
  public static final String SENSOR_VALUES_REQ = BASE_TOPIC + "/sensor/values/req";
  public static final String SENSOR_VALUES_RES = BASE_TOPIC + "/sensor/values/res";

  public static final String LULLABY_PLAYER_STATUS_REQ = BASE_TOPIC + "/lullaby-player/status/req";
  public static final String LULLABY_PLAYER_STATUS_RES = BASE_TOPIC + "/lullaby-player/status/res";
  public static final String LULLABY_PLAYER_ENABLE = BASE_TOPIC + "/lullaby-player/enable";
  public static final String LULLABY_PLAYER_DISABLE = BASE_TOPIC + "/lullaby-player/disable";
  public static final String LULLABY_PLAYER_RESTART = BASE_TOPIC + "/lullaby-player/restart";
  public static final String LULLABY_PLAYER_PLAY = BASE_TOPIC + "/lullaby-player/play";
  public static final String LULLABY_PLAYER_STOP = BASE_TOPIC + "/lullaby-player/stop";

  public static final String SLEEP_SESSION_START_REQ = BASE_TOPIC + "/sleep-session/start";
  public static final String SLEEP_SESSION_PAUSE = BASE_TOPIC + "/sleep-session/pause";
  public static final String SLEEP_SESSION_RESUME = BASE_TOPIC + "/sleep-session/resume";
  public static final String SLEEP_SESSION_TERMINATE = BASE_TOPIC + "/sleep-session/terminate";

  public static final String SLEEP_SESSION_EVENT = BASE_TOPIC + "/sleep-session/event";
  public static final String ALERT_ATTEND = BASE_TOPIC + "/sleep-session/alert/attend";

  @Value("${mqtt.broker.url:tcp://localhost:1883}")
  private String brokerUrl;

  @Value("${mqtt.client.id:BabyIO_Backend}")
  private String clientId;

  private MqttClient mqttClient;
  private ObjectMapper objectMapper;
  private SleepEventWebSocketHandler webSocketHandler;

  private final ConcurrentHashMap<String, CompletableFuture<SensorStatusDTO>> pendingSensorStatusRequests = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, CompletableFuture<Collection<SensorValueDTO>>> pendingSensorValuesRequests = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, CompletableFuture<LullabyPlayerStatusDTO>> pendingLullabyStatusRequests = new ConcurrentHashMap<>();

  private final SleepSessionService sleepSessionService;

  @Autowired
  public MqttService(SleepSessionService sleepSessionService, SleepEventWebSocketHandler webSocketHandler) {
    this.sleepSessionService = sleepSessionService;
    this.webSocketHandler = webSocketHandler;
  }

  @PostConstruct
  public void initialize() {
    this.objectMapper = new ObjectMapper();
    connectToMqttBroker();
  }

  @PreDestroy
  public void cleanup() {
    if (mqttClient != null && mqttClient.isConnected()) {
      try {
        mqttClient.disconnect();
        mqttClient.close();
      } catch (MqttException e) {
        System.err.println("Error disconnecting MQTT client: " + e.getMessage());
      }
    }
  }

  private void connectToMqttBroker() {

    try {
      mqttClient = new MqttClient(brokerUrl, clientId);
      MqttConnectOptions options = new MqttConnectOptions();
      options.setCleanSession(true);
      options.setConnectionTimeout(10);
      options.setKeepAliveInterval(60);
      options.setAutomaticReconnect(true);

      mqttClient.setCallback(new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
          System.err.println("MQTT connection lost: " + cause.getMessage());
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
          handleIncomingMessage(topic, new String(message.getPayload()));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
          // Message delivery completed
        }
      });

      mqttClient.connect(options);

      // Subscribe to response topics
      mqttClient.subscribe(SENSOR_STATUS_RES, 1);
      mqttClient.subscribe(SENSOR_VALUES_RES, 1);
      mqttClient.subscribe(LULLABY_PLAYER_STATUS_RES, 1);
      mqttClient.subscribe(SLEEP_SESSION_EVENT, 1);

      System.out.println("Connected to MQTT broker: " + brokerUrl);
    } catch (MqttException e) {
      System.err.println("Failed to connect to MQTT broker: " + e.getMessage());
      throw new RuntimeException("MQTT connection failed", e);
    }

  }

  public boolean isConnected() {
    return mqttClient != null && mqttClient.isConnected();
  }

  private void handleIncomingMessage(String topic, String payload){
    try {
      if (SENSOR_STATUS_RES.equals(topic)) {
        handleSensorStatusResponse(payload);
      } else if (SENSOR_VALUES_RES.equals(topic)) {
        handleSensorValuesResponse(payload);
      } else if (LULLABY_PLAYER_STATUS_RES.equals(topic)) {
        handleLullabyPlayerStatusResponse(payload);
      } else if (SLEEP_SESSION_EVENT.equals(topic)) {
      handleSleepSessionEvent(payload);
    }
    } catch (Exception e) {
      System.err.println("Error handling MQTT message: " + e.getMessage());
    }
  }

  private void publishMessage(String topic, String payload) {
    try {
      if (mqttClient != null && mqttClient.isConnected()) {
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(1);
        message.setRetained(false);
        mqttClient.publish(topic, message);
        System.out.println("Published to " + topic + ": " + payload);
      } else {
        System.err.println("MQTT client not connected. Cannot publish message.");
        connectToMqttBroker();
      }
    } catch (MqttException e) {
      System.err.println("Error publishing MQTT message: " + e.getMessage());
    }
  }

  public Optional<Boolean> startSleepSession(Long sessionId, SleepRoutine sleepRoutine) {
    try {
      Map<String, Object> command = new HashMap<>();
      command.put("sessionId", sessionId);
      command.put("durationMinutes", sleepRoutine.getPlannedDurationMinutes());
      command.put("enableAlerts", sleepRoutine.getEnableAlerts());
      command.put("mediumAlertTimeoutSeconds", sleepRoutine.getMediumAlertTimeoutSeconds());
      command.put("highAlertTimeoutSeconds", sleepRoutine.getHighAlertTimeoutSeconds());

      // Add sensor configurations
      List<Map<String, Object>> sensorConfigs = new ArrayList<>();
      for (SensorConfiguration config : sleepRoutine.getSensorConfigurations()) {
        Map<String, Object> sensorConfig = new HashMap<>();
        sensorConfig.put("sensorType", config.getSensorType().name().toLowerCase());
        sensorConfig.put("enabled", config.getEnabled());
        sensorConfig.put("loggingEnabled", config.getLoggingEnabled());
        sensorConfig.put("loggingIntervalMinutes", config.getLoggingIntervalMinutes());
        sensorConfig.put("mediumAlertThreshold", config.getMediumAlertThreshold());
        sensorConfig.put("highAlertThreshold", config.getHighAlertThreshold());
        sensorConfigs.add(sensorConfig);
      }
      command.put("sensorConfigurations", sensorConfigs);

      // Add lullaby player configuration
      if (sleepRoutine.getLullabyPlayerConfiguration() != null) {
        Map<String, Object> lullabyConfig = new HashMap<>();
        lullabyConfig.put("enabled", sleepRoutine.getLullabyPlayerConfiguration().getEnabled());
        lullabyConfig.put("volume", sleepRoutine.getLullabyPlayerConfiguration().getVolume());
        boolean alertLullabyEnabled = sleepRoutine.getLullabyPlayerConfiguration().getAlertLullabyEnabled();
        lullabyConfig.put("alertLullabyEnabled", alertLullabyEnabled);
        lullabyConfig.put("highAlertLullabyNumber", sleepRoutine.getLullabyPlayerConfiguration().getHighAlertLullaby().getSongNumber());
        lullabyConfig.put("mediumAlertLullabyNumber", sleepRoutine.getLullabyPlayerConfiguration().getMediumAlertLullaby().getSongNumber());
        lullabyConfig.put("periodicLullabyEnabled", sleepRoutine.getLullabyPlayerConfiguration().getPeriodicLullabyEnabled());
        lullabyConfig.put("periodicLullabyIntervalMinutes", sleepRoutine.getLullabyPlayerConfiguration().getPeriodicLullabyIntervalMinutes());
        lullabyConfig.put("periodicLullabyNumber", sleepRoutine.getLullabyPlayerConfiguration().getPeriodicLullaby().getSongNumber());
        lullabyConfig.put("wakeUpLullabyEnabled", sleepRoutine.getLullabyPlayerConfiguration().getWakeUpLullabyEnabled());
        lullabyConfig.put("wakeUpLullabyNumber", sleepRoutine.getLullabyPlayerConfiguration().getWakeUpLullaby().getSongNumber());
        command.put("lullabyPlayerConfiguration", lullabyConfig);
      }

      String payload = objectMapper.writeValueAsString(command);
      publishMessage(SLEEP_SESSION_START_REQ, payload);
      return Optional.of(true);
    } catch (Exception e) {
      System.err.println("Failed to send start sleep session command: " + e.getMessage());
      return Optional.of(false);
    }
  }

  public Optional<Boolean> terminateSleepSession() {
    try {
      publishMessage(SLEEP_SESSION_TERMINATE, "{}");
      return Optional.of(true);
    } catch (Exception e) {
      System.err.println("Failed to send terminate sleep session command: " + e.getMessage());
      return Optional.of(false);
    }
  }

  public Optional<Boolean> pauseSleepSession() {
    try {
      publishMessage(SLEEP_SESSION_PAUSE, "{}");
      return Optional.of(true);
    } catch (Exception e) {
      System.err.println("Failed to send pause sleep session command: " + e.getMessage());
      return Optional.of(false);
    }
  }

  public Optional<Boolean> resumeSleepSession() {
    try {
      publishMessage(SLEEP_SESSION_RESUME, "{}");
      return Optional.of(true);
    } catch (Exception e) {
      System.err.println("Failed to send resume sleep session command: " + e.getMessage());
      return Optional.of(false);
    }
  }

  private void handleSleepSessionEvent(String payload) {
    try {
      JsonNode eventData = objectMapper.readTree(payload);
      Long sessionId = eventData.get("sessionId").asLong();
      String eventTypeStr = eventData.get("eventType").asText();

      SleepSessionEventType eventType = SleepSessionEventType.valueOf(eventTypeStr.toUpperCase());

      SleepEvent sleepEvent = new SleepEvent();
      sleepEvent.setEventType(eventType);
      sleepEvent.setDescription(eventData.has("description") ? eventData.get("description").asText() : "");

      if (eventData.has("triggerSensorType")) {
        sleepEvent.setTriggerSensorType(SensorType.valueOf(eventData.get("triggerSensorType").asText().toUpperCase()));
      }
      if (eventData.has("alertLevel")) {
        sleepEvent.setAlertLevel(AlertLevel.valueOf(eventData.get("alertLevel").asText().toUpperCase()));
      }
      if (eventData.has("sensorValue")) {
        sleepEvent.setSensorValue(eventData.get("sensorValue").asDouble());
      }
      if (eventData.has("thresholdValue")) {
        sleepEvent.setThresholdValue(eventData.get("thresholdValue").asDouble());
      }
      if (eventData.has("resolvedAutomatically")) {
        sleepEvent.setResolvedAutomatically(eventData.get("resolvedAutomatically").asBoolean());
      }

      @SuppressWarnings("OptionalGetWithoutIsPresentCheck")
      SleepEventDTO sleepEventDTO = sleepSessionService.addSleepEventToSession(sessionId, sleepEvent).get();

      reactToSleepEvents(sessionId, eventType, sleepEventDTO);

    } catch (Exception e) {
      System.err.println("Error processing sleep session event: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void reactToSleepEvents(Long sessionId, SleepSessionEventType eventType, SleepEventDTO sleepEventDTO) {
    try {

      switch (eventType) {
        case USER_ATTENDED:
          webSocketHandler.broadcastSleepEvent(sleepEventDTO);
          break;

        case SENSOR_ALERT_TRIGGERED:
          webSocketHandler.broadcastSleepEvent(sleepEventDTO);
          break;

        case SESSION_PAUSED:
          sleepSessionService.pauseSleepSession(sessionId);
          break;

        case SESSION_RESUMED:
          sleepSessionService.resumeSleepSession(sessionId);
          break;

        case SESSION_ENDED:
          sleepSessionService.endSleepSession(sessionId);
          break;

        case SESSION_TERMINATED:
          sleepSessionService.terminateSleepSession(sessionId);
          break;

        case MUSIC_STARTED:
        case MUSIC_STOPPED:
        case PERIODIC_MUSIC_PLAYED:
        case WAKE_UP_MUSIC_PLAYED:
        case SESSION_STARTED:
      }

    } catch (Exception e) {
      System.err.println("Error reacting to sleep event: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void enableSensor(String sensorType) {
    Map<String, Object> command = Map.of("sensorType", sensorType);
    try {
      String payload = objectMapper.writeValueAsString(command);
      publishMessage(SENSOR_ENABLE, payload);
    } catch (Exception e) {
      throw new RuntimeException("Failed to send enable sensor command", e);
    }
  }

  public void disableSensor(String sensorType) {
    Map<String, Object> command = Map.of("sensorType", sensorType);
    try {
      String payload = objectMapper.writeValueAsString(command);
      publishMessage(SENSOR_DISABLE, payload);
    } catch (Exception e) {
      throw new RuntimeException("Failed to send disable sensor command", e);
    }
  }

  public void restartSensor(String sensorType) {
    Map<String, Object> command = Map.of("sensorType", sensorType);
    try {
      String payload = objectMapper.writeValueAsString(command);
      publishMessage(SENSOR_RESTART, payload);
    } catch (Exception e) {
      throw new RuntimeException("Failed to send restart sensor command", e);
    }
  }

  public SensorStatusDTO requestSensorStatus(String sensorType) {
    // Use unique key for each request to avoid race conditions
    String requestId = UUID.randomUUID().toString();
    CompletableFuture<SensorStatusDTO> future = new CompletableFuture<>();
    pendingSensorStatusRequests.put(requestId, future);

    Map<String, Object> request = Map.of(
        "sensorType", sensorType.toLowerCase(), // Send lowercase to match ESP32
        "requestId", requestId
    );

    try {
      String payload = objectMapper.writeValueAsString(request);
      publishMessage(SENSOR_STATUS_REQ, payload);
      return future.get(5, TimeUnit.SECONDS);
    } catch (Exception e) {
      pendingSensorStatusRequests.remove(requestId);
      throw new RuntimeException("Failed to request sensor status", e);
    }
  }

  private void handleSensorStatusResponse(String payload) {
    try {
      JsonNode response = objectMapper.readTree(payload);
      String sensorType = response.get("sensorType").asText();
      String requestId = response.has("requestId") ? response.get("requestId").asText() : null;

      SensorStatusDTO status = new SensorStatusDTO(
          SensorType.valueOf(sensorType.toUpperCase()), // Convert to uppercase for enum
          response.get("enabled").asBoolean(),
          response.get("connected").asBoolean()
      );

      // Complete specific request if requestId exists
      if (requestId != null && pendingSensorStatusRequests.containsKey(requestId)) {
        pendingSensorStatusRequests.get(requestId).complete(status);
        pendingSensorStatusRequests.remove(requestId);
      } else {
        // Fallback: complete all pending requests
        for (CompletableFuture<SensorStatusDTO> future : pendingSensorStatusRequests.values()) {
          if (!future.isDone()) {
            future.complete(status);
          }
        }
        pendingSensorStatusRequests.clear();
      }
    } catch (Exception e) {
      System.err.println("Error parsing sensor status response: " + e.getMessage());
      // Complete all pending requests with error
      for (CompletableFuture<SensorStatusDTO> future : pendingSensorStatusRequests.values()) {
        if (!future.isDone()) {
          future.completeExceptionally(e);
        }
      }
      pendingSensorStatusRequests.clear();
    }
  }

  public Optional<Collection<SensorValueDTO>> requestSensorValues() {
    try {
      CompletableFuture<Collection<SensorValueDTO>> future = new CompletableFuture<>();
      String requestKey = "all_values_" + System.currentTimeMillis();
      pendingSensorValuesRequests.put(requestKey, future);

      publishMessage(SENSOR_VALUES_REQ, "{}");

      Collection<SensorValueDTO> result = future.get(5, TimeUnit.SECONDS);
      return Optional.of(result);
    } catch (Exception e) {
      System.err.println("Error requesting sensor values: " + e.getMessage());
      return Optional.empty();
    }
  }

  private void handleSensorValuesResponse(String payload) {
    try {
      JsonNode response = objectMapper.readTree(payload);
      JsonNode sensorsArray = response.get("sensors");
      List<SensorValueDTO> sensorValues = new ArrayList<>();

      if (sensorsArray != null && sensorsArray.isArray()) {
        for (JsonNode sensorNode : sensorsArray) {
          String type = sensorNode.get("type").asText();
          SensorValueDTO sensorValue = new SensorValueDTO(
              SensorType.valueOf(type.toUpperCase()),
              sensorNode.get("value").asDouble()
          );
          sensorValues.add(sensorValue);
        }
      }

      for (CompletableFuture<Collection<SensorValueDTO>> future : pendingSensorValuesRequests.values()) {
        future.complete(sensorValues);
      }
      pendingSensorValuesRequests.clear();
    } catch (Exception e) {
      System.err.println("Error parsing sensor values response: " + e.getMessage());
    }
  }

  public void enableLullabyPlayer() {
    publishMessage(LULLABY_PLAYER_ENABLE, "{}");
  }

  public void disableLullabyPlayer() {
    publishMessage(LULLABY_PLAYER_DISABLE, "{}");
  }

  public void restartLullabyPlayer() {
    publishMessage(LULLABY_PLAYER_RESTART, "{}");
  }

  public void playLullaby(int songNumber) {
    Map<String, Object> command = Map.of("songNumber", songNumber);
    try {
      String payload = objectMapper.writeValueAsString(command);
      publishMessage(LULLABY_PLAYER_PLAY, payload);
    } catch (Exception e) {
      throw new RuntimeException("Failed to send play command", e);
    }
  }

  public void stopLullaby() {
    publishMessage(LULLABY_PLAYER_STOP, "{}");
  }

  public LullabyPlayerStatusDTO requestLullabyPlayerStatus() {
    try {
      CompletableFuture<LullabyPlayerStatusDTO> future = new CompletableFuture<>();
      pendingLullabyStatusRequests.put("status", future);

      publishMessage(LULLABY_PLAYER_STATUS_REQ, "{}");
      return future.get(5, TimeUnit.SECONDS);
    } catch (Exception e) {
      pendingLullabyStatusRequests.remove("status");
      System.err.println("Error requesting lullaby player status: " + e.getMessage());
      return null;
    }
  }

  private void handleLullabyPlayerStatusResponse(String payload) {
    try {
      JsonNode response = objectMapper.readTree(payload);
      LullabyPlayerStatusDTO status = new LullabyPlayerStatusDTO(
          response.get("enabled").asBoolean(),
          response.get("connected").asBoolean(),
          response.get("playing").asBoolean()
      );

      for (CompletableFuture<LullabyPlayerStatusDTO> future : pendingLullabyStatusRequests.values()) {
        future.complete(status);
      }
      pendingLullabyStatusRequests.clear();
    } catch (Exception e) {
      System.err.println("Error parsing lullaby player status response: " + e.getMessage());
    }
  }

  public void attendAlert() {
    publishMessage(ALERT_ATTEND, "{}");
  }

}