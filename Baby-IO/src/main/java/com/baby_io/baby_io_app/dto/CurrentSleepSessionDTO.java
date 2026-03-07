package com.baby_io.baby_io_app.dto;

import java.time.LocalDateTime;

public class CurrentSleepSessionDTO {

  private Long id;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String status;
  private Integer plannedDurationMinutes;
  private BabyDTO baby;

  public BabyDTO getBabyDTO() {
    return baby;
  }

  public void setBabyDTO(BabyDTO baby) {
    this.baby = baby;
  }

  public CurrentSleepSessionDTO() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getPlannedDurationMinutes() {
    return plannedDurationMinutes;
  }

  public void setPlannedDurationMinutes(Integer plannedDurationMinutes) {
    this.plannedDurationMinutes = plannedDurationMinutes;
  }

}