package com.baby_io.baby_io_app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SleepHistoryDTO {
  private Long id;
  private LocalDate trackingStartDate;
  private Integer totalSessions;
  private Integer totalSleepMinutes;
  private Integer totalWakeUps;
  private Integer longestSleepMinutes;
  private BigDecimal averageTemperature;
  private BigDecimal averageHumidity;
  private BigDecimal averageSound;
  private BigDecimal averageMotion;
  private String recommendations;

  public SleepHistoryDTO() {}

  public SleepHistoryDTO(Long id, LocalDate trackingStartDate, Integer totalSessions, Integer totalSleepMinutes,
                         Integer totalWakeUps, Integer longestSleepMinutes, BigDecimal averageTemperature,
                         BigDecimal averageHumidity, BigDecimal averageSound, BigDecimal averageMotion,
                         String recommendations) {
    this.id = id;
    this.trackingStartDate = trackingStartDate;
    this.totalSessions = totalSessions;
    this.totalSleepMinutes = totalSleepMinutes;
    this.totalWakeUps = totalWakeUps;
    this.longestSleepMinutes = longestSleepMinutes;
    this.averageTemperature = averageTemperature;
    this.averageHumidity = averageHumidity;
    this.averageSound = averageSound;
    this.averageMotion = averageMotion;
    this.recommendations = recommendations;
  }

  // Getters and Setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public LocalDate getTrackingStartDate() { return trackingStartDate; }
  public void setTrackingStartDate(LocalDate trackingStartDate) { this.trackingStartDate = trackingStartDate; }

  public Integer getTotalSessions() { return totalSessions; }
  public void setTotalSessions(Integer totalSessions) { this.totalSessions = totalSessions; }

  public Integer getTotalSleepMinutes() { return totalSleepMinutes; }
  public void setTotalSleepMinutes(Integer totalSleepMinutes) { this.totalSleepMinutes = totalSleepMinutes; }

  public Integer getTotalWakeUps() { return totalWakeUps; }
  public void setTotalWakeUps(Integer totalWakeUps) { this.totalWakeUps = totalWakeUps; }

  public Integer getLongestSleepMinutes() { return longestSleepMinutes; }
  public void setLongestSleepMinutes(Integer longestSleepMinutes) { this.longestSleepMinutes = longestSleepMinutes; }

  public BigDecimal getAverageTemperature() { return averageTemperature; }
  public void setAverageTemperature(BigDecimal averageTemperature) { this.averageTemperature = averageTemperature; }

  public BigDecimal getAverageHumidity() { return averageHumidity; }
  public void setAverageHumidity(BigDecimal averageHumidity) { this.averageHumidity = averageHumidity; }

  public BigDecimal getAverageSound() { return averageSound; }
  public void setAverageSound(BigDecimal averageSound) { this.averageSound = averageSound; }

  public BigDecimal getAverageMotion() { return averageMotion; }
  public void setAverageMotion(BigDecimal averageMotion) { this.averageMotion = averageMotion; }

  public String getRecommendations() { return recommendations; }
  public void setRecommendations(String recommendations) { this.recommendations = recommendations; }
}