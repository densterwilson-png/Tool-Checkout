package com.gbmanufacturing.ecs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.gbmanufacturing.ecs.auth.Session;
import com.gbmanufacturing.ecs.dao.EquipmentDAO;
import com.gbmanufacturing.ecs.dao.UserDAO;
import com.gbmanufacturing.ecs.model.Employee;
import com.gbmanufacturing.ecs.model.Equipment;
import com.gbmanufacturing.ecs.model.Supervisor;
import com.gbmanufacturing.ecs.model.User;
import com.gbmanufacturing.ecs.ui.ManageUsersPanel;

import java.util.List;
import java.awt.*;
 // MainPanel is the primary interface for logged-in users, displaying equipment and providing actions based on user roles.
public class MainPanel extends JPanel {
    private final EquipmentDAO equipmentDAO;
    private final JTable equipmentTable;
    private final DefaultTableModel tableModel;
    private final JButton checkoutBtn, returnBtn, addBtn, deleteBtn, reportBtn, manageUsersBtn;
    private final JLabel currentUserLabel;

    public MainPanel(User currentUser) {
        this.equipmentDAO = new EquipmentDAO();
        new UserDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table
        String[] cols = {"ID", "Equipment", "Status", "CheckedOutBy"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        equipmentTable = new JTable(tableModel);
        equipmentTable.getSelectionModel().addListSelectionListener(e -> updateButtons());
        JScrollPane scrollPane = new JScrollPane(equipmentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        checkoutBtn = new JButton("Check Out");
        checkoutBtn.addActionListener(e -> onCheckout());
        buttonPanel.add(checkoutBtn);
        returnBtn = new JButton("Return");
        returnBtn.addActionListener(e -> onReturn());
        buttonPanel.add(returnBtn);
        addBtn = new JButton("Add Equipment");
        addBtn.addActionListener(e -> onAddEquipment());
        buttonPanel.add(addBtn);
        deleteBtn = new JButton("Delete Equipment");
        deleteBtn.addActionListener(e -> onDeleteEquipment());
        buttonPanel.add(deleteBtn);
        reportBtn = new JButton("Usage Report");
        reportBtn.addActionListener(e -> onReport());
        buttonPanel.add(reportBtn);
        manageUsersBtn = new JButton("Manage Users");
        manageUsersBtn.addActionListener(e -> onManageUsers());
        buttonPanel.add(manageUsersBtn);
        buttonPanel.add(Box.createHorizontalGlue());
        currentUserLabel = new JLabel();
        buttonPanel.add(currentUserLabel);
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshList());
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshList();
    }
    // Refreshes the equipment list from the database and updates the UI based on the current user's role.
    private void refreshList() {
        try {
            tableModel.setRowCount(0);
            List<Equipment> equipmentList = equipmentDAO.getAll();
            for (Equipment e : equipmentList) {
                tableModel.addRow(new Object[]{
                    e.getId(),
                    e.getName(),
                    e.getStatus().toLowerCase(),
                    e.getCheckedOutBy()
                });
            }
            if (Session.currentUser != null) currentUserLabel.setText(Session.currentUser.getUsername() + " (" + Session.currentUser.getRole() + ")");
            boolean isSupervisor = Session.currentUser != null && Session.currentUser.isSupervisor();
            addBtn.setEnabled(isSupervisor);
            deleteBtn.setEnabled(isSupervisor);
            reportBtn.setEnabled(isSupervisor);
            manageUsersBtn.setEnabled(isSupervisor);
            updateButtons();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateButtons() {
        int row = equipmentTable.getSelectedRow();
        if (row < 0) {
            checkoutBtn.setEnabled(false);
            returnBtn.setEnabled(false);
            return;
        }
        String status = (String) tableModel.getValueAt(row, 2);
        checkoutBtn.setEnabled("available".equalsIgnoreCase(status));
        returnBtn.setEnabled("checkedOutBy".equalsIgnoreCase(status));
    }

    private void onCheckout() {

    int row = equipmentTable.getSelectedRow();
    if (row < 0) return;

    if (!(Session.currentUser instanceof Employee)) {
        JOptionPane.showMessageDialog(this, "Only signed-in employees can check out equipment.");
        return;
    }
    // Cast current user to Employee since we know it's safe after the check above
    
    Employee employee = (Employee) Session.currentUser;

    int id = (Integer) tableModel.getValueAt(row, 0);

    try {
        if (employee.checkoutEquipment(id)) {
            refreshList();
        } else {
            JOptionPane.showMessageDialog(this, "Unable to checkout. It may no longer be available.");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}
    private void onReturn() {

    int row = equipmentTable.getSelectedRow();
    if (row < 0) return;

    if (!(Session.currentUser instanceof Employee)) {
        JOptionPane.showMessageDialog(this, "Only signed-in employees can return equipment.");
        return;
    }

    Employee employee = (Employee) Session.currentUser;

    int id = (Integer) tableModel.getValueAt(row, 0);

    try {
        if (employee.checkinEquipment(id)) {
            refreshList();
        } else {
            JOptionPane.showMessageDialog(this, "Unable to return. It may already be available.");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}

    private void onAddEquipment() {
        String name = JOptionPane.showInputDialog(this, "Equipment name:", "Add Equipment", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.isBlank()) {
            try {
                if (equipmentDAO.addEquipment(name)) refreshList();
                else JOptionPane.showMessageDialog(this, "Failed to add equipment");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void onDeleteEquipment() {
        int row = equipmentTable.getSelectedRow();
        if (row < 0) return;
        int id = (Integer) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        int choice = JOptionPane.showConfirmDialog(this, "Delete equipment '" + name + "'?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                if (equipmentDAO.deleteEquipment(id)) refreshList();
                else JOptionPane.showMessageDialog(this, "Failed to delete");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

 private void onReport() {

    if (!(Session.currentUser instanceof Supervisor)) {
        JOptionPane.showMessageDialog(this, "Only supervisors can generate reports.");
        return;
    }

    Supervisor supervisor = (Supervisor) Session.currentUser;

    try {
        List<String> report = supervisor.generateReports("usage");
        String msg = report.isEmpty()
                ? "No usage data."
                : String.join("\n", report);

        JOptionPane.showMessageDialog(
                this,
                msg,
                "Usage Report",
                JOptionPane.INFORMATION_MESSAGE
        );

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}

    private void onManageUsers() {
        JFrame frame = new JFrame("Manage Users");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(new ManageUsersPanel());
        frame.pack();
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }
}

