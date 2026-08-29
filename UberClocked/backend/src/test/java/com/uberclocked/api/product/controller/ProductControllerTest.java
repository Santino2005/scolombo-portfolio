package com.uberclocked.api.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.uberclocked.api.product.model.dto.ProductDataDto;
import com.uberclocked.api.product.model.entity.Product;
import com.uberclocked.api.product.service.ProductService;
import com.uberclocked.api.security.TestSecurityConfig;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ProductService productService;

  @Test
  void create_returns200() throws Exception {
    Product product = new Product();
    product.setSkuPrefix("SKU1");
    product.setName("RTX 4090");

    when(productService.create(any(ProductDataDto.class), any())).thenReturn(product);

    MockMultipartFile image =
        new MockMultipartFile("image", "test.png", "image/png", new byte[] {1, 2});

    mockMvc
        .perform(
            multipart("/products")
                .file(image)
                .param("sku", "SKU1")
                .param("name", "RTX 4090")
                .param("componentSkuPrefix", "GPU")
                .param("price", "1500.0")
                .param("stock", "5")
                .param("attributes", "{\"brand\":\"Nvidia\"}")
                .with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.skuPrefix").value("SKU1"))
        .andExpect(jsonPath("$.name").value("RTX 4090"));
  }

  @Test
  void getAll_returns200() throws Exception {
    Product product = new Product();
    product.setSkuPrefix("SKU1");

    when(productService.getAllActive()).thenReturn(List.of(product));

    mockMvc
        .perform(get("/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].skuPrefix").value("SKU1"));
  }

  @Test
  void getById_returns200() throws Exception {
    Product product = new Product();
    product.setSkuPrefix("SKU1");

    when(productService.getById("SKU1")).thenReturn(product);

    mockMvc
        .perform(get("/products/SKU1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.skuPrefix").value("SKU1"));
  }

  @Test
  void update_returns200() throws Exception {
    Product product = new Product();
    product.setSkuPrefix("SKU1");
    product.setName("RTX 4090 Super");

    when(productService.update(eq("SKU1"), any(ProductDataDto.class), any())).thenReturn(product);

    mockMvc
        .perform(
            multipart(HttpMethod.PATCH, "/products/SKU1")
                .param("name", "RTX 4090 Super")
                .param("componentSkuPrefix", "GPU")
                .param("price", "1600.0")
                .param("stock", "3")
                .param("attributes", "{\"brand\":\"Nvidia\"}")
                .with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("RTX 4090 Super"));
  }

  @Test
  void delete_returns200() throws Exception {
    doNothing().when(productService).delete("SKU1");

    mockMvc
        .perform(delete("/products/SKU1").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk());

    verify(productService).delete("SKU1");
  }

  @Test
  void filter_returns200() throws Exception {
    Product product = new Product();
    product.setSkuPrefix("SKU1");

    when(productService.filter(any(), any(), any(), any())).thenReturn(List.of(product));

    mockMvc
        .perform(
            get("/products/filter")
                .param("componentSkuPrefix", "ALL")
                .param("minPrice", "100")
                .param("maxPrice", "500")
                .param("brand", "Nvidia"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].skuPrefix").value("SKU1"));
  }
}
