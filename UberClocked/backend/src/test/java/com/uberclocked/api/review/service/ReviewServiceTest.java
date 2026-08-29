package com.uberclocked.api.review.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uberclocked.api.common.exceptions.ResourceDoesNotExistsException;
import com.uberclocked.api.product.model.entity.Product;
import com.uberclocked.api.product.service.ProductService;
import com.uberclocked.api.review.model.dto.CreateReviewDto;
import com.uberclocked.api.review.model.dto.ModifyReviewDataDto;
import com.uberclocked.api.review.model.entity.Review;
import com.uberclocked.api.review.repository.ReviewRepository;
import com.uberclocked.api.user.model.entity.User;
import com.uberclocked.api.user.service.UsersService;
import java.time.Instant;
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
class ReviewServiceTest {

  @Mock private ReviewRepository reviewRepository;
  @Mock private UsersService usersService;
  @Mock private ProductService productService;

  private ReviewService reviewService;

  @BeforeEach
  void setUp() {
    reviewService = new ReviewService(reviewRepository, usersService, productService);
  }

  private Jwt mockJwt(String sub, List<String> roles) {
    return new Jwt(
        "token",
        Instant.now(),
        Instant.now().plusSeconds(3600),
        Map.of("alg", "none"),
        Map.of("sub", sub, "https://uberclocked.com/roles", roles != null ? roles : List.of()));
  }

  @Test
  void createReview_whenNotReviewed_savesAndReturns() {
    Jwt jwt = mockJwt("auth0|1", List.of());
    User user = new User();
    Product product = new Product();
    CreateReviewDto dto = new CreateReviewDto("SKU1", 5, "Great!");

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(productService.getById("SKU1")).thenReturn(product);
    when(reviewRepository.existsByUserAndProduct(user, product)).thenReturn(false);
    when(reviewRepository.save(any(Review.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Review result = reviewService.createReview(dto, jwt);

    assertNotNull(result);
    assertEquals(5, result.getQualification());
    assertEquals("Great!", result.getMessage());
  }

  @Test
  void createReview_whenAlreadyReviewed_throwsIllegalStateException() {
    Jwt jwt = mockJwt("auth0|1", List.of());
    User user = new User();
    Product product = new Product();
    CreateReviewDto dto = new CreateReviewDto("SKU1", 5, "Great!");

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(productService.getById("SKU1")).thenReturn(product);
    when(reviewRepository.existsByUserAndProduct(user, product)).thenReturn(true);

    assertThrows(IllegalStateException.class, () -> reviewService.createReview(dto, jwt));
  }

  @Test
  void getAllReviews_returnsList() {
    Review review = new Review();
    when(reviewRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(review));

    List<Review> result = reviewService.getAllReviews();

    assertEquals(1, result.size());
  }

  @Test
  void getByProduct_returnsList() {
    Product product = new Product();
    Review review = new Review();
    when(productService.getById("SKU1")).thenReturn(product);
    when(reviewRepository.findByProductOrderByCreatedAtDesc(product)).thenReturn(List.of(review));

    List<Review> result = reviewService.getByProduct("SKU1");

    assertEquals(1, result.size());
  }

  @Test
  void getReviewById_whenFound_returnsReview() {
    UUID id = UUID.randomUUID();
    Review review = new Review();
    review.setId(id);
    when(reviewRepository.findById(id)).thenReturn(Optional.of(review));

    Review result = reviewService.getReviewById(id);

    assertEquals(id, result.getId());
  }

  @Test
  void getReviewById_whenNotFound_throwsException() {
    UUID id = UUID.randomUUID();
    when(reviewRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(ResourceDoesNotExistsException.class, () -> reviewService.getReviewById(id));
  }

  @Test
  void getAvgRating_returnsAvg() {
    Product product = new Product();
    when(productService.getById("SKU1")).thenReturn(product);
    when(reviewRepository.avgByProduct(product)).thenReturn(4.5);

    double avg = reviewService.getAvgRating("SKU1");

    assertEquals(4.5, avg);
  }

  @Test
  void getCount_returnsCount() {
    Product product = new Product();
    when(productService.getById("SKU1")).thenReturn(product);
    when(reviewRepository.countByProduct(product)).thenReturn(10L);

    long count = reviewService.getCount("SKU1");

    assertEquals(10L, count);
  }

  @Test
  void deleteReview_asOwner_deletes() {
    UUID reviewId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|1", List.of());

    User user = new User();
    user.setId(userId);

    Review review = new Review();
    review.setId(reviewId);
    review.setUser(user);

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(usersService.getUserOrCreate(jwt)).thenReturn(user);

    reviewService.deleteReview(reviewId, jwt);

    verify(reviewRepository).delete(review);
  }

  @Test
  void deleteReview_asAdmin_deletes() {
    UUID reviewId = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|admin", List.of("Admin"));

    User owner = new User();
    owner.setId(UUID.randomUUID());

    User admin = new User();
    admin.setId(UUID.randomUUID());

    Review review = new Review();
    review.setId(reviewId);
    review.setUser(owner);

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(usersService.getUserOrCreate(jwt)).thenReturn(admin);

    reviewService.deleteReview(reviewId, jwt);

    verify(reviewRepository).delete(review);
  }

  @Test
  void deleteReview_notOwnerNotAdmin_throwsSecurityException() {
    UUID reviewId = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|other", List.of("User"));

    User owner = new User();
    owner.setId(UUID.randomUUID());

    User other = new User();
    other.setId(UUID.randomUUID());

    Review review = new Review();
    review.setId(reviewId);
    review.setUser(owner);

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(usersService.getUserOrCreate(jwt)).thenReturn(other);

    assertThrows(SecurityException.class, () -> reviewService.deleteReview(reviewId, jwt));
  }

  @Test
  void update_asOwner_updatesFields() {
    UUID reviewId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|1", List.of());

    User user = new User();
    user.setId(userId);

    Review review = new Review();
    review.setId(reviewId);
    review.setUser(user);
    review.setQualification(3);
    review.setMessage("Old message");

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(reviewRepository.save(review)).thenReturn(review);

    ModifyReviewDataDto dto = new ModifyReviewDataDto("New message", 5);
    Review result = reviewService.update(reviewId, dto, jwt);

    assertEquals(5, result.getQualification());
    assertEquals("New message", result.getMessage());
  }

  @Test
  void update_notOwner_throwsSecurityException() {
    UUID reviewId = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|other", List.of());

    User owner = new User();
    owner.setId(UUID.randomUUID());

    User other = new User();
    other.setId(UUID.randomUUID());

    Review review = new Review();
    review.setId(reviewId);
    review.setUser(owner);

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(usersService.getUserOrCreate(jwt)).thenReturn(other);

    ModifyReviewDataDto dto = new ModifyReviewDataDto("New message", 5);
    assertThrows(SecurityException.class, () -> reviewService.update(reviewId, dto, jwt));
  }

  @Test
  void getMyReviews_returnsUserReviews() {
    Jwt jwt = mockJwt("auth0|1", List.of());
    User user = new User();
    Review review = new Review();

    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(reviewRepository.findByUserOrderByCreatedAtDesc(user)).thenReturn(List.of(review));

    List<Review> result = reviewService.getMyReviews(jwt);

    assertEquals(1, result.size());
  }
}
