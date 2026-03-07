package com.backendChallenge.email.emailProvider.registry.handlers;

import com.backendChallenge.email.emailProvider.providers.SparkPostProvider;
import com.backendChallenge.email.emailProvider.registry.EmailHandler;
import com.backendChallenge.result.EmailResult;
import com.backendChallenge.result.Result;
import org.springframework.stereotype.Component;

@Component
public class SparkPostHandler extends EmailHandler {
    private final SparkPostProvider sparkPostProvider;

    public SparkPostHandler(SparkPostProvider sparkPostProvider) {
        this.sparkPostProvider = sparkPostProvider;
    }

    @Override
    protected EmailResult trySend(String from, String to, String subject, String body) {
        return sparkPostProvider.sendEmail(from, to, subject, body);
    }
}
