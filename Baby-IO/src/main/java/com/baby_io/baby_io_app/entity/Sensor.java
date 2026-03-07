  package com.baby_io.baby_io_app.entity;

  import com.baby_io.baby_io_app.types.SensorType;
  import jakarta.persistence.*;
  import java.util.ArrayList;
  import java.util.List;

  @Entity
  @Table(
      name = "sensors",
      uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "sensorType"})
  )
  public class Sensor {

    @PrePersist
    public void prePersist() {
      active = false;
      sensorConfigurations = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensorType sensorType;

    @Column(nullable = false)
    private Boolean active; // Physical device state

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SensorConfiguration> sensorConfigurations;

    public Sensor() {}

    public Sensor(SensorType sensorType) {
      this.sensorType = sensorType;
    }

    public void setUser(User user) {
      this.user = user;
    }

    public void setActive(boolean active) {
      this.active = active;
    }

    public Long getId() {
      return id;
    }

    public SensorType getSensorType() {
      return sensorType;
    }

    public Boolean getActive() {
      return active;
    }


  }