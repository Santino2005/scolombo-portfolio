package com.uberclocked.api.market.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uberclocked.api.common.exceptions.ResourceDoesNotExistsException;
import com.uberclocked.api.market.model.dto.PostDataDto;
import com.uberclocked.api.market.model.entity.Post;
import com.uberclocked.api.market.model.entity.PostStatus;
import com.uberclocked.api.market.repository.PostRepository;
import com.uberclocked.api.user.model.entity.User;
import com.uberclocked.api.user.service.UsersService;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @Mock private PostRepository postRepository;
  @Mock private UsersService usersService;

  private PostService postService;

  @BeforeEach
  void setUp() {
    postService = new PostService(postRepository, usersService);
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
  void create_savesActivePost() throws IOException {
    Jwt jwt = mockJwt("auth0|seller", List.of());
    User seller = new User();
    PostDataDto dto = new PostDataDto("Selling GPU", "Good condition", 300.0, "GPU");
    MultipartFile file = mock(MultipartFile.class);
    when(file.isEmpty()).thenReturn(false);
    when(file.getBytes()).thenReturn(new byte[] {1, 2});

    when(usersService.getUserOrCreate(jwt)).thenReturn(seller);
    when(postRepository.save(any(Post.class))).thenAnswer(i -> i.getArgument(0));

    Post post = postService.create(dto, file, jwt);

    assertNotNull(post);
    assertEquals("Selling GPU", post.getTitle());
    assertEquals(PostStatus.ACTIVE, post.getStatus());
  }

  @Test
  void getAll_returnsAll() {
    when(postRepository.findAll()).thenReturn(List.of(new Post()));
    assertEquals(1, postService.getAll().size());
  }

  @Test
  void getAllActive_returnsActive() {
    when(postRepository.findByStatus(PostStatus.ACTIVE)).thenReturn(List.of(new Post()));
    assertEquals(1, postService.getAllActive().size());
  }

  @Test
  void getById_whenFound_returnsPost() {
    UUID id = UUID.randomUUID();
    Post post = new Post();
    post.setId(id);
    when(postRepository.findById(id)).thenReturn(Optional.of(post));

    assertEquals(post, postService.getById(id));
  }

  @Test
  void getById_whenNotFound_throwsException() {
    UUID id = UUID.randomUUID();
    when(postRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(ResourceDoesNotExistsException.class, () -> postService.getById(id));
  }

  @Test
  void getMyPosts_returnsUserPosts() {
    Jwt jwt = mockJwt("auth0|1", List.of());
    User user = new User();
    when(usersService.getUserOrCreate(jwt)).thenReturn(user);
    when(postRepository.findBySeller(user)).thenReturn(List.of(new Post()));

    assertEquals(1, postService.getMyPosts(jwt).size());
  }

  @Test
  void update_asOwner_updatesFields() {
    UUID id = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|1", List.of());

    User seller = new User();
    seller.setId(userId);

    Post post = new Post();
    post.setId(id);
    post.setSeller(seller);
    post.setStatus(PostStatus.ACTIVE);

    when(postRepository.findById(id)).thenReturn(Optional.of(post));
    when(usersService.getUserOrCreate(jwt)).thenReturn(seller);
    when(postRepository.save(post)).thenReturn(post);

    PostDataDto dto = new PostDataDto("Updated Title", "Updated Desc", 400.0, "GPU");
    Post result = postService.update(id, dto, jwt);

    assertEquals("Updated Title", result.getTitle());
    assertEquals("Updated Desc", result.getDescription());
    assertEquals(400.0, result.getPrice());
  }

  @Test
  void update_notOwner_throwsIllegalStateException() {
    UUID id = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|other", List.of());

    User seller = new User();
    seller.setId(UUID.randomUUID());
    User other = new User();
    other.setId(UUID.randomUUID());

    Post post = new Post();
    post.setId(id);
    post.setSeller(seller);
    post.setStatus(PostStatus.ACTIVE);

    when(postRepository.findById(id)).thenReturn(Optional.of(post));
    when(usersService.getUserOrCreate(jwt)).thenReturn(other);

    PostDataDto dto = new PostDataDto("Title", "Desc", 400.0, "GPU");
    assertThrows(IllegalStateException.class, () -> postService.update(id, dto, jwt));
  }

  @Test
  void delete_asOwner_marksDeleted() {
    UUID id = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|1", List.of());

    User seller = new User();
    seller.setId(userId);

    Post post = new Post();
    post.setId(id);
    post.setSeller(seller);
    post.setStatus(PostStatus.ACTIVE);

    when(postRepository.findById(id)).thenReturn(Optional.of(post));
    when(usersService.getUserOrCreate(jwt)).thenReturn(seller);

    postService.delete(id, jwt);

    assertEquals(PostStatus.DELETED, post.getStatus());
    verify(postRepository).save(post);
  }

  @Test
  void markAsSold_asOwner_marksSold() {
    UUID id = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|1", List.of());

    User seller = new User();
    seller.setId(userId);

    Post post = new Post();
    post.setId(id);
    post.setSeller(seller);
    post.setStatus(PostStatus.ACTIVE);

    when(postRepository.findById(id)).thenReturn(Optional.of(post));
    when(usersService.getUserOrCreate(jwt)).thenReturn(seller);

    postService.markAsSold(id, jwt);

    assertEquals(PostStatus.SOLD, post.getStatus());
    verify(postRepository).save(post);
  }
}
