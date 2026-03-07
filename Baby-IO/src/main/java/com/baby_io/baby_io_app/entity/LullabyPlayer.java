package com.baby_io.baby_io_app.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lullaby_player")
public class LullabyPlayer {

  @PrePersist
  public void prePersist(){
    enabled = false;
    configurations = new HashSet<>();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private Boolean enabled;

  @OneToMany(mappedBy = "lullabyPlayer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<LullabyPlayerConfiguration> configurations;

  public LullabyPlayer() {}

  public void setEnabled(boolean active) {
    this.enabled = active;
  }

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public Set<LullabyPlayerConfiguration> getConfigurations() {
    return configurations;
  }

}