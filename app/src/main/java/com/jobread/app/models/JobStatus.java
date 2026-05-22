package com.jobread.app.models;

public enum JobStatus {
    APPLIED("Applied"),
    INTERVIEW_SCHEDULED("Interview Scheduled"),
    OFFER_RECEIVED("Offer Received"),
    REJECTED("Rejected"),
    ACCEPTED("Accepted");

    private final String mDisplayName;

    JobStatus(String displayName) {
        this.mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public static JobStatus fromDisplayName(String displayName) {
        for (JobStatus status : values()) {
            if (status.mDisplayName.equals(displayName)) {
                return status;
            }
        }
        return APPLIED;
    }
}
