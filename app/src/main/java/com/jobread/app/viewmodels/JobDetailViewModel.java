package com.jobread.app.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.JobStatus;
import com.jobread.app.repositories.JobRepository;
import com.jobread.app.utils.PreferenceManager;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class JobDetailViewModel extends ViewModel {

    private final JobRepository mJobRepository;
    private final PreferenceManager mPreferenceManager;
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    private final MutableLiveData<JobEntity> mJobLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> mErrorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mDeleteSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> mStatusUpdateMessage = new MutableLiveData<>();

    public JobDetailViewModel(JobRepository jobRepository, PreferenceManager preferenceManager) {
        this.mJobRepository = jobRepository;
        this.mPreferenceManager = preferenceManager;
    }

    public LiveData<JobEntity> getJob() { return mJobLiveData; }
    public LiveData<String> getError() { return mErrorLiveData; }
    public LiveData<Boolean> getDeleteSuccess() { return mDeleteSuccess; }
    public LiveData<String> getStatusUpdateMessage() { return mStatusUpdateMessage; }

    public void loadJob(String jobId) {
        mDisposables.add(
            mJobRepository.getJobById(jobId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(job -> mJobLiveData.setValue(job),
                        error -> mErrorLiveData.setValue(error.getMessage()))
        );
    }

    public void updateStatus(String jobId, JobStatus status) {
        mDisposables.add(
            mJobRepository.updateStatus(jobId, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    mStatusUpdateMessage.setValue("Status updated to " + status.getDisplayName());
                    loadJob(jobId);
                }, error -> mErrorLiveData.setValue(error.getMessage()))
        );
    }

    public void deleteJob(JobEntity job) {
        mDisposables.add(
            mJobRepository.deleteJob(job)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mDeleteSuccess.setValue(true),
                        error -> mErrorLiveData.setValue(error.getMessage()))
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposables.clear();
    }
}
