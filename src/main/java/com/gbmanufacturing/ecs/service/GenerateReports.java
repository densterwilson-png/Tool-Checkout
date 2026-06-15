package com.gbmanufacturing.ecs.service;

import java.util.ArrayList;
import java.util.List;

import com.gbmanufacturing.ecs.dao.EquipmentDAO;
import com.gbmanufacturing.ecs.dao.TransactionDAO;
import com.gbmanufacturing.ecs.model.Equipment;
import com.gbmanufacturing.ecs.model.EquipmentTransaction;

public class GenerateReports {
    private final EquipmentDAO equipmentDAO;
    private final TransactionDAO transactionDAO;

    public GenerateReports() {
        this.equipmentDAO = new EquipmentDAO();
        this.transactionDAO = new TransactionDAO();
    }

    public List<String> generateEquipmentAvailability() throws Exception {
        List<String> out = new ArrayList<>();
        out.add("Total equipment: " + equipmentDAO.getTotalEquipmentCount());
        out.add("available equipment: " + equipmentDAO.getAvailableEquipmentCount());
        out.add("Checked out equipment: " + equipmentDAO.getCheckedOutEquipmentCount());
        return out;
    }

    public List<String> generateOverdueItems() throws Exception {
        List<String> out = new ArrayList<>();
        out.add("Currently checked out items:");
        for (Equipment equipment : equipmentDAO.searchEquipmentByStatus("checked_out")) {
            out.add(String.format("%s (ID %d) checked out by %s", equipment.getName(), equipment.getId(), equipment.getCheckedOutBy()));
        }
        if (out.size() == 1) {
            out.add("No overdue or checked-out items found.");
        }
        return out;
    }

    public List<String> generateUsageHistory() throws Exception {
        List<String> out = new ArrayList<>();
        out.add("Usage history:");
        for (EquipmentTransaction transaction : transactionDAO.getAllTransactions()) {
            out.add(transaction.toString());
        }
        if (out.size() == 1) {
            out.add("No transaction history available.");
        }
        return out;
    }
}
