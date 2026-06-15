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

public boolean checkout(int id, String user, int userId) throws Exception {
    if (user == null || user.isBlank()) {
        throw new IllegalArgumentException("User name cannot be empty");
    }

    try (Connection c = DatabaseManager.getConnection()) {

        String certSql = "SELECT required_certification FROM equipment WHERE id = ?";
        try (PreparedStatement certPs = c.prepareStatement(certSql)) {
            certPs.setInt(1, id);

            ResultSet rs = certPs.executeQuery();

            if (!rs.next()) {
                return false;
            }

            String requiredCert = rs.getString("required_certification");

            if (requiredCert != null && !requiredCert.isBlank()) {
                String userCertSql =
                    "SELECT 1 FROM user_certifications " +
                    "WHERE user_id = ? AND certification_type = ?";

                try (PreparedStatement userCertPs = c.prepareStatement(userCertSql)) {
                    userCertPs.setInt(1, userId);
                    userCertPs.setString(2, requiredCert);

                    ResultSet certRs = userCertPs.executeQuery();

                    if (!certRs.next()) {
                        return false;
                    }
                }
            }
        }

        String updateSql =
            "UPDATE equipment SET availability_status='checked_out', checkedOutBy=?, checkedOutAt=? " +
            "WHERE id=? AND availability_status='available'";

        try (PreparedStatement ps = c.prepareStatement(updateSql)) {
    ps.setString(1, user.trim());
    ps.setString(2, Instant.now().toString());
    ps.setInt(3, id);

    boolean updated = ps.executeUpdate() == 1;

    if (updated) {
        try (PreparedStatement tx = c.prepareStatement(
                "INSERT INTO equipment_transaction (equipment_id, employee_id, transaction_type, transaction_date) " +
                "VALUES (?, ?, 'CHECKOUT', ?)"
        )) {
            tx.setInt(1, id);
            tx.setInt(2, userId);
            tx.setString(3, Instant.now().toString());
            tx.executeUpdate();
        }
    }

    return updated;
    }
    }
    }

 


public boolean checkin(int id, String username, int userId) throws Exception {
    try (Connection c = DatabaseManager.getConnection();
         PreparedStatement ps = c.prepareStatement(
             "UPDATE equipment " +
             "SET availability_status='available', checkedOutBy=NULL, checkedOutAt=NULL " +
             "WHERE id=? AND availability_status='checked_out'"
         )) {

        ps.setInt(1, id);
        boolean updated = ps.executeUpdate() == 1;

        if (updated) {
            try (PreparedStatement tx = c.prepareStatement(
                    "INSERT INTO equipment_transaction (equipment_id, employee_id, transaction_type, transaction_date) " +
                    "VALUES (?, ?, 'RETURN', ?)"
            )) {
                tx.setInt(1, id);
                tx.setInt(2, userId);
                tx.setString(3, Instant.now().toString());
                tx.executeUpdate();
            }
        }

        return updated;
    }


}
    public boolean addEquipment(String name) throws Exception {
    return addEquipment(name, null);
}
    

    public boolean addEquipment(String name, String certification) throws Exception {
    if (name == null || name.isBlank()) {
        throw new IllegalArgumentException("Equipment name cannot be empty");
    }

    String certValue = null;
    if (certification != null && !certification.isBlank()) {
        certValue = certification.trim().toUpperCase();
    }

    try (Connection c = DatabaseManager.getConnection();
         PreparedStatement ps = c.prepareStatement(
             "INSERT INTO equipment(equipment_name, equipment_type, condition_status, availability_status, required_certification) " +
             "VALUES(?, 'General', 'Good', 'available', ?)"
         )) {

        ps.setString(1, name.trim());

        if (certValue == null) {
            ps.setNull(2, java.sql.Types.VARCHAR);
        } else {
            ps.setString(2, certValue);
        }

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

    String sql =
        "SELECT u.username, u.first_name, u.last_name, e.equipment_name, " +
        "t.transaction_type, t.transaction_date " +
        "FROM equipment_transaction t " +
        "JOIN equipment e ON e.id = t.equipment_id " +
        "JOIN users u ON u.user_id = t.employee_id " +
        "ORDER BY t.transaction_date DESC";

    try (Connection c = DatabaseManager.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            out.add(
                rs.getString("first_name") + " " +
                rs.getString("last_name") +
                " (" + rs.getString("username") + ") - " +
                rs.getString("transaction_type") + " - " +
                rs.getString("equipment_name") + " - " +
                rs.getString("transaction_date")
            );
        }
    }

    if (out.isEmpty()) {
        out.add("No checkout history found.");
    }

    return out;
}

    
}

