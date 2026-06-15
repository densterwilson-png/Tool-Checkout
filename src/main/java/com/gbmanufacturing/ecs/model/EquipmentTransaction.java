package com.gbmanufacturing.ecs.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gbmanufacturing.ecs.dao.TransactionDAO;
import com.gbmanufacturing.ecs.database.DatabaseManager;

// Represents a single transaction involving equipment, such as check-out or return.

public class EquipmentTransaction {
    private final int id;
    private final int equipmentId;
    private final int userId;
    private final String transactionDate;
    private final String equipmentStatus;
    private final String notes;
//
    public EquipmentTransaction(int id, int equipmentId, int userId, String transactionDate, String equipmentStatus, String notes) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.userId = userId;
        this.transactionDate = transactionDate;
        this.equipmentStatus = equipmentStatus;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public int getUserId() {
        return userId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getEquipmentStatus() {
        return equipmentStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void recordTransaction() throws Exception {
        new TransactionDAO().recordTransaction(this);
    }

    @Override
    public String toString() {
        return transactionDate + " - Equipment #" + equipmentId + " changed to " + equipmentStatus + (notes == null ? "" : " (" + notes + ")");
    }
    public void checkoutEquipment(int equipmentId, int employeeId) throws SQLException {
        String sql =
        "INSERT INTO equipment_transaction " +
        "(equipment_id, employee_id, transaction_type, transaction_date) " +
        "VALUES (?, ?, 'CHECKOUT', datetime('now'))";
        
        try (Connection conn = DatabaseManager.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, equipmentId);
            stmt.setInt(2, employeeId);
            stmt.executeUpdate();
        }
    }

    public void returnEquipment(int equipmentId, int employeeId) throws SQLException {
        String sql =
        "INSERT INTO equipment_transaction " +
        "(equipment_id, employee_id, transaction_type, transaction_date) " +
        "VALUES (?, ?, 'RETURN', datetime('now'))";
        
        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, equipmentId);
            stmt.setInt(2, employeeId);
            stmt.executeUpdate();
        }
    }

    public List<ChartData> getCheckoutChartData() throws SQLException {
        String sql =
        "SELECT equipment_id, COUNT(*) AS total " +
        "FROM equipment_transaction " +
        "WHERE transaction_type = 'CHECKOUT' " +
        "GROUP BY equipment_id";
        
        List<ChartData> list = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(new ChartData(
                    rs.getString("equipment_id"),
                    rs.getInt("total")
                ));
            }
        }
         return list;
        }
}
