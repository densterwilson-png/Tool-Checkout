package com.gbmanufacturing.ecs.model;

import java.time.LocalDateTime;

/**
 * Represents a supervisor user in the Equipment Checkout System.
 *
 * Supervisors can perform employee actions and also manage inventory,
 * reports, overdue alerts, and employee access.
 */
public class Supervisor extends User {

    /**
     * Default constructor used by DAO classes.
     */
    public Supervisor() {
        super();
        setRole(Role.SUPERVISOR);
    }

    /**
     * Constructor for creating a new supervisor before the database assigns an ID.
     *
     * @param employeeId unique employee ID
     * @param username username for login/search
     * @param passwordHash hashed password
     * @param firstName supervisor first name
     * @param lastName supervisor last name
     */
    public Supervisor(String employeeId, String username, String passwordHash,
                      String firstName, String lastName) {

        super(employeeId, username, passwordHash, firstName, lastName, Role.SUPERVISOR);
    }

    /**
     * Full constructor used by DAO classes when loading a supervisor from the database.
     *
     * @param userId database user ID
     * @param employeeId unique employee ID
     * @param username username for login/search
     * @param passwordHash hashed password
     * @param firstName supervisor first name
     * @param lastName supervisor last name
     * @param failedLoginAttempts failed login count
     * @param locked account locked status
     * @param active account active status
     * @param createdAt account creation date/time
     * @param lastLoginAt last successful login date/time
     */
    public Supervisor(int userId, String employeeId, String username, String passwordHash,
                      String firstName, String lastName, int failedLoginAttempts,
                      boolean locked, boolean active, LocalDateTime createdAt,
                      LocalDateTime lastLoginAt) {

        super(userId, employeeId, username, passwordHash, firstName, lastName,
                Role.SUPERVISOR, failedLoginAttempts, locked, active, createdAt, lastLoginAt);
    }

    /**
     * Supervisors can also check out equipment.
     *
     * @return true if the supervisor account can log in
     */
    public boolean canCheckoutEquipment() {
        return canLogin();
    }

    /**
     * Supervisors can also return equipment.
     *
     * @return true if the supervisor account can log in
     */
    public boolean canReturnEquipment() {
        return canLogin();
    }

    /**
     * Returns true if the supervisor can manage inventory.
     *
     * @return true if the supervisor account can log in
     */
    public boolean canManageInventory() {
        return canLogin();
    }

    /**
     * Returns true if the supervisor can generate reports.
     *
     * @return true if the supervisor account can log in
     */
    public boolean canGenerateReports() {
        return canLogin();
    }

    /**
     * Returns true if the supervisor can view overdue alerts.
     *
     * @return true if the supervisor account can log in
     */
    public boolean canViewOverdueAlerts() {
        return canLogin();
    }

    /**
     * Returns true if the supervisor can remove or disable employee access.
     *
     * @return true if the supervisor account can log in
     */
    public boolean canRemoveEmployeeAccess() {
        return canLogin();
    }

    /**
     * Returns true if the supervisor can reset employee passwords.
     *
     * @return true if the supervisor account can log in
     */
    public boolean canResetEmployeePassword() {
        return canLogin();
    }
}