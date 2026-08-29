package com.geno_insights.scolombo.config;

import com.geno_insights.scolombo.guard.model.entity.Guard;
import com.geno_insights.scolombo.guard.repository.GuardRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final GuardRepository guardRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Override
    public void run(String... args) {
        if (guardRepository.findByUserName("admin").isEmpty()) {
            Guard admin = new Guard();
            admin.setUserName("admin");
            admin.setHashedPin(encoder.encode("admin"));
            guardRepository.save(admin);
            logger.info("Default admin guard seeded successfully (username: admin, pin: admin)");
        }
    }
}
