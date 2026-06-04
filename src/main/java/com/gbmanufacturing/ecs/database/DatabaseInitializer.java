package com.gbmanufacturing.ecs.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** ********************************************************
 * Program Name: DatabaseInitializer.java
 * Programmer's Name: Robert Sadler
 * Group: Group 4
 * Program Description: Creates the SQLite database tables needed
 * for the Equipment Checkout System.
 ********************************************************** */

public class DatabaseInitializer {

    private DatabaseInitializer() {
        // Prevent instantiation.
    }

    public static void initializeDatabase() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            createUsersTable(stmt);
            createEquipmentTable(stmt);
            createUserCertificationsTable(stmt);
            createCheckoutRecordsTable(stmt);

            DatabaseSeeder.seedDatabase(conn);

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }

    private static void createUsersTable(Statement stmt) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    role TEXT NOT NULL
                )
                """;

        stmt.execute(sql);
    }

    private static void createEquipmentTable(Statement stmt) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS equipment (
                    equipment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    equipment_name TEXT NOT NULL,
                    equipment_type TEXT NOT NULL,
                    condition_status TEXT NOT NULL,
                    availability_status TEXT NOT NULL,
                    required_certification TEXT
                )
                """;

        stmt.execute(sql);
    }

    private static void createUserCertificationsTable(Statement stmt) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS user_certifications (
                    certification_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    certification_type TEXT NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(user_id)
                )
                """;

        stmt.execute(sql);
    }

    private static void createCheckoutRecordsTable(Statement stmt) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS checkout_records (
                    checkout_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    equipment_id INTEGER NOT NULL,
                    checkout_date TEXT NOT NULL,
                    due_date TEXT,
                    return_date TEXT,
                    status TEXT NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(user_id),
                    FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id)
                )
                """;

        stmt.execute(sql);
    }
}