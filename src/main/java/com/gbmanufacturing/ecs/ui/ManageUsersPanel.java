package com.gbmanufacturing.ecs.ui;

import javax.swing.*;

import com.gbmanufacturing.ecs.model.User;
import com.gbmanufacturing.ecs.service.EmployeeAccess;

import java.awt.*;
import java.util.List;

public class ManageUsersPanel extends JPanel {
    private final EmployeeAccess employeeAccess;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JComboBox<String> roleChoice;
    private final JList<String> userList;
    private final DefaultListModel<String> listModel;

    public ManageUsersPanel() {
        this.employeeAccess = new EmployeeAccess();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(420, 300));

        // Add panel
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridLayout(2, 2, 8, 8));
        addPanel.setBorder(BorderFactory.createTitledBorder("Add User"));
        addPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        addPanel.add(usernameField);
        addPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        addPanel.add(passwordField);
        add(addPanel, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel rolePanel = new JPanel();
        rolePanel.add(new JLabel("Role:"));
        roleChoice = new JComboBox<>(new String[]{"employee", "supervisor"});
        rolePanel.add(roleChoice);
        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> onAdd());
        rolePanel.add(addBtn);
        centerPanel.add(rolePanel, BorderLayout.NORTH);

        // List
        listModel = new DefaultListModel<>();
        userList = new JList<>(listModel);
        userList.setBorder(BorderFactory.createTitledBorder("Users"));
        centerPanel.add(new JScrollPane(userList), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom
        JPanel bottomPanel = new JPanel();
        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.addActionListener(e -> onDelete());
        bottomPanel.add(deleteBtn);
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> onClose());
        bottomPanel.add(closeBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        refresh();
    }

    private void refresh() {
        try {
            listModel.clear();
            List<User> users = employeeAccess.getAllUsers();
            for (User u : users) {
                listModel.addElement(u.getUserId() + ": " + u.getUsername() + " (" + u.getRole() + ")");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onAdd() {

    String u = usernameField.getText().trim();
    String p = new String(passwordField.getPassword());
    String r = (String) roleChoice.getSelectedItem();

    if (u.isBlank() || p.isBlank() || r == null) {
        JOptionPane.showMessageDialog(this, "Username and password required");
        return;
    }

    try {

        User user = new User();

        user.setUsername(u);
        user.setPassword(p);

        user.setFirstName("New");
        user.setLastName("User");

        if ("supervisor".equalsIgnoreCase(r)) {
            user.setRole(User.Role.SUPERVISOR);
        } else {
            user.setRole(User.Role.EMPLOYEE);
        }

        if (employeeAccess.addUser(user)) {

            refresh();

            usernameField.setText("");
            passwordField.setText("");

        } else {

            JOptionPane.showMessageDialog(this, "Failed to add user");
        }

    } catch (Exception ex) {

        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());

    }
}
    private void onDelete() {
        String sel = userList.getSelectedValue();
        if (sel == null) return;
        try {
            int id = Integer.parseInt(sel.split(":")[0]);
            if (employeeAccess.deleteUser(id)) {
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onClose() {
        SwingUtilities.getWindowAncestor(this).dispose();
    }
}

