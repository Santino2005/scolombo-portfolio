package com.uberclocked.api.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uberclocked.api.cart.model.entity.Cart;
import com.uberclocked.api.cart.model.entity.CartItem;
import com.uberclocked.api.cart.model.entity.CartStatus;
import com.uberclocked.api.cart.repository.CartItemRepository;
import com.uberclocked.api.cart.repository.CartRepository;
import com.uberclocked.api.product.model.entity.Product;
import com.uberclocked.api.product.service.ProductService;
import com.uberclocked.api.promotion.service.PromotionService;
import com.uberclocked.api.user.model.entity.User;
import com.uberclocked.api.user.service.UsersService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
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
class CartServiceTest {

  @Mock private CartRepository cartRepository;
  @Mock private CartItemRepository itemRepository;
  @Mock private ProductService productService;
  @Mock private UsersService usersService;
  @Mock private PromotionService promotionService;

  private CartService cartService;

  @BeforeEach
  void setUp() {
    cartService =
        new CartService(
            cartRepository, itemRepository, productService, usersService, promotionService);
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
  void getOrCreateActiveCart_whenExists_returnsCart() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    Cart cart = new Cart();

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));

    Cart result = cartService.getOrCreateActiveCart(jwt);

    assertEquals(cart, result);
  }

  @Test
  void totalToPay_calculatesWithCheckoutFeeAndDiscount() {
    Cart cart = new Cart();
    CartItem item1 = new CartItem();
    item1.setTotalPrice(100.0);
    CartItem item2 = new CartItem();
    item2.setTotalPrice(50.0);
    cart.setItems(List.of(item1, item2));
    cart.setDiscountAmount(20.0);

    double total = cartService.totalToPay(cart);

    // subtotal 150 + 50 checkout fee - 20 discount = 180
    assertEquals(180.0, total);
  }

  @Test
  void addItem_productItem_addsAndSaves() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    Cart cart = new Cart();
    cart.setItems(new ArrayList<>());

    Product product = new Product();
    product.setSkuPrefix("GPU1");
    product.setName("RTX 3060");
    product.setPrice(300.0);
    product.setStock(10);

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
    when(productService.getById("GPU1")).thenReturn(product);
    when(cartRepository.save(cart)).thenReturn(cart);

    Cart result = cartService.addItem(jwt, "GPU1", 2, null);

    assertNotNull(result);
    assertEquals(1, cart.getItems().size());
    assertEquals(600.0, cart.getItems().get(0).getTotalPrice());
    verify(cartRepository).save(cart);
  }

  @Test
  void addItem_customPc_addsAndSaves() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    Cart cart = new Cart();
    cart.setItems(new ArrayList<>());

    Product caseProduct = new Product();
    caseProduct.setSkuPrefix("CASE1");
    caseProduct.setName("Mid Tower Case");
    caseProduct.setPrice(100.0);
    caseProduct.setStock(5);

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
    when(productService.getById("CASE1")).thenReturn(caseProduct);
    when(cartRepository.save(cart)).thenReturn(cart);

    Map<String, String> components = new HashMap<>();
    components.put("CASE", "CASE1");

    Cart result = cartService.addItem(jwt, null, 1, components);

    assertNotNull(result);
    assertEquals(1, cart.getItems().size());
    assertEquals("Custom PC", cart.getItems().get(0).getName());
    assertEquals(100.0, cart.getItems().get(0).getTotalPrice());
  }

  @Test
  void setItemQuantity_whenPositive_updatesQuantity() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    UUID cartId = UUID.randomUUID();
    UUID itemId = UUID.randomUUID();

    Cart cart = new Cart();
    cart.setId(cartId);
    cart.setItems(new ArrayList<>());

    Product product = new Product();
    product.setSkuPrefix("GPU1");
    product.setPrice(100.0);
    product.setStock(10);

    CartItem item = new CartItem();
    item.setId(itemId);
    item.setProduct(product);
    item.setQuantity(1);
    item.setTotalPrice(100.0);

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
    when(itemRepository.findByIdAndCartId(itemId, cartId)).thenReturn(Optional.of(item));
    when(productService.getById("GPU1")).thenReturn(product);
    when(itemRepository.save(item)).thenReturn(item);

    CartItem result = cartService.setItemQuantity(jwt, itemId, 3);

    assertEquals(3, result.getQuantity());
    assertEquals(300.0, result.getTotalPrice());
    verify(itemRepository).save(item);
  }

  @Test
  void removeItem_deletesItem() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    UUID cartId = UUID.randomUUID();
    UUID itemId = UUID.randomUUID();

    Cart cart = new Cart();
    cart.setId(cartId);
    cart.setItems(new ArrayList<>());

    CartItem item = new CartItem();
    item.setId(itemId);

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
    when(itemRepository.findByIdAndCartId(itemId, cartId)).thenReturn(Optional.of(item));

    cartService.removeItem(jwt, itemId);

    verify(itemRepository).delete(item);
  }

  @Test
  void checkout_completesCartAndDecreasesStock() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    user.setId(UUID.randomUUID());

    Cart cart = new Cart();
    cart.setUser(user);
    cart.setStatus(CartStatus.ACTIVE);

    Product product = new Product();
    product.setSkuPrefix("GPU1");

    CartItem item = new CartItem();
    item.setProduct(product);
    item.setQuantity(2);
    cart.setItems(List.of(item));

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
    when(cartRepository.save(cart)).thenReturn(cart);

    Cart result = cartService.checkout(jwt);

    assertEquals(CartStatus.COMPLETED, result.getStatus());
    verify(productService).decreaseStock("GPU1", 2);
    verify(cartRepository).save(cart);
  }

  @Test
  void getCart_returnsCart() {
    UUID cartId = UUID.randomUUID();
    Cart cart = new Cart();
    when(cartRepository.getReferenceById(cartId)).thenReturn(cart);

    assertEquals(cart, cartService.getCart(cartId));
  }

  @Test
  void setItemQuantity_whenZero_deletesItem() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    UUID cartId = UUID.randomUUID();
    UUID itemId = UUID.randomUUID();

    Cart cart = new Cart();
    cart.setId(cartId);
    cart.setItems(new ArrayList<>());

    CartItem item = new CartItem();
    item.setId(itemId);

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
    when(itemRepository.findByIdAndCartId(itemId, cartId)).thenReturn(Optional.of(item));

    CartItem result = cartService.setItemQuantity(jwt, itemId, 0);

    assertEquals(item, result);
    verify(itemRepository).delete(item);
  }

  @Test
  void setItemQuantity_forCustomPc_updatesQuantityAndPrice() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    UUID cartId = UUID.randomUUID();
    UUID itemId = UUID.randomUUID();

    Cart cart = new Cart();
    cart.setId(cartId);
    cart.setItems(new ArrayList<>());

    Product p1 = new Product();
    p1.setSkuPrefix("CASE1");
    p1.setPrice(100.0);
    p1.setStock(10);

    CartItem item = new CartItem();
    item.setId(itemId);
    item.setComponents(new HashMap<>(Map.of("CASE", "CASE1")));
    item.setQuantity(1);
    item.setTotalPrice(100.0);

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
    when(itemRepository.findByIdAndCartId(itemId, cartId)).thenReturn(Optional.of(item));
    when(productService.getById("CASE1")).thenReturn(p1);
    when(itemRepository.save(item)).thenReturn(item);

    CartItem result = cartService.setItemQuantity(jwt, itemId, 2);

    assertEquals(2, result.getQuantity());
    assertEquals(200.0, result.getTotalPrice());
  }

  @Test
  void updateComponentInItem_updatesComponentAndPrice() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    UUID cartId = UUID.randomUUID();
    UUID itemId = UUID.randomUUID();

    Cart cart = new Cart();
    cart.setId(cartId);
    cart.setItems(new ArrayList<>());

    Product p1 = new Product();
    p1.setSkuPrefix("GPU2");
    p1.setPrice(400.0);
    p1.setStock(5);

    CartItem item = new CartItem();
    item.setId(itemId);
    item.setComponents(new HashMap<>(Map.of("GPU", "GPU1")));
    item.setQuantity(1);

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
    when(itemRepository.findByIdAndCartId(itemId, cartId)).thenReturn(Optional.of(item));
    when(productService.getById("GPU2")).thenReturn(p1);
    when(itemRepository.save(item)).thenReturn(item);

    CartItem result = cartService.updateComponentInItem(jwt, itemId, "GPU", "GPU2");

    assertEquals("GPU2", result.getComponents().get("GPU"));
    assertEquals(400.0, result.getTotalPrice());
  }

  @Test
  void replaceComponents_updatesComponentsAndPrice() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    UUID cartId = UUID.randomUUID();
    UUID itemId = UUID.randomUUID();

    Cart cart = new Cart();
    cart.setId(cartId);
    cart.setItems(new ArrayList<>());

    Product pCase = new Product();
    pCase.setSkuPrefix("CASE1");
    pCase.setPrice(100.0);
    pCase.setStock(5);

    CartItem item = new CartItem();
    item.setId(itemId);
    item.setQuantity(1);

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
    when(itemRepository.findByIdAndCartId(itemId, cartId)).thenReturn(Optional.of(item));
    when(productService.getById("CASE1")).thenReturn(pCase);
    when(itemRepository.save(item)).thenReturn(item);

    CartItem result = cartService.replaceComponents(jwt, itemId, Map.of("CASE", "CASE1"));

    assertNotNull(result);
    assertEquals(100.0, result.getTotalPrice());
  }

  @Test
  void addItem_existingProduct_increasesQty() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    Cart cart = new Cart();

    Product product = new Product();
    product.setSkuPrefix("GPU1");
    product.setName("GPU 1");
    product.setPrice(300.0);
    product.setStock(10);

    CartItem existing = new CartItem();
    existing.setProduct(product);
    existing.setQuantity(1);
    existing.setTotalPrice(300.0);

    cart.setItems(new ArrayList<>(List.of(existing)));

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
    when(productService.getById("GPU1")).thenReturn(product);
    when(cartRepository.save(cart)).thenReturn(cart);

    Cart result = cartService.addItem(jwt, "GPU1", 2, null);

    assertEquals(3, existing.getQuantity());
    assertEquals(900.0, existing.getTotalPrice());
  }

  @Test
  void addItem_customPc_withoutCase_throwsException() {
    Jwt jwt = mockJwt("auth0|1");
    User user = new User();
    Cart cart = new Cart();
    cart.setItems(new ArrayList<>());

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));

    assertThrows(
        IllegalArgumentException.class,
        () -> cartService.addItem(jwt, null, 1, Map.of("GPU", "GPU1")));
  }
}
