package com.baby_io.baby_io_app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LullabyPlayerConfigurationDTO {

  private Long id;

  @NotNull(message = "Enabled status is required")
  private Boolean enabled;

  @NotNull(message = "Volume is required")
  @Min(value = 0, message = "Volume must be at least 0")
  @Max(value = 30, message = "Volume must not exceed 30")
  private Integer volume;

  private Long lullabyPlayerId;
  private Long sleepRoutineId;

  // Alert lullaby fields
  private Boolean alertLullabyEnabled;
  private Long mediumAlertLullabyId;
  private Long highAlertLullabyId;

  // Periodic lullaby fields
  private Boolean enablePeriodicLullaby;

  @Min(value = 5, message = "Periodic lullaby interval must be at least 5 minutes")
  @Max(value = 180, message = "Periodic lullaby interval must not exceed 180 minutes")
  private Integer periodicLullabyIntervalMinutes;

  private Long periodicLullabyId;

  // Wake up lullaby fields
  private Boolean enableWakeUpLullaby;
  private Long wakeUpLullabyId;

  public LullabyPlayerConfigurationDTO() {}

  public LullabyPlayerConfigurationDTO(Long id, Boolean enabled, Integer volume,
                                       Long lullabyPlayerId, Long sleepRoutineId) {
    this.id = id;
    this.enabled = enabled;
    this.volume = volume;
    this.lullabyPlayerId = lullabyPlayerId;
    this.sleepRoutineId = sleepRoutineId;
  }

  public LullabyPlayerConfigurationDTO(Long id, Boolean enabled, Integer volume,
                                       Long lullabyPlayerId, Long sleepRoutineId,
                                       Boolean alertLullabyEnabled, Long mediumAlertLullabyId, Long highAlertLullabyId,
                                       Boolean enablePeriodicLullaby, Integer periodicLullabyIntervalMinutes, Long periodicLullabyId,
                                       Boolean enableWakeUpLullaby, Long wakeUpLullabyId) {
    this.id = id;
    this.enabled = enabled;
    this.volume = volume;
    this.lullabyPlayerId = lullabyPlayerId;
    this.sleepRoutineId = sleepRoutineId;
    this.alertLullabyEnabled = alertLullabyEnabled;
    this.mediumAlertLullabyId = mediumAlertLullabyId;
    this.highAlertLullabyId = highAlertLullabyId;
    this.enablePeriodicLullaby = enablePeriodicLullaby;
    this.periodicLullabyIntervalMinutes = periodicLullabyIntervalMinutes;
    this.periodicLullabyId = periodicLullabyId;
    this.enableWakeUpLullaby = enableWakeUpLullaby;
    this.wakeUpLullabyId = wakeUpLullabyId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Integer getVolume() {
    return volume;
  }

  public void setVolume(Integer volume) {
    this.volume = volume;
  }

  public Long getLullabyPlayerId() {
    return lullabyPlayerId;
  }

  public void setLullabyPlayerId(Long lullabyPlayerId) {
    this.lullabyPlayerId = lullabyPlayerId;
  }

  public Long getSleepRoutineId() {
    return sleepRoutineId;
  }

  public void setSleepRoutineId(Long sleepRoutineId) {
    this.sleepRoutineId = sleepRoutineId;
  }

  // Alert lullaby getters and setters
  public Boolean getAlertLullabyEnabled() {
    return alertLullabyEnabled;
  }

  public void setAlertLullabyEnabled(Boolean alertLullabyEnabled) {
    this.alertLullabyEnabled = alertLullabyEnabled;
  }

  public Long getMediumAlertLullabyId() {
    return mediumAlertLullabyId;
  }

  public void setMediumAlertLullabyId(Long mediumAlertLullabyId) {
    this.mediumAlertLullabyId = mediumAlertLullabyId;
  }

  public Long getHighAlertLullabyId() {
    return highAlertLullabyId;
  }

  public void setHighAlertLullabyId(Long highAlertLullabyId) {
    this.highAlertLullabyId = highAlertLullabyId;
  }

  // Periodic lullaby getters and setters
  public Boolean getEnablePeriodicLullaby() {
    return enablePeriodicLullaby;
  }

  public void setEnablePeriodicLullaby(Boolean enablePeriodicLullaby) {
    this.enablePeriodicLullaby = enablePeriodicLullaby;
  }

  public Integer getPeriodicLullabyIntervalMinutes() {
    return periodicLullabyIntervalMinutes;
  }

  public void setPeriodicLullabyIntervalMinutes(Integer periodicLullabyIntervalMinutes) {
    this.periodicLullabyIntervalMinutes = periodicLullabyIntervalMinutes;
  }

  public Long getPeriodicLullabyId() {
    return periodicLullabyId;
  }

  public void setPeriodicLullabyId(Long periodicLullabyId) {
    this.periodicLullabyId = periodicLullabyId;
  }

  // Wake up lullaby getters and setters
  public Boolean getEnableWakeUpLullaby() {
    return enableWakeUpLullaby;
  }

  public void setEnableWakeUpLullaby(Boolean enableWakeUpLullaby) {
    this.enableWakeUpLullaby = enableWakeUpLullaby;
  }

  public Long getWakeUpLullabyId() {
    return wakeUpLullabyId;
  }

  public void setWakeUpLullabyId(Long wakeUpLullabyId) {
    this.wakeUpLullabyId = wakeUpLullabyId;
  }

}