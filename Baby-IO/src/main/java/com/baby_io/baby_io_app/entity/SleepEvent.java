package com.baby_io.baby_io_app.entity;

import com.baby_io.baby_io_app.types.AlertLevel;
import com.baby_io.baby_io_app.types.SensorType;
import com.baby_io.baby_io_app.types.SleepSessionEventType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sleep_events")
public class SleepEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sleep_session_id", nullable = false)
  private SleepSession sleepSession;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SleepSessionEventType eventType;

  @Column(nullable = true)
  @Enumerated(EnumType.STRING)
  private SensorType triggerSensorType;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AlertLevel alertLevel;

  @Column(nullable = true)
  private Double sensorValue;

  @Column(nullable = true)
  private Double thresholdValue;

  @Column(nullable = false)
  private LocalDateTime timestamp;

  @Column(nullable = true)
  private Boolean resolvedAutomatically;

  @Column(length = 1000, nullable = false)
  private String description;

  public SleepEvent() {
    this.timestamp = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public SleepSessionEventType getEventType() {
    return eventType;
  }

  public SleepSession getSleepSession() {
    return sleepSession;
  }

  public SensorType getTriggerSensorType() {
    return triggerSensorType;
  }

  public AlertLevel getAlertLevel() {
    return alertLevel;
  }

  public Double getSensorValue() {
    return sensorValue;
  }

  public Double getThresholdValue() {
    return thresholdValue;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public Boolean getResolvedAutomatically() {
    return resolvedAutomatically;
  }

  public String getDescription() {
    return description;
  }

  public void setEventType(SleepSessionEventType eventType) {
    this.eventType = eventType;
  }

  public void setSleepSession(SleepSession sleepSession) {
    this.sleepSession = sleepSession;
  }

  public void setTriggerSensorType(SensorType triggerSensorType) {
    this.triggerSensorType = triggerSensorType;
  }

  public void setAlertLevel(AlertLevel alertLevel) {
    this.alertLevel = alertLevel;
  }

  public void setSensorValue(Double sensorValue) {
    this.sensorValue = sensorValue;
  }

  public void setThresholdValue(Double thresholdValue) {
    this.thresholdValue = thresholdValue;
  }

  public void setResolvedAutomatically(Boolean resolvedAutomatically) {
    this.resolvedAutomatically = resolvedAutomatically;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}