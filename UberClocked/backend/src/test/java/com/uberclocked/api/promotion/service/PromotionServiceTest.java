package com.uberclocked.api.promotion.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uberclocked.api.cart.model.entity.Cart;
import com.uberclocked.api.cart.model.entity.CartItem;
import com.uberclocked.api.company.model.entity.Company;
import com.uberclocked.api.company.service.CompanyUserService;
import com.uberclocked.api.product.model.entity.Product;
import com.uberclocked.api.promotion.model.entity.Promotion;
import com.uberclocked.api.promotion.model.entity.PromotionTarget;
import com.uberclocked.api.promotion.repository.PromotionRepository;
import com.uberclocked.api.user.model.entity.User;
import com.uberclocked.api.user.service.UsersService;
import com.uberclocked.api.wheel.model.dto.WheelDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {

  @Mock private CompanyUserService companyUserService;
  @Mock private UsersService userService;
  @Mock private PromotionRepository promotionRepository;

  private PromotionService promotionService;

  @BeforeEach
  void setUp() {
    promotionService = new PromotionService(companyUserService, userService, promotionRepository);
  }

  @Test
  void canApplyPromotion_whenValid_returnsTrue() {
    UUID userId = UUID.randomUUID();
    Promotion promo = new Promotion();
    promo.setActive(true);
    promo.setDiscount(10);
    promo.setTargets(new ArrayList<>());

    Cart cart = new Cart();
    cart.setItems(Collections.emptyList());

    assertTrue(promotionService.canApplyPromotion(userId, promo, cart));
  }

  @Test
  void canApplyPromotion_whenInactive_returnsFalse() {
    UUID userId = UUID.randomUUID();
    Promotion promo = new Promotion();
    promo.setActive(false);

    assertFalse(promotionService.canApplyPromotion(userId, promo, new Cart()));
  }

  @Test
  void canApplyPromotion_whenExpired_returnsFalse() {
    UUID userId = UUID.randomUUID();
    Promotion promo = new Promotion();
    promo.setActive(true);
    promo.setEndDate(LocalDateTime.now().minusDays(1));

    assertFalse(promotionService.canApplyPromotion(userId, promo, new Cart()));
  }

  @Test
  void canApplyPromotion_whenFuture_returnsFalse() {
    UUID userId = UUID.randomUUID();
    Promotion promo = new Promotion();
    promo.setActive(true);
    promo.setStartDate(LocalDateTime.now().plusDays(1));

    assertFalse(promotionService.canApplyPromotion(userId, promo, new Cart()));
  }

  @Test
  void canApplyPromotion_whenWrongUser_returnsFalse() {
    UUID userId = UUID.randomUUID();
    User owner = new User();
    owner.setId(UUID.randomUUID());

    Promotion promo = new Promotion();
    promo.setActive(true);
    promo.setUser(owner);

    assertFalse(promotionService.canApplyPromotion(userId, promo, new Cart()));
  }

  @Test
  void canApplyPromotion_whenExhausted_returnsFalse() {
    UUID userId = UUID.randomUUID();
    Promotion promo = new Promotion();
    promo.setActive(true);
    promo.setMaxUses(2);
    promo.setUsedCount(2);

    assertFalse(promotionService.canApplyPromotion(userId, promo, new Cart()));
  }

  @Test
  void canApplyPromotion_withCompany_checksCompanyUser() {
    UUID userId = UUID.randomUUID();
    Company company = new Company();
    User user = new User();
    user.setId(userId);

    Promotion promo = new Promotion();
    promo.setActive(true);
    promo.setCompany(company);
    promo.setTargets(new ArrayList<>());

    when(userService.getUSerById(userId)).thenReturn(user);
    when(companyUserService.isUserInCompany(user, company)).thenReturn(true);

    assertTrue(promotionService.canApplyPromotion(userId, promo, new Cart()));
  }

  @Test
  void consumePromotion_incrementsAndDeactivatesWhenMaxReached() {
    UUID id = UUID.randomUUID();
    Promotion promo = new Promotion();
    promo.setId(id);
    promo.setActive(true);
    promo.setMaxUses(1);
    promo.setUsedCount(0);

    when(promotionRepository.findById(id)).thenReturn(Optional.of(promo));
    when(promotionRepository.save(promo)).thenReturn(promo);

    promotionService.consumePromotion(id);

    assertEquals(1, promo.getUsedCount());
    assertFalse(promo.isActive());
    verify(promotionRepository).save(promo);
  }

  @Test
  void appliesToItem_withIncludeTarget_returnsTrueWhenMatches() {
    Promotion promo = new Promotion();
    PromotionTarget target = new PromotionTarget();
    target.setMode(PromotionTarget.TargetMode.INCLUDE);
    target.setKind(PromotionTarget.TargetKind.PRODUCT_SKU);
    target.setSku("SKU1");
    promo.setTargets(List.of(target));

    Product product = new Product();
    product.setSkuPrefix("SKU1");

    CartItem item = new CartItem();
    item.setProduct(product);

    assertTrue(promotionService.appliesToItem(promo, item));
  }

  @Test
  void appliesToItem_withExcludeTarget_returnsFalseWhenMatches() {
    Promotion promo = new Promotion();
    PromotionTarget target = new PromotionTarget();
    target.setMode(PromotionTarget.TargetMode.EXCLUDE);
    target.setKind(PromotionTarget.TargetKind.PRODUCT_SKU);
    target.setSku("SKU1");
    promo.setTargets(List.of(target));

    Product product = new Product();
    product.setSkuPrefix("SKU1");

    CartItem item = new CartItem();
    item.setProduct(product);

    assertFalse(promotionService.appliesToItem(promo, item));
  }

  @Test
  void createWheelPromotion_createsAndSaves() {
    User user = new User();
    user.setId(UUID.randomUUID());
    when(userService.getUSerById("user1")).thenReturn(user);
    when(promotionRepository.save(any(Promotion.class)))
        .thenAnswer(
            i -> {
              Promotion p = i.getArgument(0);
              p.setId(UUID.randomUUID());
              return p;
            });

    WheelDto.PromotionDto dto =
        promotionService.createWheelPromotion("user1", "10% OFF", 10, List.of());

    assertNotNull(dto.id);
    assertEquals(10, dto.discount);
    assertTrue(dto.code.startsWith("WHEEL-"));
  }

  @Test
  void appliesToItem_withComponentTypeTarget_matches() {
    Promotion promo = new Promotion();
    PromotionTarget target = new PromotionTarget();
    target.setMode(PromotionTarget.TargetMode.INCLUDE);
    target.setKind(PromotionTarget.TargetKind.COMPONENT_TYPE);
    target.setComponentType("GPU");
    promo.setTargets(List.of(target));

    CartItem item = new CartItem();
    item.setComponents(Map.of("GPU", "GPU_SKU_1"));

    assertTrue(promotionService.appliesToItem(promo, item));
  }

  @Test
  void appliesToItem_withComponentSkuTarget_matches() {
    Promotion promo = new Promotion();
    PromotionTarget target = new PromotionTarget();
    target.setMode(PromotionTarget.TargetMode.INCLUDE);
    target.setKind(PromotionTarget.TargetKind.COMPONENT_SKU);
    target.setSku("GPU_SKU_1");
    promo.setTargets(List.of(target));

    CartItem item = new CartItem();
    item.setComponents(Map.of("GPU", "GPU_SKU_1"));

    assertTrue(promotionService.appliesToItem(promo, item));
  }

  @Test
  void getApplicablePromotions_filtersPromos() {
    UUID userId = UUID.randomUUID();
    Promotion p1 = new Promotion();
    p1.setActive(true);
    p1.setDiscount(10);
    p1.setTargets(new ArrayList<>());

    Promotion p2 = new Promotion();
    p2.setActive(false);

    Cart cart = new Cart();
    cart.setItems(List.of());

    List<Promotion> result =
        promotionService.getApplicablePromotions(userId, cart, List.of(p1, p2));

    assertEquals(1, result.size());
  }

  @Test
  void assertCanApply_whenNotApplicable_throwsException() {
    UUID userId = UUID.randomUUID();
    Promotion promo = new Promotion();
    promo.setActive(false);

    assertThrows(
        IllegalStateException.class,
        () -> promotionService.assertCanApply(userId, promo, new Cart()));
  }
}
