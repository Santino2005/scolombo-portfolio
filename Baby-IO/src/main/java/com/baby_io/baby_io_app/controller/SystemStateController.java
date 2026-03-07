package com.baby_io.baby_io_app.controller;

import com.baby_io.baby_io_app.dto.ConnectionDTO;
import com.baby_io.baby_io_app.service.MqttService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/me/system")
public class SystemStateController {

  @Autowired
  private MqttService mqttService;

  @GetMapping("/connection")
  public ResponseEntity<ConnectionDTO> getMqttConnection(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(401).build();
    }

    return ResponseEntity.ok(new ConnectionDTO(mqttService.isConnected()));
  }

}
