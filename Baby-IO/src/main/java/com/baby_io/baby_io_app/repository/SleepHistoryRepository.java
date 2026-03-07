package com.baby_io.baby_io_app.repository;

import com.baby_io.baby_io_app.entity.SleepHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SleepHistoryRepository extends JpaRepository<SleepHistory, Long> {
  Optional<SleepHistory> findByBabyId(Long id);
}
