package com.uberclocked.api.review.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.uberclocked.api.product.model.entity.Product;
import com.uberclocked.api.review.model.dto.ReviewResponseDto;
import com.uberclocked.api.review.model.entity.Review;
import com.uberclocked.api.user.model.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReviewMapperTest {

  private final ReviewMapper reviewMapper = new ReviewMapper();

  @Test
  void toDto_mapsReview() {
    UUID reviewId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();

    User user = new User();
    user.setId(userId);
    user.setUserName("john_doe");

    Product product = new Product();
    product.setSkuPrefix("GPU123");

    Review review = new Review();
    review.setId(reviewId);
    review.setUser(user);
    review.setProduct(product);
    review.setQualification(5);
    review.setMessage("Great!");
    review.setCreatedAt(LocalDateTime.now());

    ReviewResponseDto dto = reviewMapper.toDto(review);

    assertNotNull(dto);
    assertEquals(reviewId, dto.id());
    assertEquals("GPU123", dto.skuPrefix());
    assertEquals(userId, dto.userId());
    assertEquals("john_doe", dto.userName());
    assertEquals(5, dto.qualification());
    assertEquals("Great!", dto.message());
  }

  @Test
  void toDtoList_mapsList() {
    User user = new User();
    user.setId(UUID.randomUUID());
    user.setUserName("john");

    Product product = new Product();
    product.setSkuPrefix("GPU123");

    Review review = new Review();
    review.setId(UUID.randomUUID());
    review.setUser(user);
    review.setProduct(product);
    review.setQualification(4);
    review.setMessage("Nice");
    review.setCreatedAt(LocalDateTime.now());

    List<ReviewResponseDto> list = reviewMapper.toDtoList(List.of(review));

    assertEquals(1, list.size());
  }
}
