package com.jobread.app.models;

public class DashboardStats {

    private int mTotal;
    private int mInterviews;
    private int mOffers;
    private int mRejections;
    private int mAccepted;

    public DashboardStats(int total, int interviews, int offers, int rejections, int accepted) {
        this.mTotal = total;
        this.mInterviews = interviews;
        this.mOffers = offers;
        this.mRejections = rejections;
        this.mAccepted = accepted;
    }

    public int getTotal() { return mTotal; }
    public int getInterviews() { return mInterviews; }
    public int getOffers() { return mOffers; }
    public int getRejections() { return mRejections; }
    public int getAccepted() { return mAccepted; }
}
