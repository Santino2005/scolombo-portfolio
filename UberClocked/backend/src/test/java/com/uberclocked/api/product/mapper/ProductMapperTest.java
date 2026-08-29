package com.uberclocked.api.product.mapper;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.uberclocked.api.product.model.dto.ProductDataDto;
import com.uberclocked.api.product.model.entity.Product;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class ProductMapperTest {

  private final ProductMapper mapper =
      new ProductMapper() {
        @Override
        public Product toEntity(ProductDataDto dto) {
          return null;
        }

        @Override
        public void update(ProductDataDto dto, Product entity) throws IOException {}
      };

  @Test
  void mapMultipartFile_whenNullOrEmpty_returnsNull() throws IOException {
    assertNull(mapper.map(null));

    MultipartFile empty = mock(MultipartFile.class);
    when(empty.isEmpty()).thenReturn(true);
    assertNull(mapper.map(empty));
  }

  @Test
  void mapMultipartFile_whenNotEmpty_returnsBytes() throws IOException {
    MultipartFile file = mock(MultipartFile.class);
    when(file.isEmpty()).thenReturn(false);
    when(file.getBytes()).thenReturn(new byte[] {1, 2, 3});

    assertArrayEquals(new byte[] {1, 2, 3}, mapper.map(file));
  }
}
