package com.gbmanufacturing.ecs.auth;

import java.time.LocalDateTime;

import com.gbmanufacturing.ecs.model.User;
// AuthSession represents an authenticated user session, storing the user information and login time for session management purposes.
public class AuthSession {

    private final User user;
    private final LocalDateTime loginTime;

    public AuthSession(User user) {
        this.user = user;
        this.loginTime = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public int getUserId() {
        return user.getUserId();
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }
}