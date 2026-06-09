package com.gbmanufacturing.ecs.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a system user in the Equipment Checkout System.
 * 
 * The User class stores login, role, and account status information.
 * Employee and Supervisor classes may inherit from this class.
 */
public class User {

    /**
     * Defines the user's access level in the ECS application.
     */
    public enum Role {
        EMPLOYEE,
        SUPERVISOR,
        ADMIN
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
     * Default constructor used by DAO classes when mapping database records.
     */
    public User() {
        this.role = Role.EMPLOYEE;
        this.failedLoginAttempts = 0;
        this.locked = false;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Creates a new user with required account information.
     *
     * @param employeeId unique employee identifier
     * @param username login username
     * @param passwordHash hashed password value
     * @param firstName user's first name
     * @param lastName user's last name
     * @param role user's system role
     */
    public User(String employeeId, String username, String passwordHash,
                String firstName, String lastName, Role role) {

        setEmployeeId(employeeId);
        setUsername(username);
        setPasswordHash(passwordHash);
        setFirstName(firstName);
        setLastName(lastName);
        setRole(role);

        this.failedLoginAttempts = 0;
        this.locked = false;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Records a failed login attempt and locks the account after five failures.
     */
    public void recordFailedLoginAttempt() {
        failedLoginAttempts++;

        if (failedLoginAttempts >= 5) {
            locked = true;
        }
    }

    /**
     * Resets failed login attempts after a successful login or password reset.
     */
    public void resetFailedLoginAttempts() {
        failedLoginAttempts = 0;
    }

    /**
     * Marks the account as locked.
     */
    public void lockAccount() {
        locked = true;
    }

    /**
     * Unlocks the account and clears failed login attempts.
     */
    public void unlockAccount() {
        locked = false;
        resetFailedLoginAttempts();
    }

    /**
     * Returns whether the user is allowed to log in.
     *
     * @return true if the user is active and not locked
     */
    public boolean canLogin() {
        return active && !locked;
    }

    /**
     * Updates the user's last login timestamp.
     */
    public void updateLastLogin() {
        lastLoginAt = LocalDateTime.now();
        resetFailedLoginAttempts();
    }

    /**
     * Returns true if the user has supervisor-level access.
     *
     * @return true if role is SUPERVISOR or ADMIN
     */
    public boolean hasSupervisorAccess() {
        return role == Role.SUPERVISOR || role == Role.ADMIN;
    }

    /**
     * Returns the user's full name.
     *
     * @return first and last name combined
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Gets the database user ID.
     *
     * @return user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the database user ID.
     *
     * @param userId user ID
     */
    public void setUserId(int userId) {
        if (userId < 0) {
            throw new IllegalArgumentException("User ID cannot be negative.");
        }
        this.userId = userId;
    }

    /**
     * Gets the employee ID.
     *
     * @return employee ID
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the employee ID.
     *
     * @param employeeId unique employee ID
     */
    public void setEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.isBlank()) {
            throw new IllegalArgumentException("Employee ID is required.");
        }
        this.employeeId = employeeId.trim();
    }

    /**
     * Gets the username.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username login username
     */
    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }
        this.username = username.trim();
    }

    /**
     * Gets the hashed password.
     *
     * @return password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the hashed password.
     *
     * @param passwordHash hashed password
     */
    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash is required.");
        }
        this.passwordHash = passwordHash;
    }

    /**
     * Gets the user's first name.
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required.");
        }
        this.firstName = firstName.trim();
    }

    /**
     * Gets the user's last name.
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName last name
     */
    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required.");
        }
        this.lastName = lastName.trim();
    }

    /**
     * Gets the user's role.
     *
     * @return user role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the user's role.
     *
     * @param role user role
     */
    public void setRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("User role is required.");
        }
        this.role = role;
    }

    /**
     * Gets failed login attempts.
     *
     * @return failed login attempt count
     */
    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    /**
     * Sets failed login attempts.
     *
     * @param failedLoginAttempts number of failed attempts
     */
    public void setFailedLoginAttempts(int failedLoginAttempts) {
        if (failedLoginAttempts < 0) {
            throw new IllegalArgumentException("Failed login attempts cannot be negative.");
        }
        this.failedLoginAttempts = failedLoginAttempts;
    }

    /**
     * Checks whether the account is locked.
     *
     * @return true if locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Sets the locked status.
     *
     * @param locked locked status
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Checks whether the account is active.
     *
     * @return true if active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether the account is active.
     *
     * @param active active status
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the account creation timestamp.
     *
     * @return created timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the account creation timestamp.
     *
     * @param createdAt created timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last login timestamp.
     *
     * @return last login timestamp
     */
    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    /**
     * Sets the last login timestamp.
     *
     * @param lastLoginAt last login timestamp
     */
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    /**
     * Compares users by user ID and employee ID.
     *
     * @param object object to compare
     * @return true if users match
     */
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

    /**
     * Generates a hash code for the user.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, employeeId);
    }

    /**
     * Returns a readable user summary.
     *
     * @return user summary string
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", employeeId='" + employeeId + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", role=" + role +
                ", locked=" + locked +
                ", active=" + active +
                '}';
    }
}