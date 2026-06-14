package com.gbmanufacturing.ecs;

// Represents a single transaction involving equipment, such as check-out or return.

public class EquipmentTransaction {
    private final int id;
    private final int equipmentId;
    private final int userId;
    private final String transactionDate;
    private final String equipmentStatus;
    private final String notes;
//
    public EquipmentTransaction(int id, int equipmentId, int userId, String transactionDate, String equipmentStatus, String notes) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.userId = userId;
        this.transactionDate = transactionDate;
        this.equipmentStatus = equipmentStatus;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public int getUserId() {
        return userId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getEquipmentStatus() {
        return equipmentStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void recordTransaction() throws Exception {
        new TransactionDAO().recordTransaction(this);
    }

    @Override
    public String toString() {
        return transactionDate + " - Equipment #" + equipmentId + " changed to " + equipmentStatus + (notes == null ? "" : " (" + notes + ")");
    }
}
