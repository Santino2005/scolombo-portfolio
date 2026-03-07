package com.baby_io.baby_io_app.repository;

import com.baby_io.baby_io_app.entity.Sensor;
import com.baby_io.baby_io_app.types.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
  Optional<Sensor> findByUserIdAndSensorType(Long userId, SensorType sensorType);
  Sensor findBySensorType(SensorType sensorType);
}

