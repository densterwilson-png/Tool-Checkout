package com.gbmanufacturing.ecs.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
// CheckoutDAO manages equipment checkouts and returns, allowing for recording transactions, retrieving active checkouts, and checking equipment status in the Equipment Checkout System.
public class CheckoutDAO {

    private final Connection connection;

    public CheckoutDAO(Connection connection) {
        this.connection = connection;
    }

    // ---------------- CREATE CHECKOUT ----------------
    public void checkoutItem(int userId, int equipmentId) throws SQLException {

        String sql = "INSERT INTO checkouts (id, equipment_id, checkout_time) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, equipmentId);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
        }
    }

    // ---------------- RETURN ITEM ----------------
    public void returnItem(int checkoutId) throws SQLException {

        String sql = "UPDATE checkouts SET return_time = ? WHERE id = ? AND return_time IS NULL";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, checkoutId);
            stmt.executeUpdate();
        }
    }

    // ---------------- GET ACTIVE CHECKOUTS ----------------
    public List<Integer> getActiveCheckoutsByUser(int userId) throws SQLException {

        String sql = "SELECT id FROM checkouts WHERE id = ? AND return_time IS NULL";

        List<Integer> results = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(rs.getInt("id"));
            }
        }

        return results;
    }

    // ---------------- CHECK IF ITEM IS OUT ----------------
    public boolean isEquipmentCheckedOut(int equipmentId) throws SQLException {

        String sql = "SELECT COUNT(*) FROM checkouts WHERE equipment_id = ? AND return_time IS NULL";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, equipmentId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }

        return false;
    }

    // ---------------- HISTORY ----------------
    public List<Integer> getAllCheckoutsByUser(int userId) throws SQLException {

        String sql = "SELECT id FROM checkouts WHERE id = ? ORDER BY checkout_time DESC";

        List<Integer> results = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(rs.getInt("id"));
            }
        }

        return results;
    }
}