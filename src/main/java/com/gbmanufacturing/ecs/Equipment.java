package com.gbmanufacturing.ecs;

public class Equipment {
    private int id;
    private String name;
    private String status;
    private String checkedOutBy;
    private String checkedOutAt;

    public Equipment(int id, String name, String status, String checkedOutBy, String checkedOutAt) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.checkedOutBy = checkedOutBy;
        this.checkedOutAt = checkedOutAt;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getStatus() { return status; }
    public String getCheckedOutBy() { return checkedOutBy; }
    public String getCheckedOutAt() { return checkedOutAt; }
}

