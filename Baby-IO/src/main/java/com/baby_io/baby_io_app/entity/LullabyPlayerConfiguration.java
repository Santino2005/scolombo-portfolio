package com.baby_io.baby_io_app.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "lullaby_player_configurations")
public class LullabyPlayerConfiguration {

  @PrePersist
  public void prePersist() {
    if (enabled == null) enabled = true;
    if (volume == null) volume = 15;
    if (alertLullabyEnabled == null) alertLullabyEnabled = false;
    if (periodicLullabyEnabled == null) periodicLullabyEnabled = false;
    if (wakeUpLullabyEnabled == null) wakeUpLullabyEnabled = false;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lullaby_player_id", nullable = false)
  private LullabyPlayer lullabyPlayer;

  @OneToOne(mappedBy = "lullabyPlayerConfiguration", fetch = FetchType.LAZY)
  private SleepRoutine sleepRoutine;

  @Column(nullable = false)
  private Boolean enabled;

  @Min(0)
  @Max(30)
  @Column(nullable = false)
  private Integer volume;

  @Column(nullable = false)
  private Boolean alertLullabyEnabled;

  @Nullable
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "medium_alert_lullaby_id")
  private Lullaby mediumAlertLullaby;

  @Nullable
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "high_alert_lullaby_id")
  private Lullaby highAlertLullaby;

  @Column(nullable = false)
  private Boolean periodicLullabyEnabled;

  @Nullable
  @Min(5)
  @Max(180)
  private Integer periodicLullabyIntervalMinutes;

  @Nullable
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "periodic_lullaby_id")
  private Lullaby periodicLullaby;

  @Column(nullable = false)
  private Boolean wakeUpLullabyEnabled;

  @Nullable
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "wake_up_lullaby_id")
  private Lullaby wakeUpLullaby;

  // Constructors
  public LullabyPlayerConfiguration() {}

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public LullabyPlayer getLullabyPlayer() {
    return lullabyPlayer;
  }

  public void setLullabyPlayer(LullabyPlayer lullabyPlayer) {
    this.lullabyPlayer = lullabyPlayer;
  }

  public SleepRoutine getSleepRoutine() {
    return sleepRoutine;
  }

  public void setSleepRoutine(SleepRoutine sleepRoutine) {
    this.sleepRoutine = sleepRoutine;
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

  public Boolean getAlertLullabyEnabled() {
    return alertLullabyEnabled;
  }

  public void setAlertLullabyEnabled(Boolean alertLullabyEnabled) {
    this.alertLullabyEnabled = alertLullabyEnabled;
  }

  @Nullable
  public Lullaby getMediumAlertLullaby() {
    return mediumAlertLullaby;
  }

  public void setMediumAlertLullaby(@Nullable Lullaby mediumAlertLullaby) {
    this.mediumAlertLullaby = mediumAlertLullaby;
  }

  @Nullable
  public Lullaby getHighAlertLullaby() {
    return highAlertLullaby;
  }

  public void setHighAlertLullaby(@Nullable Lullaby highAlertLullaby) {
    this.highAlertLullaby = highAlertLullaby;
  }

  public Boolean getPeriodicLullabyEnabled() {
    return periodicLullabyEnabled;
  }

  public void setPeriodicLullabyEnabled(Boolean periodicLullabyEnabled) {
    this.periodicLullabyEnabled = periodicLullabyEnabled;
  }

  @Nullable
  public Integer getPeriodicLullabyIntervalMinutes() {
    return periodicLullabyIntervalMinutes;
  }

  public void setPeriodicLullabyIntervalMinutes(@Nullable Integer periodicLullabyIntervalMinutes) {
    this.periodicLullabyIntervalMinutes = periodicLullabyIntervalMinutes;
  }

  @Nullable
  public Lullaby getPeriodicLullaby() {
    return periodicLullaby;
  }

  public void setPeriodicLullaby(@Nullable Lullaby periodicLullaby) {
    this.periodicLullaby = periodicLullaby;
  }

  public Boolean getWakeUpLullabyEnabled() {
    return wakeUpLullabyEnabled;
  }

  public void setWakeUpLullabyEnabled(Boolean wakeUpLullabyEnabled) {
    this.wakeUpLullabyEnabled = wakeUpLullabyEnabled;
  }

  @Nullable
  public Lullaby getWakeUpLullaby() {
    return wakeUpLullaby;
  }

  public void setWakeUpLullaby(@Nullable Lullaby wakeUpLullaby) {
    this.wakeUpLullaby = wakeUpLullaby;
  }
}