package com.gbmanufacturing.ecs.model;

import java.time.LocalDateTime;

public class User {

    public enum Role {
        EMPLOYEE,
        SUPERVISOR
    }

    protected int userId;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected Role role;

    protected boolean locked;
    protected boolean active;
    protected int failedLoginAttempts;
    protected LocalDateTime createdAt;
    protected LocalDateTime lastLoginAt;

    public User() {
        this.role = Role.EMPLOYEE;
        this.active = true;
        this.locked = false;
        this.failedLoginAttempts = 0;
        this.createdAt = LocalDateTime.now();
    }

    // ---------------- GETTERS ----------------

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isActive() {
        return active;
    }

    // ---------------- SETTERS ----------------

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setFailedLoginAttempts(int attempts) {
        this.failedLoginAttempts = attempts;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
    
    public boolean isSupervisor() {
        return role == Role.SUPERVISOR;
    }

    public boolean isAdmin() {
        return false;
    }
}