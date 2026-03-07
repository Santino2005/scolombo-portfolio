package com.backendChallenge.email.emailProvider.registry;
import com.backendChallenge.email.emailProvider.EmailProviders;
import com.backendChallenge.result.EmailResult;
import com.backendChallenge.result.Result;
import org.springframework.stereotype.Component;

@Component
public abstract class EmailHandler implements EmailProviders {

    protected EmailHandler next;
    public void setNext(EmailHandler next) {
        this.next = next;
    }

    @Override
    public EmailResult sendEmail(String from, String to, String subject, String body) {
        EmailResult result = trySend(from, to, subject, body);
        if (!result.successful() && next != null) {
            return next.sendEmail(from, to, subject, body);
        }
        return result;
    }
    protected abstract EmailResult trySend(String from, String to, String subject, String body);
}
