package com.baby_io.baby_io_app.controller;

import com.baby_io.baby_io_app.dto.LullabyDTO;
import com.baby_io.baby_io_app.dto.LullabyPlayerStatusDTO;
import com.baby_io.baby_io_app.dto.PlayLullabyDTO;
import com.baby_io.baby_io_app.service.LullabyPlayerService;
import com.baby_io.baby_io_app.service.LullabyService;
import com.baby_io.baby_io_app.service.MqttService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/auth/me/lullaby-player")
public class LullabyPlayerController {

  private final LullabyPlayerService lullabyPlayerService;
  private final LullabyService lullabyService;
  private final MqttService mqttService;

  @Autowired
  public LullabyPlayerController(LullabyPlayerService lullabyPlayerService,
                                 LullabyService lullabyService,
                                 MqttService mqttService) {
    this.lullabyPlayerService = lullabyPlayerService;
    this.lullabyService = lullabyService;
    this.mqttService = mqttService;
  }

  @GetMapping("/lullabies")
  public ResponseEntity<Collection<LullabyDTO>> getAllLullabies(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    List<LullabyDTO> lullabies = lullabyService.getAllLullabies();
    return ResponseEntity.ok(lullabies);
  }

  @GetMapping("/lullabies/{name}")
  public ResponseEntity<LullabyDTO> getLullabyByName(HttpServletRequest request,
                                               @PathVariable String name) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    try {
      LullabyDTO lullaby = lullabyService.getLullabyByName(name);
      return ResponseEntity.ok(lullaby);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/enable")
  public ResponseEntity<String> enableLullabyPlayer(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    try {
      // Update database
      lullabyPlayerService.enablePlayer(userId);

      // Send MQTT command if needed
      mqttService.enableLullabyPlayer();

      return ResponseEntity.ok("Lullaby player enabled successfully");
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("Failed to enable player: " + e.getMessage());
    }
  }

  @PostMapping("/disable")
  public ResponseEntity<String> disableLullabyPlayer(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    try {
      // Stop any playing music first
      mqttService.disableLullabyPlayer();

      // Update database
      lullabyPlayerService.disablePlayer(userId);

      return ResponseEntity.ok("Lullaby player disabled successfully");
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("Failed to disable player: " + e.getMessage());
    }
  }

  @PostMapping("/restart")
  public ResponseEntity<String> restartLullabyPlayer(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    // Send MQTT command to ESP32
    mqttService.restartLullabyPlayer();

    return ResponseEntity.ok("Lullaby player restarted successfully");
  }

  @GetMapping("/status")
  public ResponseEntity<LullabyPlayerStatusDTO> getLullabyPlayerStatus(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    try {
      LullabyPlayerStatusDTO status = mqttService.requestLullabyPlayerStatus();
      return ResponseEntity.ok(status);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @PostMapping("/play")
  public ResponseEntity<String> playLullaby(HttpServletRequest request,
                                            @Valid @RequestBody PlayLullabyDTO playDto) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    try {
      // Validate lullaby exists
      LullabyDTO lullaby = lullabyService.getLullabyByName(playDto.getName());

      // Send MQTT command to ESP32 using the song number from the lullaby
      mqttService.playLullaby(lullaby.getSongNumber());

      return ResponseEntity.ok("Playing lullaby: " + lullaby.getName());
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body("Lullaby not found: " + e.getMessage());
    }
  }

  @PostMapping("/stop")
  public ResponseEntity<String> stopLullaby(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    try {

      // Send MQTT command to ESP32
      mqttService.stopLullaby();

      return ResponseEntity.ok("Music stopped successfully");
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("Failed to stop music: " + e.getMessage());
    }
  }

}