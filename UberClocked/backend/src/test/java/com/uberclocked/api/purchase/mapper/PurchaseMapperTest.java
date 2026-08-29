package com.uberclocked.api.purchase.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.uberclocked.api.cart.mapper.CartMapper;
import com.uberclocked.api.cart.model.dto.CartItemDto;
import com.uberclocked.api.cart.model.entity.Cart;
import com.uberclocked.api.cart.model.entity.CartItem;
import com.uberclocked.api.purchase.model.dto.PurchaseResponseDto;
import com.uberclocked.api.purchase.model.entity.Purchase;
import com.uberclocked.api.purchase.model.entity.PurchaseStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PurchaseMapperTest {

  @Mock private CartMapper cartMapper;

  private PurchaseMapper purchaseMapper;

  @BeforeEach
  void setUp() {
    purchaseMapper = new PurchaseMapper(cartMapper);
  }

  @Test
  void toDto_whenNull_returnsNull() {
    assertNull(purchaseMapper.toDto(null));
  }

  @Test
  void toDto_whenValid_mapsFields() {
    UUID purchaseId = UUID.randomUUID();
    UUID cartId = UUID.randomUUID();

    Cart cart = new Cart();
    cart.setId(cartId);
    CartItem item = new CartItem();
    cart.setItems(List.of(item));

    Purchase purchase = new Purchase();
    purchase.setId(purchaseId);
    purchase.setStatus(PurchaseStatus.PAID);
    purchase.setTotalAmount(250.0);
    purchase.setCreatedAt(LocalDateTime.now());
    purchase.setUpdatedAt(LocalDateTime.now());
    purchase.setCart(cart);

    CartItemDto itemDto =
        new CartItemDto(
            UUID.randomUUID(), "Item", null, 5, 5, 1, 250.0, "SKU1", "Item", null, null);
    when(cartMapper.toItemDto(any())).thenReturn(itemDto);

    PurchaseResponseDto dto = purchaseMapper.toDto(purchase);

    assertNotNull(dto);
    assertEquals(purchaseId, dto.id());
    assertEquals(PurchaseStatus.PAID, dto.status());
    assertEquals(250.0, dto.totalAmount());
    assertEquals(cartId, dto.cartId());
    assertEquals(1, dto.items().size());
  }

  @Test
  void toDtoList_mapsList() {
    Purchase purchase = new Purchase();
    purchase.setId(UUID.randomUUID());
    purchase.setCart(new Cart());

    List<PurchaseResponseDto> list = purchaseMapper.toDtoList(List.of(purchase));

    assertEquals(1, list.size());
  }
}
