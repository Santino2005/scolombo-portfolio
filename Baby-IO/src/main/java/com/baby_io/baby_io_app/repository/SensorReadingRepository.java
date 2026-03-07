package com.baby_io.baby_io_app.repository;

import com.baby_io.baby_io_app.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
}
