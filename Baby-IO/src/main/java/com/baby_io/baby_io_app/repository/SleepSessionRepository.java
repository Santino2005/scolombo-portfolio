package com.baby_io.baby_io_app.repository;

import com.baby_io.baby_io_app.entity.SleepSession;
import com.baby_io.baby_io_app.types.SessionSaveDecision;
import com.baby_io.baby_io_app.types.SleepSessionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SleepSessionRepository extends JpaRepository<SleepSession, Long> {
  Optional<SleepSession> findByBabyIdAndStatus(Long id, SleepSessionState sleepSessionStatus);
  Collection<Object> findByBabyIdAndStatusOrderByStartTimeDesc(Long id, SleepSessionState sleepSessionStatus);
  Optional<SleepSession> findByBabyIdAndStatusIn(Long babyId, List<SleepSessionState> active);
  List<SleepSession> findByBabyIdOrderByStartTimeDesc(Long id);
  List<SleepSession> findByBabyIdAndSessionSaveDecisionOrderByStartTimeDesc(Long babyId, SessionSaveDecision sessionSaveDecision);
  List<SleepSession> findByBabyIdAndStatusInOrderByStartTimeDesc(Long babyId, List<SleepSessionState> completed);
}
