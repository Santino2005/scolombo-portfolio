package com.uberclocked.api.wheel.controller;

import com.uberclocked.api.wheel.model.dto.WheelDto;
import com.uberclocked.api.wheel.service.WheelService;
import java.time.ZoneId;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wheel")
public class WheelController {

  private final WheelService wheelService;

  public WheelController(WheelService wheelService) {
    this.wheelService = wheelService;
  }

  @GetMapping("/prizes")
  public java.util.List<WheelDto.WheelPrizeDto> prizes() {
    return wheelService.getPrizes();
  }

  @GetMapping("/status")
  public WheelDto.WheelStatusResponse status(@AuthenticationPrincipal Jwt jwt) {
    String userId = jwt.getSubject();
    return wheelService.status(userId, ZoneId.of("America/Argentina/Buenos_Aires"));
  }

  @PostMapping("/spin")
  public WheelDto.WheelSpinResponse spin(@AuthenticationPrincipal Jwt jwt) {
    String userId = jwt.getSubject();
    return wheelService.spin(userId, ZoneId.of("America/Argentina/Buenos_Aires"));
  }
}
