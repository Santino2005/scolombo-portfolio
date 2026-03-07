package com.baby_io.baby_io_app.controller;

import com.baby_io.baby_io_app.component.JwtUtil;
import com.baby_io.baby_io_app.dto.UserDTO;
import com.baby_io.baby_io_app.dto.UserLoginDTO;
import com.baby_io.baby_io_app.dto.UserSignUpDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baby_io.baby_io_app.service.UserService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

  private final UserService userService;
  private final JwtUtil jwtUtil;

  @Autowired
  public AuthController(UserService userService, JwtUtil jwtUtil) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/signup")
  public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody UserSignUpDTO dto) {
    UserDTO createdUser = userService.signUp(dto);
    String token = jwtUtil.generateToken(createdUser.getId());

    Map<String, Object> response = new HashMap<>();
    response.put("userId", createdUser);
    response.put("token", token);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginDTO dto) {
    UserDTO user = userService.login(dto);
    String token = jwtUtil.generateToken(user.getId());

    Map<String, Object> response = new HashMap<>();
    response.put("userId", user);
    response.put("token", token);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/me")
  public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    UserDTO user = userService.findUserById(userId).orElse(null);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return ResponseEntity.ok(user);
  }

}