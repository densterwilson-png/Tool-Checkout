package com.gbmanufacturing.ecs.model;

import java.util.ArrayList;
import java.util.List;

public class Supervisor extends User {

    public Supervisor() {
        super();
        setRole(Role.SUPERVISOR);
    }

    public Supervisor(int userId, String username, String password, String firstName, String lastName) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = Role.SUPERVISOR;
    }

    public List<String> generateReports(String type) {
    try {
        com.gbmanufacturing.ecs.dao.EquipmentDAO dao =
                new com.gbmanufacturing.ecs.dao.EquipmentDAO();

        List<String> report = dao.getUsageReport();

        if (report.isEmpty()) {
            report.add("No usage data available.");
        }

        return report;

    } catch (Exception e) {
        List<String> errorReport = new ArrayList<>();
        errorReport.add("Error generating report: " + e.getMessage());
        return errorReport;
        }
    }
}