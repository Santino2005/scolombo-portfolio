package com.backendChallenge.result;

import com.sendgrid.helpers.mail.Mail;

public interface Result {
    public boolean successful();
    public String message();
    public Mail data();

}
