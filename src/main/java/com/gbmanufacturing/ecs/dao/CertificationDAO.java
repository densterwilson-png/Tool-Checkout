package com.gbmanufacturing.ecs.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
// CertificationDAO manages user certifications, allowing for adding, retrieving, checking, and revoking certifications for users in the Equipment Checkout System.
public class CertificationDAO {

    private final Connection connection;

    public CertificationDAO(Connection connection) {
        this.connection = connection;
    }

    // ADD certification
    public void addCertification(int userId, String type) throws SQLException {

        String sql =
            "INSERT INTO user_certifications (user_id, certification_type) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, type);

            stmt.executeUpdate();
        }
    }

    // GET certifications
    public List<String> getCertificationsByUser(int userId) throws SQLException {

        String sql =
            "SELECT certification_type FROM user_certifications WHERE user_id = ?";

        List<String> certs = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                certs.add(rs.getString("certification_type"));
            }
        }

        return certs;
    }

    // CHECK certification exists
    public boolean isUserCertified(int userId, String type) throws SQLException {

        String sql =
            "SELECT 1 FROM user_certifications WHERE user_id = ? AND certification_type = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, type);

            ResultSet rs = stmt.executeQuery();

            return rs.next();
        }
    }

    // DELETE certification
    public void revokeCertification(int userId, String type) throws SQLException {

        String sql =
            "DELETE FROM user_certifications WHERE user_id = ? AND certification_type = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, type);

            stmt.executeUpdate();
        }
    }
}