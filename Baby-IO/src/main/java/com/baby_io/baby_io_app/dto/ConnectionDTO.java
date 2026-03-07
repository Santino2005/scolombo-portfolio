package com.baby_io.baby_io_app.dto;

public class ConnectionDTO {

  private boolean connected;

  public ConnectionDTO() {}

  public ConnectionDTO(boolean connected) {
    this.connected = connected;
  }

  public boolean isConnected() {
    return connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }

}