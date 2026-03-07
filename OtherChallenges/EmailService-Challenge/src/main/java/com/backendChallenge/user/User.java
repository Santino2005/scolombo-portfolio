package com.backendChallenge.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.ZoneId;

@Entity
@Table(name = "backend-user")
public class User {

    @Id
    private String id;

    private String email;

    private static ZoneId zone = ZoneId.of("UTC");

    public User(String id,String email,ZoneId zone) {
        this.id = id;
        this.email = email;
        User.zone = zone != null ? zone : ZoneId.of("UTC");
    }
    public User() {}

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public ZoneId getZone() {
        return zone;
    }
}
