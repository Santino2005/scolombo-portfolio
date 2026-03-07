package com.baby_io.baby_io_app.dto;

import com.baby_io.baby_io_app.types.SensorType;

public class SensorValueDTO {
  private SensorType sensorType;
  private Double value;

  public SensorValueDTO() {}

  public SensorValueDTO(SensorType sensorType, Double value) {
    this.sensorType = sensorType;
    this.value = value;
  }

  public SensorType getSensorType() {
    return sensorType;
  }

  public void setSensorType(SensorType sensorType) {
    this.sensorType = sensorType;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

}

