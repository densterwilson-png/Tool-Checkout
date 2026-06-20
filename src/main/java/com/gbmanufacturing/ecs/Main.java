package com.gbmanufacturing.ecs;

import javax.swing.*;

import com.gbmanufacturing.ecs.auth.LoginPanel;
import com.gbmanufacturing.ecs.database.DatabaseInitializer;
// Main entry point for the Equipment Checkout System application. Initializes the database and launches the login interface.
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseInitializer.init();
                JFrame frame = new JFrame("Equipment Checkout System - Login");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(new LoginPanel(frame));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to start application: " + ex.getMessage(), "Startup Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
