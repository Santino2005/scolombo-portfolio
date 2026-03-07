package com.baby_io.baby_io_app.service;

import com.baby_io.baby_io_app.dto.LullabyPlayerConfigurationDTO;
import com.baby_io.baby_io_app.entity.Lullaby;
import com.baby_io.baby_io_app.entity.LullabyPlayer;
import com.baby_io.baby_io_app.entity.LullabyPlayerConfiguration;
import com.baby_io.baby_io_app.entity.SleepRoutine;
import com.baby_io.baby_io_app.entity.User;
import com.baby_io.baby_io_app.repository.LullabyPlayerRepository;
import com.baby_io.baby_io_app.repository.LullabyPlayerConfigurationRepository;
import com.baby_io.baby_io_app.repository.LullabyRepository;
import com.baby_io.baby_io_app.repository.SleepRoutineRepository;
import com.baby_io.baby_io_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LullabyPlayerService {

  @Autowired
  private LullabyPlayerRepository lullabyPlayerRepository;

  @Autowired
  private LullabyPlayerConfigurationRepository lullabyPlayerConfigurationRepository;

  @Autowired
  private SleepRoutineRepository sleepRoutineRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LullabyRepository lullabyRepository;

  /**
   * Enable lullaby player for user
   */
  public boolean enablePlayer(Long userId) {
    Optional<LullabyPlayer> playerOpt = lullabyPlayerRepository.findByUserId(userId);
    LullabyPlayer player = playerOpt.orElseGet(() -> createDefaultPlayer(userId));

    if (player == null) {
      return false;
    }

    player.setEnabled(true);
    lullabyPlayerRepository.save(player);
    return true;
  }

  /**
   * Disable lullaby player for user
   */
  public boolean disablePlayer(Long userId) {
    Optional<LullabyPlayer> playerOpt = lullabyPlayerRepository.findByUserId(userId);
    if (playerOpt.isEmpty()) {
      return false;
    }

    LullabyPlayer player = playerOpt.get();
    player.setEnabled(false);
    lullabyPlayerRepository.save(player);
    return true;
  }

  /**
   * Update lullaby player configuration for a specific sleep routine
   */
  public boolean updateLullabyPlayerConfiguration(Long userId,
                                                  Long routineId,
                                                  LullabyPlayerConfigurationDTO dto) {
    // Verify sleep routine exists and user has access
    Optional<SleepRoutine> routineOpt = sleepRoutineRepository.findById(routineId);
    if (routineOpt.isEmpty()) {
      return false;
    }

    SleepRoutine routine = routineOpt.get();
    if (!hasUserAccessToRoutine(routine, userId)) {
      return false;
    }

    // Find existing configuration or create new one
    Optional<LullabyPlayerConfiguration> configOpt = lullabyPlayerConfigurationRepository
        .findByUserIdAndSleepRoutineId(userId, routineId);

    LullabyPlayerConfiguration config = configOpt.orElseGet(() ->
        createDefaultConfiguration(userId, routineId));

    if (config == null) {
      return false;
    }

    // Update configuration with DTO values
    updateConfigurationFromDTO(config, dto);

    lullabyPlayerConfigurationRepository.save(config);
    return true;
  }

  /**
   * Get lullaby player configuration for a specific sleep routine
   */
  public LullabyPlayerConfigurationDTO getLullabyPlayerConfiguration(Long userId, Long routineId) {
    Optional<SleepRoutine> routineOpt = sleepRoutineRepository.findById(routineId);
    if (routineOpt.isEmpty()) {
      return null;
    }

    SleepRoutine routine = routineOpt.get();
    if (!hasUserAccessToRoutine(routine, userId)) {
      return null;
    }

    Optional<LullabyPlayerConfiguration> configOpt = lullabyPlayerConfigurationRepository
        .findByUserIdAndSleepRoutineId(userId, routineId);

    if (configOpt.isEmpty()) {
      return createDefaultConfigurationDTO(routineId);
    }

    return convertToDTO(configOpt.get());
  }

  /**
   * Check if user has access to the sleep routine
   */
  private boolean hasUserAccessToRoutine(SleepRoutine routine, Long userId) {
    return routine.getBabies().stream()
        .anyMatch(baby -> baby.getUser().getId().equals(userId));
  }

  /**
   * Create default lullaby player for user
   */
  private LullabyPlayer createDefaultPlayer(Long userId) {
    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isEmpty()) {
      return null;
    }

    LullabyPlayer player = new LullabyPlayer();
    player.setUser(userOpt.get());
    player.setEnabled(false);
    return lullabyPlayerRepository.save(player);
  }

  /**
   * Create default lullaby player configuration
   */
  private LullabyPlayerConfiguration createDefaultConfiguration(Long userId, Long routineId) {
    Optional<User> userOpt = userRepository.findById(userId);
    Optional<SleepRoutine> routineOpt = sleepRoutineRepository.findById(routineId);

    if (userOpt.isEmpty() || routineOpt.isEmpty()) {
      return null;
    }

    // Get or create lullaby player for user
    Optional<LullabyPlayer> playerOpt = lullabyPlayerRepository.findByUserId(userId);
    LullabyPlayer player = playerOpt.orElseGet(() -> createDefaultPlayer(userId));

    if (player == null) {
      return null;
    }

    LullabyPlayerConfiguration config = new LullabyPlayerConfiguration();
    config.setSleepRoutine(routineOpt.get());
    config.setLullabyPlayer(player);
    config.setEnabled(true);
    config.setVolume(15);
    return config;
  }

  /**
   * Create default configuration DTO
   */
  private LullabyPlayerConfigurationDTO createDefaultConfigurationDTO(Long routineId) {
    LullabyPlayerConfigurationDTO dto = new LullabyPlayerConfigurationDTO();
    dto.setEnabled(true);
    dto.setVolume(15);
    dto.setSleepRoutineId(routineId);
    dto.setAlertLullabyEnabled(false);
    dto.setEnablePeriodicLullaby(false);
    dto.setEnableWakeUpLullaby(false);
    return dto;
  }

  /**
   * Update configuration entity from DTO
   */
  private void updateConfigurationFromDTO(LullabyPlayerConfiguration config, LullabyPlayerConfigurationDTO dto) {
    if (dto.getEnabled() != null) {
      config.setEnabled(dto.getEnabled());
    }
    if (dto.getVolume() != null && dto.getVolume() >= 0 && dto.getVolume() <= 30) {
      config.setVolume(dto.getVolume());
    }

    // Update alert lullaby settings
    if (dto.getAlertLullabyEnabled() != null) {
      config.setAlertLullabyEnabled(dto.getAlertLullabyEnabled());
    }
    if (dto.getMediumAlertLullabyId() != null) {
      Optional<Lullaby> lullabyOpt = lullabyRepository.findById(dto.getMediumAlertLullabyId());
      config.setMediumAlertLullaby(lullabyOpt.orElse(null));
    }
    if (dto.getHighAlertLullabyId() != null) {
      Optional<Lullaby> lullabyOpt = lullabyRepository.findById(dto.getHighAlertLullabyId());
      config.setHighAlertLullaby(lullabyOpt.orElse(null));
    }

    // Update periodic lullaby settings
    if (dto.getEnablePeriodicLullaby() != null) {
      config.setPeriodicLullabyEnabled(dto.getEnablePeriodicLullaby());
    }
    if (dto.getPeriodicLullabyIntervalMinutes() != null &&
        dto.getPeriodicLullabyIntervalMinutes() >= 5 &&
        dto.getPeriodicLullabyIntervalMinutes() <= 180) {
      config.setPeriodicLullabyIntervalMinutes(dto.getPeriodicLullabyIntervalMinutes());
    }
    if (dto.getPeriodicLullabyId() != null) {
      Optional<Lullaby> lullabyOpt = lullabyRepository.findById(dto.getPeriodicLullabyId());
      config.setPeriodicLullaby(lullabyOpt.orElse(null));
    }

    // Update wake up lullaby settings
    if (dto.getEnableWakeUpLullaby() != null) {
      config.setWakeUpLullabyEnabled(dto.getEnableWakeUpLullaby());
    }
    if (dto.getWakeUpLullabyId() != null) {
      Optional<Lullaby> lullabyOpt = lullabyRepository.findById(dto.getWakeUpLullabyId());
      config.setWakeUpLullaby(lullabyOpt.orElse(null));
    }
  }

  /**
   * Convert configuration entity to DTO
   */
  private LullabyPlayerConfigurationDTO convertToDTO(LullabyPlayerConfiguration config) {
    LullabyPlayerConfigurationDTO dto = new LullabyPlayerConfigurationDTO();
    dto.setId(config.getId());
    dto.setEnabled(config.getEnabled());
    dto.setVolume(config.getVolume());
    dto.setLullabyPlayerId(config.getLullabyPlayer() != null ? config.getLullabyPlayer().getId() : null);
    dto.setSleepRoutineId(config.getSleepRoutine() != null ? config.getSleepRoutine().getId() : null);

    // Map alert lullaby fields
    dto.setAlertLullabyEnabled(config.getAlertLullabyEnabled());
    dto.setMediumAlertLullabyId(config.getMediumAlertLullaby() != null ? config.getMediumAlertLullaby().getId() : null);
    dto.setHighAlertLullabyId(config.getHighAlertLullaby() != null ? config.getHighAlertLullaby().getId() : null);

    // Map periodic lullaby fields
    dto.setEnablePeriodicLullaby(config.getPeriodicLullabyEnabled());
    dto.setPeriodicLullabyIntervalMinutes(config.getPeriodicLullabyIntervalMinutes());
    dto.setPeriodicLullabyId(config.getPeriodicLullaby() != null ? config.getPeriodicLullaby().getId() : null);

    // Map wake up lullaby fields
    dto.setEnableWakeUpLullaby(config.getWakeUpLullabyEnabled());
    dto.setWakeUpLullabyId(config.getWakeUpLullaby() != null ? config.getWakeUpLullaby().getId() : null);

    return dto;
  }
}