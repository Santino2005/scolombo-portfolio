package com.baby_io.baby_io_app.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "babies")
public class Baby {

  @PrePersist
  public void prePersist(){
    sleepRoutines = new HashSet<>();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToMany
  @JoinTable(
      name = "baby_sleep_routines",
      joinColumns = @JoinColumn(name = "baby_id"),
      inverseJoinColumns = @JoinColumn(name = "routine_id")
  )
  private Set<SleepRoutine> sleepRoutines;

  @OneToOne(mappedBy = "baby", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
  private SleepHistory sleepHistory;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String gender;

  @Column(nullable = false)
  private int ageInMonths;

  @Column(nullable = false)
  private Boolean isBioVulnerable = false;

  @Column
  private String medicalNotes;

  @Column
  private Double weightInKilograms;

  @OneToMany(mappedBy = "baby", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<SleepSession> sleepSessions;

  @Column(nullable = false)
  private boolean isSelected = false;

  public Long getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getGender() {
    return gender;
  }

  public void setAgeInMonths(int ageInMonths) {
    this.ageInMonths = ageInMonths;
  }

  public Integer getAgeInMonths() {
    return ageInMonths;
  }

  public void setIsBioVulnerable(Boolean isBioVulnerable) {
    this.isBioVulnerable = isBioVulnerable;
  }

  public Boolean getIsBioVulnerable() {
    return isBioVulnerable;
  }

  public void setMedicalNotes(String medicalNotes) {
    this.medicalNotes = medicalNotes;
  }

  public String getMedicalNotes() {
    return medicalNotes;
  }

  public void setWeightInKilograms(Double weightInKilograms) {
    this.weightInKilograms = weightInKilograms;
  }

  public Double getWeightInKilograms() {
    return weightInKilograms;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Set<SleepRoutine> getSleepRoutines() {
    return sleepRoutines;
  }

  public void setSleepHistory(SleepHistory sleepHistory) {
    this.sleepHistory = sleepHistory;
  }

  public SleepHistory getSleepHistory() {
    return sleepHistory;
  }

  public void setSelected(boolean isSelected) {
    this.isSelected = isSelected;
  }

  public boolean getIsSelected() {
    return isSelected;
  }

  public void setSleepRoutines(Set<SleepRoutine> sleepRoutines) {
    this.sleepRoutines = sleepRoutines;
  }

}
