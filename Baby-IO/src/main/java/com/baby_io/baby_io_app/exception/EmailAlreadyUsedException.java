package com.baby_io.baby_io_app.exception;

public class EmailAlreadyUsedException extends RuntimeException {
  public EmailAlreadyUsedException() {
    super("Email is already used");
  }
}
