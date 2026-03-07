package com.baby_io.baby_io_app.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.baby_io.baby_io_app.service.UserService;

@RestController
@RequestMapping("/api/auth/me")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteUser(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) return ResponseEntity.status(401).build();

    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }
}