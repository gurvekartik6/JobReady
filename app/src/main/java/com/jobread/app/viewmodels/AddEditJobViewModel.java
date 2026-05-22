package com.jobread.app.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.JobStatus;
import com.jobread.app.repositories.JobRepository;
import com.jobread.app.utils.DateUtils;
import com.jobread.app.utils.PreferenceManager;

import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddEditJobViewModel extends ViewModel {

    private final JobRepository mJobRepository;
    private final PreferenceManager mPreferenceManager;
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    private final MutableLiveData<JobEntity> mJobLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mSaveSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> mErrorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mLoadingLiveData = new MutableLiveData<>(false);

    public AddEditJobViewModel(JobRepository jobRepository, PreferenceManager preferenceManager) {
        this.mJobRepository = jobRepository;
        this.mPreferenceManager = preferenceManager;
    }

    public LiveData<JobEntity> getJob() { return mJobLiveData; }
    public LiveData<Boolean> getSaveSuccess() { return mSaveSuccess; }
    public LiveData<String> getError() { return mErrorLiveData; }
    public LiveData<Boolean> getLoading() { return mLoadingLiveData; }

    public void loadJob(String jobId) {
        if (jobId == null) return;
        mLoadingLiveData.setValue(true);
        mDisposables.add(
            mJobRepository.getJobById(jobId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(job -> {
                    mJobLiveData.setValue(job);
                    mLoadingLiveData.setValue(false);
                }, error -> {
                    mErrorLiveData.setValue(error.getMessage());
                    mLoadingLiveData.setValue(false);
                })
        );
    }

    public void saveJob(String jobId,
                        String company, String role, Date dateApplied,
                        JobStatus status, String notes,
                        String contactPerson, String contactPhone, String contactEmail,
                        String salaryRange) {

        String validationError = validate(company, role, dateApplied);
        if (validationError != null) {
            mErrorLiveData.setValue(validationError);
            return;
        }

        String userId = mPreferenceManager.getUserId();
        if (userId == null) {
            mErrorLiveData.setValue("Not logged in");
            return;
        }

        mLoadingLiveData.setValue(true);

        if (jobId == null) {
            // Create new
            JobEntity newJob = JobEntity.create(company, role, dateApplied, status, userId);
            newJob.setNotes(notes);
            newJob.setContactPerson(contactPerson);
            newJob.setContactPhone(contactPhone);
            newJob.setContactEmail(contactEmail);
            newJob.setSalaryRange(salaryRange);

            mDisposables.add(
                mJobRepository.insertJob(newJob)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        mLoadingLiveData.setValue(false);
                        mSaveSuccess.setValue(true);
                    }, error -> {
                        mLoadingLiveData.setValue(false);
                        mErrorLiveData.setValue(error.getMessage());
                    })
            );
        } else {
            // Update existing
            JobEntity existing = mJobLiveData.getValue();
            if (existing == null) {
                mErrorLiveData.setValue("Job not found");
                return;
            }
            existing.setCompany(company);
            existing.setRole(role);
            existing.setDateApplied(dateApplied);
            existing.setStatus(status);
            existing.setNotes(notes);
            existing.setContactPerson(contactPerson);
            existing.setContactPhone(contactPhone);
            existing.setContactEmail(contactEmail);
            existing.setSalaryRange(salaryRange);
            existing.setUpdatedAt(new Date());

            mDisposables.add(
                mJobRepository.updateJob(existing)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        mLoadingLiveData.setValue(false);
                        mSaveSuccess.setValue(true);
                    }, error -> {
                        mLoadingLiveData.setValue(false);
                        mErrorLiveData.setValue(error.getMessage());
                    })
            );
        }
    }

    private String validate(String company, String role, Date dateApplied) {
        if (company == null || company.trim().isEmpty()) {
            return "Company name is required";
        }
        if (role == null || role.trim().isEmpty()) {
            return "Role is required";
        }
        if (dateApplied != null && DateUtils.isInFuture(dateApplied)) {
            return "Date applied cannot be in the future";
        }
        return null;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposables.clear();
    }
}
