package com.baby_io.baby_io_app.repository;

import com.baby_io.baby_io_app.entity.SensorConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SensorConfigurationRepository extends JpaRepository<SensorConfiguration, Long> {
  Optional<SensorConfiguration> findBySleepRoutineIdAndSensorId(Long sleepRoutineId, Long sensorId);
}
