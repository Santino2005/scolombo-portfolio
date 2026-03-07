package com.baby_io.baby_io_app.dto;

public class LullabyDTO {
  private Long id;
  private boolean active;
  private String name;
  private String description;
  private String recommendedFor;
  private Integer songNumber;
  private Integer durationSeconds;
  private Boolean available;

  // Constructors, getters, and setters
  public LullabyDTO() {}

  public LullabyDTO(Long id,
                    boolean active,
                    String name,
                    String description,
                    String recommendedFor,
                    Integer songNumber,
                    Integer durationSeconds, Boolean available) {
    this.id = id;
    this.active = active;
    this.name = name;
    this.description = description;
    this.recommendedFor = recommendedFor;
    this.songNumber = songNumber;
    this.durationSeconds = durationSeconds;
    this.available = available;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public Integer getSongNumber() { return songNumber; }
  public void setSongNumber(Integer songNumber) { this.songNumber = songNumber; }

  public Integer getDurationSeconds() { return durationSeconds; }
  public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }

  public Boolean getAvailable() { return available; }
  public void setAvailable(Boolean available) { this.available = available; }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Boolean getActive() { return active; }

  public void setRecommendedFor(String recommendedFor) { this.recommendedFor = recommendedFor; }
}