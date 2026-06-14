package com.gbmanufacturing.ecs.service;

import java.util.List;

import com.gbmanufacturing.ecs.dao.EquipmentDAO;
import com.gbmanufacturing.ecs.model.Equipment;

public class Inventory {
    private final EquipmentDAO equipmentDAO;

    public Inventory() {
        this.equipmentDAO = new EquipmentDAO();
    }

    public int getTotalItems() throws Exception {
        return equipmentDAO.getTotalEquipmentCount();
    }

    public int getAvailableItems() throws Exception {
        return equipmentDAO.getAvailableEquipmentCount();
    }

    public boolean addItem(Equipment equipment) throws Exception {
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment cannot be null");
        }
        return equipmentDAO.addEquipment(equipment.getName());
    }

    public boolean removeItem(int equipmentId) throws Exception {
        return equipmentDAO.deleteEquipment(equipmentId);
    }

    public List<Equipment> searchItem(String keyword) throws Exception {
        return equipmentDAO.searchEquipment(keyword);
    }

    public List<Equipment> listEquipment() throws Exception {
        return equipmentDAO.getAll();
    }
}
