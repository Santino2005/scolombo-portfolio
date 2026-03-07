package com.baby_io.baby_io_app.controller;

import com.baby_io.baby_io_app.dto.CreateSleepRoutineDTO;
import com.baby_io.baby_io_app.dto.SleepRoutineDTO;
import com.baby_io.baby_io_app.dto.SensorConfigurationDTO;
import com.baby_io.baby_io_app.dto.LullabyPlayerConfigurationDTO;
import com.baby_io.baby_io_app.service.SensorService;
import com.baby_io.baby_io_app.service.SleepRoutineService;
import com.baby_io.baby_io_app.service.LullabyPlayerService;
import com.baby_io.baby_io_app.types.SensorType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/me/sleep/routines")
public class SleepRoutineController {

  private final SleepRoutineService sleepRoutineService;
  private final LullabyPlayerService lullabyPlayerService;
  private final SensorService sensorService;

  public SleepRoutineController(SleepRoutineService sleepRoutineService,
                                LullabyPlayerService lullabyPlayerService, SensorService sensorService) {
    this.sleepRoutineService = sleepRoutineService;
    this.lullabyPlayerService = lullabyPlayerService;
    this.sensorService = sensorService;
  }

  @GetMapping
  public ResponseEntity<List<SleepRoutineDTO>> getAllSleepRoutines(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    List<SleepRoutineDTO> routines = sleepRoutineService.getAllSleepRoutines(userId);
    return ResponseEntity.ok(routines);
  }

  @GetMapping("/{routineId}")
  public ResponseEntity<SleepRoutineDTO> getSleepRoutine(HttpServletRequest request,
                                                         @PathVariable Long routineId) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    Optional<SleepRoutineDTO> routine = sleepRoutineService.getSleepRoutine(userId, routineId);
    return routine.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/create")
  public ResponseEntity<SleepRoutineDTO> createSleepRoutine(HttpServletRequest request,
                                                            @Valid @RequestBody CreateSleepRoutineDTO dto) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    Optional<SleepRoutineDTO> createdRoutine = sleepRoutineService.createSleepRoutine(userId, dto);
    return createdRoutine.map(ResponseEntity::ok)
            .orElse(ResponseEntity.badRequest().build());
  }

  @PutMapping("/update/{routineId}")
  public ResponseEntity<SleepRoutineDTO> updateSleepRoutine(HttpServletRequest request,
                                                            @PathVariable Long routineId,
                                                            @Valid @RequestBody SleepRoutineDTO dto) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    Optional<SleepRoutineDTO> updatedRoutine = sleepRoutineService.updateSleepRoutine(userId, routineId, dto);
    return updatedRoutine.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/delete/{routineId}")
  public ResponseEntity<String> deleteSleepRoutine(HttpServletRequest request,
                                                   @PathVariable Long routineId) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    boolean deleted = sleepRoutineService.deleteSleepRoutine(userId, routineId);
    if (!deleted) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok("Sleep routine deleted successfully");
  }

  @PutMapping("/{routineId}/sensors/{sensorType}/configuration/update")
  public ResponseEntity<String> updateSensorConfiguration(HttpServletRequest request,
                                                          @PathVariable Long routineId,
                                                          @PathVariable SensorType sensorType,
                                                          @Valid @RequestBody SensorConfigurationDTO configDTO) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    boolean updated = sensorService.updateSensorConfiguration(userId, routineId, sensorType, configDTO);
    if (!updated) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok("Sensor " + sensorType + " configuration updated");
  }

  @GetMapping("/{routineId}/sensors/{sensorType}/configuration")
  public ResponseEntity<SensorConfigurationDTO> getSensorConfiguration(HttpServletRequest request,
                                                                       @PathVariable Long routineId,
                                                                       @PathVariable SensorType sensorType) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    Optional<SensorConfigurationDTO> optionalConfig = sensorService.getSensorConfiguration(userId, routineId, sensorType);
    if (optionalConfig.isPresent()) {
      return ResponseEntity.ok(optionalConfig.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{routineId}/lullaby-player/configuration")
  public ResponseEntity<LullabyPlayerConfigurationDTO> getLullabyPlayerConfiguration(HttpServletRequest request,
                                                                                     @PathVariable Long routineId) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    LullabyPlayerConfigurationDTO config = lullabyPlayerService.getLullabyPlayerConfiguration(userId, routineId);
    if (config == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(config);
  }

  @PutMapping("/{routineId}/lullaby-player/configuration/update")
  public ResponseEntity<String> updateLullabyPlayerConfiguration(HttpServletRequest request,
                                                                 @PathVariable Long routineId,
                                                                 @Valid @RequestBody LullabyPlayerConfigurationDTO dto) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    boolean updated = lullabyPlayerService.updateLullabyPlayerConfiguration(userId, routineId, dto);
    if (!updated) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok("Lullaby player configuration updated successfully");
  }

}