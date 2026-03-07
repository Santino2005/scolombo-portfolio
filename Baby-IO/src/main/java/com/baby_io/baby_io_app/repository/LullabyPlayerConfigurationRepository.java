package com.baby_io.baby_io_app.repository;

import com.baby_io.baby_io_app.entity.LullabyPlayerConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LullabyPlayerConfigurationRepository extends JpaRepository<LullabyPlayerConfiguration, Long> {
  Optional<LullabyPlayerConfiguration> findByUserIdAndSleepRoutineId(Long userId, Long routineId);
}
