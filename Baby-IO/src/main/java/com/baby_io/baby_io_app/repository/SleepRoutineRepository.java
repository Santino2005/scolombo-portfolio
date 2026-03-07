package com.baby_io.baby_io_app.repository;

import com.baby_io.baby_io_app.entity.SleepRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SleepRoutineRepository extends JpaRepository<SleepRoutine, Long> {
  List<SleepRoutine> findAllByUserId(Long userId);
}