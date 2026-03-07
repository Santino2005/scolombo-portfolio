package com.baby_io.baby_io_app.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_state")
public class SystemState {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private com.baby_io.baby_io_app.types.SystemState status = com.baby_io.baby_io_app.types.SystemState.IDLE;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "active_sleep_session_id")
  private SleepSession activeSleepSession;

  @Column(nullable = false)
  private Boolean alertActive = false;

  @Column(nullable = false)
  private Boolean userAttending = false;

  @Column(nullable = false)
  private LocalDateTime lastUpdated = LocalDateTime.now();

}