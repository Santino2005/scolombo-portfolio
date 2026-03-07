package com.baby_io.baby_io_app.dto;

import java.time.LocalDateTime;

public class SleepEventDTO {
  private Long id;
  private String eventType;
  private String triggerSensorType;
  private String alertLevel;
  private Double sensorValue;
  private Double thresholdValue;
  private LocalDateTime timestamp;
  private Boolean resolvedAutomatically;
  private String description;

  // Constructors
  public SleepEventDTO() {}

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public String getTriggerSensorType() {
    return triggerSensorType;
  }

  public void setTriggerSensorType(String triggerSensorType) {
    this.triggerSensorType = triggerSensorType;
  }

  public String getAlertLevel() {
    return alertLevel;
  }

  public void setAlertLevel(String alertLevel) {
    this.alertLevel = alertLevel;
  }

  public Double getSensorValue() {
    return sensorValue;
  }

  public void setSensorValue(Double sensorValue) {
    this.sensorValue = sensorValue;
  }

  public Double getThresholdValue() {
    return thresholdValue;
  }

  public void setThresholdValue(Double thresholdValue) {
    this.thresholdValue = thresholdValue;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public Boolean getResolvedAutomatically() {
    return resolvedAutomatically;
  }

  public void setResolvedAutomatically(Boolean resolvedAutomatically) {
    this.resolvedAutomatically = resolvedAutomatically;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
