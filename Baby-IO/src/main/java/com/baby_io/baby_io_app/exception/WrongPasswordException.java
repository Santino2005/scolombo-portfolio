package com.baby_io.baby_io_app.exception;

public class WrongPasswordException extends RuntimeException {
  public WrongPasswordException() {
    super("Wrong password");
  }
}
