package com.uberclocked.api.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.uberclocked.api.cart.repository.CartRepository;
import com.uberclocked.api.common.exceptions.ResourceDoesNotExistsException;
import com.uberclocked.api.company.service.CompanyService;
import com.uberclocked.api.company.service.CompanyUserService;
import com.uberclocked.api.user.mapper.UserMapperImpl;
import com.uberclocked.api.user.model.dto.UserDataDto;
import com.uberclocked.api.user.model.entity.User;
import com.uberclocked.api.user.repository.UsersRepository;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DataJpaTest
@Import({UsersService.class, UserMapperImpl.class})
class UsersServiceJpaTest {

  @Autowired UsersService service;
  @Autowired UsersRepository repository;
  @MockitoBean CartRepository cartRepository;
  @MockitoBean CompanyService companyService;
  @MockitoBean CompanyUserService companyUserService;

  private Jwt jwt(String sub, String email, String name) {
    return new Jwt(
        "token",
        Instant.now(),
        Instant.now().plusSeconds(3600),
        Map.of("alg", "none"),
        Map.of("sub", sub, "email", email, "name", name));
  }

  @Test
  void create_persistsEntity_whenNew() {
    String auth0Id = "auth0|new";
    Jwt jwt = jwt(auth0Id, "new@mail.com", "New User");

    User created = service.getUserOrCreate(jwt);

    assertNotNull(created);
    assertEquals(1, repository.count());
    assertTrue(repository.findByAuth0Id(auth0Id).isPresent());
    assertNotNull(repository.findByAuth0Id(auth0Id).get().getLastLogin());
  }

  @Test
  void create_returnsExisting_whenAlreadyExists_andDoesNotDuplicate() {
    String auth0Id = "auth0|same";
    Jwt jwt = jwt(auth0Id, "same@mail.com", "Same User");

    service.getUserOrCreate(jwt);
    assertEquals(1, repository.count());

    service.getUserOrCreate(jwt);
    assertEquals(1, repository.count());
  }

  @Test
  void update_updatesOnlyProvidedFields() {
    String auth0Id = "auth0|upd";
    Jwt jwt = jwt(auth0Id, "mail@test.com", "Original");

    service.getUserOrCreate(jwt);

    UserDataDto dto = new UserDataDto(null, "Nuevo Nombre", null, "AR", null);

    service.updateData(jwt, dto);

    User stored = repository.findByAuth0Id(auth0Id).orElseThrow();
    assertEquals("Original", stored.getUserName());
    assertEquals("mail@test.com", stored.getEmail());
    assertEquals("AR", stored.getCountry());
    assertNull(stored.getCellPhone());
  }

  @Test
  void update_whenUserDoesNotExist_throwsException() {
    Jwt jwt = jwt("auth0|missing", "x@mail.com", "X");
    UserDataDto dto = new UserDataDto(null, "Name", null, null, null);

    assertThrows(ResourceDoesNotExistsException.class, () -> service.updateData(jwt, dto));
  }

  @Test
  void delete_whenUserExists_deletesRow() {
    String auth0Id = "auth0|123";
    repository.save(new User(auth0Id, "Santino", "santino@mail.com"));

    Jwt jwt = jwt(auth0Id, "santino@mail.com", "Santino");
    service.delete(jwt);

    assertTrue(repository.findByAuth0Id(auth0Id).isEmpty());
  }

  @Test
  void delete_whenMissing_throwsException() {
    Jwt jwt = jwt("auth0|missing", "missing@mail.com", "Missing");
    assertThrows(ResourceDoesNotExistsException.class, () -> service.delete(jwt));
  }
}
