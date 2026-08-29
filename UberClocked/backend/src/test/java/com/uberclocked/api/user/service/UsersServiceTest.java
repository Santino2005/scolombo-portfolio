package com.uberclocked.api.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uberclocked.api.cart.repository.CartRepository;
import com.uberclocked.api.common.exceptions.ResourceDoesNotExistsException;
import com.uberclocked.api.company.service.CompanyService;
import com.uberclocked.api.company.service.CompanyUserService;
import com.uberclocked.api.user.mapper.UserMapper;
import com.uberclocked.api.user.model.dto.UserDataDto;
import com.uberclocked.api.user.model.entity.User;
import com.uberclocked.api.user.repository.UsersRepository;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

  @Mock UsersRepository repository;
  @Mock UserMapper mapper;
  @Mock CartRepository cartRepository;
  @Mock CompanyService companyService;
  @Mock CompanyUserService companyUserService;

  @InjectMocks UsersService service;

  private Jwt jwt(String sub, String email, String name) {
    return new Jwt(
        "token",
        Instant.now(),
        Instant.now().plusSeconds(3600),
        Map.of("alg", "none"),
        Map.of("sub", sub, "email", email, "name", name));
  }

  @Test
  void create_whenAuth0IdExists_returnsExisting_andSavesLastLogin() {
    String auth0Id = "auth0|existing";
    Jwt jwt = jwt(auth0Id, "existing@mail.com", "Existing");

    User existingUser = new User(auth0Id, "Existing", "existing@mail.com");

    when(repository.findByAuth0Id(auth0Id)).thenReturn(Optional.of(existingUser));
    when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

    User result = service.getUserOrCreate(jwt);

    assertNotNull(result);
    assertSame(existingUser, result);
    verify(repository).save(existingUser);
    verify(repository).findByAuth0Id(auth0Id);
  }

  @Test
  void create_whenAuth0IdDoesNotExist_savesNewUser_andSetsLastLogin() {
    String auth0Id = "auth0|new";
    Jwt jwt = jwt(auth0Id, "new@mail.com", "New User");

    when(repository.findByAuth0Id(auth0Id)).thenReturn(Optional.empty());
    when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

    User result = service.getUserOrCreate(jwt);

    assertNotNull(result);
    assertEquals(auth0Id, result.getAuth0Id());
    assertEquals("new@mail.com", result.getEmail());
    assertEquals("New User", result.getUserName());
    assertNotNull(result.getLastLogin());
  }

  @Test
  void update_whenUserExists_updatesAndSaves() {
    doAnswer(
            inv -> {
              UserDataDto dto = inv.getArgument(0);
              User entity = inv.getArgument(1);

              if (dto.userName() != null) entity.setUserName(dto.userName());
              if (dto.email() != null) entity.setEmail(dto.email());
              if (dto.country() != null) entity.setCountry(dto.country());
              if (dto.cellPhone() != null) entity.setCellPhone(dto.cellPhone());

              return null;
            })
        .when(mapper)
        .update(any(UserDataDto.class), any(User.class));

    String auth0Id = "auth0|123";
    Jwt jwt = jwt(auth0Id, "mail@test.com", "Original");

    User user = new User(auth0Id, "Original", "mail@test.com");
    when(repository.findByAuth0Id(auth0Id)).thenReturn(Optional.of(user));
    when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

    UserDataDto dto = new UserDataDto(null, "Nuevo", null, "UY", null);

    User result = service.updateData(jwt, dto);

    verify(mapper).update(dto, user);
    verify(repository).save(user);

    assertNotNull(result);
    assertEquals("Nuevo", result.getUserName());
    assertEquals("mail@test.com", result.getEmail());
    assertEquals("UY", result.getCountry());
  }

  @Test
  void update_whenUserMissing_throwsException() {
    Jwt jwt = jwt("auth0|missing", "x@mail.com", "X");
    when(repository.findByAuth0Id("auth0|missing")).thenReturn(Optional.empty());

    UserDataDto dto = new UserDataDto(null, "Name", null, null, null);

    assertThrows(ResourceDoesNotExistsException.class, () -> service.updateData(jwt, dto));
    verify(repository, never()).save(any());
  }

  @Test
  void delete_whenUserExists_callsDelete() {
    String auth0Id = "auth0|me";
    Jwt jwt = jwt(auth0Id, "", "");
    User user = new User(auth0Id, "Santino", "santino@mail.com");

    when(repository.findByAuth0Id(auth0Id)).thenReturn(Optional.of(user));

    service.delete(jwt);

    verify(repository).findByAuth0Id(auth0Id);
    verify(repository).delete(user);
  }

  @Test
  void delete_whenMissing_throws_andDoesNotDelete() {
    String auth0Id = "auth0|missing";
    Jwt jwt = jwt(auth0Id, "", "");

    when(repository.findByAuth0Id(auth0Id)).thenReturn(Optional.empty());

    assertThrows(ResourceDoesNotExistsException.class, () -> service.delete(jwt));

    verify(repository).findByAuth0Id(auth0Id);
    verify(repository, never()).delete(any());
  }
}
