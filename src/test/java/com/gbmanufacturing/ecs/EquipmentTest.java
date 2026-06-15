package com.gbmanufacturing.ecs;

import org.junit.Test;

import com.gbmanufacturing.ecs.model.Equipment;

import static org.junit.Assert.*;

public class EquipmentTest {
    @Test
    public void equipmentPropertiesAreAccessible() {
        Equipment equipment = new Equipment(7, "Cordless Drill", "available", "", "");

        assertEquals(7, equipment.getId());
        assertEquals("Cordless Drill", equipment.getName());
        assertEquals("available", equipment.getStatus());
        assertEquals("", equipment.getCheckedOutBy());
        assertEquals("", equipment.getCheckedOutAt());
    }
}
