package com.gbmanufacturing.ecs.database;

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

            return rs.next() && rs.getInt(1) > 0;
        }
    }

    // ---------------- USERS ----------------
    private static void seedUsers(Connection conn) throws SQLException {

        String sql =
            "INSERT INTO users (username, password, first_name, last_name, role) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            addUser(pstmt, "jsmith", "password123", "John", "Smith", "EMPLOYEE");
            addUser(pstmt, "agarcia", "password123", "Ana", "Garcia", "EMPLOYEE");
            addUser(pstmt, "mjohnson", "password123", "Mike", "Johnson", "EMPLOYEE");
            addUser(pstmt, "kwilliams", "password123", "Karen", "Williams", "EMPLOYEE");
            addUser(pstmt, "dlee", "password123", "David", "Lee", "EMPLOYEE");

            addUser(pstmt, "sroberts", "admin123", "Sarah", "Roberts", "SUPERVISOR");
            addUser(pstmt, "tbrown", "admin123", "Thomas", "Brown", "SUPERVISOR");

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

    // ---------------- EQUIPMENT ----------------
    private static void seedEquipment(Connection conn) throws SQLException {

        String sql =
            "INSERT INTO equipment " +
            "(equipment_name, equipment_type, condition_status, availability_status, required_certification) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            addEquipment(pstmt, "Cordless Drill", "Power Tool", "Good", "available", null);
            addEquipment(pstmt, "Circular Saw", "Power Tool", "Good", "available", null);
            addEquipment(pstmt, "Socket Set", "Hand Tool", "Excellent", "available", null);
            addEquipment(pstmt, "Ladder", "Safety Equipment", "Fair", "available", null);
            addEquipment(pstmt, "Pressure Washer", "Cleaning Equipment", "Maintenance Required", "unavailable", null);

            addEquipment(pstmt, "Forklift Key", "Vehicle Access", "Good", "available", "FORKLIFT");
            addEquipment(pstmt, "Welding Kit", "Specialized Tool", "Good", "available", "WELDING");

            pstmt.executeBatch();
        }
    }

    private static void addEquipment(
            PreparedStatement pstmt,
            String name,
            String type,
            String condition,
            String availability,
            String cert
    ) throws SQLException {

        pstmt.setString(1, name);
        pstmt.setString(2, type);
        pstmt.setString(3, condition);
        pstmt.setString(4, availability);

        if (cert == null) {
            pstmt.setNull(5, java.sql.Types.VARCHAR);
        } else {
            pstmt.setString(5, cert);
        }

        pstmt.addBatch();
    }

    // ---------------- CERTIFICATIONS ----------------
    private static void seedCertifications(Connection conn) throws SQLException {

        String sql =
            "INSERT INTO user_certifications (user_id, certification_type) " +
            "VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            addCertification(pstmt, 6, "FORKLIFT");
            addCertification(pstmt, 7, "WELDING");
            addCertification(pstmt, 7, "FORKLIFT");
            addCertification(pstmt, 6, "WELDING");

            pstmt.executeBatch();
        }
    }

    private static void addCertification(
            PreparedStatement pstmt,
            int userId,
            String type
    ) throws SQLException {

        pstmt.setInt(1, userId);
        pstmt.setString(2, type);
        pstmt.addBatch();
    }
}