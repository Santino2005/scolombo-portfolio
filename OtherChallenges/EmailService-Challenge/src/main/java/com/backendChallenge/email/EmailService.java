package com.backendChallenge.email;

import com.backendChallenge.email.emailProvider.registry.EmailHandler;
import com.backendChallenge.result.EmailResult;
import com.backendChallenge.result.Result;
import com.backendChallenge.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmailService {

    private final EmailHandler emailHandler;
    private final EmailRepository emailRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);


    public EmailService(@Qualifier("emailChain") EmailHandler emailHandler, EmailRepository emailRepository) {
        this.emailHandler = emailHandler;
        this.emailRepository = emailRepository;
    }

    public EmailResult sendEmail(User user, String to, String subject, String body) {
        LocalDate today = LocalDate.now(user.getZone());
        long sentToday = emailRepository.countByUserIdAndDate(user.getId(), today);
        if (sentToday >= 1000) {
            return EmailResult.notSuccess("Daily limit reached 1000 emails");
        }
        EmailResult result = emailHandler.sendEmail(user.getEmail(),to, subject, body);
        if(result.successful()){
            emailRepository.save(new Email(user.getId(), LocalDate.now(user.getZone()), result.provider()));
            return result;
        }
        return result;
    }
}
