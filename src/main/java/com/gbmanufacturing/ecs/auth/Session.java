package com.gbmanufacturing.ecs.auth;

import com.gbmanufacturing.ecs.model.User;

public class Session {

    public static User currentUser;

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void logout() {
        currentUser = null;
    }

    public static boolean isSupervisor() {
        return currentUser != null &&
               currentUser.getRole() == User.Role.SUPERVISOR;
    }

    public static boolean isEmployee() {
        return currentUser != null &&
               currentUser.getRole() == User.Role.EMPLOYEE;
    }
}

