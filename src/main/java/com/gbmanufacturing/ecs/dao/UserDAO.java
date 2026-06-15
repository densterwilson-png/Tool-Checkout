package com.gbmanufacturing.ecs.dao;

import com.gbmanufacturing.ecs.database.DatabaseManager;
import com.gbmanufacturing.ecs.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // ---------------- ADD USER ----------------
    public boolean addUser(User user) throws Exception {

        String sql =
            "INSERT INTO users (username, password, first_name, last_name, role) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getRole().name());

            return ps.executeUpdate() == 1;
        }
    }

    // ---------------- DELETE ----------------
    public boolean deleteUser(int userId) throws Exception {

        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            return ps.executeUpdate() == 1;
        }
    }

    // ---------------- UPDATE ROLE ----------------
    public boolean updateUserRole(int userId, User.Role role) throws Exception {

        String sql = "UPDATE users SET role = ? WHERE user_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role.name());
            ps.setInt(2, userId);

            return ps.executeUpdate() == 1;
        }
    }

    // ---------------- GET ALL ----------------
    public List<User> getAll() throws Exception {

        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapUser(rs));
            }
        }

        return users;
    }

    // ---------------- MAP USER (FIXED) ----------------
    private User mapUser(ResultSet rs) throws Exception {

        User user;

        int userId = rs.getInt("user_id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String role = rs.getString("role");

        if ("SUPERVISOR".equalsIgnoreCase(role)) {
            user = new Supervisor();
        } else {
            user = new Employee();
        }

        user.setUserId(userId);
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(User.Role.valueOf(role.toUpperCase()));

        return user;
    }

    public User getByUsername(String username) throws Exception {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                
                return mapUser(rs);
            }
        }
        
        return null;
    }

    public User authenticate(String username, String password) throws Exception {
        
        String sql =
        
        "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
        
        PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                
                return mapUser(rs);
            }
        }
        
        return null;
    }

}