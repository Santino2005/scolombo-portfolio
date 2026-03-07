package com.baby_io.baby_io_app.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
    super("User does not exist");
  }
}
