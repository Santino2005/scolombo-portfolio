package com.uberclocked.api.purchase.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uberclocked.api.purchase.mapper.PurchaseMapper;
import com.uberclocked.api.purchase.model.dto.PurchaseResponseDto;
import com.uberclocked.api.purchase.model.dto.UpdatePurchaseDto;
import com.uberclocked.api.purchase.model.entity.Purchase;
import com.uberclocked.api.purchase.model.entity.PurchaseStatus;
import com.uberclocked.api.purchase.service.PurchaseService;
import com.uberclocked.api.security.TestSecurityConfig;
import java.time.LocalDateTime;
import java.util.List;
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

@WebMvcTest(PurchaseController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class PurchaseControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private PurchaseService purchaseService;
  @MockitoBean private PurchaseMapper purchaseMapper;

  @Test
  void create_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    Purchase purchase = new Purchase();
    PurchaseResponseDto dto =
        new PurchaseResponseDto(
            id,
            PurchaseStatus.PAID,
            200.0,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            UUID.randomUUID(),
            List.of());

    when(purchaseService.createPurchase(any())).thenReturn(purchase);
    when(purchaseMapper.toDto(purchase)).thenReturn(dto);

    mockMvc
        .perform(post("/purchases/me").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()));
  }

  @Test
  void myPurchases_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    Purchase purchase = new Purchase();
    PurchaseResponseDto dto =
        new PurchaseResponseDto(
            id,
            PurchaseStatus.PAID,
            200.0,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            UUID.randomUUID(),
            List.of());

    when(purchaseService.getMyPurchases(any())).thenReturn(List.of(purchase));
    when(purchaseMapper.toDtoList(List.of(purchase))).thenReturn(List.of(dto));

    mockMvc
        .perform(get("/purchases/me").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(id.toString()));
  }

  @Test
  void getAll_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    Purchase purchase = new Purchase();
    PurchaseResponseDto dto =
        new PurchaseResponseDto(
            id,
            PurchaseStatus.PAID,
            200.0,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            UUID.randomUUID(),
            List.of());

    when(purchaseService.getAllPurchases()).thenReturn(List.of(purchase));
    when(purchaseMapper.toDtoList(List.of(purchase))).thenReturn(List.of(dto));

    mockMvc
        .perform(get("/purchases").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(id.toString()));
  }

  @Test
  void update_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    Purchase purchase = new Purchase();
    PurchaseResponseDto dto =
        new PurchaseResponseDto(
            id,
            PurchaseStatus.DELIVERED,
            200.0,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            UUID.randomUUID(),
            List.of());

    when(purchaseService.updatePurchase(eq(id), any(UpdatePurchaseDto.class), any()))
        .thenReturn(purchase);
    when(purchaseMapper.toDto(purchase)).thenReturn(dto);

    UpdatePurchaseDto updateDto = new UpdatePurchaseDto(PurchaseStatus.DELIVERED, null);

    mockMvc
        .perform(
            patch("/purchases/" + id)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("DELIVERED"));
  }

  @Test
  void delete_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    doNothing().when(purchaseService).deletePurchase(eq(id), any());

    mockMvc
        .perform(delete("/purchases/" + id).with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk());

    verify(purchaseService).deletePurchase(eq(id), any());
  }
}
