package com.baby_io.baby_io_app.service;

import com.baby_io.baby_io_app.dto.SensorConfigurationDTO;
import com.baby_io.baby_io_app.entity.Sensor;
import com.baby_io.baby_io_app.entity.SensorConfiguration;
import com.baby_io.baby_io_app.entity.SleepRoutine;
import com.baby_io.baby_io_app.repository.SensorConfigurationRepository;
import com.baby_io.baby_io_app.repository.SensorRepository;
import com.baby_io.baby_io_app.repository.SleepRoutineRepository;
import com.baby_io.baby_io_app.types.SensorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SensorService {

  @Autowired
  private SensorRepository sensorRepository;

  @Autowired
  private SleepRoutineRepository sleepRoutineRepository;

  @Autowired
  private SensorConfigurationRepository sensorConfigurationRepository;

  /**
   * Enable a specific sensor
   * @return true if successful, false if sensor not found
   */
  public boolean enableSensor(Long userId, SensorType sensorType) {
    Optional<Sensor> sensorOpt = sensorRepository.findByUserIdAndSensorType(userId, sensorType);
    if (sensorOpt.isEmpty()) {
      return false;
    }

    Sensor sensor = sensorOpt.get();
    sensor.setActive(true);
    sensorRepository.save(sensor);
    return true;
  }

  /**
   * Disable a specific sensor
   * @return true if successful, false if sensor not found
   */
  public boolean disableSensor(Long userId, SensorType sensorType) {
    Optional<Sensor> sensorOpt = sensorRepository.findByUserIdAndSensorType(userId, sensorType);
    if (sensorOpt.isEmpty()) {
      return false;
    }

    Sensor sensor = sensorOpt.get();
    sensor.setActive(false);
    sensorRepository.save(sensor);
    return true;
  }

  /**
   * Get sensor configuration for a routine and sensor type
   * @return Optional containing the configuration DTO, or empty if not found/accessible
   */
  public Optional<SensorConfigurationDTO> getSensorConfiguration(Long userId, Long routineId, SensorType sensorType) {
    Optional<SleepRoutine> routineOpt = sleepRoutineRepository.findById(routineId);
    if (routineOpt.isEmpty()) {
      return Optional.empty();
    }

    SleepRoutine routine = routineOpt.get();
    if (!hasUserAccess(routine, userId)) {
      return Optional.empty();
    }

    Optional<Sensor> sensorOpt = sensorRepository.findByUserIdAndSensorType(userId, sensorType);
    if (sensorOpt.isEmpty()) {
      return Optional.empty();
    }

    Sensor sensor = sensorOpt.get();
    Optional<SensorConfiguration> configOpt = sensorConfigurationRepository
        .findBySleepRoutineIdAndSensorId(routine.getId(), sensor.getId());

    SensorConfiguration config = configOpt
        .orElseGet(() -> createDefaultSensorConfiguration(sensor, routine));

    SensorConfigurationDTO dto = convertToSensorConfigurationDTO(config, sensor);
    return Optional.of(dto);
  }

  public boolean updateSensorConfiguration(Long userId, Long routineId, SensorType sensorType, SensorConfigurationDTO configDTO) {
    Optional<SleepRoutine> routineOpt = sleepRoutineRepository.findById(routineId);
    if (routineOpt.isEmpty()) {
      return false;
    }

    SleepRoutine routine = routineOpt.get();
    if (!hasUserAccess(routine, userId)) {
      return false;
    }

    Optional<Sensor> sensorOpt = sensorRepository.findByUserIdAndSensorType(userId, sensorType);
    if (sensorOpt.isEmpty()) {
      return false;
    }

    Sensor sensor = sensorOpt.get();
    Optional<SensorConfiguration> configOpt = sensorConfigurationRepository
        .findBySleepRoutineIdAndSensorId(routine.getId(), sensor.getId());

    SensorConfiguration config = configOpt
        .orElseGet(() -> new SensorConfiguration(sensor, routine));

    // Update all configuration fields from DTO
    if (configDTO.getLoggingEnabled() != null) {
      config.setLoggingEnabled(configDTO.getLoggingEnabled());
    }

    if (configDTO.getLoggingIntervalMinutes() != null) {
      config.setSamplingIntervalMinutes(configDTO.getLoggingIntervalMinutes());
    }

    if (configDTO.getMediumAlertThreshold() != null) {
      config.setMediumAlertThreshold(configDTO.getMediumAlertThreshold());
    }

    if (configDTO.getHighAlertThreshold() != null) {
      config.setHighAlertThreshold(configDTO.getHighAlertThreshold());
    }

    // Update sensor enabled status if provided
    if (configDTO.getEnabled() != null) {
      sensor.setActive(configDTO.getEnabled());
      sensorRepository.save(sensor);
    }

    sensorConfigurationRepository.save(config);
    return true;
  }

  /**
   * Create default sensor configuration
   */
  private SensorConfiguration createDefaultSensorConfiguration(Sensor sensor, SleepRoutine sleepRoutine) {
    SensorConfiguration config = new SensorConfiguration(sensor, sleepRoutine);
    config.setLoggingEnabled(true);
    config.setSamplingIntervalMinutes(5); // Default 5 minutes
    config.setMediumAlertThreshold(50.0); // Default medium threshold
    config.setHighAlertThreshold(80.0); // Default high threshold
    return config;
  }

  /**
   * Convert SensorConfiguration to DTO
   */
  private SensorConfigurationDTO convertToSensorConfigurationDTO(SensorConfiguration config, Sensor sensor) {
    SensorConfigurationDTO dto = new SensorConfigurationDTO();
    dto.setSensorType(sensor.getSensorType());
    dto.setEnabled(sensor.getActive());
    dto.setLoggingEnabled(config.getLoggingEnabled());
    dto.setLoggingIntervalMinutes(config.getSamplingIntervalMinutes());
    dto.setMediumAlertThreshold(config.getMediumAlertThreshold());
    dto.setHighAlertThreshold(config.getHighAlertThreshold());
    return dto;
  }

  /**
   * Check if user has access to the sleep routine
   */
  private boolean hasUserAccess(SleepRoutine routine, Long userId) {
    return routine.getBabies().stream()
        .anyMatch(baby -> baby.getUser().getId().equals(userId));
  }
}