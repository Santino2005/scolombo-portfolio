package com.uberclocked.api.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uberclocked.api.common.exceptions.ResourceDoesNotExistsException;
import com.uberclocked.api.component.model.entity.Component;
import com.uberclocked.api.component.service.ComponentService;
import com.uberclocked.api.product.mapper.ProductMapper;
import com.uberclocked.api.product.model.dto.ProductDataDto;
import com.uberclocked.api.product.model.entity.Product;
import com.uberclocked.api.product.repository.ProductRepository;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock private ProductRepository productRepository;
  @Mock private ComponentService componentService;
  @Mock private ProductMapper productMapper;

  private ProductService productService;

  @BeforeEach
  void setUp() {
    productService = new ProductService(productRepository, componentService, productMapper);
  }

  @Test
  void create_whenSkuNotExists_createsAndSaves() throws IOException {
    ProductDataDto dto = new ProductDataDto("SKU1", "GPU", "COMP1", 500.0, 10, Map.of());
    MultipartFile image = mock(MultipartFile.class);
    when(image.isEmpty()).thenReturn(false);
    when(image.getBytes()).thenReturn(new byte[] {1, 2, 3});

    when(productRepository.existsById("SKU1")).thenReturn(false);
    Component comp = new Component("COMP1", "Component 1");
    when(componentService.getEntityById("COMP1")).thenReturn(comp);

    Product product = new Product();
    when(productMapper.toEntity(dto)).thenReturn(product);
    when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

    Product result = productService.create(dto, image);

    assertNotNull(result);
    assertEquals("SKU1", result.getSkuPrefix());
    verify(productRepository).save(product);
  }

  @Test
  void create_whenSkuExists_throwsIllegalArgumentException() {
    ProductDataDto dto = new ProductDataDto("SKU1", "GPU", "COMP1", 500.0, 10, Map.of());
    when(productRepository.existsById("SKU1")).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> productService.create(dto, null));
  }

  @Test
  void getAllActive_returnsList() {
    Product product = new Product();
    when(productRepository.findByActiveTrue()).thenReturn(List.of(product));

    List<Product> list = productService.getAllActive();

    assertEquals(1, list.size());
  }

  @Test
  void getById_whenFound_returnsProduct() {
    Product product = new Product();
    when(productRepository.findById("SKU1")).thenReturn(Optional.of(product));

    Product result = productService.getById("SKU1");

    assertEquals(product, result);
  }

  @Test
  void getById_whenNotFound_throwsException() {
    when(productRepository.findById("SKU1")).thenReturn(Optional.empty());

    assertThrows(ResourceDoesNotExistsException.class, () -> productService.getById("SKU1"));
  }

  @Test
  void update_updatesAndReturnsProduct() throws IOException {
    Product product = new Product();
    when(productRepository.findById("SKU1")).thenReturn(Optional.of(product));

    Component comp = new Component("COMP1", "Component 1");
    when(componentService.getEntityById("COMP1")).thenReturn(comp);

    MultipartFile image = mock(MultipartFile.class);
    when(image.isEmpty()).thenReturn(false);
    when(image.getBytes()).thenReturn(new byte[] {4, 5, 6});

    ProductDataDto dto = new ProductDataDto("SKU1", "GPU New", "COMP1", 600.0, 15, Map.of());
    when(productRepository.save(product)).thenReturn(product);

    Product result = productService.update("SKU1", dto, image);

    assertEquals(product, result);
    verify(productMapper).update(dto, product);
    verify(productRepository).save(product);
  }

  @Test
  void delete_marksInactiveAndDeletes() {
    Product product = new Product();
    when(productRepository.findById("SKU1")).thenReturn(Optional.of(product));

    productService.delete("SKU1");

    verify(productRepository).delete(product);
  }

  @Test
  void decreaseStock_whenEnoughStock_decreases() {
    Product product = new Product();
    product.setStock(10);
    when(productRepository.findById("SKU1")).thenReturn(Optional.of(product));
    when(productRepository.save(product)).thenReturn(product);

    productService.decreaseStock("SKU1", 3);

    assertEquals(7, product.getStock());
    verify(productRepository).save(product);
  }

  @Test
  void decreaseStock_whenNotEnoughStock_throwsException() {
    Product product = new Product();
    product.setStock(2);
    product.setName("GPU");
    when(productRepository.findById("SKU1")).thenReturn(Optional.of(product));

    assertThrows(IllegalArgumentException.class, () -> productService.decreaseStock("SKU1", 5));
  }

  @Test
  @SuppressWarnings("unchecked")
  void filter_queriesSpecification() {
    when(productRepository.findAll(any(Specification.class))).thenReturn(List.of(new Product()));

    Map<String, String> attrs = new HashMap<>();
    attrs.put("brand", "asus");

    List<Product> result = productService.filter("GPU", 100.0, 1000.0, attrs);

    assertEquals(1, result.size());
    verify(productRepository).findAll(any(Specification.class));
  }
}
