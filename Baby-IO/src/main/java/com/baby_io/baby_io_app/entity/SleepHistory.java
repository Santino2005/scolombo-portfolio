package com.baby_io.baby_io_app.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sleep_history")
public class SleepHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "baby_id", nullable = false, unique = true)
  private Baby baby;

  @Column(nullable = false)
  private LocalDate trackingStartDate = LocalDate.now();

  @Column(nullable = false)
  private Integer totalSessions = 0;

  @Column(nullable = false)
  private Integer totalSleepMinutes = 0;

  @Column(nullable = false)
  private Integer totalWakeUps = 0;

  @Column(nullable = false)
  private Integer longestSleepMinutes = 0;

  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal averageTemperature = BigDecimal.valueOf(0);

  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal averageHumidity = BigDecimal.valueOf(0);

  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal averageSound = BigDecimal.valueOf(0);

  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal averageMotion = BigDecimal.valueOf(0);

  @Column(length = 2000)
  private String recommendations;

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Baby getBaby() {
    return baby;
  }

  public void setBaby(Baby baby) {
    this.baby = baby;
  }

  public LocalDate getTrackingStartDate() {
    return trackingStartDate;
  }

  public void setTrackingStartDate(LocalDate trackingStartDate) {
    this.trackingStartDate = trackingStartDate;
  }

  public Integer getTotalSessions() {
    return totalSessions;
  }

  public void setTotalSessions(Integer totalSessions) {
    this.totalSessions = totalSessions;
  }

  public Integer getTotalSleepMinutes() {
    return totalSleepMinutes;
  }

  public void setTotalSleepMinutes(Integer totalSleepMinutes) {
    this.totalSleepMinutes = totalSleepMinutes;
  }

  public Integer getTotalWakeUps() {
    return totalWakeUps;
  }

  public void setTotalWakeUps(Integer totalWakeUps) {
    this.totalWakeUps = totalWakeUps;
  }

  public Integer getLongestSleepMinutes() {
    return longestSleepMinutes;
  }

  public void setLongestSleepMinutes(Integer longestSleepMinutes) {
    this.longestSleepMinutes = longestSleepMinutes;
  }

  public BigDecimal getAverageTemperature() {
    return averageTemperature;
  }

  public void setAverageTemperature(BigDecimal averageTemperature) {
    this.averageTemperature = averageTemperature;
  }

  public BigDecimal getAverageHumidity() {
    return averageHumidity;
  }

  public void setAverageHumidity(BigDecimal averageHumidity) {
    this.averageHumidity = averageHumidity;
  }

  public BigDecimal getAverageSound() {
    return averageSound;
  }

  public void setAverageSound(BigDecimal averageSound) {
    this.averageSound = averageSound;
  }

  public BigDecimal getAverageMotion() {
    return averageMotion;
  }

  public void setAverageMotion(BigDecimal averageMotion) {
    this.averageMotion = averageMotion;
  }

  public String getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(String recommendations) {
    this.recommendations = recommendations;
  }
}