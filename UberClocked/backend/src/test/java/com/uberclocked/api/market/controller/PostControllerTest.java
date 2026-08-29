package com.uberclocked.api.market.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uberclocked.api.market.model.dto.PostDataDto;
import com.uberclocked.api.market.model.dto.PostInterestDto;
import com.uberclocked.api.market.model.entity.Post;
import com.uberclocked.api.market.model.entity.PostStatus;
import com.uberclocked.api.market.service.PostInterestService;
import com.uberclocked.api.market.service.PostService;
import com.uberclocked.api.security.TestSecurityConfig;
import com.uberclocked.api.user.model.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class PostControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private PostService postService;
  @MockitoBean private PostInterestService interestService;

  private Post createMockPost(UUID id, String title) {
    User seller = new User();
    seller.setId(UUID.randomUUID());
    seller.setUserName("Seller");
    Post post = new Post(title, null, "Desc", 100.0, "GPU", seller, LocalDateTime.now());
    post.setId(id);
    post.setStatus(PostStatus.ACTIVE);
    return post;
  }

  @Test
  void create_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    Post post = createMockPost(id, "GPU 3080");

    when(postService.create(any(PostDataDto.class), any(), any())).thenReturn(post);

    PostDataDto dataDto = new PostDataDto("GPU 3080", "Desc", 100.0, "GPU");
    MockMultipartFile dataPart =
        new MockMultipartFile(
            "data", "", "application/json", objectMapper.writeValueAsBytes(dataDto));

    mockMvc
        .perform(
            multipart("/posts").file(dataPart).with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("GPU 3080"));
  }

  @Test
  void getAll_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    Post post = createMockPost(id, "GPU 3080");
    when(postService.getAllActive()).thenReturn(List.of(post));

    mockMvc
        .perform(get("/posts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("GPU 3080"));
  }

  @Test
  void getById_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    Post post = createMockPost(id, "GPU 3080");
    when(postService.getById(id)).thenReturn(post);

    mockMvc
        .perform(get("/posts/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("GPU 3080"));
  }

  @Test
  void myPosts_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    Post post = createMockPost(id, "GPU 3080");
    when(postService.getMyPosts(any())).thenReturn(List.of(post));

    mockMvc
        .perform(get("/posts/me").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("GPU 3080"));
  }

  @Test
  void update_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    Post post = createMockPost(id, "GPU 3080 Updated");
    when(postService.update(eq(id), any(PostDataDto.class), any())).thenReturn(post);

    PostDataDto dataDto = new PostDataDto("GPU 3080 Updated", "Desc", 100.0, "GPU");

    mockMvc
        .perform(
            patch("/posts/" + id)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dataDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("GPU 3080 Updated"));
  }

  @Test
  void delete_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    doNothing().when(postService).delete(eq(id), any());

    mockMvc
        .perform(delete("/posts/" + id).with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk());

    verify(postService).delete(eq(id), any());
  }

  @Test
  void markAsSold_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    doNothing().when(postService).markAsSold(eq(id), any());

    mockMvc
        .perform(post("/posts/" + id + "/sold").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk());

    verify(postService).markAsSold(eq(id), any());
  }

  @Test
  void markInterest_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    doNothing().when(interestService).markInterest(eq(id), any());

    mockMvc
        .perform(
            post("/posts/" + id + "/interest").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk());

    verify(interestService).markInterest(eq(id), any());
  }

  @Test
  void getInterested_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    PostInterestDto dto = new PostInterestDto();
    dto.setId(UUID.randomUUID());
    dto.setUserName("Buyer");
    dto.setInfoPurchased(false);
    when(interestService.getInterestedUsers(eq(id), any())).thenReturn(List.of(dto));

    mockMvc
        .perform(
            get("/posts/" + id + "/interested").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].userName").value("Buyer"));
  }

  @Test
  void purchaseInterestedInfo_returns200() throws Exception {
    UUID postId = UUID.randomUUID();
    UUID buyerId = UUID.randomUUID();
    User buyer = new User();
    buyer.setId(buyerId);
    buyer.setUserName("Buyer");
    buyer.setEmail("buyer@mail.com");

    when(interestService.buyInterestedInfo(eq(postId), eq(buyerId), any())).thenReturn(buyer);

    mockMvc
        .perform(
            post("/posts/" + postId + "/interested/" + buyerId + "/purchase")
                .with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userName").value("Buyer"));
  }

  @Test
  void hasMyInterest_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    when(interestService.hasInterest(eq(id), any())).thenReturn(true);

    mockMvc
        .perform(
            get("/posts/" + id + "/interest/me").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(true));
  }
}
