package com.baby_io.baby_io_app.dto;

import com.baby_io.baby_io_app.types.SensorType;
import jakarta.validation.constraints.*;

public class SensorConfigurationDTO {

  @NotNull
  private SensorType sensorType;

  @NotNull
  private Boolean enabled;

  @NotNull
  private Boolean loggingEnabled;

  @DecimalMin("1")
  @DecimalMax("60")
  private Integer loggingIntervalMinutes;

  @DecimalMin("0.0")
  @DecimalMax("100.0")
  private Double mediumAlertThreshold;

  @DecimalMin("0.0")
  @DecimalMax("100.0")
  private Double highAlertThreshold;

  public SensorConfigurationDTO() {}

  public SensorConfigurationDTO(SensorType sensorType, Boolean enabled, Boolean loggingEnabled) {
    this.sensorType = sensorType;
    this.enabled = enabled;
    this.loggingEnabled = loggingEnabled;
  }

  // Getters and setters
  public SensorType getSensorType() { return sensorType; }
  public void setSensorType(SensorType sensorType) { this.sensorType = sensorType; }

  public Boolean getEnabled() { return enabled; }
  public void setEnabled(Boolean enabled) { this.enabled = enabled; }

  public Boolean getLoggingEnabled() { return loggingEnabled; }
  public void setLoggingEnabled(Boolean loggingEnabled) { this.loggingEnabled = loggingEnabled; }

  public Integer getLoggingIntervalMinutes() { return loggingIntervalMinutes; }
  public void setLoggingIntervalMinutes(Integer loggingIntervalMinutes) { this.loggingIntervalMinutes = loggingIntervalMinutes; }

  public Double getMediumAlertThreshold() { return mediumAlertThreshold; }
  public void setMediumAlertThreshold(Double mediumAlertThreshold) { this.mediumAlertThreshold = mediumAlertThreshold; }

  public Double getHighAlertThreshold() { return highAlertThreshold; }
  public void setHighAlertThreshold(Double highAlertThreshold) { this.highAlertThreshold = highAlertThreshold; }

}