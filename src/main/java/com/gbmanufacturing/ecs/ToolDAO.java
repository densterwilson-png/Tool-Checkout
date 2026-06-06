package com.gbmanufacturing.ecs;

import com.gbmanufacturing.ecs.database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

public class ToolDAO {

    public List<Tool> getAll() throws Exception {
        List<Tool> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id,name,status,checkedOutBy,checkedOutAt FROM tools ORDER BY id")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Tool(rs.getInt("id"), rs.getString("name"), rs.getString("status"), rs.getString("checkedOutBy"), rs.getString("checkedOutAt")));
            }
        }
        return list;
    }

    public boolean checkout(int id, String user) throws Exception {
        if (user == null || user.isBlank()) throw new IllegalArgumentException("User name cannot be empty");
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE tools SET status='checked_out', checkedOutBy=?, checkedOutAt=? WHERE id=? AND status='available'")) {
            ps.setString(1, user.trim());
            ps.setString(2, Instant.now().toString());
            ps.setInt(3, id);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean checkin(int id) throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE tools SET status='available', checkedOutBy=NULL, checkedOutAt=NULL WHERE id=? AND status='checked_out'")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean addTool(String name) throws Exception {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Tool name cannot be empty");
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO tools(name,status) VALUES(?, 'available')")){
            ps.setString(1, name.trim());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteTool(int id) throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM tools WHERE id=?")){
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public List<String> getUsageReport() throws Exception {
        List<String> out = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT name, COUNT(*) as totalCheckouts FROM tools WHERE checkedOutAt IS NOT NULL GROUP BY name")){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(rs.getString("name") + ": " + rs.getInt("totalCheckouts") + " checkouts");
            }
        }
        return out;
    }
}

