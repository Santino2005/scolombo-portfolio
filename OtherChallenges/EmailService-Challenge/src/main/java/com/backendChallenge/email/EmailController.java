package com.backendChallenge.email;

import com.backendChallenge.email.dto.EmailRequest;
import com.backendChallenge.result.EmailResult;
import com.backendChallenge.result.Result;
import com.backendChallenge.user.User;
import com.backendChallenge.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;
    private final UserService userService;

    public EmailController(EmailService emailService,UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request, @AuthenticationPrincipal Jwt jwt) {
        User user = userService.existingUser(jwt);
        EmailResult result = emailService.sendEmail(user,request.to(),request.subject(), request.body());
        if (result.successful()) {
            return ResponseEntity.ok(result.message() + " with provider: " + result.provider());
        }
        return ResponseEntity.badRequest().body(result.message());
    }
}
