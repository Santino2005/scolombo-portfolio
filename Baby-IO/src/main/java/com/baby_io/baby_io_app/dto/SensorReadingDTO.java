package com.baby_io.baby_io_app.dto;

import java.time.LocalDateTime;

public class SensorReadingDTO {
  private Long id;
  private String sensorType;
  private Double currentValue;
  private Double minValue;
  private Double maxValue;
  private Double avgValue;
  private Integer readingsCount;
  private LocalDateTime timestamp;

  // Constructors
  public SensorReadingDTO() {}

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSensorType() {
    return sensorType;
  }

  public void setSensorType(String sensorType) {
    this.sensorType = sensorType;
  }

  public Double getCurrentValue() {
    return currentValue;
  }

  public void setCurrentValue(Double currentValue) {
    this.currentValue = currentValue;
  }

  public Double getMinValue() {
    return minValue;
  }

  public void setMinValue(Double minValue) {
    this.minValue = minValue;
  }

  public Double getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(Double maxValue) {
    this.maxValue = maxValue;
  }

  public Double getAvgValue() {
    return avgValue;
  }

  public void setAvgValue(Double avgValue) {
    this.avgValue = avgValue;
  }

  public Integer getReadingsCount() {
    return readingsCount;
  }

  public void setReadingsCount(Integer readingsCount) {
    this.readingsCount = readingsCount;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }
}