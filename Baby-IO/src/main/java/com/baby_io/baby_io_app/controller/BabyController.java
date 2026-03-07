package com.baby_io.baby_io_app.controller;

import com.baby_io.baby_io_app.dto.*;
import com.baby_io.baby_io_app.service.BabyService;
import com.baby_io.baby_io_app.service.SleepRoutineService;
import com.baby_io.baby_io_app.types.SensorType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/me/babies")
public class BabyController {

  @Autowired
  private BabyService babyService;
  @Autowired
  private SleepRoutineService sleepRoutineService;

  @PostMapping
  public ResponseEntity<BabyDTO> addBaby(@Valid @RequestBody UpdateBabyDTO updateBabyDTO,
                                         HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<BabyDTO> createdBaby = babyService.createBaby(userId, updateBabyDTO);
    return createdBaby
        .map(baby -> ResponseEntity.status(HttpStatus.CREATED).body(baby))
        .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
  }

  @DeleteMapping("/{babyId}")
  public ResponseEntity<Void> deleteBaby(@PathVariable Long babyId,
                                         HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    boolean deleted = babyService.deleteBaby(userId, babyId);
    return deleted
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @PutMapping("/{babyId}")
  public ResponseEntity<BabyDTO> editBaby(@PathVariable Long babyId,
                                          @Valid @RequestBody UpdateBabyDTO updateBabyDTO,
                                          HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<BabyDTO> updatedBaby = babyService.updateBaby(userId, babyId, updateBabyDTO);
    return updatedBaby
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/selected")
  public ResponseEntity<BabyDTO> getSelectedBaby(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<BabyDTO> selectedBaby = babyService.getSelectedBaby(userId);
    return selectedBaby
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/{babyId}/select")
  public ResponseEntity<BabyDTO> selectBaby(@PathVariable Long babyId,
                                            HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<BabyDTO> selectedBaby = babyService.selectBaby(userId, babyId);
    return selectedBaby
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/{babyId}/assign-sleep-routine/{routineId}")
  public ResponseEntity<String> assignSleepRoutine(@PathVariable Long babyId,
                                                   @PathVariable Long routineId,
                                                   HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Check if baby exists and get bio-vulnerable status
    Optional<BabyDTO> babyOpt = babyService.findBabyById(userId, babyId);
    if (babyOpt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    boolean isBioVulnerable = babyOpt.get().getIsBioVulnerable();

    // Check if routine exists
    Optional<SleepRoutineDTO> routineOpt = sleepRoutineService.getSleepRoutine(userId, routineId);
    if (routineOpt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    // Only validate for bio-vulnerable babies
    if (isBioVulnerable) {
      SleepRoutineDTO routine = routineOpt.get();

      if (!routine.getEnableAlerts()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cannot assign to bio-vulnerable baby - alerts must be enabled");
      }

      if (routine.getSensorConfigurations() == null || routine.getSensorConfigurations().isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cannot assign to bio-vulnerable baby - no sensors configured");
      }

      boolean allSensorsEnabled = routine.getSensorConfigurations().stream()
              .allMatch(SensorConfigurationDTO::getEnabled);

      if (!allSensorsEnabled) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cannot assign to bio-vulnerable baby - all sensors must be enabled");
      }
    }

    // Attempt assignment
    try {
      Optional<SleepRoutineDTO> assigned = babyService.setSleepRoutine(userId, babyId, routineId);
      if (assigned.isEmpty()) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Routine already assigned to this baby");
      }
      return ResponseEntity.ok("Routine assigned successfully");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Failed to assign routine: " + e.getMessage());
    }
  }

  @PostMapping("/{babyId}/remove/sleep-routine/{sleepRoutineId}")
  public ResponseEntity<Void> removeSleepRoutine(@PathVariable Long babyId,
                                                  @PathVariable Long sleepRoutineId,
                                                  HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<BabyDTO> babyOpt = babyService.findBabyById(userId, babyId);
    if (babyOpt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    boolean removed = babyService.removeSleepRoutine(userId, babyId, sleepRoutineId);
    return removed
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();

  }

  @GetMapping("/list")
  public ResponseEntity<Collection<BabyDTO>> getAllBabies(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Collection<BabyDTO> babies = babyService.getAllBabiesByUserId(userId);
    return ResponseEntity.ok(babies);
  }

  @GetMapping("/{babyId}")
  public ResponseEntity<BabyDTO> getBaby(@PathVariable Long babyId,
                                         HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<BabyDTO> baby = babyService.findBabyById(userId, babyId);
    return baby
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/{babyId}/sleep-routines")
  public ResponseEntity<Collection<SleepRoutineDTO>> getBabySleepRoutines(@PathVariable Long babyId,
                                                                          HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<Collection<SleepRoutineDTO>> routines = babyService.getSleepRoutinesByBabyId(babyId, userId);
    return routines
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/{babyId}/sleep-history")
  public ResponseEntity<SleepHistoryDTO> getBabySleepHistory(@PathVariable Long babyId,
                                                             HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<SleepHistoryDTO> sleepHistory = babyService.getSleepHistoryByBabyId(babyId, userId);
    return sleepHistory
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

}