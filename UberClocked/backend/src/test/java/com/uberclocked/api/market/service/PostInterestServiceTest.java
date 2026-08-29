package com.uberclocked.api.market.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uberclocked.api.emailData.EmailService;
import com.uberclocked.api.market.model.dto.PostInterestDto;
import com.uberclocked.api.market.model.entity.Post;
import com.uberclocked.api.market.model.entity.PostInterest;
import com.uberclocked.api.market.repository.PostInterestRepository;
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
class PostInterestServiceTest {

  @Mock private PostService postService;
  @Mock private PostInterestRepository interestRepository;
  @Mock private UsersService usersService;
  @Mock private EmailService emailService;

  private PostInterestService interestService;

  @BeforeEach
  void setUp() {
    interestService =
        new PostInterestService(postService, interestRepository, usersService, emailService);
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
  void markInterest_whenValid_savesInterest() {
    UUID postId = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|buyer");

    User seller = new User();
    seller.setId(UUID.randomUUID());

    User buyer = new User();
    buyer.setId(UUID.randomUUID());

    Post post = new Post();
    post.setSeller(seller);

    when(postService.getById(postId)).thenReturn(post);
    when(usersService.getUserOrCreate(jwt)).thenReturn(buyer);
    when(interestRepository.existsByPostAndInterested(post, buyer)).thenReturn(false);

    interestService.markInterest(postId, jwt);

    verify(interestRepository).save(any(PostInterest.class));
  }

  @Test
  void markInterest_onOwnPost_throwsIllegalStateException() {
    UUID postId = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|seller");

    UUID userId = UUID.randomUUID();
    User seller = new User();
    seller.setId(userId);

    Post post = new Post();
    post.setSeller(seller);

    when(postService.getById(postId)).thenReturn(post);
    when(usersService.getUserOrCreate(jwt)).thenReturn(seller);

    assertThrows(IllegalStateException.class, () -> interestService.markInterest(postId, jwt));
  }

  @Test
  void getInterestedUsers_asSeller_returnsList() {
    UUID postId = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|seller");

    UUID sellerId = UUID.randomUUID();
    User seller = new User();
    seller.setId(sellerId);

    Post post = new Post();
    post.setSeller(seller);

    User buyer = new User();
    buyer.setId(UUID.randomUUID());
    buyer.setUserName("Buyer");
    PostInterest interest = new PostInterest(post, buyer);

    when(postService.getById(postId)).thenReturn(post);
    when(usersService.getUserOrCreate(jwt)).thenReturn(seller);
    when(interestRepository.findByPost(post)).thenReturn(List.of(interest));

    List<PostInterestDto> list = interestService.getInterestedUsers(postId, jwt);

    assertEquals(1, list.size());
  }

  @Test
  void buyInterestedInfo_setsInfoPurchasedAndSendsEmail() {
    UUID postId = UUID.randomUUID();
    UUID buyerId = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|seller");

    UUID sellerId = UUID.randomUUID();
    User seller = new User();
    seller.setId(sellerId);
    seller.setEmail("seller@mail.com");

    Post post = new Post();
    post.setTitle("RTX 4090");
    post.setSeller(seller);

    User buyer = new User();
    buyer.setId(buyerId);
    buyer.setUserName("Buyer");
    buyer.setEmail("buyer@mail.com");
    buyer.setCellPhone("123");
    buyer.setCountry("AR");

    PostInterest interest = new PostInterest(post, buyer);

    when(postService.getById(postId)).thenReturn(post);
    when(usersService.getUserOrCreate(jwt)).thenReturn(seller);
    when(usersService.getUSerById(buyerId)).thenReturn(buyer);
    when(interestRepository.findByPostAndInterested(post, buyer)).thenReturn(Optional.of(interest));

    User result = interestService.buyInterestedInfo(postId, buyerId, jwt);

    assertNotNull(result);
    assertTrue(interest.isInfoPurchased());
    verify(interestRepository).save(interest);
    verify(emailService).sendMail(eq("seller@mail.com"), any(), any());
  }

  @Test
  void getInterestedInfoIfPurchased_whenPurchased_returnsUser() {
    UUID postId = UUID.randomUUID();
    UUID buyerId = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|seller");

    UUID sellerId = UUID.randomUUID();
    User seller = new User();
    seller.setId(sellerId);

    Post post = new Post();
    post.setSeller(seller);

    User buyer = new User();
    buyer.setId(buyerId);

    PostInterest interest = new PostInterest(post, buyer);
    interest.setInfoPurchased(true);

    when(postService.getById(postId)).thenReturn(post);
    when(usersService.getUserOrCreate(jwt)).thenReturn(seller);
    when(usersService.getUSerById(buyerId)).thenReturn(buyer);
    when(interestRepository.findByPostAndInterested(post, buyer)).thenReturn(Optional.of(interest));

    User result = interestService.getInterestedInfoIfPurchased(postId, buyerId, jwt);

    assertEquals(buyer, result);
  }

  @Test
  void hasInterest_returnsBoolean() {
    UUID postId = UUID.randomUUID();
    Jwt jwt = mockJwt("auth0|buyer");

    User seller = new User();
    seller.setId(UUID.randomUUID());

    User buyer = new User();
    buyer.setId(UUID.randomUUID());

    Post post = new Post();
    post.setSeller(seller);

    when(postService.getById(postId)).thenReturn(post);
    when(usersService.getUserOrCreate(jwt)).thenReturn(buyer);
    when(interestRepository.existsByPostAndInterested(post, buyer)).thenReturn(true);

    assertTrue(interestService.hasInterest(postId, jwt));
  }
}
