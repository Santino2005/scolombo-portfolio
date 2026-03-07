package com.backendChallenge.email.emailProvider.providers;

import com.backendChallenge.email.emailProvider.EmailProviders;
import com.backendChallenge.result.Result;
import com.backendChallenge.result.EmailResult;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SendGridProvider implements EmailProviders {

    private final SendGrid sendGrid;
    private static final Logger log = LoggerFactory.getLogger(SendGridProvider.class);

    public SendGridProvider(@Value("${sendgrid.api-key}") String apiKey) {
       this.sendGrid = new SendGrid(apiKey);
    }

    @Override
    public EmailResult sendEmail(String from, String to, String subject, String body){
        log.info("Preparing to send email from '{}' to '{}'", from, to);
        Mail mail = generateMail(from, to, subject, body);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        try {
            String mailBody = mail.build();
            log.debug("Generated SendGrid mail payload: {}", mailBody);
            request.setBody(mailBody);
            Response response = sendGrid.api(request);
            log.info("SendGrid response: status={}, body={}", response.getStatusCode(), response.getBody());
            if (response.getStatusCode() >= 400) {
                return EmailResult.notSuccess("SendGrid failed: " + response.getBody());
            }
            log.info("Email sent successfully to {}", to);
            return EmailResult.success(mail,"Mail send successful","SendGrid");
        }catch(Exception e){
            return EmailResult.notSuccess("SendGrid failed sending the mail, occurred: " + e.getMessage());
        }
    }

    private Mail generateMail(String from, String to, String subject, String body) {
        Email emailSender = new Email(from);
        Email emailReceiver = new Email(to);
        Content content = new Content("text/plain", body);
        return new Mail(emailSender, subject, emailReceiver, content);
    }
}
