package com.jobread.app.models;

public enum SortOption {
    DATE_NEWEST("By Date (Newest First)"),
    COMPANY_AZ("By Company (A-Z)"),
    STATUS("By Status");

    private final String mLabel;

    SortOption(String label) {
        this.mLabel = label;
    }

    public String getLabel() {
        return mLabel;
    }
}
