package com.baby_io.baby_io_app.service;

import com.baby_io.baby_io_app.dto.CreateSleepRoutineDTO;
import com.baby_io.baby_io_app.dto.SleepRoutineDTO;
import com.baby_io.baby_io_app.dto.SensorConfigurationDTO;
import com.baby_io.baby_io_app.dto.LullabyPlayerConfigurationDTO;
import com.baby_io.baby_io_app.dto.CurrentSleepSessionDTO;
import com.baby_io.baby_io_app.entity.*;
import com.baby_io.baby_io_app.repository.*;
import com.baby_io.baby_io_app.types.SensorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SleepRoutineService {

  @Autowired
  private SleepRoutineRepository sleepRoutineRepository;

  @Autowired
  private UserRepository userRepository;
    @Autowired
    private BabyRepository babyRepository;
    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private LullabyRepository lullabyRepository;
    @Autowired
    private LullabyPlayerRepository lullabyPlayerRepository;

  public List<SleepRoutine> getAllSleepRoutineEntities(Long userId) {
    return sleepRoutineRepository.findAllByUserId(userId);
  }

  public Optional<SleepRoutine> getSleepRoutineEntity(Long userId, Long routineId) {
    Optional<SleepRoutine> routineOpt = sleepRoutineRepository.findById(routineId);
    if (routineOpt.isEmpty()) {
      return Optional.empty();
    }

    SleepRoutine routine = routineOpt.get();
    if (!hasUserAccess(routine, userId)) {
      return Optional.empty();
    }

    return Optional.of(routine);
  }

  @Transactional
  public Optional<SleepRoutine> createSleepRoutineEntity(Long userId, SleepRoutineDTO dto) {
    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isEmpty()) {
      return Optional.empty();
    }

    User user = userOpt.get();
    SleepRoutine routine = new SleepRoutine();

    // Set the user BEFORE calling updateRoutineFromDTO
    routine.setUser(user);

    // Initialize collections to avoid null pointer exceptions
    if (routine.getSensorConfigurations() == null) {
      routine.setSensorConfigurations(new ArrayList<>());
    }

    // Now updateRoutineFromDTO can safely access routine.getUser()
    updateRoutineFromDTO(routine, dto);

    // Save the routine first to get an ID
    SleepRoutine savedRoutine = sleepRoutineRepository.save(routine);

    // Update user's sleep routines collection
    if (user.getSleepRoutines() == null) {
      user.setSleepRoutines(new HashSet<>());
    }
    if (!user.getSleepRoutines().contains(savedRoutine)) {
      user.getSleepRoutines().add(savedRoutine);
      userRepository.save(user);
    }

    return Optional.of(savedRoutine);
  }

  @Transactional
  public Optional<SleepRoutine> updateSleepRoutineEntity(Long userId, Long routineId, SleepRoutineDTO dto) {
    Optional<SleepRoutine> routineOpt = sleepRoutineRepository.findById(routineId);
    if (routineOpt.isEmpty()) {
      return Optional.empty();
    }

    SleepRoutine routine = routineOpt.get();
    if (!hasUserAccess(routine, userId)) {
      return Optional.empty();
    }

    // Remove the routine from all associated babies before updating
    Set<Baby> associatedBabies = new HashSet<>(routine.getBabies());
    for (Baby baby : associatedBabies) {
      baby.getSleepRoutines().remove(routine);
      babyRepository.save(baby);
    }

    // Clear the babies from the routine side
    routine.getBabies().clear();

    updateRoutineFromDTO(routine, dto);

    SleepRoutine savedRoutine = sleepRoutineRepository.save(routine);
    return Optional.of(savedRoutine);
  }

  public List<SleepRoutineDTO> getAllSleepRoutines(Long userId) {
    return getAllSleepRoutineEntities(userId)
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public Optional<SleepRoutineDTO> getSleepRoutine(Long userId, Long routineId) {
    return getSleepRoutineEntity(userId, routineId)
        .map(this::convertToDTO);
  }

  public Optional<SleepRoutineDTO> createSleepRoutine(Long userId, CreateSleepRoutineDTO dto) {
    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isEmpty()) {
      return Optional.empty();
    }

    User user = userOpt.get();
    SleepRoutine routine = new SleepRoutine();
    routine.setUser(user);

    // Initialize collections
    routine.setSensorConfigurations(new ArrayList<>());

    // Convert CreateSleepRoutineDTO to SleepRoutine entity
    convertCreateDtoToEntity(routine, dto);

    SleepRoutine savedRoutine = sleepRoutineRepository.save(routine);

    // Update user's sleep routines collection
    if (user.getSleepRoutines() == null) {
      user.setSleepRoutines(new HashSet<>());
    }
    user.getSleepRoutines().add(savedRoutine);
    userRepository.save(user);

    return Optional.of(convertToDTO(savedRoutine));
  }

  private void convertCreateDtoToEntity(SleepRoutine routine, CreateSleepRoutineDTO dto) {
    if (routine == null || dto == null) {
      return;
    }

    User user = routine.getUser();

    // Set basic fields
    routine.setName(dto.getName());
    routine.setDescription(dto.getDescription());
    routine.setPlannedDurationMinutes(dto.getDefaultDurationMinutes());
    routine.setEnableAlerts(dto.getEnableAlerts());
    routine.setMediumAlertTimeoutSeconds(dto.getMediumAlertTimeoutSeconds());
    routine.setHighAlertTimeoutSeconds(dto.getHighAlertTimeoutSeconds());

    // Process sensor configurations
    if (dto.getSensorConfigurations() != null) {
      for (SensorConfigurationDTO sensorDto : dto.getSensorConfigurations()) {
        Sensor sensor = sensorRepository.findBySensorType(sensorDto.getSensorType());
        if (sensor != null) {
          SensorConfiguration config = new SensorConfiguration();
          config.setSensor(sensor);
          config.setSleepRoutine(routine);
          config.setUser(routine.getUser());
          config.setEnabled(sensorDto.getEnabled());
          config.setLoggingEnabled(sensorDto.getLoggingEnabled());
          config.setLoggingIntervalMinutes(sensorDto.getLoggingIntervalMinutes());
          config.setMediumAlertThreshold(sensorDto.getMediumAlertThreshold());
          config.setHighAlertThreshold(sensorDto.getHighAlertThreshold());
          routine.getSensorConfigurations().add(config);
        }
      }
    }

    // Process lullaby player configuration
    if (dto.getLullabyPlayerConfiguration() != null) {
      LullabyPlayerConfiguration config = new LullabyPlayerConfiguration();
      config.setUser(routine.getUser());
      config.setSleepRoutine(routine);

      // Find the user's lullaby player
      Optional<LullabyPlayer> lullabyPlayerOpt = lullabyPlayerRepository.findByUserId(user.getId());
      if (lullabyPlayerOpt.isPresent()) {
        config.setLullabyPlayer(lullabyPlayerOpt.get());
      }

      // Set configuration values
      LullabyPlayerConfigurationDTO lullabyDto = dto.getLullabyPlayerConfiguration();
      config.setEnabled(lullabyDto.getEnabled());
      config.setVolume(lullabyDto.getVolume());
      config.setAlertLullabyEnabled(lullabyDto.getAlertLullabyEnabled());

      // Set lullabies if they exist
      if (lullabyDto.getMediumAlertLullabyId() != null) {
        lullabyRepository.findById(lullabyDto.getMediumAlertLullabyId())
                .ifPresent(config::setMediumAlertLullaby);
      }
      if (lullabyDto.getHighAlertLullabyId() != null) {
        lullabyRepository.findById(lullabyDto.getHighAlertLullabyId())
                .ifPresent(config::setHighAlertLullaby);
      }

      config.setPeriodicLullabyEnabled(lullabyDto.getEnablePeriodicLullaby());
      config.setPeriodicLullabyIntervalMinutes(lullabyDto.getPeriodicLullabyIntervalMinutes());

      if (lullabyDto.getPeriodicLullabyId() != null) {
        lullabyRepository.findById(lullabyDto.getPeriodicLullabyId())
                .ifPresent(config::setPeriodicLullaby);
      }

      config.setWakeUpLullabyEnabled(lullabyDto.getEnableWakeUpLullaby());

      if (lullabyDto.getWakeUpLullabyId() != null) {
        lullabyRepository.findById(lullabyDto.getWakeUpLullabyId())
                .ifPresent(config::setWakeUpLullaby);
      }

      routine.setLullabyPlayerConfiguration(config);
    }
  }

  public Optional<SleepRoutineDTO> updateSleepRoutine(Long userId, Long routineId, SleepRoutineDTO dto) {
    return updateSleepRoutineEntity(userId, routineId, dto)
        .map(this::convertToDTO);
  }

  @Transactional
  public boolean deleteSleepRoutine(Long userId, Long routineId) {
    Optional<SleepRoutine> routineOpt = sleepRoutineRepository.findById(routineId);
    if (routineOpt.isEmpty()) {
      return false;
    }

    SleepRoutine routine = routineOpt.get();
    if (!hasUserAccess(routine, userId)) {
      return false;
    }

    // Remove the routine from all associated babies
    Set<Baby> associatedBabies = new HashSet<>(routine.getBabies());
    for (Baby baby : associatedBabies) {
      baby.getSleepRoutines().remove(routine);
      babyRepository.save(baby);
    }

    // Clear the babies from the routine side
    routine.getBabies().clear();
    sleepRoutineRepository.save(routine);

    // Now delete the routine
    sleepRoutineRepository.delete(routine);
    return true;
  }

  private boolean hasUserAccess(SleepRoutine routine, Long userId) {
    return routine.getUser().getId().equals(userId);
  }

  private SleepRoutineDTO convertToDTO(SleepRoutine routine) {
    if (routine == null) {
      return null;
    }

    SleepRoutineDTO dto = new SleepRoutineDTO();
    dto.setId(routine.getId());
    dto.setName(routine.getName());
    dto.setDescription(routine.getDescription());
    dto.setDefaultDurationMinutes(routine.getPlannedDurationMinutes());
    dto.setEnableAlerts(routine.getEnableAlerts());
    dto.setMediumAlertTimeoutSeconds(routine.getMediumAlertTimeoutSeconds());
    dto.setHighAlertTimeoutSeconds(routine.getHighAlertTimeoutSeconds());

    if (routine.getSensorConfigurations() != null) {
      List<SensorConfigurationDTO> sensorConfigDTOs = routine.getSensorConfigurations()
          .stream()
          .map(this::convertSensorConfigurationToDTO)
          .collect(Collectors.toList());
      dto.setSensorConfigurations(sensorConfigDTOs);
    }

    if (routine.getLullabyPlayerConfiguration() != null) {
      dto.setLullabyPlayerConfiguration(convertLullabyPlayerConfigurationToDTO(routine.getLullabyPlayerConfiguration()));
    }

    return dto;
  }

  private SensorConfigurationDTO convertSensorConfigurationToDTO(SensorConfiguration config) {
    if (config == null) {
      return null;
    }

    SensorConfigurationDTO dto = new SensorConfigurationDTO();
    dto.setSensorType(config.getSensor().getSensorType());
    dto.setEnabled(config.getEnabled());
    dto.setLoggingEnabled(config.getLoggingEnabled());
    dto.setLoggingIntervalMinutes((Integer) config.getLoggingIntervalMinutes());
    dto.setMediumAlertThreshold(config.getMediumAlertThreshold());
    dto.setHighAlertThreshold(config.getHighAlertThreshold());

    return dto;
  }

  private LullabyPlayerConfigurationDTO convertLullabyPlayerConfigurationToDTO(LullabyPlayerConfiguration config) {
    if (config == null) {
      return null;
    }

    LullabyPlayerConfigurationDTO dto = new LullabyPlayerConfigurationDTO();
    dto.setId(config.getId());
    dto.setEnabled(config.getEnabled());
    dto.setVolume(config.getVolume());

    // Set lullaby player ID if available
    if (config.getLullabyPlayer() != null) {
      dto.setLullabyPlayerId(config.getLullabyPlayer().getId());
    }

    // Set sleep routine ID if available
    if (config.getSleepRoutine() != null) {
      dto.setSleepRoutineId(config.getSleepRoutine().getId());
    }

    // Alert lullaby settings
    dto.setAlertLullabyEnabled(config.getAlertLullabyEnabled());
    if (config.getMediumAlertLullaby() != null) {
      dto.setMediumAlertLullabyId(config.getMediumAlertLullaby().getId());
    }
    if (config.getHighAlertLullaby() != null) {
      dto.setHighAlertLullabyId(config.getHighAlertLullaby().getId());
    }

    // Periodic lullaby settings
    dto.setEnablePeriodicLullaby(config.getPeriodicLullabyEnabled());
    dto.setPeriodicLullabyIntervalMinutes(config.getPeriodicLullabyIntervalMinutes());
    if (config.getPeriodicLullaby() != null) {
      dto.setPeriodicLullabyId(config.getPeriodicLullaby().getId());
    }

    // Wake up lullaby settings
    dto.setEnableWakeUpLullaby(config.getWakeUpLullabyEnabled());
    if (config.getWakeUpLullaby() != null) {
      dto.setWakeUpLullabyId(config.getWakeUpLullaby().getId());
    }

    return dto;
  }

  private void updateRoutineFromDTO(SleepRoutine routine, SleepRoutineDTO dto) {
    if (routine == null || dto == null) {
      return;
    }

    // Update basic fields
    if (dto.getName() != null) routine.setName(dto.getName());
    if (dto.getDescription() != null) routine.setDescription(dto.getDescription());
    if (dto.getDefaultDurationMinutes() != null) routine.setPlannedDurationMinutes(dto.getDefaultDurationMinutes());
    if (dto.getEnableAlerts() != null) routine.setEnableAlerts(dto.getEnableAlerts());
    if (dto.getMediumAlertTimeoutSeconds() != null) routine.setMediumAlertTimeoutSeconds(dto.getMediumAlertTimeoutSeconds());
    if (dto.getHighAlertTimeoutSeconds() != null) routine.setHighAlertTimeoutSeconds(dto.getHighAlertTimeoutSeconds());

    // Update sensor configurations
    if (dto.getSensorConfigurations() != null) {
      updateSensorConfigurations(routine, dto.getSensorConfigurations());
    }

    // Update lullaby player configuration
    if (dto.getLullabyPlayerConfiguration() != null) {
      updateLullabyPlayerConfiguration(routine, dto.getLullabyPlayerConfiguration());
    }
  }

  private void updateSensorConfigurations(SleepRoutine routine, List<SensorConfigurationDTO> sensorDTOs) {
    // Initialize sensor configurations list if it's null
    if (routine.getSensorConfigurations() == null) {
      routine.setSensorConfigurations(new ArrayList<>());
    }

    // Create a map of existing sensor configurations by sensor type for easy lookup
    Map<SensorType, SensorConfiguration> existingConfigsMap = new HashMap<>();

    // Build map from existing configurations (works for both new and existing routines)
    for (SensorConfiguration config : routine.getSensorConfigurations()) {
      if (config.getSensor() != null) {
        existingConfigsMap.put(config.getSensor().getSensorType(), config);
      }
    }

    // Clear the existing list - we'll rebuild it
    routine.getSensorConfigurations().clear();

    // Process each sensor configuration from DTO
    for (SensorConfigurationDTO sensorDto : sensorDTOs) {
      // Find the sensor entity by sensor type
      Sensor sensor = sensorRepository.findBySensorType(sensorDto.getSensorType());
      if (sensor == null) {
        throw new IllegalArgumentException("Sensor not found for type: " + sensorDto.getSensorType());
      }

      // Check if we have an existing configuration for this sensor type
      SensorConfiguration sensorConfig = existingConfigsMap.get(sensorDto.getSensorType());
      if (sensorConfig == null) {
        // Create new sensor configuration
        sensorConfig = new SensorConfiguration();
        sensorConfig.setSensor(sensor);
        sensorConfig.setSleepRoutine(routine);
        sensorConfig.setUser(routine.getUser());
      }

      // Update configuration values from DTO - ALWAYS set values from DTO
      sensorConfig.setEnabled(sensorDto.getEnabled() != null ? sensorDto.getEnabled() : false);
      sensorConfig.setLoggingEnabled(sensorDto.getLoggingEnabled() != null ? sensorDto.getLoggingEnabled() : false);
      sensorConfig.setLoggingIntervalMinutes(sensorDto.getLoggingIntervalMinutes() != null ? sensorDto.getLoggingIntervalMinutes() : 5);
      sensorConfig.setMediumAlertThreshold(sensorDto.getMediumAlertThreshold() != null ? sensorDto.getMediumAlertThreshold() : 0.0);
      sensorConfig.setHighAlertThreshold(sensorDto.getHighAlertThreshold() != null ? sensorDto.getHighAlertThreshold() : 0.0);

      // Add the configuration back to the routine
      routine.getSensorConfigurations().add(sensorConfig);
    }
  }

  private void updateLullabyPlayerConfiguration(SleepRoutine routine, LullabyPlayerConfigurationDTO lullabyDto) {
    if (routine.getLullabyPlayerConfiguration() == null) {
      // Find the user's lullaby player
      Optional<LullabyPlayer> lullabyPlayerOpt = lullabyPlayerRepository.findByUserId(routine.getUser().getId());
      if (lullabyPlayerOpt.isEmpty()) {
        throw new IllegalStateException("No lullaby player found for user");
      }

      LullabyPlayer lullabyPlayer = lullabyPlayerOpt.get();
      LullabyPlayerConfiguration config = new LullabyPlayerConfiguration();
      config.setUser(routine.getUser());
      config.setLullabyPlayer(lullabyPlayer);
      config.setSleepRoutine(routine);
      routine.setLullabyPlayerConfiguration(config);
    }

    LullabyPlayerConfiguration lullabyConfig = routine.getLullabyPlayerConfiguration();

    // Map DTO fields to entity fields
    if (lullabyDto.getEnabled() != null) {
      lullabyConfig.setEnabled(lullabyDto.getEnabled());
    }
    if (lullabyDto.getVolume() != null) {
      lullabyConfig.setVolume(lullabyDto.getVolume());
    }
    if (lullabyDto.getAlertLullabyEnabled() != null) {
      lullabyConfig.setAlertLullabyEnabled(lullabyDto.getAlertLullabyEnabled());
    }

    // Handle lullaby ID assignments with proper repository lookups
    if (lullabyDto.getMediumAlertLullabyId() != null) {
      Lullaby mediumLullaby = lullabyRepository.findById(lullabyDto.getMediumAlertLullabyId()).orElse(null);
      lullabyConfig.setMediumAlertLullaby(mediumLullaby);
    }
    if (lullabyDto.getHighAlertLullabyId() != null) {
      Lullaby highLullaby = lullabyRepository.findById(lullabyDto.getHighAlertLullabyId()).orElse(null);
      lullabyConfig.setHighAlertLullaby(highLullaby);
    }
    if (lullabyDto.getEnablePeriodicLullaby() != null) {
      lullabyConfig.setPeriodicLullabyEnabled(lullabyDto.getEnablePeriodicLullaby());
    }
    if (lullabyDto.getPeriodicLullabyIntervalMinutes() != null) {
      lullabyConfig.setPeriodicLullabyIntervalMinutes(lullabyDto.getPeriodicLullabyIntervalMinutes());
    }
    if (lullabyDto.getPeriodicLullabyId() != null) {
      Lullaby periodicLullaby = lullabyRepository.findById(lullabyDto.getPeriodicLullabyId()).orElse(null);
      lullabyConfig.setPeriodicLullaby(periodicLullaby);
    }
    if (lullabyDto.getEnableWakeUpLullaby() != null) {
      lullabyConfig.setWakeUpLullabyEnabled(lullabyDto.getEnableWakeUpLullaby());
    }
    if (lullabyDto.getWakeUpLullabyId() != null) {
      Lullaby wakeUpLullaby = lullabyRepository.findById(lullabyDto.getWakeUpLullabyId()).orElse(null);
      lullabyConfig.setWakeUpLullaby(wakeUpLullaby);
    }
  }

}