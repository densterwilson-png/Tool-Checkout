package com.gbmanufacturing.ecs;

import com.gbmanufacturing.ecs.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// DAO for recording equipment transactions (checkouts, returns, etc.)
public class TransactionDAO {
    // Records a transaction with the current timestamp

    public boolean recordTransaction(int equipmentId, int userId, String equipmentStatus) throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement(
                "INSERT INTO equipment_transactions(equipment_id, user_id, transaction_date, equipment_status, notes) VALUES(?,?,?,?,?)")) {
            ps.setInt(1, equipmentId);
            ps.setInt(2, userId);
            ps.setString(3, Instant.now().toString());
            ps.setString(4, equipmentStatus);
            ps.setString(5, null);
            return ps.executeUpdate() == 1;
        }
    }
    // Records a transaction using an EquipmentTransaction object (allows custom timestamps and notes)

    public boolean recordTransaction(EquipmentTransaction transaction) throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement(
                "INSERT INTO equipment_transactions(equipment_id, user_id, transaction_date, equipment_status, notes) VALUES(?,?,?,?,?)")) {
            // Populate SQL statement parameters from the EquipmentTransaction object       
            ps.setInt(1, transaction.getEquipmentId());
            ps.setInt(2, transaction.getUserId());
            ps.setString(3, transaction.getTransactionDate());
            ps.setString(4, transaction.getEquipmentStatus());
            ps.setString(5, transaction.getNotes());
            return ps.executeUpdate() == 1;
        }
    }

    // Retrieves all transactions for a specific user, ordered by most recent first

    public List<EquipmentTransaction> getTransactionsForUser(int userId) throws Exception {
        List<EquipmentTransaction> out = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement(
                "SELECT id, equipment_id, user_id, transaction_date, equipment_status, notes FROM equipment_transactions WHERE user_id=? ORDER BY transaction_date DESC")) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(new EquipmentTransaction(
                        rs.getInt("id"),
                        rs.getInt("equipment_id"),
                        rs.getInt("user_id"),
                        rs.getString("transaction_date"),
                        rs.getString("equipment_status"),
                        rs.getString("notes")
                ));
            }
        }
        return out;
    }

    //

    public List<EquipmentTransaction> getAllTransactions() throws Exception {
        List<EquipmentTransaction> out = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement(
                "SELECT id, equipment_id, user_id, transaction_date, equipment_status, notes FROM equipment_transactions ORDER BY transaction_date DESC")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(new EquipmentTransaction(
                        rs.getInt("id"),
                        rs.getInt("equipment_id"),
                        rs.getInt("user_id"),
                        rs.getString("transaction_date"),
                        rs.getString("equipment_status"),
                        rs.getString("notes")
                ));
            }
        }
        return out;
    }
}
