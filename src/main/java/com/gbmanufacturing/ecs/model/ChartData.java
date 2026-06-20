package com.gbmanufacturing.ecs.model;

public class ChartData {
    private final String label;
    private final int value;

    public ChartData(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public int getValue() {
        return value;
    }
}