package com.baby_io.baby_io_app.controller;

import com.baby_io.baby_io_app.dto.*;
import com.baby_io.baby_io_app.entity.SleepRoutine;
import com.baby_io.baby_io_app.entity.SleepSession;
import com.baby_io.baby_io_app.service.MqttService;
import com.baby_io.baby_io_app.service.SleepSessionService;
import com.baby_io.baby_io_app.service.SleepRoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.*;

@RestController
@RequestMapping("/api/auth/me/sleep/session")
public class SleepSessionController {

  private final SleepSessionService sleepSessionService;
  private final SleepRoutineService sleepRoutineService;
  private final MqttService mqttService;

  @Autowired
  public SleepSessionController(SleepSessionService sleepSessionService,
                                SleepRoutineService sleepRoutineService,
                                MqttService mqttService) {
    this.sleepSessionService = sleepSessionService;
    this.sleepRoutineService = sleepRoutineService;
    this.mqttService = mqttService;

  }

  @PostMapping("/start")
  public ResponseEntity<String> startSleepSession(
      @Valid @RequestBody StartSleepSessionDTO startSessionDTO,
      HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).body("User not authenticated");
    }

    Optional<SleepRoutine> sleepRoutineOpt = sleepRoutineService.getSleepRoutineEntity(userId, startSessionDTO.getSleepRoutineId());
    if (sleepRoutineOpt.isEmpty()) {
      return ResponseEntity.status(404).body("Sleep routine not found");
    }

    SleepRoutine sleepRoutine = sleepRoutineOpt.get();

    Optional<SleepSession> sessionOpt = sleepSessionService.createSleepSessionFromSleepRoutineEntity(userId, sleepRoutine);

    if (sessionOpt.isEmpty()) {
      return ResponseEntity.status(409).body("Cannot start session - another session may be active");
    }

    SleepSession sleepSession = sessionOpt.get();

    try {
      Optional<Boolean> mqttResult = mqttService.startSleepSession(sleepSession.getId(), sleepSession.getSleepRoutine());
      if (mqttResult.isEmpty() || !mqttResult.get()) {
        return ResponseEntity.status(500).body("Failed to start sleep session on device");
      }

      return ResponseEntity.ok("Sleep session started successfully");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error configuring session: " + e.getMessage());
    }
  }

  @PostMapping("current/attend-alert")
  public ResponseEntity<?> attendAlert(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).body("User not authenticated");
    }

    mqttService.attendAlert();

    return ResponseEntity.ok().build();
  }

  @PostMapping("/current/stop")
  public ResponseEntity<String> terminateSleepSession(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).body("User not authenticated");
    }

    Optional<Boolean> stopResult = sleepSessionService.terminateSleepSession(userId);
    if (stopResult.isEmpty() || !stopResult.get()) {
      return ResponseEntity.status(404).body("No active sleep session found to stop");
    }

    Optional<Boolean> mqttResult = mqttService.terminateSleepSession();
    if (mqttResult.isEmpty() || !mqttResult.get()) {
      return ResponseEntity.status(500).body("Failed to stop sleep session on device");
    }

    return ResponseEntity.ok("Sleep session stopped successfully");
  }

  @PostMapping("/current/pause")
  public ResponseEntity<String> pauseSleepSession(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).body("User not authenticated");
    }

    Optional<Boolean> pauseResult = sleepSessionService.pauseSleepSession(userId);
    if (pauseResult.isEmpty() || !pauseResult.get()) {
      return ResponseEntity.status(404).body("No active sleep session found to pause");
    }

    Optional<Boolean> mqttResult = mqttService.pauseSleepSession();
    if (mqttResult.isEmpty() || !mqttResult.get()) {
      return ResponseEntity.status(500).body("Failed to pause sleep session on device");
    }

    return ResponseEntity.ok("Sleep session paused successfully");
  }

  @PostMapping("/current/resume")
  public ResponseEntity<String> resumeSleepSession(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).body("User not authenticated");
    }

    Optional<Boolean> resumeResult = sleepSessionService.resumeSleepSession(userId);
    if (resumeResult.isEmpty() || !resumeResult.get()) {
      return ResponseEntity.status(404).body("No paused sleep session found to resume");
    }

    Optional<Boolean> mqttResult = mqttService.resumeSleepSession();
    if (mqttResult.isEmpty() || !mqttResult.get()) {
      return ResponseEntity.status(500).body("Failed to resume sleep session on device");
    }

    return ResponseEntity.ok("Sleep session resumed successfully");
  }

  @PostMapping("/{sessionId}/save")
  public ResponseEntity<String> saveSleepSession(
      @PathVariable Long sessionId,
      HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).body("User not authenticated");
    }

    Optional<Boolean> saveResult = sleepSessionService.saveSleepSessionToHistory(sessionId, userId);
    if (saveResult.isEmpty() || !saveResult.get()) {
      return ResponseEntity.status(400).body("Failed to save sleep session - session may not be eligible for saving");
    }

    return ResponseEntity.ok("Sleep session saved successfully");
  }

  @GetMapping("/current/sensors/values")
  public ResponseEntity<Collection<SensorValueDTO>> getCurrentSensorValues(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
      // Check if MQTT service is connected
      if (!mqttService.isConnected()) {
        System.err.println("MQTT service is not connected");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
      }

      // Request sensor values from IoT device
      Optional<Collection<SensorValueDTO>> sensorDataOpt = mqttService.requestSensorValues();

      if (sensorDataOpt.isEmpty()) {
        System.err.println("Failed to retrieve sensor values");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }

      Collection<SensorValueDTO> sensorValues = sensorDataOpt.get();

      // Check if we received any sensor data
      if (sensorValues.isEmpty()) {
        System.out.println("No sensor data available");
        return ResponseEntity.noContent().build();
      }

      return ResponseEntity.ok(sensorValues);

    } catch (Exception e) {
      System.err.println("Error retrieving current sensor values: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/current")
  public ResponseEntity<CurrentSleepSessionDTO> getCurrentSleepSession(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    Optional<CurrentSleepSessionDTO> currentSessionOpt = sleepSessionService.getCurrentSleepSession(userId);
    return currentSessionOpt
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.ok().build());
  }

  @GetMapping("/all/saved")
  public ResponseEntity<List<SleepSessionDTO>> getAllSavedSleepSessions(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    List<SleepSessionDTO> sessions = sleepSessionService.getAllSavedSleepSessions(userId);
    return ResponseEntity.ok(sessions);
  }

  @GetMapping("/all/discarded")
  public ResponseEntity<List<SleepSessionDTO>> getDiscardedSleepSessions(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    List<SleepSessionDTO> sessions = sleepSessionService.getAllDiscardedSessions(userId);
    return ResponseEntity.ok(sessions);
  }

  @GetMapping("/{sessionId}")
  public ResponseEntity<SleepSessionDTO> getSleepSessionById(
      @PathVariable Long sessionId,
      HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    Optional<SleepSessionDTO> sessionOpt = sleepSessionService.getSleepSessionById(sessionId, userId);
    if (sessionOpt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(sessionOpt.get());
  }

  @DeleteMapping("/{sessionId}/delete")
  public ResponseEntity<String> deleteSleepSession(
      @PathVariable Long sessionId,
      HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).body("User not authenticated");
    }

    Optional<Boolean> deleteResult = sleepSessionService.deleteSleepSession(sessionId, userId);
    if (deleteResult.isEmpty() || !deleteResult.get()) {
      return ResponseEntity.status(404).body("Sleep session not found or cannot be deleted");
    }

    return ResponseEntity.ok("Sleep session deleted successfully");
  }

}