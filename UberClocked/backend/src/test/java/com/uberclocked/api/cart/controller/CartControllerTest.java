package com.uberclocked.api.cart.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uberclocked.api.cart.mapper.CartMapper;
import com.uberclocked.api.cart.model.dto.AddCartItemDto;
import com.uberclocked.api.cart.model.dto.CartDto;
import com.uberclocked.api.cart.model.entity.Cart;
import com.uberclocked.api.cart.service.CartService;
import com.uberclocked.api.product.service.ProductService;
import com.uberclocked.api.promotion.model.dto.ApplyCouponRequest;
import com.uberclocked.api.promotion.service.CartPromotionService;
import com.uberclocked.api.security.TestSecurityConfig;
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

@WebMvcTest(CartController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class CartControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private CartService cartService;
  @MockitoBean private ProductService productService;
  @MockitoBean private CartMapper mapper;
  @MockitoBean private CartPromotionService cartPromotionService;

  private CartDto mockCartDto(UUID id) {
    return new CartDto(
        id,
        java.time.LocalDateTime.now(),
        java.time.LocalDateTime.now(),
        "ACTIVE",
        List.of(),
        null,
        0.0);
  }

  @Test
  void getMyCart_returns200() throws Exception {
    UUID cartId = UUID.randomUUID();
    Cart cart = new Cart();
    CartDto dto = mockCartDto(cartId);

    when(cartService.getOrCreateActiveCart(any())).thenReturn(cart);
    when(mapper.toDto(cart)).thenReturn(dto);

    mockMvc
        .perform(get("/carts/me").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(cartId.toString()));
  }

  @Test
  void addItem_returns200() throws Exception {
    UUID cartId = UUID.randomUUID();
    Cart cart = new Cart();
    CartDto dto = mockCartDto(cartId);
    AddCartItemDto itemDto = new AddCartItemDto("GPU1", 1, null);

    when(cartService.addItem(any(), eq("GPU1"), eq(1), any())).thenReturn(cart);
    when(mapper.toDto(cart)).thenReturn(dto);

    mockMvc
        .perform(
            post("/carts/me/items")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(cartId.toString()));
  }

  @Test
  void updateItem_returns200() throws Exception {
    UUID cartId = UUID.randomUUID();
    UUID itemId = UUID.randomUUID();
    Cart cart = new Cart();
    CartDto dto = mockCartDto(cartId);

    when(cartService.getOrCreateActiveCart(any())).thenReturn(cart);
    when(mapper.toDto(cart)).thenReturn(dto);

    mockMvc
        .perform(
            patch("/carts/me/items/" + itemId)
                .param("quantity", "2")
                .with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(cartId.toString()));
  }

  @Test
  void removeItem_returns200() throws Exception {
    UUID cartId = UUID.randomUUID();
    UUID itemId = UUID.randomUUID();
    Cart cart = new Cart();
    CartDto dto = mockCartDto(cartId);

    when(cartService.getOrCreateActiveCart(any())).thenReturn(cart);
    when(mapper.toDto(cart)).thenReturn(dto);

    mockMvc
        .perform(
            delete("/carts/me/items/" + itemId).with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(cartId.toString()));
  }

  @Test
  void checkout_returns200() throws Exception {
    UUID cartId = UUID.randomUUID();
    Cart cart = new Cart();
    CartDto dto = mockCartDto(cartId);

    when(cartService.checkout(any())).thenReturn(cart);
    when(mapper.toDto(cart)).thenReturn(dto);

    mockMvc
        .perform(post("/carts/me/checkout").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(cartId.toString()));
  }

  @Test
  void applyCoupon_returns200() throws Exception {
    UUID cartId = UUID.randomUUID();
    Cart cart = new Cart();
    CartDto dto = mockCartDto(cartId);
    ApplyCouponRequest req = new ApplyCouponRequest("PROMO10");

    when(cartPromotionService.applyCoupon(any(), eq("PROMO10"))).thenReturn(cart);
    when(mapper.toDto(cart)).thenReturn(dto);

    mockMvc
        .perform(
            post("/carts/coupon/apply")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(cartId.toString()));
  }

  @Test
  void removeCoupon_returns200() throws Exception {
    UUID cartId = UUID.randomUUID();
    Cart cart = new Cart();
    CartDto dto = mockCartDto(cartId);

    when(cartPromotionService.removeCoupon(any())).thenReturn(cart);
    when(mapper.toDto(cart)).thenReturn(dto);

    mockMvc
        .perform(post("/carts/coupon/remove").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(cartId.toString()));
  }
}
