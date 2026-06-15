package com.gbmanufacturing.ecs.service;

import com.gbmanufacturing.ecs.dao.UserDAO;
import com.gbmanufacturing.ecs.model.User;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAccess {

    private final UserDAO userDAO = new UserDAO();

    // GET ALL USERS
    public List<User> getAllUsers() throws Exception {
        return userDAO.getAll();
    }

    // TOTAL USERS
    public int getTotalUsers() throws Exception {
        return userDAO.getAll().size();
    }

    // ADD USER
    public boolean addUser(User user) throws Exception {
        return userDAO.addUser(user);
    }

    // DELETE USER
    public boolean deleteUser(int userId) throws Exception {
        return userDAO.deleteUser(userId);
    }

    // UPDATE ROLE
    public boolean updateUserRole(int userId, User.Role role) throws Exception {
        return userDAO.updateUserRole(userId, role);
    }

    // EMPLOYEES ONLY
    public List<User> getEmployees() throws Exception {

        List<User> employees = new ArrayList<>();

        for (User user : userDAO.getAll()) {
            if (user.getRole() == User.Role.EMPLOYEE) {
                employees.add(user);
            }
        }

        return employees;
    }
}