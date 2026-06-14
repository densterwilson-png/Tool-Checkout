package com.gbmanufacturing.ecs.dao;

import com.gbmanufacturing.ecs.model.Equipment;
import com.gbmanufacturing.ecs.database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class EquipmentDAO {

    public List<Equipment> getAll() throws Exception {
        List<Equipment> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id, equipment_name, availability_status, checkedOutBy, checkedOutAt FROM equipment ORDER BY id")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Equipment(rs.getInt("id"), rs.getString("equipment_name"), rs.getString("availability_status"), rs.getString("checkedOutBy"), rs.getString("checkedOutAt")));
            }
        }
        return list;
    }

    public List<Equipment> searchEquipment(String keyword) throws Exception {
        List<Equipment> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id, equipment_name, availability_status, checkedOutBy, checkedOutAt FROM equipment WHERE equipment_name LIKE ? OR equipment_type LIKE ? ORDER BY id")) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Equipment(rs.getInt("id"), rs.getString("equipment_name"), rs.getString("availability_status"), rs.getString("checkedOutBy"), rs.getString("checkedOutAt")));
            }
        }
        return list;
    }

    public List<Equipment> searchEquipmentByStatus(String status) throws Exception {
        List<Equipment> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id, equipment_name, availability_status, checkedOutBy, checkedOutAt FROM equipment WHERE availability_status=? ORDER BY id")) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Equipment(rs.getInt("id"), rs.getString("equipment_name"), rs.getString("availability_status"), rs.getString("checkedOutBy"), rs.getString("checkedOutAt")));
            }
        }
        return list;
    }

    public int getTotalEquipmentCount() throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM equipment")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int getAvailableEquipmentCount() throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM equipment WHERE availability_status='available'")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int getCheckedOutEquipmentCount() throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM equipment WHERE availability_status='checked_out'")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public boolean checkout(int id, String user) {
        try {
            System.out.println("Checkout attempt:");
            System.out.println("ID = " + id);
            System.out.println("User = " + user);
            
            Connection c = DatabaseManager.getConnection();
            
            PreparedStatement ps = c.prepareStatement(
                "UPDATE equipment " +
                "SET availability_status='checked_out', " +
                "checkedOutBy=?, " +
                "checkedOutAt=? " +
                "WHERE id=? AND availability_status='available'"
            );
            
            ps.setString(1, user);
            ps.setString(2, Instant.now().toString());
            ps.setInt(3, id);
            
            int rows = ps.executeUpdate();
            
            System.out.println("Rows updated = " + rows);
            
            return rows == 1;
        
        } catch (Exception e) {
            
            e.printStackTrace();
            return false;
        }
    }


    public boolean checkin(int id) throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE equipment SET availability_status='available', checkedOutBy=NULL, checkedOutAt=NULL WHERE id=? AND availability_status='checked_out'")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean addEquipment(String name) throws Exception {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Equipment name cannot be empty");
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO equipment(equipment_name, equipment_type, condition_status, availability_status) VALUES(?, 'General', 'Good', 'available')")) {
            ps.setString(1, name.trim());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteEquipment(int id) throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM equipment WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean updateEquipmentStatus(int id, String status) throws Exception {
        if (status == null || status.isBlank()) throw new IllegalArgumentException("Status cannot be empty");
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE equipment SET availability_status=? WHERE id=?")) {
            ps.setString(1, status.trim());
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        }
    }

    public List<String> getUsageReport() throws Exception {
        List<String> out = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT e.equipment_name, COUNT(t.id) as totalCheckouts FROM equipment_transactions t JOIN equipment e ON e.id = t.equipment_id GROUP BY e.equipment_name")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(rs.getString("equipment_name") + ": " + rs.getInt("totalCheckouts") + " checkouts");
            }
        }
        return out;
    }

    
}

