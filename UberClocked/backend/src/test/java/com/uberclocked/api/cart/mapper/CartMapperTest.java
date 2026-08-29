package com.uberclocked.api.cart.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.uberclocked.api.cart.model.dto.CartDto;
import com.uberclocked.api.cart.model.dto.CartItemDto;
import com.uberclocked.api.cart.model.entity.Cart;
import com.uberclocked.api.cart.model.entity.CartItem;
import com.uberclocked.api.cart.model.entity.CartStatus;
import com.uberclocked.api.product.model.entity.Product;
import com.uberclocked.api.product.service.ProductService;
import com.uberclocked.api.promotion.model.entity.Promotion;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartMapperTest {

  @Mock private ProductService productService;

  private CartMapper cartMapper;

  @BeforeEach
  void setUp() {
    cartMapper = new CartMapper(productService);
  }

  @Test
  void toDto_withProductAndPromotion_mapsCorrectly() {
    Cart cart = new Cart();
    cart.setId(UUID.randomUUID());
    cart.setStatus(CartStatus.ACTIVE);
    cart.setCreatedAt(LocalDateTime.now());
    cart.setUpdatedAt(LocalDateTime.now());
    cart.setDiscountAmount(15.0);

    Promotion promo = new Promotion();
    promo.setId(UUID.randomUUID());
    promo.setCode("SAVE15");
    promo.setDiscount(15);
    promo.setTitle("Promo Title");
    promo.setDescription("Promo Desc");
    promo.setStartDate(LocalDateTime.now().minusDays(1));
    promo.setEndDate(LocalDateTime.now().plusDays(1));
    cart.setAppliedPromotion(promo);

    Product product = new Product();
    product.setSkuPrefix("GPU1");
    product.setName("RTX 3070");
    product.setStock(5);
    product.setImage(new byte[] {1, 2, 3});

    CartItem item = new CartItem();
    item.setId(UUID.randomUUID());
    item.setName("RTX 3070");
    item.setProduct(product);
    item.setQuantity(2);
    item.setTotalPrice(600.0);

    cart.setItems(List.of(item));

    CartDto dto = cartMapper.toDto(cart);

    assertNotNull(dto);
    assertEquals("ACTIVE", dto.status());
    assertEquals(15.0, dto.discountAmount());
    assertNotNull(dto.appliedPromotion());
    assertEquals("SAVE15", dto.appliedPromotion().code());
    assertEquals(1, dto.items().size());

    CartItemDto itemDto = dto.items().get(0);
    assertEquals("GPU1", itemDto.productSku());
    assertEquals("RTX 3070", itemDto.productName());
    assertEquals(5, itemDto.stock());
    assertEquals(5, itemDto.availableStock());
    assertNotNull(itemDto.image());
  }

  @Test
  void toDto_withCustomPcItem_mapsCorrectly() {
    Cart cart = new Cart();
    cart.setId(UUID.randomUUID());
    cart.setStatus(CartStatus.ACTIVE);

    Product caseProduct = new Product();
    caseProduct.setSkuPrefix("CASE1");
    caseProduct.setName("Case X");
    caseProduct.setStock(4);
    caseProduct.setImage(new byte[] {4, 5, 6});

    Product gpuProduct = new Product();
    gpuProduct.setSkuPrefix("GPU1");
    gpuProduct.setName("GPU X");
    gpuProduct.setStock(2);

    when(productService.getById("CASE1")).thenReturn(caseProduct);
    when(productService.getById("GPU1")).thenReturn(gpuProduct);

    CartItem item = new CartItem();
    item.setId(UUID.randomUUID());
    item.setName("Custom PC");
    item.setComponents(Map.of("CASE", "CASE1", "GPU", "GPU1"));
    item.setQuantity(1);
    item.setTotalPrice(700.0);

    cart.setItems(List.of(item));

    CartDto dto = cartMapper.toDto(cart);

    assertNotNull(dto);
    assertNull(dto.appliedPromotion());
    assertEquals(1, dto.items().size());

    CartItemDto itemDto = dto.items().get(0);
    assertNull(itemDto.productSku());
    assertEquals(2, itemDto.availableStock());
    assertEquals(2, itemDto.componentsStock().get("GPU1"));
    assertEquals(4, itemDto.componentsStock().get("CASE1"));
    assertNotNull(itemDto.image());
  }
}
