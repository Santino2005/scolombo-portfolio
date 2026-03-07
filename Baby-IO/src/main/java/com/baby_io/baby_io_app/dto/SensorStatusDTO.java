package com.baby_io.baby_io_app.dto;

import com.baby_io.baby_io_app.types.SensorType;

public class SensorStatusDTO {
  private SensorType sensorType;
  private Boolean enabled;
  private Boolean connected;

  public SensorStatusDTO() {}

  public SensorStatusDTO(SensorType sensorType, Boolean enabled, Boolean connected) {
    this.sensorType = sensorType;
    this.enabled = enabled;
    this.connected = connected;
  }

  // Getters and setters
  public SensorType getSensorType() { return sensorType; }
  public void setSensorType(SensorType sensorType) { this.sensorType = sensorType; }

  public Boolean getConnected() { return connected; }
  public void setConnected(Boolean connected) { this.connected = connected; }

  public Boolean getEnabled() { return enabled; }
  public void setEnabled(Boolean enabled) { this.enabled = enabled; }

}