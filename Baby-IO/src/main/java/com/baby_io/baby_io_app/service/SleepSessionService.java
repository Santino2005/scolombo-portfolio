package com.baby_io.baby_io_app.service;

import com.baby_io.baby_io_app.dto.BabyDTO;
import com.baby_io.baby_io_app.dto.CurrentSleepSessionDTO;
import com.baby_io.baby_io_app.dto.SleepSessionDTO;
import com.baby_io.baby_io_app.dto.SensorReadingDTO;
import com.baby_io.baby_io_app.dto.SleepEventDTO;
import com.baby_io.baby_io_app.entity.SleepRoutine;
import com.baby_io.baby_io_app.entity.SleepSession;
import com.baby_io.baby_io_app.entity.Baby;
import com.baby_io.baby_io_app.entity.SleepHistory;
import com.baby_io.baby_io_app.entity.SensorReading;
import com.baby_io.baby_io_app.entity.SleepEvent;
import com.baby_io.baby_io_app.repository.SleepSessionRepository;
import com.baby_io.baby_io_app.repository.SleepRoutineRepository;
import com.baby_io.baby_io_app.repository.BabyRepository;
import com.baby_io.baby_io_app.repository.SleepHistoryRepository;
import com.baby_io.baby_io_app.types.SleepSessionState;
import com.baby_io.baby_io_app.types.SessionSaveDecision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

@Service
@Transactional
public class SleepSessionService {

  @Autowired
  private SleepSessionRepository sleepSessionRepository;

  @Autowired
  private SleepRoutineRepository sleepRoutineRepository;

  @Autowired
  private BabyRepository babyRepository;

  @Autowired
  private SleepHistoryRepository sleepHistoryRepository;

  public Optional<SleepSession> createSleepSessionFromSleepRoutineEntity(Long babyId, SleepRoutine routine) {
    Optional<SleepRoutine> routineOpt = sleepRoutineRepository.findById(routine.getId());
    if (routineOpt.isEmpty()) {
      return Optional.empty();
    }

    Optional<Baby> babyOpt = babyRepository.findById(babyId);
    if (babyOpt.isEmpty()) {
      return Optional.empty();
    }

    Baby baby = babyOpt.get();
    Optional<SleepSession> existingSession = sleepSessionRepository
        .findByBabyIdAndStatus(baby.getId(), SleepSessionState.ACTIVE);

    if (existingSession.isPresent()) {
      return Optional.empty();
    }

    SleepSession session = new SleepSession(routine);
    session.setBaby(baby);
    session.setStatus(SleepSessionState.ACTIVE);
    session.setDurationMinutes(routine.getPlannedDurationMinutes());
    session.setStartTime(LocalDateTime.now());
    session.setEndTime(null);
    session.setSessionSaveDecision(null);

    return Optional.of(sleepSessionRepository.save(session));
  }

  public Optional<Boolean> terminateSleepSession(Long babyId) {
    Optional<SleepSession> activeSessionOpt = getCurrentSleepSessionByBabyId(babyId);
    if (activeSessionOpt.isEmpty()) {
      return Optional.of(false);
    }

    SleepSession activeSession = activeSessionOpt.get();
    activeSession.setEndTime(LocalDateTime.now());
    activeSession.setStatus(SleepSessionState.TERMINATED_BY_USER);
    sleepSessionRepository.save(activeSession);

    return Optional.of(true);
  }

  public Optional<Boolean> endSleepSession(Long babyId) {
    Optional<SleepSession> activeSessionOpt = getCurrentSleepSessionByBabyId(babyId);
    if (activeSessionOpt.isEmpty()) {
      return Optional.of(false);
    }

    SleepSession activeSession = activeSessionOpt.get();
    activeSession.setEndTime(LocalDateTime.now());
    activeSession.setStatus(SleepSessionState.COMPLETED);
    sleepSessionRepository.save(activeSession);

    return Optional.of(true);
  }

  public Optional<Boolean> pauseSleepSession(Long babyId) {
    Optional<SleepSession> activeSessionOpt = getCurrentSleepSessionByBabyId(babyId);
    if (activeSessionOpt.isEmpty()) {
      return Optional.of(false);
    }

    SleepSession activeSession = activeSessionOpt.get();
    activeSession.setStatus(SleepSessionState.PAUSED);
    sleepSessionRepository.save(activeSession);
    return Optional.of(true);
  }

  public Optional<Boolean> resumeSleepSession(Long babyId) {
    Optional<Baby> babyOpt = babyRepository.findById(babyId);
    if (babyOpt.isEmpty()) {
      return Optional.of(false);
    }

    Baby baby = babyOpt.get();
    Optional<SleepSession> pausedSessionOpt = sleepSessionRepository
        .findByBabyIdAndStatus(baby.getId(), SleepSessionState.PAUSED);

    if (pausedSessionOpt.isEmpty()) {
      return Optional.of(false);
    }

    SleepSession pausedSession = pausedSessionOpt.get();
    pausedSession.setStatus(SleepSessionState.ACTIVE);
    sleepSessionRepository.save(pausedSession);

    return Optional.of(true);
  }

  public Optional<Boolean> saveSleepSessionToHistory(Long sessionId, Long babyId) {
    Optional<SleepSession> sessionOpt = sleepSessionRepository.findById(sessionId);
    if (sessionOpt.isEmpty()) {
      return Optional.of(false);
    }

    SleepSession session = sessionOpt.get();

    // Check if session belongs to the baby
    if (!session.getBaby().getId().equals(babyId)) {
      return Optional.of(false);
    }

    // Check if session is in correct status
    if (session.getStatus() != SleepSessionState.COMPLETED &&
        session.getStatus() != SleepSessionState.TERMINATED_BY_USER) {
      return Optional.of(false);
    }

    // Check if decision was already made
    if (session.getSessionSaveDecision() != null) {
      return Optional.of(false);
    }

    // Only update sleep history if session has good data
    if (isSessionDataValid(session)) {
      session.setSessionSaveDecision(SessionSaveDecision.SAVED);
      sleepSessionRepository.save(session);
      updateSleepHistory(session);
      return Optional.of(true);
    }

    return Optional.of(false);
  }

  public Optional<CurrentSleepSessionDTO> getCurrentSleepSession(Long babyId) {
    Optional<SleepSession> sessionOpt = getCurrentSleepSessionByBabyId(babyId);
    if (sessionOpt.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(convertToCurrentDTO(sessionOpt.get()));
  }

  public List<SleepSessionDTO> getAllSavedSleepSessions(Long babyId) {
    Optional<Baby> babyOpt = babyRepository.findById(babyId);
    if (babyOpt.isEmpty()) {
      return new ArrayList<>();
    }

    List<SleepSession> sessions = sleepSessionRepository
        .findByBabyIdAndSessionSaveDecisionOrderByStartTimeDesc(babyId, SessionSaveDecision.SAVED);

    List<SleepSessionDTO> result = new ArrayList<>();
    for (SleepSession session : sessions) {
      result.add(convertToSleepSessionDTO(session));
    }
    return result;
  }

  public List<SleepSessionDTO> getAllDiscardedSessions(Long babyId) {
    Optional<Baby> babyOpt = babyRepository.findById(babyId);
    if (babyOpt.isEmpty()) {
      return new ArrayList<>();
    }

    List<SleepSession> sessions = sleepSessionRepository
        .findByBabyIdAndSessionSaveDecisionOrderByStartTimeDesc(babyId, SessionSaveDecision.DISCARDED);

    List<SleepSessionDTO> result = new ArrayList<>();
    for (SleepSession session : sessions) {
      result.add(convertToSleepSessionDTO(session));
    }
    return result;
  }

  public Optional<Boolean> deleteSleepSession(Long sessionId, Long babyId) {
    Optional<SleepSession> sessionOpt = sleepSessionRepository.findById(sessionId);
    if (sessionOpt.isEmpty()) {
      return Optional.of(false);
    }

    SleepSession session = sessionOpt.get();

    // Check if session belongs to the baby
    if (!session.getBaby().getId().equals(babyId)) {
      return Optional.of(false);
    }

    // If session was saved to history, we need to update the history
    if (session.getSessionSaveDecision() == SessionSaveDecision.SAVED) {
      removeFromSleepHistory(session);
    }

    sleepSessionRepository.delete(session);
    return Optional.of(true);
  }

  public Optional<SleepSessionDTO> getSleepSessionById(Long sessionId, Long babyId) {
    Optional<SleepSession> sessionOpt = sleepSessionRepository.findById(sessionId);
    if (sessionOpt.isEmpty()) {
      return Optional.empty();
    }

    SleepSession session = sessionOpt.get();

    // Check if session belongs to the baby
    if (!session.getBaby().getId().equals(babyId)) {
      return Optional.empty();
    }

    return Optional.of(convertToSleepSessionDTO(session));
  }

  private Optional<SleepSession> getCurrentSleepSessionByBabyId(Long babyId) {
    return sleepSessionRepository
        .findByBabyIdAndStatusIn(babyId,
            List.of(SleepSessionState.ACTIVE, SleepSessionState.PAUSED));
  }

  private boolean isSessionDataValid(SleepSession session) {
    if (session == null) return false;
    if (session.getStartTime() == null) return false;
    if (session.getEndTime() == null) return false;
    if (session.getSleepRoutine() == null) return false;
    if (session.getBaby() == null) return false;

    Duration duration = Duration.between(session.getStartTime(), session.getEndTime());
    return duration.toMinutes() >= 5; // At least 5 minutes
  }

  private void updateSleepHistory(SleepSession completedSession) {
    Baby baby = completedSession.getBaby();

    // Get existing sleep history or create new one
    Optional<SleepHistory> historyOpt = sleepHistoryRepository.findByBabyId(baby.getId());
    SleepHistory history;

    if (historyOpt.isPresent()) {
      history = historyOpt.get();
    } else {
      // First time creating history
      history = new SleepHistory();
      history.setBaby(baby);
      history.setTotalSessions(0);
      history.setTotalSleepMinutes(0);
      history.setTotalWakeUps(0);
      history.setLongestSleepMinutes(0);
      history.setAverageTemperature(BigDecimal.ZERO);
      history.setAverageHumidity(BigDecimal.ZERO);
      history.setAverageSound(BigDecimal.ZERO);
      history.setAverageMotion(BigDecimal.ZERO);
    }

    history.setTotalSessions(history.getTotalSessions() + 1);

    if (completedSession.getEndTime() != null) {
      Duration duration = Duration.between(completedSession.getStartTime(), completedSession.getEndTime());
      int sessionMinutes = (int) duration.toMinutes();

      history.setTotalSleepMinutes(history.getTotalSleepMinutes() + sessionMinutes);

      if (sessionMinutes > history.getLongestSleepMinutes()) {
        history.setLongestSleepMinutes(sessionMinutes);
      }
    }

    // Update sensor averages if sensor readings exist
    updateSensorAverages(history, completedSession);

    // Count wake-ups from sleep events
    int wakeUpCount = 0;
    List<SleepEvent> events = completedSession.getSleepEvents();
    if (events != null) {
      for (SleepEvent event : events) {
        String eventType = event.getEventType().name();
        if (eventType.contains("WAKE") || eventType.contains("ALERT")) {
          wakeUpCount++;
        }
      }
    }

    history.setTotalWakeUps(history.getTotalWakeUps() + wakeUpCount);
    history.setRecommendations(generateRecommendations(history));
    sleepHistoryRepository.save(history);
  }

  private void removeFromSleepHistory(SleepSession sessionToRemove) {
    Baby baby = sessionToRemove.getBaby();
    Optional<SleepHistory> historyOpt = sleepHistoryRepository.findByBabyId(baby.getId());

    if (historyOpt.isEmpty()) {
      return; // No history to update
    }

    SleepHistory history = historyOpt.get();

    // Decrease total sessions
    history.setTotalSessions(Math.max(0, history.getTotalSessions() - 1));

    // Remove session minutes from total
    if (sessionToRemove.getEndTime() != null) {
      Duration duration = Duration.between(sessionToRemove.getStartTime(), sessionToRemove.getEndTime());
      int sessionMinutes = (int) duration.toMinutes();
      history.setTotalSleepMinutes(Math.max(0, history.getTotalSleepMinutes() - sessionMinutes));
    }

    // Remove wake-ups from this session
    int wakeUpCount = 0;
    List<SleepEvent> events = sessionToRemove.getSleepEvents();
    if (events != null) {
      for (SleepEvent event : events) {
        String eventType = event.getEventType().name();
        if (eventType.contains("WAKE") || eventType.contains("ALERT")) {
          wakeUpCount++;
        }
      }
    }

    history.setTotalWakeUps(Math.max(0, history.getTotalWakeUps() - wakeUpCount));

    // Note: We don't recalculate longest sleep or sensor averages as it would require
    // reprocessing all saved sessions. This is a limitation of the current approach.

    // If no sessions left, reset history
    if (history.getTotalSessions() == 0) {
      history.setTotalSleepMinutes(0);
      history.setTotalWakeUps(0);
      history.setLongestSleepMinutes(0);
      history.setAverageTemperature(BigDecimal.ZERO);
      history.setAverageHumidity(BigDecimal.ZERO);
      history.setAverageSound(BigDecimal.ZERO);
      history.setAverageMotion(BigDecimal.ZERO);
      history.setRecommendations("No sleep data available yet.");
    } else {
      history.setRecommendations(generateRecommendations(history));
    }

    sleepHistoryRepository.save(history);
  }

  private void updateSensorAverages(SleepHistory history, SleepSession session) {
    List<SensorReading> readings = session.getSensorReadings();
    if (readings == null || readings.isEmpty()) {
      return;
    }

    int totalSessions = history.getTotalSessions();
    Map<String, List<BigDecimal>> sensorValues = new HashMap<>();

    // Group sensor readings by type
    for (SensorReading reading : readings) {
      String sensorType = reading.getSensorType().name();
      BigDecimal value = BigDecimal.valueOf(reading.getAvgValue() != null ? reading.getAvgValue() : reading.getCurrentValue());

      sensorValues.computeIfAbsent(sensorType, k -> new ArrayList<>()).add(value);
    }

    // Calculate averages for each sensor type
    for (Map.Entry<String, List<BigDecimal>> entry : sensorValues.entrySet()) {
      String sensorType = entry.getKey();
      List<BigDecimal> values = entry.getValue();

      BigDecimal sum = BigDecimal.ZERO;
      for (BigDecimal value : values) {
        sum = sum.add(value);
      }
      BigDecimal avgValue = sum.divide(BigDecimal.valueOf(values.size()), 2, BigDecimal.ROUND_HALF_UP);

      switch (sensorType) {
        case "TEMPERATURE":
          history.setAverageTemperature(calculateWeightedAverage(
              history.getAverageTemperature(), avgValue, totalSessions));
          break;
        case "HUMIDITY":
          history.setAverageHumidity(calculateWeightedAverage(
              history.getAverageHumidity(), avgValue, totalSessions));
          break;
        case "SOUND":
          history.setAverageSound(calculateWeightedAverage(
              history.getAverageSound(), avgValue, totalSessions));
          break;
        case "MOTION":
          history.setAverageMotion(calculateWeightedAverage(
              history.getAverageMotion(), avgValue, totalSessions));
          break;
      }
    }
  }

  private BigDecimal calculateWeightedAverage(BigDecimal currentAvg, BigDecimal newValue, int totalSessions) {
    if (totalSessions == 1) {
      return newValue;
    }

    // Weighted average: ((currentAvg * (totalSessions - 1)) + newValue) / totalSessions
    return currentAvg.multiply(BigDecimal.valueOf(totalSessions - 1))
        .add(newValue)
        .divide(BigDecimal.valueOf(totalSessions), 2, BigDecimal.ROUND_HALF_UP);
  }

  private String generateRecommendations(SleepHistory history) {
    StringBuilder recommendations = new StringBuilder();

    if (history.getTotalSessions() > 0) {
      int avgSleepPerSession = history.getTotalSleepMinutes() / history.getTotalSessions();

      if (avgSleepPerSession < 90) {
        recommendations.append("Consider extending sleep sessions - babies typically need 90-120 minutes of continuous sleep. ");
      }

      if (history.getLongestSleepMinutes() < 180) {
        recommendations.append("Try to establish longer continuous sleep periods. ");
      }

      if (history.getTotalSessions() > 5 && avgSleepPerSession < 60) {
        recommendations.append("Frequent short naps may indicate overtiredness - consider adjusting bedtime routine. ");
      }
    }

    // Environmental recommendations based on sensor averages
    if (history.getAverageTemperature().compareTo(BigDecimal.valueOf(20)) < 0) {
      recommendations.append("Room temperature seems low - ideal range is 20-22°C (68-72°F). ");
    } else if (history.getAverageTemperature().compareTo(BigDecimal.valueOf(24)) > 0) {
      recommendations.append("Room temperature seems high - ideal range is 20-22°C (68-72°F). ");
    }

    if (history.getAverageHumidity().compareTo(BigDecimal.valueOf(30)) < 0) {
      recommendations.append("Consider using a humidifier - ideal humidity is 30-50%. ");
    } else if (history.getAverageHumidity().compareTo(BigDecimal.valueOf(60)) > 0) {
      recommendations.append("Room humidity is high - consider improving ventilation or using dehumidifier. ");
    }

    if (history.getAverageSound().compareTo(BigDecimal.valueOf(40)) > 0) {
      recommendations.append("Environment may be too noisy - consider white noise or soundproofing. ");
    }

    if (history.getAverageMotion().compareTo(BigDecimal.valueOf(20)) > 0) {
      recommendations.append("High motion detected - ensure crib is stable and baby is comfortable. ");
    }

    // General recommendations
    if (recommendations.isEmpty()) {
      recommendations.append("Sleep patterns look good! Continue with current routine. ");
    }

    recommendations.append("Always consult with your pediatrician for personalized sleep advice.");

    return recommendations.toString();
  }

  private CurrentSleepSessionDTO convertToCurrentDTO(SleepSession session) {
    if (session == null) {
      return null;
    }

    CurrentSleepSessionDTO dto = new CurrentSleepSessionDTO();
    dto.setId(session.getId());
    dto.setStartTime(session.getStartTime());
    dto.setEndTime(session.getEndTime());
    dto.setStatus(session.getStatus().toString());
    dto.setPlannedDurationMinutes(session.getDurationMinutes());
    Baby baby = session.getBaby();
    BabyDTO babyDTO = new BabyDTO(baby.getId(),
            baby.getName(),
            baby.getAgeInMonths(),
            baby.getWeightInKilograms(),
            baby.getIsBioVulnerable(),
            baby.getGender(),
            baby.getMedicalNotes(),
            baby.getIsSelected());
    dto.setBabyDTO(babyDTO);
    return dto;
  }

  private SleepSessionDTO convertToSleepSessionDTO(SleepSession session) {
    if (session == null) {
      return null;
    }

    SleepSessionDTO dto = new SleepSessionDTO();
    dto.setId(session.getId());
    dto.setRoutineName(session.getSleepRoutine().getName());
    dto.setStartTime(session.getStartTime());
    dto.setEndTime(session.getEndTime());
    dto.setStatus(session.getStatus().toString());
    dto.setPlannedDurationMinutes(session.getDurationMinutes());
    dto.setNotes(session.getNotes());
    dto.setSessionSaveDecision(session.getSessionSaveDecision());
    Baby baby = session.getBaby();
    BabyDTO babyDTO = new BabyDTO(baby.getId(),
            baby.getName(),
            baby.getAgeInMonths(),
            baby.getWeightInKilograms(),
            baby.getIsBioVulnerable(),
            baby.getGender(),
            baby.getMedicalNotes(),
            baby.getIsSelected());
    dto.setBabyDTO(babyDTO);

    // Convert sensor readings
    List<SensorReadingDTO> sensorReadingDTOs = new ArrayList<>();
    List<SensorReading> sensorReadings = session.getSensorReadings();
    if (sensorReadings != null) {
      for (SensorReading reading : sensorReadings) {
        sensorReadingDTOs.add(convertToSensorReadingDTO(reading));
      }
    }
    dto.setSensorReadings(sensorReadingDTOs);

    // Convert sleep events
    List<SleepEventDTO> sleepEventDTOs = new ArrayList<>();
    List<SleepEvent> sleepEvents = session.getSleepEvents();
    if (sleepEvents != null) {
      for (SleepEvent event : sleepEvents) {
        sleepEventDTOs.add(convertToSleepEventDTO(event));
      }
    }
    dto.setSleepEvents(sleepEventDTOs);

    return dto;
  }

  private SensorReadingDTO convertToSensorReadingDTO(SensorReading reading) {
    if (reading == null) {
      return null;
    }

    SensorReadingDTO dto = new SensorReadingDTO();
    dto.setId(reading.getId());
    dto.setSensorType(reading.getSensorType().toString());
    dto.setCurrentValue(reading.getCurrentValue());
    dto.setMinValue(reading.getMinValue());
    dto.setMaxValue(reading.getMaxValue());
    dto.setAvgValue(reading.getAvgValue());
    dto.setReadingsCount(reading.getReadingsCount());
    dto.setTimestamp(reading.getTimestamp());

    return dto;
  }

  private SleepEventDTO convertToSleepEventDTO(SleepEvent event) {
    if (event == null) {
      return null;
    }

    SleepEventDTO dto = new SleepEventDTO();
    dto.setId(event.getId());
    dto.setEventType(event.getEventType().toString());
    dto.setTriggerSensorType(event.getTriggerSensorType() != null ? event.getTriggerSensorType().toString() : null);
    dto.setAlertLevel(event.getAlertLevel() != null ? event.getAlertLevel().toString() : null);
    dto.setSensorValue(event.getSensorValue());
    dto.setThresholdValue(event.getThresholdValue());
    dto.setTimestamp(event.getTimestamp());
    dto.setResolvedAutomatically(event.getResolvedAutomatically());
    dto.setDescription(event.getDescription());

    return dto;
  }

  public Optional<SleepEventDTO> addSleepEventToSession(Long sessionId, SleepEvent sleepEvent) {
    Optional<SleepSession> sleepSessionOptional = sleepSessionRepository.findById(sessionId);

    if (sleepSessionOptional.isPresent()) {
      SleepSession sleepSession = sleepSessionOptional.get();
      sleepSession.getSleepEvents().add(sleepEvent);
      sleepSessionRepository.save(sleepSession);
      return Optional.of(convertToSleepEventDTO(sleepEvent));
    }

    return Optional.empty();
  }

}