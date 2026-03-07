package com.baby_io.baby_io_app.entity;

import com.baby_io.baby_io_app.types.SensorType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_readings")
public class SensorReading {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SensorType sensorType;

  @Column(nullable = false)
  private Double currentValue;

  @Column(nullable = false)
  private Double minValue;

  @Column(nullable = false)
  private Double maxValue;

  @Column(nullable = false)
  private Double avgValue;

  @Column(nullable = false)
  private Integer readingsCount;

  @Column(nullable = false)
  private LocalDateTime timestamp;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sleep_session_id")
  private SleepSession sleepSession;

  public SensorReading() {}
  public SensorReading(SensorType sensorType, Double currentValue, Double minValue,
                       Double maxValue, Double avgValue, Integer readingsCount) {
    this.sensorType = sensorType;
    this.currentValue = currentValue;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.avgValue = avgValue;
    this.readingsCount = readingsCount;
    this.timestamp = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public SensorType getSensorType() {
    return sensorType;
  }

  public void setSensorType(SensorType sensorType) {
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

}