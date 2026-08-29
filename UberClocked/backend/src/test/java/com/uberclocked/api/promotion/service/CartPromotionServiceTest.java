package com.uberclocked.api.promotion.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uberclocked.api.cart.model.entity.Cart;
import com.uberclocked.api.cart.model.entity.CartItem;
import com.uberclocked.api.cart.model.entity.CartStatus;
import com.uberclocked.api.cart.repository.CartRepository;
import com.uberclocked.api.promotion.model.entity.Promotion;
import com.uberclocked.api.promotion.repository.PromotionRepository;
import com.uberclocked.api.user.model.entity.User;
import com.uberclocked.api.user.service.UsersService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

@ExtendWith(MockitoExtension.class)
class CartPromotionServiceTest {

  @Mock private CartRepository cartRepository;
  @Mock private PromotionRepository promotionRepository;
  @Mock private PromotionService promotionService;
  @Mock private UsersService usersService;

  private CartPromotionService cartPromotionService;

  @BeforeEach
  void setUp() {
    cartPromotionService =
        new CartPromotionService(
            cartRepository, promotionRepository, promotionService, usersService);
  }

  private Jwt mockJwt(String sub) {
    return new Jwt(
        "token",
        Instant.now(),
        Instant.now().plusSeconds(3600),
        Map.of("alg", "none"),
        Map.of("sub", sub));
  }

  @Test
  void applyCoupon_whenValid_calculatesDiscountAndSaves() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    user.setId(UUID.randomUUID());

    Cart cart = new Cart();
    CartItem item = new CartItem();
    item.setTotalPrice(100.0);
    cart.setItems(List.of(item));

    Promotion promo = new Promotion();
    promo.setDiscount(20);

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
    when(promotionRepository.findByCodeIgnoreCase("PROMO20")).thenReturn(Optional.of(promo));
    doNothing().when(promotionService).assertCanApply(eq(user.getId()), eq(promo), eq(cart));
    when(promotionService.appliesToItem(promo, item)).thenReturn(true);
    when(cartRepository.save(cart)).thenReturn(cart);

    Cart result = cartPromotionService.applyCoupon(jwt, "PROMO20");

    assertNotNull(result);
    assertEquals(promo, result.getAppliedPromotion());
    assertEquals(20.0, result.getDiscountAmount());
    verify(cartRepository).save(cart);
  }

  @Test
  void removeCoupon_clearsPromotionAndSaves() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    Cart cart = new Cart();
    cart.setAppliedPromotion(new Promotion());
    cart.setDiscountAmount(25.0);

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
    when(cartRepository.save(cart)).thenReturn(cart);

    Cart result = cartPromotionService.removeCoupon(jwt);

    assertNull(result.getAppliedPromotion());
    assertNull(result.getDiscountAmount());
    verify(cartRepository).save(cart);
  }
}
