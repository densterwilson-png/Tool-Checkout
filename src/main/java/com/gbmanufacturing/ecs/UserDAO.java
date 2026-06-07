package com.gbmanufacturing.ecs;

import com.gbmanufacturing.ecs.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public User authenticate(String username, String password) throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id,username,password,first_name,last_name,role,department FROM users WHERE username=? AND password=?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
            return null;
        }
    }

    public List<User> getAll() throws Exception {
        List<User> out = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id,username,password,first_name,last_name,role,department FROM users ORDER BY id")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(mapUser(rs));
            }
        }
        return out;
    }

    public User getUserById(int id) throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id,username,password,first_name,last_name,role,department FROM users WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
            return null;
        }
    }

    public boolean addUser(String username, String password, String role) throws Exception {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username cannot be empty");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password cannot be empty");
        if (role == null || role.isBlank()) throw new IllegalArgumentException("Role cannot be empty");
        if (!isValidRole(role)) throw new IllegalArgumentException("Invalid role");
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO users(username,password,role,first_name,last_name,department) VALUES(?,?,?,?,?,?)")) {
            ps.setString(1, username.trim());
            ps.setString(2, password);
            ps.setString(3, role.trim());
            ps.setString(4, null);
            ps.setString(5, null);
            ps.setString(6, null);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteUser(int id) throws Exception {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean updateUserRole(int id, String role) throws Exception {
        if (role == null || role.isBlank() || !isValidRole(role)) {
            throw new IllegalArgumentException("Invalid role");
        }
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE users SET role=? WHERE id=?")) {
            ps.setString(1, role.trim());
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        }
    }

    private boolean isValidRole(String role) {
        return "employee".equalsIgnoreCase(role) || "supervisor".equalsIgnoreCase(role);
    }

    private User mapUser(ResultSet rs) throws Exception {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String role = rs.getString("role");
        String department = rs.getString("department");
        if ("supervisor".equalsIgnoreCase(role)) {
            return new Supervisor(id, username, password, firstName, lastName, role, department);
        }
        return new Employee(id, username, password, firstName, lastName, role, department);
    }
}

