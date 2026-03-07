package com.backendChallenge.result;

import com.sendgrid.helpers.mail.Mail;

public record EmailResult(Mail data, String message, boolean successful, String provider) implements Result {

    public static EmailResult success(Mail data, String message, String provider) {
        return new EmailResult(data, message, true, provider);
    }

    public static EmailResult notSuccess(String message) {
        return new EmailResult(null, message, false,null);
    }
}
