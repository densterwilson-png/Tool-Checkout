
package com.gbmanufacturing.ecs.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** ********************************************************
 * Program Name: DatabaseManager.java
 * Programmer's Name: Robert Sadler
 * Group: Group 4
 * Program Description: Manages the SQLite database connection
 * for the Equipment Checkout System.
 ********************************************************** */

public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:database/ecs.db";

    private DatabaseManager() {
        // Prevent instantiation.
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}