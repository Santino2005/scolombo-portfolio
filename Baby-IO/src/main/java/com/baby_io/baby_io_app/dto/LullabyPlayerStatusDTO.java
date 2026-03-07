package com.baby_io.baby_io_app.dto;

public class LullabyPlayerStatusDTO {
  private Boolean enabled;
  private Boolean connected;
  private Boolean playing;

  public LullabyPlayerStatusDTO() {}

  public LullabyPlayerStatusDTO(Boolean enabled, Boolean connected, Boolean playing) {
    this.enabled = enabled;
    this.connected = connected;
    this.playing = playing;
  }

  public Boolean getEnabled() { return enabled; }
  public void setEnabled(Boolean enabled) { this.enabled = enabled; }

  public Boolean getConnected() { return connected; }
  public void setConnected(Boolean connected) { this.connected = connected; }

  public Boolean getPlaying() { return playing; }
  public void setPlaying(Boolean playing) { this.playing = playing; }

}