package com.uberclocked.api.purchase.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uberclocked.api.cart.model.entity.Cart;
import com.uberclocked.api.cart.model.entity.CartItem;
import com.uberclocked.api.cart.service.CartService;
import com.uberclocked.api.emailData.EmailService;
import com.uberclocked.api.purchase.model.dto.UpdatePurchaseDto;
import com.uberclocked.api.purchase.model.entity.Purchase;
import com.uberclocked.api.purchase.model.entity.PurchaseStatus;
import com.uberclocked.api.purchase.repository.PurchaseRepository;
import com.uberclocked.api.user.model.entity.User;
import com.uberclocked.api.user.service.UsersService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
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
class PurchaseServiceTest {

  @Mock private PurchaseRepository purchaseRepository;
  @Mock private CartService cartService;
  @Mock private UsersService usersService;
  @Mock private EmailService emailService;

  private PurchaseService purchaseService;

  @BeforeEach
  void setUp() {
    purchaseService =
        new PurchaseService(purchaseRepository, cartService, usersService, emailService);
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
  void getPurchase_returnsReference() {
    UUID id = UUID.randomUUID();
    Purchase p = new Purchase();
    when(purchaseRepository.getReferenceById(id)).thenReturn(p);

    assertEquals(p, purchaseService.getPurchase(id));
  }

  @Test
  void createPurchase_whenCartNotEmpty_createsPurchase() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    Cart cart = new Cart();
    CartItem item = new CartItem();
    cart.setItems(List.of(item));

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartService.checkout(jwt)).thenReturn(cart);
    when(cartService.totalToPay(cart)).thenReturn(250.0);
    when(purchaseRepository.save(any(Purchase.class))).thenAnswer(i -> i.getArgument(0));

    Purchase purchase = purchaseService.createPurchase(jwt);

    assertNotNull(purchase);
    assertEquals(250.0, purchase.getTotalAmount());
    assertEquals(PurchaseStatus.PAID, purchase.getStatus());
  }

  @Test
  void createPurchase_whenCartEmpty_throwsIllegalStateException() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    Cart cart = new Cart();
    cart.setItems(Collections.emptyList());

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartService.checkout(jwt)).thenReturn(cart);

    assertThrows(IllegalStateException.class, () -> purchaseService.createPurchase(jwt));
  }

  @Test
  void getMyPurchases_returnsList() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(purchaseRepository.findByUser(user)).thenReturn(List.of(new Purchase()));

    assertEquals(1, purchaseService.getMyPurchases(jwt).size());
  }

  @Test
  void getAllPurchases_returnsList() {
    when(purchaseRepository.findAll()).thenReturn(List.of(new Purchase()));
    assertEquals(1, purchaseService.getAllPurchases().size());
  }

  @Test
  void updatePurchase_updatesAndSendsEmail() {
    UUID id = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    user.setEmail("user@mail.com");

    Purchase purchase = new Purchase();
    purchase.setId(id);
    purchase.setUser(user);
    purchase.setStatus(PurchaseStatus.PAID);

    when(purchaseRepository.findById(id)).thenReturn(Optional.of(purchase));
    when(purchaseRepository.save(purchase)).thenReturn(purchase);

    LocalDateTime pickup = LocalDateTime.now().plusDays(2);
    UpdatePurchaseDto dto = new UpdatePurchaseDto(PurchaseStatus.DELIVERED, pickup);

    Purchase result = purchaseService.updatePurchase(id, dto, jwt);

    assertEquals(PurchaseStatus.DELIVERED, result.getStatus());
    assertEquals(pickup, result.getPickupDate());
    verify(emailService).sendMail(eq("user@mail.com"), any(), any());
  }

  @Test
  void deletePurchase_setsCancelled() {
    UUID id = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|1");

    Purchase purchase = new Purchase();
    purchase.setId(id);
    purchase.setStatus(PurchaseStatus.PAID);

    when(purchaseRepository.findById(id)).thenReturn(Optional.of(purchase));
    when(purchaseRepository.save(purchase)).thenReturn(purchase);

    purchaseService.deletePurchase(id, jwt);

    assertEquals(PurchaseStatus.CANCELLED, purchase.getStatus());
    verify(purchaseRepository).save(purchase);
  }
}
