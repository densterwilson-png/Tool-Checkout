package com.gbmanufacturing.ecs.auth;

import javax.swing.*;

import com.gbmanufacturing.ecs.MainPanel;
import com.gbmanufacturing.ecs.dao.UserDAO;
import com.gbmanufacturing.ecs.model.User;

import java.awt.*;

public class LoginPanel extends JPanel {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel messageLabel;
    private final UserDAO userDAO;
    private final JFrame parentFrame;

    public LoginPanel(JFrame frame) {
        this.parentFrame = frame;
        this.userDAO = new UserDAO();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Equipment Checkout - Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(titleLabel);
        add(Box.createVerticalStrut(10));

        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        gridPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        gridPanel.add(usernameField);
        gridPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        gridPanel.add(passwordField);
        gridPanel.add(new JLabel(""));
        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);
        gridPanel.add(messageLabel);
        add(gridPanel);

        add(Box.createVerticalStrut(10));
        JPanel buttonPanel = new JPanel();
        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> onLogin());
        buttonPanel.add(loginBtn);
        JButton quitBtn = new JButton("Quit");
        quitBtn.addActionListener(e -> onQuit());
        buttonPanel.add(quitBtn);
        add(buttonPanel);

        setPreferredSize(new Dimension(350, 180));
    }

    private void onLogin() {
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword());
        if (u.isBlank() || p.isBlank()) {
            messageLabel.setText("Username and password required");
            return;
        }
        try {
            User user = userDAO.authenticate(u, p);
            if (user == null) {
                messageLabel.setText("Invalid credentials");
                return;
            }
            Session.currentUser = user;
            parentFrame.setTitle("Equipment Checkout System - " + user.getUsername());
            parentFrame.setContentPane(new MainPanel(Session.currentUser));
            parentFrame.pack();
            parentFrame.setSize(800, 500);
            parentFrame.setLocationRelativeTo(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            messageLabel.setText("Login error: " + ex.getMessage());
        }
    }

    private void onQuit() {
        System.exit(0);
    }
}

