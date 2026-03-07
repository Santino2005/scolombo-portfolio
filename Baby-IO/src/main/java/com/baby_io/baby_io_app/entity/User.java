package com.baby_io.baby_io_app.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

  @PrePersist
  public void prePersist() {
    sleepRoutines = new HashSet<>();
    babies = new HashSet<>();
    sensors = new HashSet<>();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  Set<SleepRoutine> sleepRoutines;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  Set<Baby> babies;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private Set<Sensor> sensors;

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {return this.password; }

  public String getEmail() {
    return email;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Baby> getBabies() {
    return babies;
  }

  public Set<SleepRoutine> getSleepRoutines() {
    return sleepRoutines;
  }

  public Set<Sensor> getSensors() {
    return sensors;
  }

  public void setSleepRoutines(Set<SleepRoutine> sleepRoutines) {
    this.sleepRoutines = sleepRoutines;
  }
}
