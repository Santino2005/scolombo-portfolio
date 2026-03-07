package com.baby_io.baby_io_app.dto;

import jakarta.validation.constraints.NotEmpty;

public class UserLoginDTO {

  @NotEmpty(message = "Email or Username is required")
  private String credential;

  @NotEmpty(message = "Password is required")
  private String password;

  public UserLoginDTO() {}

  public UserLoginDTO(String credential, String password) {
    this.credential = credential;
    this.password = password;
  }

  public String getCredential() {
    return credential;
  }

  public String getPassword() {
    return password;
  }
}