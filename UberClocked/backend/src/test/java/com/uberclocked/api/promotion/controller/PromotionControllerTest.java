package com.uberclocked.api.promotion.controller;

import static org.mockito.ArgumentMatchers.any;
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
import com.uberclocked.api.cart.repository.CartRepository;
import com.uberclocked.api.company.service.CompanyService;
import com.uberclocked.api.promotion.model.dto.PromotionCreateRequest;
import com.uberclocked.api.promotion.model.dto.PromotionUpdateRequest;
import com.uberclocked.api.promotion.model.entity.Promotion;
import com.uberclocked.api.promotion.repository.PromotionRepository;
import com.uberclocked.api.promotion.service.PromotionService;
import com.uberclocked.api.security.TestSecurityConfig;
import com.uberclocked.api.user.model.entity.User;
import com.uberclocked.api.user.service.UsersService;
import java.util.List;
import java.util.Optional;
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

@WebMvcTest(PromotionController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class PromotionControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private PromotionRepository promotionRepository;
  @MockitoBean private PromotionService promotionService;
  @MockitoBean private UsersService usersService;
  @MockitoBean private CompanyService companyService;
  @MockitoBean private CartRepository cartRepository;

  @Test
  void create_returns200() throws Exception {
    Promotion promo = new Promotion();
    promo.setId(UUID.randomUUID());
    promo.setCode("CODE10");
    promo.setDiscount(10);

    when(promotionRepository.save(any(Promotion.class))).thenReturn(promo);

    PromotionCreateRequest req =
        new PromotionCreateRequest(
            "CODE10", "Title", "Desc", 10, null, null, true, 5, null, null, null);

    mockMvc
        .perform(
            post("/promotions")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("CODE10"));
  }

  @Test
  void list_returns200() throws Exception {
    Promotion promo = new Promotion();
    promo.setId(UUID.randomUUID());
    promo.setCode("CODE10");

    when(promotionRepository.findAll()).thenReturn(List.of(promo));

    mockMvc
        .perform(get("/promotions").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].code").value("CODE10"));
  }

  @Test
  void delete_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    doNothing().when(promotionRepository).deleteById(id);

    mockMvc
        .perform(delete("/promotions/" + id).with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk());

    verify(promotionRepository).deleteById(id);
  }

  @Test
  void update_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    Promotion promo = new Promotion();
    promo.setId(id);
    promo.setCode("CODE20");

    when(promotionRepository.findById(id)).thenReturn(Optional.of(promo));
    when(promotionRepository.save(promo)).thenReturn(promo);

    PromotionUpdateRequest req =
        new PromotionUpdateRequest(
            "CODE20", "Title", "Desc", 20, null, null, true, 10, null, null, null);

    mockMvc
        .perform(
            patch("/promotions/" + id)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("CODE20"));
  }

  @Test
  void applicable_returns200() throws Exception {
    User user = new User();
    user.setId(UUID.randomUUID());

    Promotion promo = new Promotion();
    promo.setId(UUID.randomUUID());
    promo.setCode("CODE10");
    promo.setActive(true);

    when(usersService.getUserOrCreate(any())).thenReturn(user);
    when(cartRepository.findByUserAndStatus(any(), any())).thenReturn(Optional.empty());
    when(promotionRepository.findAll()).thenReturn(List.of(promo));

    mockMvc
        .perform(get("/promotions/applicable").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].code").value("CODE10"));
  }
}
