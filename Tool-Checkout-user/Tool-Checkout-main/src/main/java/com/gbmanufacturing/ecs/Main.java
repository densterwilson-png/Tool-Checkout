
package com.gbmanufacturing.ecs;

import com.gbmanufacturing.ecs.database.DatabaseInitializer;

/** ********************************************************
 * Program Name: Main.java
 * Programmer's Name: Robert Sadler
 * Group: Group 4
 * Program Description: Starts the Equipment Checkout System application.
 ********************************************************** */

public class Main {

    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();
        System.out.println("ECS application started successfully.");
    }
}