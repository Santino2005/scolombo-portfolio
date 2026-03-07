package com.backendChallenge.email.emailProvider;

import com.backendChallenge.result.Result;

public interface EmailProviders {

    public Result sendEmail(String to, String from, String subject, String body);
}
