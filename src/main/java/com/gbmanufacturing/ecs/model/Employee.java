package com.gbmanufacturing.ecs.model;

import java.time.LocalDateTime;

/**
 * Represents an employee user in the Equipment Checkout System.
 *
 * Employees can check out equipment, return equipment, and view their own usage history.
 */
public class Employee extends User {

    /**
     * Default constructor used by DAO classes.
     */
    public Employee() {
        super();
        setRole(Role.EMPLOYEE);
    }

    /**
     * Constructor for creating a new employee before the database assigns an ID.
     *
     * @param employeeId unique employee ID
     * @param username username for login/search
     * @param passwordHash hashed password
     * @param firstName employee first name
     * @param lastName employee last name
     */
    public Employee(String employeeId, String username, String passwordHash,
                    String firstName, String lastName) {

        super(employeeId, username, passwordHash, firstName, lastName, Role.EMPLOYEE);
    }

    /**
     * Full constructor used by DAO classes when loading an employee from the database.
     *
     * @param userId database user ID
     * @param employeeId unique employee ID
     * @param username username for login/search
     * @param passwordHash hashed password
     * @param firstName employee first name
     * @param lastName employee last name
     * @param failedLoginAttempts failed login count
     * @param locked account locked status
     * @param active account active status
     * @param createdAt account creation date/time
     * @param lastLoginAt last successful login date/time
     */
    public Employee(int userId, String employeeId, String username, String passwordHash,
                    String firstName, String lastName, int failedLoginAttempts,
                    boolean locked, boolean active, LocalDateTime createdAt,
                    LocalDateTime lastLoginAt) {

        super(userId, employeeId, username, passwordHash, firstName, lastName,
                Role.EMPLOYEE, failedLoginAttempts, locked, active, createdAt, lastLoginAt);
    }

    /**
     * Returns true if this employee can check out equipment.
     *
     * @return true if the employee account can log in
     */
    public boolean canCheckoutEquipment() {
        return canLogin();
    }

    /**
     * Returns true if this employee can return equipment.
     *
     * @return true if the employee account can log in
     */
    public boolean canReturnEquipment() {
        return canLogin();
    }

    /**
     * Returns true if this employee can view personal checkout history.
     *
     * @return true if the employee account can log in
     */
    public boolean canViewPersonalHistory() {
        return canLogin();
    }
}