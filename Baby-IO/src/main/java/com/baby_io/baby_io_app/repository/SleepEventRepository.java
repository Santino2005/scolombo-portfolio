package com.baby_io.baby_io_app.repository;

import com.baby_io.baby_io_app.entity.SleepEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SleepEventRepository extends JpaRepository<SleepEvent, Long> {
}
