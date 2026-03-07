package com.baby_io.baby_io_app.dto;

import java.util.List;

public class CreateSleepRoutineDTO {

  private String name;
  private String description;
  private Integer defaultDurationMinutes;
  private Boolean enableAlerts;
  private Integer mediumAlertTimeoutSeconds;
  private Integer highAlertTimeoutSeconds;
  private List<SensorConfigurationDTO> sensorConfigurations;
  private LullabyPlayerConfigurationDTO lullabyPlayerConfiguration;

  public CreateSleepRoutineDTO() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getDefaultDurationMinutes() {
    return defaultDurationMinutes;
  }

  public void setDefaultDurationMinutes(Integer defaultDurationMinutes) {
    this.defaultDurationMinutes = defaultDurationMinutes;
  }

  public Boolean getEnableAlerts() {
    return enableAlerts;
  }

  public void setEnableAlerts(Boolean enableAlerts) {
    this.enableAlerts = enableAlerts;
  }

  public Integer getMediumAlertTimeoutSeconds() {
    return mediumAlertTimeoutSeconds;
  }

  public void setMediumAlertTimeoutSeconds(Integer mediumAlertTimeoutSeconds) {
    this.mediumAlertTimeoutSeconds = mediumAlertTimeoutSeconds;
  }

  public Integer getHighAlertTimeoutSeconds() {
    return highAlertTimeoutSeconds;
  }

  public void setHighAlertTimeoutSeconds(Integer highAlertTimeoutSeconds) {
    this.highAlertTimeoutSeconds = highAlertTimeoutSeconds;
  }

  public void setLullabyPlayerConfiguration(LullabyPlayerConfigurationDTO lullabyPlayerConfiguration) {
    this.lullabyPlayerConfiguration = lullabyPlayerConfiguration;
  }

  public LullabyPlayerConfigurationDTO getLullabyPlayerConfiguration() {
    return lullabyPlayerConfiguration;
  }

  public List<SensorConfigurationDTO> getSensorConfigurations() {
    return sensorConfigurations;
  }

  public void setSensorConfigurations(List<SensorConfigurationDTO> sensorConfigurations) {
    this.sensorConfigurations = sensorConfigurations;
  }

}
