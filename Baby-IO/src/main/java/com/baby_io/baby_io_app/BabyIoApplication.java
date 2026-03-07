package com.baby_io.baby_io_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class BabyIoApplication {

  public static void main(String[] args) {
    SpringApplication.run(BabyIoApplication.class, args);
  }

  @GetMapping("/hello")
  public String Hello(){
    return "Hello";
  }
}
