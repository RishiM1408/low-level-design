package com.lld.splitwise.model;

public class ExpenseMetadata {
    private String name;
    private String imgUrl;
    private String notes;

    public ExpenseMetadata(String name, String imgUrl, String notes) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }
    // getters...
}
