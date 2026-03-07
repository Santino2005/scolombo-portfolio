package com.baby_io.baby_io_app.types;

public enum SensorType {
  TEMPERATURE("temperature", "°C"),
  HUMIDITY("humidity", "%"),
  SOUND("sound", "%"),
  MOTION("motion", "%");

  private final String name;
  private final String unit;

  SensorType(String name, String unit) {
    this.name = name;
    this.unit = unit;
  }

  public String getName() { return name; }
  public String getUnit() { return unit; }
}
