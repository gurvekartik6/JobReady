package com.jobread.app.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.jobread.app.models.JobStatus;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "jobs")
public class JobEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String mId;

    @ColumnInfo(name = "company")
    private String mCompany;

    @ColumnInfo(name = "role")
    private String mRole;

    @ColumnInfo(name = "date_applied")
    private Date mDateApplied;

    @ColumnInfo(name = "status")
    private JobStatus mStatus;

    @ColumnInfo(name = "notes")
    private String mNotes;

    @ColumnInfo(name = "contact_person")
    private String mContactPerson;

    @ColumnInfo(name = "contact_phone")
    private String mContactPhone;

    @ColumnInfo(name = "contact_email")
    private String mContactEmail;

    @ColumnInfo(name = "salary_range")
    private String mSalaryRange;

    @ColumnInfo(name = "is_archived")
    private boolean mArchived;

    @ColumnInfo(name = "user_id")
    private String mUserId;

    @ColumnInfo(name = "created_at")
    private Date mCreatedAt;

    @ColumnInfo(name = "updated_at")
    private Date mUpdatedAt;

    // ---- Static Factory ----
    public static JobEntity create(String company, String role, Date dateApplied,
                                    JobStatus status, String userId) {
        JobEntity entity = new JobEntity();
        entity.mId = UUID.randomUUID().toString();
        entity.mCompany = company;
        entity.mRole = role;
        entity.mDateApplied = dateApplied;
        entity.mStatus = status;
        entity.mArchived = false;
        entity.mUserId = userId;
        entity.mCreatedAt = new Date();
        entity.mUpdatedAt = new Date();
        return entity;
    }

    public JobEntity duplicate() {
        JobEntity copy = new JobEntity();
        copy.mId = UUID.randomUUID().toString();
        copy.mCompany = this.mCompany + " (Copy)";
        copy.mRole = this.mRole;
        copy.mDateApplied = new Date();
        copy.mStatus = JobStatus.APPLIED;
        copy.mNotes = this.mNotes;
        copy.mContactPerson = this.mContactPerson;
        copy.mContactPhone = this.mContactPhone;
        copy.mContactEmail = this.mContactEmail;
        copy.mSalaryRange = this.mSalaryRange;
        copy.mArchived = false;
        copy.mUserId = this.mUserId;
        copy.mCreatedAt = new Date();
        copy.mUpdatedAt = new Date();
        return copy;
    }

    // ---- Getters & Setters ----
    @NonNull
    public String getId() { return mId; }
    public void setId(@NonNull String id) { this.mId = id; }

    public String getCompany() { return mCompany; }
    public void setCompany(String company) { this.mCompany = company; }

    public String getRole() { return mRole; }
    public void setRole(String role) { this.mRole = role; }

    public Date getDateApplied() { return mDateApplied; }
    public void setDateApplied(Date dateApplied) { this.mDateApplied = dateApplied; }

    public JobStatus getStatus() { return mStatus; }
    public void setStatus(JobStatus status) {
        this.mStatus = status;
        this.mUpdatedAt = new Date();
    }

    public String getNotes() { return mNotes; }
    public void setNotes(String notes) { this.mNotes = notes; }

    public String getContactPerson() { return mContactPerson; }
    public void setContactPerson(String contactPerson) { this.mContactPerson = contactPerson; }

    public String getContactPhone() { return mContactPhone; }
    public void setContactPhone(String contactPhone) { this.mContactPhone = contactPhone; }

    public String getContactEmail() { return mContactEmail; }
    public void setContactEmail(String contactEmail) { this.mContactEmail = contactEmail; }

    public String getSalaryRange() { return mSalaryRange; }
    public void setSalaryRange(String salaryRange) { this.mSalaryRange = salaryRange; }

    public boolean isArchived() { return mArchived; }
    public void setArchived(boolean archived) { this.mArchived = archived; }

    public String getUserId() { return mUserId; }
    public void setUserId(String userId) { this.mUserId = userId; }

    public Date getCreatedAt() { return mCreatedAt; }
    public void setCreatedAt(Date createdAt) { this.mCreatedAt = createdAt; }

    public Date getUpdatedAt() { return mUpdatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.mUpdatedAt = updatedAt; }
}
