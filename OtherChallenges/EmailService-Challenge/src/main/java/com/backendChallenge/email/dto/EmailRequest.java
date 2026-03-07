package com.backendChallenge.email.dto;

public record EmailRequest(String to, String subject, String body) {
}
