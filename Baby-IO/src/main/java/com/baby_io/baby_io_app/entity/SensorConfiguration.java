package com.baby_io.baby_io_app.entity;

import com.baby_io.baby_io_app.types.SensorType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

@Entity
@Table(
    name = "sensor_configurations",
    uniqueConstraints = @UniqueConstraint(columnNames = {"sensor_id", "sleep_routine_id"})
)
public class SensorConfiguration {

  @PrePersist
  public void prePersist() {
    // Only set defaults if values are null
    if (enabled == null) {
      enabled = true;
    }
    if (loggingEnabled == null) {
      loggingEnabled = true;
    }
    if (mediumAlertThreshold == null) {
      mediumAlertThreshold = 50.0;
    }
    if (highAlertThreshold == null) {
      highAlertThreshold = 80.0;
    }
    if (loggingIntervalMinutes == null) {
      loggingIntervalMinutes = 5;
    }
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sensor_id", nullable = false)
  private Sensor sensor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sleep_routine_id", nullable = false)
  private SleepRoutine sleepRoutine;

  @Column(nullable = false)
  private Boolean enabled;

  @Column(nullable = false)
  private Boolean loggingEnabled;

  @DecimalMin("1")
  @DecimalMax("60")
  @Column(nullable = false)
  private Integer loggingIntervalMinutes;

  @DecimalMin("0.0")
  @DecimalMax("100.0")
  @Column(nullable = false)
  private Double mediumAlertThreshold;

  @DecimalMin("0.0")
  @DecimalMax("100.0")
  @Column(nullable = false)
  private Double highAlertThreshold;

  public SensorConfiguration() {}

  public SensorConfiguration(Sensor sensor, SleepRoutine sleepRoutine) {
    this.sensor = sensor;
    this.sleepRoutine = sleepRoutine;
  }

  public void setLoggingEnabled(Boolean loggingEnabled) {
    this.loggingEnabled = loggingEnabled;
  }

  public void setSamplingIntervalMinutes(Integer samplingIntervalMinutes) {
    this.loggingIntervalMinutes = samplingIntervalMinutes;
  }

  public void setMediumAlertThreshold(Double mediumAlertThreshold) {
    this.mediumAlertThreshold = mediumAlertThreshold;
  }

  public void setHighAlertThreshold(Double highAlertThreshold) {
    this.highAlertThreshold = highAlertThreshold;
  }

  public Boolean getLoggingEnabled() {
    return loggingEnabled;
  }

  public Integer getSamplingIntervalMinutes() {
    return loggingIntervalMinutes;
  }

  public Double getMediumAlertThreshold() {
    return mediumAlertThreshold;
  }

  public Double getHighAlertThreshold() {
    return highAlertThreshold;
  }

  public boolean getEnabled() {
    return enabled;
  }

  public Sensor getSensor() {
    return sensor;
  }

  public Object getLoggingIntervalMinutes() {
    return loggingIntervalMinutes;
  }

  public SensorType getSensorType() {
    return sensor.getSensorType();
  }

  public void setLoggingIntervalMinutes(Integer loggingIntervalMinutes) {
    this.loggingIntervalMinutes = loggingIntervalMinutes;
  }

  public void setSleepRoutine(SleepRoutine routine) {
    this.sleepRoutine = routine;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setSensor(Sensor sensor) {
    this.sensor = sensor;
  }
}
