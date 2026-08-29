package com.uberclocked.api.payment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uberclocked.api.payment.model.dto.IdentificationDto;
import com.uberclocked.api.payment.model.dto.InterestedInfoPaymentDto;
import com.uberclocked.api.payment.model.dto.InterestedInfoPreferenceRequest;
import com.uberclocked.api.payment.model.dto.MpBrickSubmitDto;
import com.uberclocked.api.payment.model.dto.PayerDto;
import com.uberclocked.api.payment.model.dto.PaymentDto;
import com.uberclocked.api.payment.model.dto.PaymentStatus;
import com.uberclocked.api.payment.model.dto.PreferenceDto;
import com.uberclocked.api.payment.service.MercadoPagoService;
import com.uberclocked.api.security.TestSecurityConfig;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MercadoPagoController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class MercadoPagoControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private MercadoPagoService mpService;

  @Test
  void createPreference_returns200() throws Exception {
    PreferenceDto dto = new PreferenceDto("pref_123");
    when(mpService.createPreference(any())).thenReturn(dto);

    mockMvc
        .perform(post("/mp/preference").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("pref_123"));
  }

  @Test
  void createPayment_returns200() throws Exception {
    PaymentDto dto = new PaymentDto(UUID.randomUUID(), 999L, PaymentStatus.APPROVED);
    when(mpService.createPayment(any(), any())).thenReturn(dto);

    PayerDto payer = new PayerDto("test@mail.com", new IdentificationDto("DNI", "12345678"));
    MpBrickSubmitDto body =
        new MpBrickSubmitDto(UUID.randomUUID(), "token123", "visa", "issuer1", 1, payer);

    mockMvc
        .perform(
            post("/mp/payment")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.payment_id").value(999));
  }

  @Test
  void createInterestedInfoPayment_returns200() throws Exception {
    PaymentDto dto = new PaymentDto(null, 888L, PaymentStatus.APPROVED);
    when(mpService.createInterestedInfoPayment(any(), any())).thenReturn(dto);

    PayerDto payer = new PayerDto("test@mail.com", new IdentificationDto("DNI", "12345678"));
    InterestedInfoPaymentDto body =
        new InterestedInfoPaymentDto(
            UUID.randomUUID(), UUID.randomUUID(), "token123", "visa", "issuer1", 1, payer);

    mockMvc
        .perform(
            post("/mp/payment/interested-info")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.payment_id").value(888));
  }

  @Test
  void createInterestedInfoPreference_returns200() throws Exception {
    PreferenceDto dto = new PreferenceDto("pref_interest_123");
    when(mpService.createInterestedInfoPreference(any(), any())).thenReturn(dto);

    InterestedInfoPreferenceRequest body =
        new InterestedInfoPreferenceRequest(UUID.randomUUID(), UUID.randomUUID());

    mockMvc
        .perform(
            post("/mp/preference/interested-info")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("pref_interest_123"));
  }
}
