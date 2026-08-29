package com.uberclocked.api.review.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uberclocked.api.review.mapper.ReviewMapper;
import com.uberclocked.api.review.model.dto.CreateReviewDto;
import com.uberclocked.api.review.model.dto.ModifyReviewDataDto;
import com.uberclocked.api.review.model.dto.ReviewResponseDto;
import com.uberclocked.api.review.model.entity.Review;
import com.uberclocked.api.review.service.ReviewService;
import com.uberclocked.api.security.TestSecurityConfig;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReviewsController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class ReviewsControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private ReviewService reviewService;
  @MockitoBean private ReviewMapper reviewMapper;

  @Test
  void create_returns201() throws Exception {
    CreateReviewDto dto = new CreateReviewDto("SKU1", 5, "Amazing product!");
    Review review = new Review();
    ReviewResponseDto resDto =
        new ReviewResponseDto(
            UUID.randomUUID(),
            "SKU1",
            UUID.randomUUID(),
            "User1",
            5,
            "Amazing product!",
            LocalDateTime.now());

    when(reviewService.createReview(any(CreateReviewDto.class), any())).thenReturn(review);
    when(reviewMapper.toDto(review)).thenReturn(resDto);

    mockMvc
        .perform(
            post("/reviews")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.qualification").value(5));
  }

  @Test
  void byProduct_returns200() throws Exception {
    Review review = new Review();
    ReviewResponseDto resDto =
        new ReviewResponseDto(
            UUID.randomUUID(), "SKU1", UUID.randomUUID(), "User1", 5, "Good", LocalDateTime.now());

    when(reviewService.getByProduct("SKU1")).thenReturn(List.of(review));
    when(reviewMapper.toDtoList(List.of(review))).thenReturn(List.of(resDto));

    mockMvc
        .perform(get("/reviews/product/SKU1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].skuPrefix").value("SKU1"));
  }

  @Test
  void rating_returns200() throws Exception {
    when(reviewService.getAvgRating("SKU1")).thenReturn(4.2);
    when(reviewService.getCount("SKU1")).thenReturn(15L);

    mockMvc
        .perform(get("/reviews/product/SKU1/rating"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.avgRating").value(4.2))
        .andExpect(jsonPath("$.count").value(15));
  }

  @Test
  void myReviews_returns200() throws Exception {
    Review review = new Review();
    ReviewResponseDto resDto =
        new ReviewResponseDto(
            UUID.randomUUID(), "SKU1", UUID.randomUUID(), "User1", 4, "Nice", LocalDateTime.now());

    when(reviewService.getMyReviews(any())).thenReturn(List.of(review));
    when(reviewMapper.toDtoList(List.of(review))).thenReturn(List.of(resDto));

    mockMvc
        .perform(get("/reviews/me").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].qualification").value(4));
  }

  @Test
  void getReviewById_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    Review review = new Review();
    review.setId(id);
    review.setQualification(5);

    when(reviewService.getReviewById(id)).thenReturn(review);

    mockMvc
        .perform(get("/reviews/" + id.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()));
  }

  @Test
  void update_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    ModifyReviewDataDto dto = new ModifyReviewDataDto("Updated message", 4);
    Review review = new Review();
    ReviewResponseDto resDto =
        new ReviewResponseDto(
            id, "SKU1", UUID.randomUUID(), "User1", 4, "Updated message", LocalDateTime.now());

    when(reviewService.update(eq(id), any(ModifyReviewDataDto.class), any())).thenReturn(review);
    when(reviewMapper.toDto(review)).thenReturn(resDto);

    mockMvc
        .perform(
            patch("/reviews/" + id)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Updated message"));
  }

  @Test
  void deleteReview_returns204() throws Exception {
    UUID id = UUID.randomUUID();
    doNothing().when(reviewService).deleteReview(eq(id), any());

    mockMvc
        .perform(delete("/reviews/" + id).with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isNoContent());

    verify(reviewService).deleteReview(eq(id), any());
  }

  @Test
  void allReviews_returns200() throws Exception {
    Review review = new Review();
    ReviewResponseDto resDto =
        new ReviewResponseDto(
            UUID.randomUUID(), "SKU1", UUID.randomUUID(), "User1", 5, "Good", LocalDateTime.now());

    when(reviewService.getAllReviews()).thenReturn(List.of(review));
    when(reviewMapper.toDtoList(List.of(review))).thenReturn(List.of(resDto));

    mockMvc
        .perform(get("/reviews"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].qualification").value(5));
  }
}
