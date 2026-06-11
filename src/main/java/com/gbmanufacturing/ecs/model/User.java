package com.gbmanufacturing.ecs.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base class for all Equipment Checkout System users.
 *
 * Employee and Supervisor extend this class.
 */
public class User {

    /**
     * User role values used for role-based access control.
     */
    public enum Role {
        EMPLOYEE,
        SUPERVISOR
    }

    private int userId;
    private String employeeId;
    private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private Role role;
    private int failedLoginAttempts;
    private boolean locked;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    /**
     * Default constructor used by DAO classes.
     */
    public User() {
        this.role = Role.EMPLOYEE;
        this.failedLoginAttempts = 0;
        this.locked = false;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructor for creating a new user before the database assigns an ID.
     *
     * @param employeeId unique employee ID
     * @param username username for login
     * @param passwordHash hashed password
     * @param firstName user's first name
     * @param lastName user's last name
     * @param role user's system role
     */
    public User(String employeeId, String username, String passwordHash,
                String firstName, String lastName, Role role) {

        this();
        setEmployeeId(employeeId);
        setUsername(username);
        setPasswordHash(passwordHash);
        setFirstName(firstName);
        setLastName(lastName);
        setRole(role);
    }

    /**
     * Full constructor used by DAO classes when loading users from the database.
     *
     * @param userId database user ID
     * @param employeeId unique employee ID
     * @param username username for login
     * @param passwordHash hashed password
     * @param firstName user's first name
     * @param lastName user's last name
     * @param role user's system role
     * @param failedLoginAttempts failed login count
     * @param locked account locked status
     * @param active account active status
     * @param createdAt account creation timestamp
     * @param lastLoginAt last login timestamp
     */
    public User(int userId, String employeeId, String username, String passwordHash,
                String firstName, String lastName, Role role, int failedLoginAttempts,
                boolean locked, boolean active, LocalDateTime createdAt,
                LocalDateTime lastLoginAt) {

        setUserId(userId);
        setEmployeeId(employeeId);
        setUsername(username);
        setPasswordHash(passwordHash);
        setFirstName(firstName);
        setLastName(lastName);
        setRole(role);
        setFailedLoginAttempts(failedLoginAttempts);
        setLocked(locked);
        setActive(active);
        setCreatedAt(createdAt);
        setLastLoginAt(lastLoginAt);
    }

    /**
     * Converts a database role value into a Role enum.
     *
     * @param roleValue role value from database
     * @return matching Role enum
     */
    public static Role roleFromString(String roleValue) {
        if (roleValue == null || roleValue.isBlank()) {
            throw new IllegalArgumentException("Role value cannot be empty.");
        }

        String normalizedRole = roleValue.trim().toUpperCase();

        return switch (normalizedRole) {
            case "EMPLOYEE" -> Role.EMPLOYEE;
            case "SUPERVISOR" -> Role.SUPERVISOR;
            default -> throw new IllegalArgumentException(
                    "Unsupported user role: " + roleValue
            );
        };
    }

    /**
     * Records a failed login attempt and locks the account after five failed attempts.
     */
    public void recordFailedLoginAttempt() {
        failedLoginAttempts++;

        if (failedLoginAttempts >= 5) {
            locked = true;
        }
    }

    /**
     * Resets the failed login count.
     */
    public void resetFailedLoginAttempts() {
        failedLoginAttempts = 0;
    }

    /**
     * Locks the user account.
     */
    public void lockAccount() {
        locked = true;
    }

    /**
     * Unlocks the user account and resets failed login attempts.
     */
    public void unlockAccount() {
        locked = false;
        resetFailedLoginAttempts();
    }

    /**
     * Returns true if the user can currently log in.
     *
     * @return true if active and not locked
     */
    public boolean canLogin() {
        return active && !locked;
    }

    /**
     * Updates the last login timestamp.
     */
    public void updateLastLogin() {
        lastLoginAt = LocalDateTime.now();
        resetFailedLoginAttempts();
    }

    /**
     * Returns the user's full name.
     *
     * @return first and last name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Returns the role name as a database-friendly string.
     *
     * @return role name
     */
    public String getRoleName() {
        return role.name();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId < 0) {
            throw new IllegalArgumentException("User ID cannot be negative.");
        }

        this.userId = userId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.isBlank()) {
            throw new IllegalArgumentException("Employee ID is required.");
        }

        this.employeeId = employeeId.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }

        this.username = username.trim();
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash is required.");
        }

        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required.");
        }

        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required.");
        }

        this.lastName = lastName.trim();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role is required.");
        }

        this.role = role;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        if (failedLoginAttempts < 0) {
            throw new IllegalArgumentException("Failed login attempts cannot be negative.");
        }

        this.failedLoginAttempts = failedLoginAttempts;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof User otherUser)) {
            return false;
        }

        return userId == otherUser.userId
                && Objects.equals(employeeId, otherUser.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, employeeId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", employeeId='" + employeeId + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", role=" + role +
                ", failedLoginAttempts=" + failedLoginAttempts +
                ", locked=" + locked +
                ", active=" + active +
                '}';
    }
}