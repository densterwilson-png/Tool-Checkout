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
        List<String> report = new ArrayList<>();
        report.add("Report Type: " + type);
        report.add("System is operational");
        return report;
    }
}