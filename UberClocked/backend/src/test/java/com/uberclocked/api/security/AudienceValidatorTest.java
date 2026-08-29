package com.uberclocked.api.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

class AudienceValidatorTest {

  @Test
  void validate_whenAudienceMatches_returnsSuccess() {
    AudienceValidator validator = new AudienceValidator("https://uberclocked-api");
    Jwt jwt =
        new Jwt(
            "token",
            Instant.now(),
            Instant.now().plusSeconds(3600),
            Map.of("alg", "none"),
            Map.of("aud", List.of("https://uberclocked-api")));

    OAuth2TokenValidatorResult result = validator.validate(jwt);

    assertFalse(result.hasErrors());
  }

  @Test
  void validate_whenAudienceMismatch_returnsFailure() {
    AudienceValidator validator = new AudienceValidator("https://uberclocked-api");
    Jwt jwt =
        new Jwt(
            "token",
            Instant.now(),
            Instant.now().plusSeconds(3600),
            Map.of("alg", "none"),
            Map.of("aud", List.of("https://other-api")));

    OAuth2TokenValidatorResult result = validator.validate(jwt);

    assertTrue(result.hasErrors());
  }
}
