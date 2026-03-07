package com.backendChallenge.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User existingUser(Jwt jwt) {
        String id = jwt.getClaim("sub");
        String email = (String) jwt.getClaims().get("https://backendchallenge/email");
        User user = userRepository.findById(id).orElse(null);
        if(user != null) {
            return user;
        }
        logger.info("Claims: {}", jwt.getClaims());
        User userToCreate = new User(id,email,ZoneId.of("UTC"));
        return userRepository.save(userToCreate);
    }
}
