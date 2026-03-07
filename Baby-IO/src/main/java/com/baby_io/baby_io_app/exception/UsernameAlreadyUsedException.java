package com.baby_io.baby_io_app.exception;

public class UsernameAlreadyUsedException extends RuntimeException {
  public UsernameAlreadyUsedException() {
    super("Username is already used");
  }
}
