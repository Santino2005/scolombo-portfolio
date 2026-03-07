package com.backendChallenge.email;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String userId;

    private LocalDate emailDate;

    private String provider;

    public Email() {}
    public Email(String userId, LocalDate lastEmail, String provider) {
        this.userId = userId;
        this.emailDate = lastEmail;
        this.provider = provider;
    }

    public UUID getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getEmailDate() {
        return emailDate;
    }
    public String getProvider() {
        return provider;
    }

}
