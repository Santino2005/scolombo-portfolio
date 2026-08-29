package com.uberclocked.api.wheel.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.uberclocked.api.security.TestSecurityConfig;
import com.uberclocked.api.wheel.model.dto.WheelDto;
import com.uberclocked.api.wheel.service.WheelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WheelController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class WheelControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private WheelService wheelService;

  @Test
  void status_returns200() throws Exception {
    WheelDto.WheelStatusResponse res = new WheelDto.WheelStatusResponse();
    res.canSpin = true;

    when(wheelService.status(eq("auth0|123"), any())).thenReturn(res);

    mockMvc
        .perform(
            get("/wheel/status")
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(j -> j.subject("auth0|123"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.canSpin").value(true));
  }

  @Test
  void spin_returns200() throws Exception {
    WheelDto.WheelSpinResponse res = new WheelDto.WheelSpinResponse();
    res.canSpin = true;

    when(wheelService.spin(eq("auth0|123"), any())).thenReturn(res);

    mockMvc
        .perform(
            post("/wheel/spin")
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(j -> j.subject("auth0|123"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.canSpin").value(true));
  }
}
