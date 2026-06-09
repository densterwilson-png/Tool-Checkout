
package com.gbmanufacturing.ecs.database;

/** ********************************************************
 * Program Name: DatabaseSeeder.java
 * Programmer's Name: Robert Sadler
 * Group: Group 4
 * Program Description: Seeds the Equipment Checkout System database
 * with sample users, equipment, and user certifications.
 ********************************************************** */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseSeeder {

    public static void seedDatabase(Connection conn) {
        try {
            if (isAlreadySeeded(conn)) {
                System.out.println("Database already contains seed data. Skipping seeding.");
                return;
            }

            seedUsers(conn);
            seedEquipment(conn);
            seedCertifications(conn);

            System.out.println("Database seeded successfully.");

        } catch (SQLException e) {
            System.out.println("Error seeding database: " + e.getMessage());
        }
    }

    private static boolean isAlreadySeeded(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }

        return false;
    }

    private static void seedUsers(Connection conn) throws SQLException {
        String sql = """
                INSERT INTO users
                (username, password, first_name, last_name, role)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            addUser(pstmt, "jsmith", "password123", "John", "Smith", "Employee");
            addUser(pstmt, "agarcia", "password123", "Ana", "Garcia", "Employee");
            addUser(pstmt, "mjohnson", "password123", "Mike", "Johnson", "Employee");
            addUser(pstmt, "kwilliams", "password123", "Karen", "Williams", "Employee");
            addUser(pstmt, "dlee", "password123", "David", "Lee", "Employee");

            addUser(pstmt, "sroberts", "admin123", "Sarah", "Roberts", "Supervisor");
            addUser(pstmt, "tbrown", "admin123", "Thomas", "Brown", "Supervisor");

            pstmt.executeBatch();
        }
    }

    private static void addUser(
            PreparedStatement pstmt,
            String username,
            String password,
            String firstName,
            String lastName,
            String role
    ) throws SQLException {

        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.setString(3, firstName);
        pstmt.setString(4, lastName);
        pstmt.setString(5, role);
        pstmt.addBatch();
    }

    private static void seedEquipment(Connection conn) throws SQLException {
        String sql = """
                INSERT INTO equipment
                (equipment_name, equipment_type, condition_status,
                 availability_status, required_certification)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            addEquipment(pstmt, "Cordless Drill", "Power Tool",
                    "Good", "Available", null);

            addEquipment(pstmt, "Circular Saw", "Power Tool",
                    "Good", "Available", null);

            addEquipment(pstmt, "Socket Set", "Hand Tool",
                    "Excellent", "Available", null);

            addEquipment(pstmt, "Ladder", "Safety Equipment",
                    "Fair", "Available", null);

            addEquipment(pstmt, "Pressure Washer", "Cleaning Equipment",
                    "Maintenance Required", "Unavailable", null);

            addEquipment(pstmt, "Forklift Key", "Vehicle Access",
                    "Good", "Available", "FORKLIFT");

            addEquipment(pstmt, "Welding Kit", "Specialized Tool",
                    "Good", "Available", "WELDING");

            pstmt.executeBatch();
        }
    }

    private static void addEquipment(
            PreparedStatement pstmt,
            String equipmentName,
            String equipmentType,
            String conditionStatus,
            String availabilityStatus,
            String requiredCertification
    ) throws SQLException {

        pstmt.setString(1, equipmentName);
        pstmt.setString(2, equipmentType);
        pstmt.setString(3, conditionStatus);
        pstmt.setString(4, availabilityStatus);

        if (requiredCertification == null) {
            pstmt.setNull(5, java.sql.Types.VARCHAR);
        } else {
            pstmt.setString(5, requiredCertification);
        }

        pstmt.addBatch();
    }

    private static void seedCertifications(Connection conn) throws SQLException {
        String sql = """
                INSERT INTO user_certifications
                (user_id, certification_type)
                VALUES (?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Mike Johnson is forklift certified.
            addCertification(pstmt, 3, "FORKLIFT");

            // David Lee is welding certified.
            addCertification(pstmt, 5, "WELDING");

            // Thomas Brown is both forklift and welding certified.
            // He is also a supervisor, but supervisor status alone does not grant certification.
            addCertification(pstmt, 7, "FORKLIFT");
            addCertification(pstmt, 7, "WELDING");

            pstmt.executeBatch();
        }
    }

    private static void addCertification(
            PreparedStatement pstmt,
            int userId,
            String certificationType
    ) throws SQLException {

        pstmt.setInt(1, userId);
        pstmt.setString(2, certificationType);
        pstmt.addBatch();
    }
}