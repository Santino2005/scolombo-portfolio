package com.baby_io.baby_io_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;

@Entity
@Table(name = "sleep_routines")
public class SleepRoutine {

  // Initialize collections in constructor instead of @PrePersist
  public SleepRoutine() {
    this.babies = new HashSet<>();
    this.sensorConfigurations = new ArrayList<>();
    this.sleepSessions = new ArrayList<>();
    // Set default values
    this.plannedDurationMinutes = 60;
    this.enableAlerts = true;
    this.mediumAlertTimeoutSeconds = 30;
    this.highAlertTimeoutSeconds = 60;
  }

  @PrePersist
  public void prePersist() {
    // Ensure collections are initialized if they're null
    if (babies == null) babies = new HashSet<>();
    if (sensorConfigurations == null) sensorConfigurations = new ArrayList<>();
    if (sleepSessions == null) sleepSessions = new ArrayList<>();

    // Set default values if not already set
    if (plannedDurationMinutes == null) plannedDurationMinutes = 60;
    if (enableAlerts == null) enableAlerts = true;
    if (mediumAlertTimeoutSeconds == null) mediumAlertTimeoutSeconds = 30;
    if (highAlertTimeoutSeconds == null) highAlertTimeoutSeconds = 60;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToMany(mappedBy = "sleepRoutines")
  private Set<Baby> babies;

  @NotBlank
  @Size(max = 100)
  @Column(nullable = false)
  private String name;

  @Size(max = 500)
  private String description;

  @Min(1)
  @Max(1440)
  @Column(nullable = false)
  private Integer plannedDurationMinutes;

  @Column(nullable = false)
  private Boolean enableAlerts;

  @Min(1)
  @Max(300)
  private Integer mediumAlertTimeoutSeconds;

  @Min(1)
  @Max(300)
  private Integer highAlertTimeoutSeconds;

  @OneToMany(mappedBy = "sleepRoutine", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<SensorConfiguration> sensorConfigurations;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "lullaby_player_configuration_id")
  private LullabyPlayerConfiguration lullabyPlayerConfiguration;

  @OneToMany(mappedBy = "sleepRoutine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<SleepSession> sleepSessions;

  // Getters and Setters
  public Long getId() {
    return id;
  }

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

  public Integer getPlannedDurationMinutes() {
    return plannedDurationMinutes;
  }

  public void setPlannedDurationMinutes(Integer plannedDurationMinutes) {
    this.plannedDurationMinutes = plannedDurationMinutes;
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

  public Set<Baby> getBabies() {
    if (babies == null) babies = new HashSet<>();
    return babies;
  }

  public void setBabies(Set<Baby> babies) {
    this.babies = babies;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<SensorConfiguration> getSensorConfigurations() {
    if (sensorConfigurations == null) sensorConfigurations = new ArrayList<>();
    return sensorConfigurations;
  }

  public void setSensorConfigurations(List<SensorConfiguration> sensorConfigurations) {
    this.sensorConfigurations = sensorConfigurations;
  }

  public LullabyPlayerConfiguration getLullabyPlayerConfiguration() {
    return lullabyPlayerConfiguration;
  }

  public void setLullabyPlayerConfiguration(LullabyPlayerConfiguration lullabyPlayerConfiguration) {
    this.lullabyPlayerConfiguration = lullabyPlayerConfiguration;
  }

  public List<SleepSession> getSleepSessions() {
    if (sleepSessions == null) sleepSessions = new ArrayList<>();
    return sleepSessions;
  }

  public void setSleepSessions(List<SleepSession> sleepSessions) {
    this.sleepSessions = sleepSessions;
  }
}