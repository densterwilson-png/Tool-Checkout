package com.gbmanufacturing.ecs.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the SQLite database connection for the Equipment Checkout System.
 */
public class DatabaseManager {
    private static final String DB_DIR = "database";
    private static final String DB_FILE = DB_DIR + "/ecs.db";
    private static final String DATABASE_URL = "jdbc:sqlite:" + DB_FILE;

    private DatabaseManager() {
        // Prevent instantiation.
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}
