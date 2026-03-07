#include <WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include <DHT.h>
#include <MPU6050.h>
#include <Wire.h>
#include <HardwareSerial.h>
#include <DFRobotDFPlayerMini.h>

// === WiFi and MQTT Configuration ===
const char* WIFI_SSID = "UA-Alumnos";
const char* WIFI_PASSWORD = "41umn05WLC";
const char* MQTT_SERVER = "52.202.248.7";
const int MQTT_PORT = 1883;
const char* CLIENT_ID = "ESP32_BabyIO";

// === MQTT Topics ===
const char* BASE_TOPIC = "babyio";
const char* SENSOR_STATUS_REQ = "babyio/sensor/status/req";
const char* SENSOR_STATUS_RES = "babyio/sensor/status/res";
const char* SENSOR_ENABLE = "babyio/sensor/enable";
const char* SENSOR_DISABLE = "babyio/sensor/disable";
const char* SENSOR_RESTART = "babyio/sensor/restart";
const char* SENSOR_VALUES_REQ = "babyio/sensor/values/req";
const char* SENSOR_VALUES_RES = "babyio/sensor/values/res";
const char* LULLABY_PLAYER_STATUS_REQ = "babyio/lullaby-player/status/req";
const char* LULLABY_PLAYER_STATUS_RES = "babyio/lullaby-player/status/res";
const char* LULLABY_PLAYER_ENABLE = "babyio/lullaby-player/enable";
const char* LULLABY_PLAYER_DISABLE = "babyio/lullaby-player/disable";
const char* LULLABY_PLAYER_RESTART = "babyio/lullaby-player/restart";
const char* LULLABY_PLAYER_PLAY = "babyio/lullaby-player/play";
const char* LULLABY_PLAYER_STOP = "babyio/lullaby-player/stop";
const char* SLEEP_SESSION_START_REQ = "babyio/sleep-session/start";
const char* SLEEP_SESSION_PAUSE = "babyio/sleep-session/pause";
const char* SLEEP_SESSION_RESUME = "babyio/sleep-session/resume";
const char* SLEEP_SESSION_TERMINATE = "babyio/sleep-session/terminate";
const char* SLEEP_SESSION_EVENT = "babyio/sleep-session/event";
const char* ALERT_ATTEND = "babyio/sleep-session/alert/attend";

WiFiClient espClient;
PubSubClient mqttClient(espClient);

// === Hardware Pin Definitions ===
#define SOUND_SENSOR_PIN 33
#define DHT_PIN 4
#define DHT_TYPE DHT11
#define DFPLAYER_RX_PIN 16
#define DFPLAYER_TX_PIN 15

// === Sensor Objects ===
DHT dhtSensor(DHT_PIN, DHT_TYPE);
MPU6050 motionSensorHW;
HardwareSerial dfPlayerSerial(1);
DFRobotDFPlayerMini dfPlayer;

// === Sensor States ===
struct SensorState {
  bool enabled;
  bool connected;
  unsigned long lastReading;
  float lastValue;
};

// === Lullaby Player State ===
struct LullabyPlayerState {
  bool enabled;
  bool connected;
  bool playing;
  int currentVolume;
};

// === Sleep Session State ===
struct SleepSessionState {
  bool active;
  bool paused;
  unsigned long sessionId;
  unsigned long startTime;
  int durationMinutes;
  bool enableAlerts;
  int mediumAlertTimeoutSeconds;
  int highAlertTimeoutSeconds;
  bool alertLullabyEnabled;
  int highAlertLullabyNumber;
  int mediumAlertLullabyNumber;
  bool periodicLullabyEnabled;
  int periodicLullabyIntervalMinutes;
  int periodicLullabyNumber;
  bool wakeUpLullabyEnabled;
  int wakeUpLullabyNumber;
  unsigned long lastPeriodicLullaby;
};

// === Sensor Configuration ===
struct SensorConfiguration {
  bool enabled;
  bool loggingEnabled;
  int loggingIntervalMinutes;
  float mediumAlertThreshold;
  float highAlertThreshold;
};

SensorState temperatureSensor = {true, false, 0, 0};
SensorState humiditySensor = {true, false, 0, 0};
SensorState soundSensor = {true, false, 0, 0};
SensorState motionSensor = {true, false, 0, 0};
LullabyPlayerState lullabyPlayer = {false, true, false, 15};
SleepSessionState sleepSession = {false, false, 0, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0};

// Sensor configurations for sleep session
SensorConfiguration tempConfig = {true, true, 5, 25.0, 30.0};
SensorConfiguration humidityConfig = {true, true, 5, 70.0, 80.0};
SensorConfiguration soundConfig = {true, true, 1, 50.0, 80.0};
SensorConfiguration motionConfig = {true, true, 1, 30.0, 60.0};

// === System Variables ===
unsigned long lastSensorCheck = 0;
const unsigned long SENSOR_CHECK_INTERVAL = 1000; // Check sensors every 1 second
unsigned long lastTempLogging = 0;
unsigned long lastHumidityLogging = 0;
unsigned long lastSoundLogging = 0;
unsigned long lastMotionLogging = 0;

// Alert state tracking
struct AlertState {
  bool temperatureAlertActive;
  bool humidityAlertActive;
  bool soundAlertActive;
  bool motionAlertActive;
  unsigned long lastAlertTime;
};

AlertState alertState = {false, false, false, false, 0};

void setup() {
  Serial.begin(115200);

  // Initialize sensors
  initializeSensors();

  // Initialize DFPlayer
  dfPlayerSerial.begin(9600, SERIAL_8N1, DFPLAYER_RX_PIN, DFPLAYER_TX_PIN);
  Serial.println("Initializing DFPlayer...");

  if (dfPlayer.begin(dfPlayerSerial)) {
    Serial.println("DFPlayer initialized successfully");
    dfPlayer.volume(lullabyPlayer.currentVolume);
    lullabyPlayer.connected = true;
  } else {
    Serial.println("Failed to initialize DFPlayer");
    lullabyPlayer.connected = false;
  }

  // Setup WiFi and MQTT
  setupWiFi();
  setupMQTT();

  Serial.println("Baby Monitor System initialized");
}

void loop() {
  // Maintain MQTT connection
  if (!mqttClient.connected()) {
    reconnectMQTT();
  }
  mqttClient.loop();

  // Check sensors periodically
  if (millis() - lastSensorCheck > SENSOR_CHECK_INTERVAL) {
    checkSensors();
    lastSensorCheck = millis();
  }

  // Handle sleep session logic
  if (sleepSession.active && !sleepSession.paused) {
    handleSleepSessionLogic();
  }

  delay(100);
}

void initializeSensors() {
  // Initialize DHT sensor
  dhtSensor.begin();
  temperatureSensor.connected = true;
  humiditySensor.connected = true;

  // Initialize MPU6050 motion sensor
  Wire.begin();
  motionSensorHW.initialize();
  motionSensor.connected = motionSensorHW.testConnection();

  // Initialize sound sensor (analog pin)
  pinMode(SOUND_SENSOR_PIN, INPUT);
  soundSensor.connected = true;

  Serial.println("Sensors initialized");
}

void checkSensors() {
  unsigned long currentTime = millis();

  // Read temperature sensor
  if (temperatureSensor.enabled && temperatureSensor.connected) {
    float temperature = dhtSensor.readTemperature();
    if (!isnan(temperature)) {
      temperatureSensor.lastValue = temperature;
      temperatureSensor.lastReading = currentTime;
    }
  }

  // Read humidity sensor
  if (humiditySensor.enabled && humiditySensor.connected) {
    float humidity = dhtSensor.readHumidity();
    if (!isnan(humidity)) {
      humiditySensor.lastValue = humidity;
      humiditySensor.lastReading = currentTime;
    }
  }

  // Read sound sensor
  if (soundSensor.enabled && soundSensor.connected) {
    int soundRaw = analogRead(SOUND_SENSOR_PIN);
    float soundLevel = map(soundRaw, 0, 4095, 0, 100); // Convert to 0-100 range
    soundSensor.lastValue = soundLevel;
    soundSensor.lastReading = currentTime;
  }

  // Read motion sensor
  if (motionSensor.enabled && motionSensor.connected) {
    int16_t ax, ay, az, gx, gy, gz;
    motionSensorHW.getMotion6(&ax, &ay, &az, &gx, &gy, &gz);

    // Calculate motion magnitude
    float motionMagnitude = sqrt(ax*ax + ay*ay + az*az) / 16384.0 * 100; // Convert to 0-100 range
    motionSensor.lastValue = motionMagnitude;
    motionSensor.lastReading = currentTime;
  }
}

void setupWiFi() {
  delay(10);
  Serial.println("Connecting to WiFi...");
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("\nWiFi connected!");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
}

void setupMQTT() {
  mqttClient.setServer(MQTT_SERVER, MQTT_PORT);
  mqttClient.setCallback(mqttCallback);
}

void mqttCallback(char* topic, byte* payload, unsigned int length) {
    String message;
    for (int i = 0; i < length; i++) {
        message += (char)payload[i];
    }

    Serial.print("Message received [");
    Serial.print(topic);
    Serial.print("]: ");
    Serial.println(message);

    // Parse JSON message
    DynamicJsonDocument doc(2048); // Increased size for sleep session data
    DeserializationError error = deserializeJson(doc, message);

    if (error) {
        Serial.print("JSON parsing failed: ");
        Serial.println(error.c_str());
        return;
    }

    // Handle different topics
    if (strcmp(topic, SENSOR_STATUS_REQ) == 0) {
        handleSensorStatusRequest(doc);
    } else if (strcmp(topic, SENSOR_ENABLE) == 0) {
        handleSensorEnable(doc);
    } else if (strcmp(topic, SENSOR_DISABLE) == 0) {
        handleSensorDisable(doc);
    } else if (strcmp(topic, SENSOR_RESTART) == 0) {
        handleSensorRestart(doc);
    } else if (strcmp(topic, SENSOR_VALUES_REQ) == 0) {
        handleSensorValuesRequest(doc);
    } else if (strcmp(topic, LULLABY_PLAYER_STATUS_REQ) == 0) {
        handleLullabyPlayerStatusRequest(doc);
    } else if (strcmp(topic, LULLABY_PLAYER_ENABLE) == 0) {
        handleLullabyPlayerEnable(doc);
    } else if (strcmp(topic, LULLABY_PLAYER_DISABLE) == 0) {
        handleLullabyPlayerDisable(doc);
    } else if (strcmp(topic, LULLABY_PLAYER_RESTART) == 0) {
        handleLullabyPlayerRestart(doc);
    } else if (strcmp(topic, LULLABY_PLAYER_PLAY) == 0) {
        handleLullabyPlayerPlay(doc);
    } else if (strcmp(topic, LULLABY_PLAYER_STOP) == 0) {
        handleLullabyPlayerStop(doc);
    } else if (strcmp(topic, SLEEP_SESSION_START_REQ) == 0) {
        handleSleepSessionStart(doc);
    } else if (strcmp(topic, SLEEP_SESSION_PAUSE) == 0) {
        handleSleepSessionPause(doc);
    } else if (strcmp(topic, SLEEP_SESSION_RESUME) == 0) {
        handleSleepSessionResume(doc);
    } else if (strcmp(topic, SLEEP_SESSION_TERMINATE) == 0) {
        handleSleepSessionTerminate(doc);
    } else if (strcmp(topic, ALERT_ATTEND) == 0) {
        handleAlertAttend(doc);
    }
}

void reconnectMQTT() {
    while (!mqttClient.connected()) {
        Serial.print("Attempting MQTT connection...");
        if (mqttClient.connect(CLIENT_ID)) {
            Serial.println("connected");

            // Subscribe to all command topics
            mqttClient.subscribe(SENSOR_STATUS_REQ);
            mqttClient.subscribe(SENSOR_ENABLE);
            mqttClient.subscribe(SENSOR_DISABLE);
            mqttClient.subscribe(SENSOR_RESTART);
            mqttClient.subscribe(SENSOR_VALUES_REQ);
            mqttClient.subscribe(LULLABY_PLAYER_STATUS_REQ);
            mqttClient.subscribe(LULLABY_PLAYER_ENABLE);
            mqttClient.subscribe(LULLABY_PLAYER_DISABLE);
            mqttClient.subscribe(LULLABY_PLAYER_RESTART);
            mqttClient.subscribe(LULLABY_PLAYER_PLAY);
            mqttClient.subscribe(LULLABY_PLAYER_STOP);
            mqttClient.subscribe(SLEEP_SESSION_START_REQ);
            mqttClient.subscribe(SLEEP_SESSION_PAUSE);
            mqttClient.subscribe(SLEEP_SESSION_RESUME);
            mqttClient.subscribe(SLEEP_SESSION_TERMINATE);
            mqttClient.subscribe(ALERT_ATTEND);

            Serial.println("Subscribed to MQTT topics");
        } else {
            Serial.print("failed, rc=");
            Serial.print(mqttClient.state());
            Serial.println(" try again in 5 seconds");
            delay(5000);
        }
    }
}

// === SENSOR HANDLERS ===
void handleSensorEnable(DynamicJsonDocument& doc) {
  String sensorType = doc["sensorType"];

  if (sensorType == "temperature") {
    temperatureSensor.enabled = true;
    Serial.println("Temperature sensor enabled");
  } else if (sensorType == "humidity") {
    humiditySensor.enabled = true;
    Serial.println("Humidity sensor enabled");
  } else if (sensorType == "sound") {
    soundSensor.enabled = true;
    Serial.println("Sound sensor enabled");
  } else if (sensorType == "motion") {
    motionSensor.enabled = true;
    Serial.println("Motion sensor enabled");
  }
}

void handleSensorDisable(DynamicJsonDocument& doc) {
  String sensorType = doc["sensorType"];

  if (sensorType == "temperature") {
    temperatureSensor.enabled = false;
    Serial.println("Temperature sensor disabled");
  } else if (sensorType == "humidity") {
    humiditySensor.enabled = false;
    Serial.println("Humidity sensor disabled");
  } else if (sensorType == "sound") {
    soundSensor.enabled = false;
    Serial.println("Sound sensor disabled");
  } else if (sensorType == "motion") {
    motionSensor.enabled = false;
    Serial.println("Motion sensor disabled");
  }
}

void handleSensorRestart(DynamicJsonDocument& doc) {
  String sensorType = doc["sensorType"];

  if (sensorType == "temperature" || sensorType == "humidity") {
    dhtSensor.begin();
    temperatureSensor.connected = true;
    humiditySensor.connected = true;
    Serial.println("DHT sensor restarted");
  } else if (sensorType == "sound") {
    soundSensor.connected = true;
    Serial.println("Sound sensor restarted");
  } else if (sensorType == "motion") {
    motionSensorHW.initialize();
    motionSensor.connected = motionSensorHW.testConnection();
    Serial.println("Motion sensor restarted");
  }
}

void handleSensorStatusRequest(DynamicJsonDocument& doc) {
  String sensorType = doc["sensorType"];

  DynamicJsonDocument responseDoc(512);
  responseDoc["sensorType"] = sensorType;

  if (sensorType == "temperature") {
    responseDoc["enabled"] = temperatureSensor.enabled;
    responseDoc["connected"] = temperatureSensor.connected;
  } else if (sensorType == "humidity") {
    responseDoc["enabled"] = humiditySensor.enabled;
    responseDoc["connected"] = humiditySensor.connected;
  } else if (sensorType == "sound") {
    responseDoc["enabled"] = soundSensor.enabled;
    responseDoc["connected"] = soundSensor.connected;
  } else if (sensorType == "motion") {
    responseDoc["enabled"] = motionSensor.enabled;
    responseDoc["connected"] = motionSensor.connected;
  }

  String responseMsg;
  serializeJson(responseDoc, responseMsg);
  mqttClient.publish(SENSOR_STATUS_RES, responseMsg.c_str());
}

void handleSensorValuesRequest(DynamicJsonDocument& doc) {
    Serial.println("Handling sensor values request");

    DynamicJsonDocument responseDoc(1024);
    JsonArray sensorsArray = responseDoc.createNestedArray("sensors");

    // Add temperature sensor data
    if (temperatureSensor.enabled && temperatureSensor.connected) {
        JsonObject tempSensor = sensorsArray.createNestedObject();
        tempSensor["type"] = "temperature";
        tempSensor["value"] = temperatureSensor.lastValue;
    }

    // Add humidity sensor data
    if (humiditySensor.enabled && humiditySensor.connected) {
        JsonObject humiditySensorObj = sensorsArray.createNestedObject();
        humiditySensorObj["type"] = "humidity";
        humiditySensorObj["value"] = humiditySensor.lastValue;
    }

    // Add sound sensor data
    if (soundSensor.enabled && soundSensor.connected) {
        JsonObject soundSensorObj = sensorsArray.createNestedObject();
        soundSensorObj["type"] = "sound";
        soundSensorObj["value"] = soundSensor.lastValue;
    }

    // Add motion sensor data
    if (motionSensor.enabled && motionSensor.connected) {
        JsonObject motionSensorObj = sensorsArray.createNestedObject();
        motionSensorObj["type"] = "motion";
        motionSensorObj["value"] = motionSensor.lastValue;
    }

    String responseMsg;
    serializeJson(responseDoc, responseMsg);
    mqttClient.publish(SENSOR_VALUES_RES, responseMsg.c_str());

    Serial.println("Sensor values response sent");
}

// === LULLABY PLAYER HANDLERS ===
void handleLullabyPlayerStatusRequest(DynamicJsonDocument& doc) {
  DynamicJsonDocument response(256);
  response["enabled"] = lullabyPlayer.enabled;
  response["connected"] = lullabyPlayer.connected;
  response["playing"] = lullabyPlayer.playing;

  String responseMsg;
  serializeJson(response, responseMsg);
  mqttClient.publish(LULLABY_PLAYER_STATUS_RES, responseMsg.c_str());
}

void handleLullabyPlayerEnable(DynamicJsonDocument& doc) {
  lullabyPlayer.enabled = true;
  Serial.println("Lullaby player enabled");
}

void handleLullabyPlayerDisable(DynamicJsonDocument& doc) {
  lullabyPlayer.enabled = false;
  if (lullabyPlayer.playing) {
    dfPlayer.stop();
    lullabyPlayer.playing = false;
  }
  Serial.println("Lullaby player disabled");
}

void handleLullabyPlayerRestart(DynamicJsonDocument& doc) {
  if (lullabyPlayer.playing) {
    dfPlayer.stop();
    lullabyPlayer.playing = false;
  }

  dfPlayer.reset();
  delay(1000);
  dfPlayer.volume(lullabyPlayer.currentVolume);

  lullabyPlayer.enabled = true;
  lullabyPlayer.connected = true;
  lullabyPlayer.playing = false;

  Serial.println("Lullaby player restarted");
}

void handleLullabyPlayerPlay(DynamicJsonDocument& doc) {
  if (lullabyPlayer.enabled && lullabyPlayer.connected && doc.containsKey("songNumber")) {
    int songNumber = doc["songNumber"];
    dfPlayer.play(songNumber);
    lullabyPlayer.playing = true;
    Serial.println("Playing song: " + String(songNumber));

    // Publish music started event
    publishSleepSessionEvent("MUSIC_STARTED", "Music playback started", "", "", 0, 0, false);
  }
}

void handleLullabyPlayerStop(DynamicJsonDocument& doc) {
  if (lullabyPlayer.playing) {
    dfPlayer.stop();
    lullabyPlayer.playing = false;
    Serial.println("Music stopped");

    // Publish music stopped event
    publishSleepSessionEvent("MUSIC_STOPPED", "Music playback stopped", "", "", 0, 0, false);
  }
}

// === SLEEP SESSION HANDLERS ===
void handleSleepSessionStart(DynamicJsonDocument& doc) {
  Serial.println("Starting sleep session");

  sleepSession.active = true;
  sleepSession.paused = false;
  sleepSession.sessionId = doc["sessionId"];
  sleepSession.startTime = millis();
  sleepSession.durationMinutes = doc["durationMinutes"];
  sleepSession.enableAlerts = doc["enableAlerts"];
  sleepSession.mediumAlertTimeoutSeconds = doc["mediumAlertTimeoutSeconds"];
  sleepSession.highAlertTimeoutSeconds = doc["highAlertTimeoutSeconds"];

  // Parse sensor configurations
  JsonArray sensorConfigs = doc["sensorConfigurations"];
  for (JsonObject config : sensorConfigs) {
    String sensorType = config["sensorType"];

    if (sensorType == "temperature") {
      tempConfig.enabled = config["enabled"];
      tempConfig.loggingEnabled = config["loggingEnabled"];
      tempConfig.loggingIntervalMinutes = config["loggingIntervalMinutes"];
      tempConfig.mediumAlertThreshold = config["mediumAlertThreshold"];
      tempConfig.highAlertThreshold = config["highAlertThreshold"];
    } else if (sensorType == "humidity") {
      humidityConfig.enabled = config["enabled"];
      humidityConfig.loggingEnabled = config["loggingEnabled"];
      humidityConfig.loggingIntervalMinutes = config["loggingIntervalMinutes"];
      humidityConfig.mediumAlertThreshold = config["mediumAlertThreshold"];
      humidityConfig.highAlertThreshold = config["highAlertThreshold"];
    } else if (sensorType == "sound") {
      soundConfig.enabled = config["enabled"];
      soundConfig.loggingEnabled = config["loggingEnabled"];
      soundConfig.loggingIntervalMinutes = config["loggingIntervalMinutes"];
      soundConfig.mediumAlertThreshold = config["mediumAlertThreshold"];
      soundConfig.highAlertThreshold = config["highAlertThreshold"];
    } else if (sensorType == "motion") {
      motionConfig.enabled = config["enabled"];
      motionConfig.loggingEnabled = config["loggingEnabled"];
      motionConfig.loggingIntervalMinutes = config["loggingIntervalMinutes"];
      motionConfig.mediumAlertThreshold = config["mediumAlertThreshold"];
      motionConfig.highAlertThreshold = config["highAlertThreshold"];
    }
  }

  // Parse lullaby player configuration
  if (doc.containsKey("lullabyPlayerConfiguration")) {
    JsonObject lullabyConfig = doc["lullabyPlayerConfiguration"];
    lullabyPlayer.enabled = lullabyConfig["enabled"];
    lullabyPlayer.currentVolume = lullabyConfig["volume"];
    dfPlayer.volume(lullabyPlayer.currentVolume);

    sleepSession.alertLullabyEnabled = lullabyConfig["alertLullabyEnabled"];
    sleepSession.highAlertLullabyNumber = lullabyConfig["highAlertLullabyNumber"];
    sleepSession.mediumAlertLullabyNumber = lullabyConfig["mediumAlertLullabyNumber"];
    sleepSession.periodicLullabyEnabled = lullabyConfig["periodicLullabyEnabled"];
    sleepSession.periodicLullabyIntervalMinutes = lullabyConfig["periodicLullabyIntervalMinutes"];
    sleepSession.periodicLullabyNumber = lullabyConfig["periodicLullabyNumber"];
    sleepSession.wakeUpLullabyEnabled = lullabyConfig["wakeUpLullabyEnabled"];
    sleepSession.wakeUpLullabyNumber = lullabyConfig["wakeUpLullabyNumber"];
  }

  sleepSession.lastPeriodicLullaby = millis();
  lastTempLogging = millis();
  lastHumidityLogging = millis();
  lastSoundLogging = millis();
  lastMotionLogging = millis();

  // Reset alert states
  alertState.temperatureAlertActive = false;
  alertState.humidityAlertActive = false;
  alertState.soundAlertActive = false;
  alertState.motionAlertActive = false;
  alertState.lastAlertTime = 0;

  Serial.println("Sleep session started with ID: " + String(sleepSession.sessionId));

  // Publish session started event
  publishSleepSessionEvent("SESSION_STARTED", "Sleep session started", "", "", 0, 0, false);
}

void handleSleepSessionPause(DynamicJsonDocument& doc) {
  if (sleepSession.active) {
    sleepSession.paused = true;
    Serial.println("Sleep session paused");

    // Stop any playing music
    if (lullabyPlayer.playing) {
      dfPlayer.stop();
      lullabyPlayer.playing = false;
    }

    // Publish session paused event
    publishSleepSessionEvent("SESSION_PAUSED", "Sleep session paused", "", "", 0, 0, false);
  }
}

void handleSleepSessionResume(DynamicJsonDocument& doc) {
  if (sleepSession.active && sleepSession.paused) {
    sleepSession.paused = false;
    Serial.println("Sleep session resumed");

    // Publish session resumed event
    publishSleepSessionEvent("SESSION_RESUMED", "Sleep session resumed", "", "", 0, 0, false);
  }
}

void handleSleepSessionTerminate(DynamicJsonDocument& doc) {
  if (sleepSession.active) {
    sleepSession.active = false;
    sleepSession.paused = false;

    // Stop any playing music
    if (lullabyPlayer.playing) {
      dfPlayer.stop();
      lullabyPlayer.playing = false;
    }

    Serial.println("Sleep session terminated");

    // Publish session terminated event
    publishSleepSessionEvent("SESSION_TERMINATED", "Sleep session terminated", "", "", 0, 0, false);
  }
}

void handleAlertAttend(DynamicJsonDocument& doc) {
  Serial.println("Alert attended by user");

  // Stop any alert music
  if (lullabyPlayer.playing) {
    dfPlayer.stop();
    lullabyPlayer.playing = false;
  }

  // Reset alert states
  alertState.temperatureAlertActive = false;
  alertState.humidityAlertActive = false;
  alertState.soundAlertActive = false;
  alertState.motionAlertActive = false;

  // Publish user attended event
  publishSleepSessionEvent("USER_ATTENDED", "User attended to alert", "", "", 0, 0, false);
}

// === SLEEP SESSION LOGIC ===
void handleSleepSessionLogic() {
  unsigned long currentTime = millis();

  // Check if session duration has elapsed
  if (sleepSession.durationMinutes > 0) {
    unsigned long sessionDuration = currentTime - sleepSession.startTime;
    if (sessionDuration >= (sleepSession.durationMinutes * 60000UL)) {
      // Session time is up
      if (sleepSession.wakeUpLullabyEnabled && lullabyPlayer.enabled && lullabyPlayer.connected) {
        dfPlayer.play(sleepSession.wakeUpLullabyNumber);
        lullabyPlayer.playing = true;
        publishSleepSessionEvent("WAKE_UP_MUSIC_PLAYED", "Wake up music played", "", "", 0, 0, false);
      }

      sleepSession.active = false;
      publishSleepSessionEvent("SESSION_ENDED", "Sleep session ended naturally", "", "", 0, 0, false);
      return;
    }
  }

  // Check for periodic lullaby
  if (sleepSession.periodicLullabyEnabled && lullabyPlayer.enabled && lullabyPlayer.connected) {
    unsigned long timeSinceLastLullaby = currentTime - sleepSession.lastPeriodicLullaby;
    if (timeSinceLastLullaby >= (sleepSession.periodicLullabyIntervalMinutes * 60000UL)) {
      if (!lullabyPlayer.playing) {
        dfPlayer.play(sleepSession.periodicLullabyNumber);
        lullabyPlayer.playing = true;
        sleepSession.lastPeriodicLullaby = currentTime;
        publishSleepSessionEvent("PERIODIC_MUSIC_PLAYED", "Periodic lullaby played", "", "", 0, 0, false);
      }
    }
  }

  // Check sensor thresholds for alerts
  if (sleepSession.enableAlerts) {
    checkSensorAlerts();
  }

  // Handle periodic sensor logging for each sensor type
  checkSensorLogging(currentTime);
}

// Add this function after handleSleepSessionLogic() - it was cut off
void checkSensorAlerts() {
  unsigned long currentTime = millis();

  // Check temperature alerts
  if (tempConfig.enabled && temperatureSensor.enabled && temperatureSensor.connected) {
    float temp = temperatureSensor.lastValue;

    if (temp >= tempConfig.highAlertThreshold && !alertState.temperatureAlertActive) {
      alertState.temperatureAlertActive = true;
      alertState.lastAlertTime = currentTime;

      publishSleepSessionEvent("SENSOR_ALERT_TRIGGERED", "High temperature alert",
                               "temperature", "HIGH", temp, tempConfig.highAlertThreshold, false);

      if (sleepSession.alertLullabyEnabled && lullabyPlayer.enabled && lullabyPlayer.connected) {
        dfPlayer.play(sleepSession.highAlertLullabyNumber);
        lullabyPlayer.playing = true;
      }

      Serial.println("HIGH Temperature Alert: " + String(temp) + "°C");

    } else if (temp >= tempConfig.mediumAlertThreshold && temp < tempConfig.highAlertThreshold && !alertState.temperatureAlertActive) {
      alertState.temperatureAlertActive = true;
      alertState.lastAlertTime = currentTime;

      publishSleepSessionEvent("SENSOR_ALERT_TRIGGERED", "Medium temperature alert",
                               "temperature", "MEDIUM", temp, tempConfig.mediumAlertThreshold, false);

      if (sleepSession.alertLullabyEnabled && lullabyPlayer.enabled && lullabyPlayer.connected) {
        dfPlayer.play(sleepSession.mediumAlertLullabyNumber);
        lullabyPlayer.playing = true;
      }

      Serial.println("MEDIUM Temperature Alert: " + String(temp) + "°C");
    }
  }

  // Check humidity alerts
  if (humidityConfig.enabled && humiditySensor.enabled && humiditySensor.connected) {
    float humidity = humiditySensor.lastValue;

    if (humidity >= humidityConfig.highAlertThreshold && !alertState.humidityAlertActive) {
      alertState.humidityAlertActive = true;
      alertState.lastAlertTime = currentTime;

      publishSleepSessionEvent("SENSOR_ALERT_TRIGGERED", "High humidity alert",
                               "humidity", "HIGH", humidity, humidityConfig.highAlertThreshold, false);

      if (sleepSession.alertLullabyEnabled && lullabyPlayer.enabled && lullabyPlayer.connected) {
        dfPlayer.play(sleepSession.highAlertLullabyNumber);
        lullabyPlayer.playing = true;
      }

      Serial.println("HIGH Humidity Alert: " + String(humidity) + "%");

    } else if (humidity >= humidityConfig.mediumAlertThreshold && humidity < humidityConfig.highAlertThreshold && !alertState.humidityAlertActive) {
      alertState.humidityAlertActive = true;
      alertState.lastAlertTime = currentTime;

      publishSleepSessionEvent("SENSOR_ALERT_TRIGGERED", "Medium humidity alert",
                               "humidity", "MEDIUM", humidity, humidityConfig.mediumAlertThreshold, false);

      if (sleepSession.alertLullabyEnabled && lullabyPlayer.enabled && lullabyPlayer.connected) {
        dfPlayer.play(sleepSession.mediumAlertLullabyNumber);
        lullabyPlayer.playing = true;
      }

      Serial.println("MEDIUM Humidity Alert: " + String(humidity) + "%");
    }
  }

  // Check sound alerts
  if (soundConfig.enabled && soundSensor.enabled && soundSensor.connected) {
    float sound = soundSensor.lastValue;

    if (sound >= soundConfig.highAlertThreshold && !alertState.soundAlertActive) {
      alertState.soundAlertActive = true;
      alertState.lastAlertTime = currentTime;

      publishSleepSessionEvent("SENSOR_ALERT_TRIGGERED", "High sound alert",
                               "sound", "HIGH", sound, soundConfig.highAlertThreshold, false);

      if (sleepSession.alertLullabyEnabled && lullabyPlayer.enabled && lullabyPlayer.connected) {
        dfPlayer.play(sleepSession.highAlertLullabyNumber);
        lullabyPlayer.playing = true;
      }

      Serial.println("HIGH Sound Alert: " + String(sound) + " dB");

    } else if (sound >= soundConfig.mediumAlertThreshold && sound < soundConfig.highAlertThreshold && !alertState.soundAlertActive) {
      alertState.soundAlertActive = true;
      alertState.lastAlertTime = currentTime;

      publishSleepSessionEvent("SENSOR_ALERT_TRIGGERED", "Medium sound alert",
                               "sound", "MEDIUM", sound, soundConfig.mediumAlertThreshold, false);

      if (sleepSession.alertLullabyEnabled && lullabyPlayer.enabled && lullabyPlayer.connected) {
        dfPlayer.play(sleepSession.mediumAlertLullabyNumber);
        lullabyPlayer.playing = true;
      }

      Serial.println("MEDIUM Sound Alert: " + String(sound) + " dB");
    }
  }

  // Check motion alerts
  if (motionConfig.enabled && motionSensor.enabled && motionSensor.connected) {
    float motion = motionSensor.lastValue;

    if (motion >= motionConfig.highAlertThreshold && !alertState.motionAlertActive) {
      alertState.motionAlertActive = true;
      alertState.lastAlertTime = currentTime;

      publishSleepSessionEvent("SENSOR_ALERT_TRIGGERED", "High motion alert",
                               "motion", "HIGH", motion, motionConfig.highAlertThreshold, false);

      if (sleepSession.alertLullabyEnabled && lullabyPlayer.enabled && lullabyPlayer.connected) {
        dfPlayer.play(sleepSession.highAlertLullabyNumber);
        lullabyPlayer.playing = true;
      }

      Serial.println("HIGH Motion Alert: " + String(motion));

    } else if (motion >= motionConfig.mediumAlertThreshold && motion < motionConfig.highAlertThreshold && !alertState.motionAlertActive) {
      alertState.motionAlertActive = true;
      alertState.lastAlertTime = currentTime;

      publishSleepSessionEvent("SENSOR_ALERT_TRIGGERED", "Medium motion alert",
                               "motion", "MEDIUM", motion, motionConfig.mediumAlertThreshold, false);

      if (sleepSession.alertLullabyEnabled && lullabyPlayer.enabled && lullabyPlayer.connected) {
        dfPlayer.play(sleepSession.mediumAlertLullabyNumber);
        lullabyPlayer.playing = true;
      }

      Serial.println("MEDIUM Motion Alert: " + String(motion));
    }
  }

}

void publishSleepSessionEvent(String eventType, String description, String triggerSensorType,
                              String alertLevel, float sensorValue, float thresholdValue, bool resolvedAutomatically) {
  DynamicJsonDocument eventDoc(1024);

  eventDoc["sessionId"] = sleepSession.sessionId;
  eventDoc["eventType"] = eventType;
  eventDoc["description"] = description;
  eventDoc["timestamp"] = millis();

  if (triggerSensorType != "") {
    eventDoc["triggerSensorType"] = triggerSensorType;
  }

  if (alertLevel != "") {
    eventDoc["alertLevel"] = alertLevel;
  }

  if (sensorValue != 0) {
    eventDoc["sensorValue"] = sensorValue;
  }

  if (thresholdValue != 0) {
    eventDoc["thresholdValue"] = thresholdValue;
  }

  eventDoc["resolvedAutomatically"] = resolvedAutomatically;

  String eventMessage;
  serializeJson(eventDoc, eventMessage);
  mqttClient.publish(SLEEP_SESSION_EVENT, eventMessage.c_str());

  Serial.println("Sleep event published: " + eventType);
}

void checkSensorLogging(unsigned long currentTime) {
  // Log temperature data based on its specific interval
  if (tempConfig.loggingEnabled && temperatureSensor.enabled && temperatureSensor.connected) {
    if (currentTime - lastTempLogging >= (tempConfig.loggingIntervalMinutes * 60000UL)) {
      publishSleepSessionEvent("SENSOR_DATA_LOGGED", "Temperature data logged",
                               "temperature", "", temperatureSensor.lastValue, 0, false);
      lastTempLogging = currentTime;
    }
  }

  // Log humidity data based on its specific interval
  if (humidityConfig.loggingEnabled && humiditySensor.enabled && humiditySensor.connected) {
    if (currentTime - lastHumidityLogging >= (humidityConfig.loggingIntervalMinutes * 60000UL)) {
      publishSleepSessionEvent("SENSOR_DATA_LOGGED", "Humidity data logged",
                               "humidity", "", humiditySensor.lastValue, 0, false);
      lastHumidityLogging = currentTime;
    }
  }

  // Log sound data based on its specific interval
  if (soundConfig.loggingEnabled && soundSensor.enabled && soundSensor.connected) {
    if (currentTime - lastSoundLogging >= (soundConfig.loggingIntervalMinutes * 60000UL)) {
      publishSleepSessionEvent("SENSOR_DATA_LOGGED", "Sound data logged",
                               "sound", "", soundSensor.lastValue, 0, false);
      lastSoundLogging = currentTime;
    }
  }

  // Log motion data based on its specific interval
  if (motionConfig.loggingEnabled && motionSensor.enabled && motionSensor.connected) {
    if (currentTime - lastMotionLogging >= (motionConfig.loggingIntervalMinutes * 60000UL)) {
      publishSleepSessionEvent("SENSOR_DATA_LOGGED", "Motion data logged",
                               "motion", "", motionSensor.lastValue, 0, false);
      lastMotionLogging = currentTime;
    }
  }
}
