package com.geno_insights.scolombo.guard.service;

import com.geno_insights.scolombo.guard.model.entity.Guard;
import com.geno_insights.scolombo.guard.repository.GuardRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuardService {

    private final GuardRepository guardRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private static final Logger logger =
            LoggerFactory.getLogger(GuardService.class);

    public String login(String username, String pin) {
        Guard guard = guardRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Guard not found"));
        logger.info("Guard logged in: {} && {}", guard.getUserName(), guard.getHashedPin());
        logger.info("Pin: {}", encoder.encode("admin"));
        if (!encoder.matches(pin, guard.getHashedPin())) {
            throw new RuntimeException("Invalid pin");
        }

        return "Login successful";
    }

    public Guard register(String username, String pin) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (pin == null || pin.trim().isEmpty()) {
            throw new IllegalArgumentException("PIN cannot be empty");
        }
        String cleanUsername = username.trim();
        if (guardRepository.findByUserName(cleanUsername).isPresent()) {
            throw new IllegalArgumentException("Guard username already exists");
        }

        Guard guard = new Guard();
        guard.setUserName(cleanUsername);
        guard.setHashedPin(encoder.encode(pin.trim()));
        return guardRepository.save(guard);
    }
}
