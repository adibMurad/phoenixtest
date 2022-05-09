package com.example.phoenixtest.model;

public enum SortOrder {
    ASCENDING("asc"),
    DESCENDING("desc");

    private final String label;

    SortOrder(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
