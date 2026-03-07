package com.baby_io.baby_io_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lullabies")
public class Lullaby {

  @PrePersist
  public void prePersist(){
    active = false;
    mediumAlertConfigurations = new HashSet<>();
    highAlertConfigurations = new HashSet<>();
    periodicLullabyConfigurations = new HashSet<>();
    wakeUpLullabyConfigurations = new HashSet<>();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "mediumAlertLullaby")
  private Set<LullabyPlayerConfiguration> mediumAlertConfigurations;

  @OneToMany(mappedBy = "highAlertLullaby")
  private Set<LullabyPlayerConfiguration> highAlertConfigurations;

  @OneToMany(mappedBy = "periodicLullaby")
  private Set<LullabyPlayerConfiguration> periodicLullabyConfigurations;

  @OneToMany(mappedBy = "wakeUpLullaby")
  private Set<LullabyPlayerConfiguration> wakeUpLullabyConfigurations;

  @NotBlank
  @Size(max = 100)
  @Column(nullable = false)
  private String name;

  @Min(1)
  @Max(999)
  @Column(nullable = false, unique = true)
  private Integer songNumber;

  @Size(max = 500)
  private String description;

  @Min(1)
  @Max(3600)
  private Integer durationSeconds;

  @Column(nullable = false)
  private Boolean active;

  @Size(max = 200)
  private String recommendedFor; // "preventing wake up", "deep sleep", "extending sleep"

  public Lullaby() {}
  public Lullaby(String name,
                 Integer songNumber,
                 String description,
                 Integer durationSeconds,
                 Boolean active,
                 String recommendedFor) {
    this.name = name;
    this.songNumber = songNumber;
    this.description = description;
    this.durationSeconds = durationSeconds;
    this.active = active;
    this.recommendedFor = recommendedFor;
  }

  // Getters and Setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public Integer getSongNumber() { return songNumber; }
  public void setSongNumber(Integer songNumber) { this.songNumber = songNumber; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public Integer getDurationSeconds() { return durationSeconds; }
  public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }

  public Boolean getActive() { return active; }
  public void setActive(Boolean active) { this.active = active; }

  public String getRecommendedFor() {
    return recommendedFor;
  }

}
