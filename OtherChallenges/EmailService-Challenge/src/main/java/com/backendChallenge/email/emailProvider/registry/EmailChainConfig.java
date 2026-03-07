package com.backendChallenge.email.emailProvider.registry;


import com.backendChallenge.email.emailProvider.registry.handlers.SendGridHandler;
import com.backendChallenge.email.emailProvider.registry.handlers.SparkPostHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailChainConfig {

    @Bean
    public EmailHandler emailChain(SendGridHandler sendGrid, SparkPostHandler sparkPost) {
        sparkPost.setNext(sendGrid);
        return sparkPost;
    }
}
