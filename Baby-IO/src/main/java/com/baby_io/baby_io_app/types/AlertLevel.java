package com.baby_io.baby_io_app.types;

public enum AlertLevel {
  NONE("none", 0),
  MEDIUM("medium", 1),
  HIGH("high", 2);

  private final String name;
  private final int priority;

  AlertLevel(String name, int priority) {
    this.name = name;
    this.priority = priority;
  }

  public String getName() { return name; }
  public int getPriority() { return priority; }
}