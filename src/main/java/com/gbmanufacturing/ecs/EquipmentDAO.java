package com.gbmanufacturing.ecs;

import com.gbmanufacturing.ecs.database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

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

    public boolean checkout(int id, String user) throws Exception {
        if (user == null || user.isBlank()) throw new IllegalArgumentException("User name cannot be empty");
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE equipment SET availability_status='checked_out', checkedOutBy=?, checkedOutAt=? WHERE id=? AND availability_status='available'")) {
            ps.setString(1, user.trim());
            ps.setString(2, Instant.now().toString());
            ps.setInt(3, id);
            return ps.executeUpdate() == 1;
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
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO equipment(equipment_name, equipment_type, condition_status, availability_status) VALUES(?, 'General', 'Good', 'available')")){
            ps.setString(1, name.trim());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteEquipment(int id) throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM equipment WHERE id=?")){
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public List<String> getUsageReport() throws Exception {
        List<String> out = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT equipment_name, COUNT(*) as totalCheckouts FROM equipment WHERE checkedOutAt IS NOT NULL GROUP BY equipment_name")){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(rs.getString("equipment_name") + ": " + rs.getInt("totalCheckouts") + " checkouts");
            }
        }
        return out;
    }
}

