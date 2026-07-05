package com.geno_insights.scolombo.guard.controller;

import com.geno_insights.scolombo.guard.service.GuardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guard")
@RequiredArgsConstructor
public class GuardController {

    private final GuardService guardService;

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String pin
    ) {
        return guardService.login(username, pin);
    }
}
