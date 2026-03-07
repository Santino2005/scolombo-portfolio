package com.baby_io.baby_io_app.entity;

import com.baby_io.baby_io_app.types.SessionSaveDecision;
import com.baby_io.baby_io_app.types.SleepSessionState;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sleep_sessions")
public class SleepSession {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "sleep_routine_id", nullable = false)
  private SleepRoutine sleepRoutine;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "baby_id", nullable = false)
  private Baby baby;

  @Column(nullable = false)
  private LocalDateTime startTime;

  @Column
  private LocalDateTime endTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SleepSessionState status;

  @Column
  private SessionSaveDecision sessionSaveDecision;

  @Column(name = "duration_minutes")
  private Integer durationMinutes;

  @Column(length = 1000)
  private String notes;

  @OneToMany(mappedBy = "sleepSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<SensorReading> sensorReadings = new ArrayList<>();

  @OneToMany(mappedBy = "sleepSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<SleepEvent> sleepEvents = new ArrayList<>();

  public SleepSession() {}

  public SleepSession(SleepRoutine sleepRoutine) {
    this.sleepRoutine = sleepRoutine;
    this.startTime = LocalDateTime.now();
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public SleepRoutine getSleepRoutine() {
    return sleepRoutine;
  }

  public void setSleepRoutine(SleepRoutine sleepRoutine) {
    this.sleepRoutine = sleepRoutine;
  }

  public Baby getBaby() {
    return baby;
  }

  public void setBaby(Baby baby) {
    this.baby = baby;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public SleepSessionState getStatus() {
    return status;
  }

  public void setStatus(SleepSessionState status) {
    this.status = status;
  }

  public Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(Integer plannedDurationMinutes) {
    this.durationMinutes = plannedDurationMinutes;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public List<SensorReading> getSensorReadings() {
    return sensorReadings;
  }

  public void setSensorReadings(List<SensorReading> sensorReadings) {
    this.sensorReadings = sensorReadings;
  }

  public List<SleepEvent> getSleepEvents() {
    return sleepEvents;
  }

  public void setSleepEvents(List<SleepEvent> sleepEvents) {
    this.sleepEvents = sleepEvents;
  }

  public boolean isActive() {
    return status == SleepSessionState.ACTIVE;
  }

  public void setSessionSaveDecision(SessionSaveDecision sessionSaveDecision) {
    this.sessionSaveDecision = sessionSaveDecision;
  }

  public SessionSaveDecision getSessionSaveDecision() {
    return sessionSaveDecision;
  }

}