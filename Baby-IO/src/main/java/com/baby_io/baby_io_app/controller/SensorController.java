package com.baby_io.baby_io_app.controller;

import com.baby_io.baby_io_app.dto.SensorStatusDTO;
import com.baby_io.baby_io_app.service.MqttService;
import com.baby_io.baby_io_app.service.SensorService;
import com.baby_io.baby_io_app.types.SensorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/auth/me/sensors")
public class SensorController {

  private final SensorService sensorService;
  private final MqttService mqttService;

  @Autowired
  public SensorController(SensorService sensorService, MqttService mqttService) {
    this.sensorService = sensorService;
    this.mqttService = mqttService;
  }

  @GetMapping("/list")
  public ResponseEntity<Collection<SensorType>> getSensorList(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    List<SensorType> sensorTypes = new ArrayList<>(Arrays.asList(SensorType.values()));
    return ResponseEntity.ok(sensorTypes);
  }

  @GetMapping("/status/{sensorType}")
  public ResponseEntity<SensorStatusDTO> getSensorStatus(
      @PathVariable String sensorType,
      HttpServletRequest request) {

    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    SensorType sensorEnum;
    try {
      sensorEnum = SensorType.valueOf(sensorType.toUpperCase());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }

    try {
      SensorStatusDTO sensorStatus = mqttService.requestSensorStatus(sensorEnum.toString());
      if (sensorStatus == null) {
        return ResponseEntity.status(408).build();
      }
      return ResponseEntity.ok(sensorStatus);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/enable/{sensorType}")
  public ResponseEntity<String> enableSensor(
      @PathVariable SensorType sensorType,
      HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    // Update database
    sensorService.enableSensor(userId, sensorType);

    // Send MQTT command to ESP32
    mqttService.enableSensor(sensorType.toString().toLowerCase());

    return ResponseEntity.ok("Sensor " + sensorType + " enabled successfully");
  }

  @PostMapping("/disable/{sensorType}")
  public ResponseEntity<String> disableSensor(
      @PathVariable SensorType sensorType,
      HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    // Update database
    sensorService.disableSensor(userId, sensorType);

    // Send MQTT command to ESP32
    mqttService.disableSensor(sensorType.toString().toLowerCase());

    return ResponseEntity.ok("Sensor " + sensorType + " disabled successfully");
  }

  @PostMapping("/restart/{sensorType}")
  public ResponseEntity<String> restartSensor(
      @PathVariable SensorType sensorType,
      HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    // Send MQTT command to ESP32
    mqttService.restartSensor(sensorType.toString().toLowerCase());

    return ResponseEntity.ok("Sensor " + sensorType + " restarted successfully");
  }

}