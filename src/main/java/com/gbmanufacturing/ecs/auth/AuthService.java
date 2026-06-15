package com.gbmanufacturing.ecs.auth;

import com.gbmanufacturing.ecs.dao.UserDAO;
import com.gbmanufacturing.ecs.model.User;
// AuthService handles user authentication and session management for the Equipment Checkout System.
public class AuthService {

    private final UserDAO userDAO = new UserDAO();
    private User currentUser;

    public boolean authenticate(String username, String password) throws Exception {

        User user = userDAO.getByUsername(username);

        if (user == null) return false;

        if (!user.getPassword().equals(password)) return false;

        currentUser = user;
        return true;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}