package com.baby_io.baby_io_app.dto;

import com.baby_io.baby_io_app.types.SessionSaveDecision;

import java.time.LocalDateTime;
import java.util.List;

public class SleepSessionDTO {
  private Long id;
  private String routineName;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String status;
  private Integer plannedDurationMinutes;
  private String notes;
  private List<SensorReadingDTO> sensorReadings;
  private List<SleepEventDTO> sleepEvents;
  private SessionSaveDecision sessionSaveDecision;
  private BabyDTO babyDTO;

  // Constructors
  public SleepSessionDTO() {}

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRoutineName() {
    return routineName;
  }

  public void setRoutineName(String routineName) {
    this.routineName = routineName;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getPlannedDurationMinutes() {
    return plannedDurationMinutes;
  }

  public void setPlannedDurationMinutes(Integer plannedDurationMinutes) {
    this.plannedDurationMinutes = plannedDurationMinutes;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public List<SensorReadingDTO> getSensorReadings() {
    return sensorReadings;
  }

  public void setSensorReadings(List<SensorReadingDTO> sensorReadings) {
    this.sensorReadings = sensorReadings;
  }

  public List<SleepEventDTO> getSleepEvents() {
    return sleepEvents;
  }

  public void setSleepEvents(List<SleepEventDTO> sleepEvents) {
    this.sleepEvents = sleepEvents;
  }

  public void setSessionSaveDecision(SessionSaveDecision sessionSaveDecision) {
    this.sessionSaveDecision = sessionSaveDecision;
  }

  public SessionSaveDecision getSessionSaveDecision() {
    return sessionSaveDecision;
  }

  public BabyDTO getBabyDTO() {
    return babyDTO;
  }

  public void setBabyDTO(BabyDTO babyDTO) {
    this.babyDTO = babyDTO;
  }

}