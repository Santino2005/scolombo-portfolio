package com.uberclocked.api.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.uberclocked.api.cart.model.entity.Cart;
import com.uberclocked.api.cart.model.entity.CartItem;
import com.uberclocked.api.cart.service.CartService;
import com.uberclocked.api.emailData.AdminConfig;
import com.uberclocked.api.emailData.EmailService;
import com.uberclocked.api.market.service.PostInterestService;
import com.uberclocked.api.payment.model.dto.IdentificationDto;
import com.uberclocked.api.payment.model.dto.InterestedInfoPaymentDto;
import com.uberclocked.api.payment.model.dto.InterestedInfoPreferenceRequest;
import com.uberclocked.api.payment.model.dto.MpBrickSubmitDto;
import com.uberclocked.api.payment.model.dto.PayerDto;
import com.uberclocked.api.payment.model.dto.PaymentDto;
import com.uberclocked.api.payment.model.dto.PaymentStatus;
import com.uberclocked.api.payment.model.dto.PreferenceDto;
import com.uberclocked.api.payment.repository.MercadoPagoRepository;
import com.uberclocked.api.purchase.model.entity.Purchase;
import com.uberclocked.api.purchase.service.PurchaseService;
import com.uberclocked.api.user.model.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

@ExtendWith(MockitoExtension.class)
class MercadoPagoServiceTest {

  @Mock private MercadoPagoRepository mpRepository;
  @Mock private CartService cartService;
  @Mock private PurchaseService purchaseService;
  @Mock private PostInterestService postService;
  @Mock private AdminConfig adminConfig;
  @Mock private EmailService emailService;

  private MercadoPagoService mpService;

  @BeforeEach
  void setUp() {
    mpService =
        new MercadoPagoService(
            cartService, purchaseService, mpRepository, postService, adminConfig, emailService);
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
  void createPreference_returnsPreferenceDto() {
    Jwt jwt = mockJwt("auth0|1");
    Cart cart = new Cart();
    cart.setId(UUID.randomUUID());
    CartItem item = new CartItem();
    item.setId(UUID.randomUUID());
    item.setName("GPU");
    item.setQuantity(1);
    item.setTotalPrice(500.0);
    cart.setItems(List.of(item));

    when(cartService.getOrCreateActiveCart(jwt)).thenReturn(cart);
    when(cartService.subtotal(cart)).thenReturn(500.0);
    when(cartService.totalToPay(cart)).thenReturn(550.0);

    Preference mpPref = mock(Preference.class);
    when(mpPref.getId()).thenReturn("mp_pref_123");
    when(mpRepository.createPreference(any())).thenReturn(mpPref);

    PreferenceDto result = mpService.createPreference(jwt);

    assertNotNull(result);
    assertEquals("mp_pref_123", result.id());
  }

  @Test
  void createInterestedInfoPreference_returnsPreferenceDto() {
    Jwt jwt = mockJwt("auth0|1");
    InterestedInfoPreferenceRequest body =
        new InterestedInfoPreferenceRequest(UUID.randomUUID(), UUID.randomUUID());

    Preference mpPref = mock(Preference.class);
    when(mpPref.getId()).thenReturn("mp_interest_pref_123");
    when(mpRepository.createPreference(any())).thenReturn(mpPref);

    PreferenceDto result = mpService.createInterestedInfoPreference(jwt, body);

    assertNotNull(result);
    assertEquals("mp_interest_pref_123", result.id());
  }

  @Test
  void createInterestedInfoPayment_processesAndBuysInfo() {
    Jwt jwt = mockJwt("auth0|1");
    UUID postId = UUID.randomUUID();
    UUID interestedUserId = UUID.randomUUID();
    PayerDto payer = new PayerDto("test@mail.com", new IdentificationDto("DNI", "12345678"));
    InterestedInfoPaymentDto body =
        new InterestedInfoPaymentDto(postId, interestedUserId, "token", "visa", "issuer", 1, payer);

    Payment payment = mock(Payment.class);
    when(payment.getId()).thenReturn(777L);
    when(mpRepository.createPayment(any())).thenReturn(payment);

    PaymentDto result = mpService.createInterestedInfoPayment(jwt, body);

    assertNotNull(result);
    assertEquals(777L, result.payment_id());
    assertEquals(PaymentStatus.APPROVED, result.status());
    verify(postService).buyInterestedInfo(postId, interestedUserId, jwt);
  }

  @Test
  void createPayment_processesAndNotifiesAdmin() {
    Jwt jwt = mockJwt("auth0|1");
    UUID purchaseId = UUID.randomUUID();

    User user = new User();
    user.setUserName("buyer");
    user.setEmail("buyer@mail.com");

    Cart cart = new Cart();
    CartItem item = new CartItem();
    item.setId(UUID.randomUUID());
    item.setName("GPU");
    item.setQuantity(1);
    item.setTotalPrice(300.0);
    cart.setItems(List.of(item));

    Purchase purchase = new Purchase();
    purchase.setId(purchaseId);
    purchase.setUser(user);
    purchase.setCart(cart);
    purchase.setTotalAmount(350.0);

    PayerDto payer = new PayerDto("buyer@mail.com", new IdentificationDto("DNI", "12345678"));
    MpBrickSubmitDto body =
        new MpBrickSubmitDto(UUID.randomUUID(), "token", "visa", "issuer", 1, payer);

    Payment payment = mock(Payment.class);
    when(payment.getId()).thenReturn(111L);

    when(purchaseService.createPurchase(jwt)).thenReturn(purchase);
    when(mpRepository.createPayment(any())).thenReturn(payment);
    when(adminConfig.getAdminEmails()).thenReturn(List.of("admin@mail.com"));

    PaymentDto result = mpService.createPayment(jwt, body);

    assertNotNull(result);
    assertEquals(111L, result.payment_id());
    assertEquals(PaymentStatus.APPROVED, result.status());
    verify(emailService).sendToMany(eq(List.of("admin@mail.com")), any(), any());
  }
}
