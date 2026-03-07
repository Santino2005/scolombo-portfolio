package com.backendChallenge.user;

import com.backendChallenge.email.EmailRepository;
import com.backendChallenge.user.dto.UserStatsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stats")
public class
StatsController {

    private final EmailRepository emailRepository;

    public StatsController(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @PreAuthorize("hasAuthority('ROLE_admin')")
    @GetMapping
    public ResponseEntity<List<UserStatsDTO>> getStats() {
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));
        List<UserStatsDTO> stats = emailRepository.findDailyStats(today).stream()
                .filter(user -> user.emailCount() != 0).collect(Collectors.toList());
        return ResponseEntity.ok(stats);
    }
}
