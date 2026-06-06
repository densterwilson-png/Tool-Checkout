package com.gbmanufacturing.ecs;

import com.gbmanufacturing.ecs.database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public User authenticate(String username, String password) throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id,username,role FROM users WHERE username=? AND password=?")){
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new User(rs.getInt("id"), rs.getString("username"), rs.getString("role"));
            return null;
        }
    }

    public List<User> getAll() throws Exception {
        List<User> out = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id,username,role FROM users ORDER BY id")){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) out.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("role")));
        }
        return out;
    }

    public boolean addUser(String username, String password, String role) throws Exception {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username cannot be empty");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password cannot be empty");
        if (role == null || role.isBlank()) throw new IllegalArgumentException("Role cannot be empty");
        if (!role.equals("employee") && !role.equals("supervisor")) throw new IllegalArgumentException("Invalid role");
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO users(username,password,role) VALUES(?,?,?)")){
            ps.setString(1, username.trim());
            ps.setString(2, password);
            ps.setString(3, role.trim());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteUser(int id) throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE id=?")){
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }
}

