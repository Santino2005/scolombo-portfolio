package com.backendChallenge.email.emailProvider.registry.handlers;

import com.backendChallenge.email.emailProvider.providers.SendGridProvider;
import com.backendChallenge.email.emailProvider.registry.EmailHandler;
import com.backendChallenge.result.EmailResult;
import com.backendChallenge.result.Result;
import org.springframework.stereotype.Component;

@Component
public class SendGridHandler extends EmailHandler {

    private final SendGridProvider sendGrid;

    public SendGridHandler(SendGridProvider sendGrid) {
        this.sendGrid = sendGrid;
    }

    @Override
    protected EmailResult trySend(String from, String to, String subject, String body) {
        return sendGrid.sendEmail(from, to, subject, body);
    }
}
