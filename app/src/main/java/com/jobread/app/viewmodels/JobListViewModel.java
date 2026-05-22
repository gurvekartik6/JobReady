package com.jobread.app.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.JobStatus;
import com.jobread.app.models.SortOption;
import com.jobread.app.repositories.JobRepository;
import com.jobread.app.utils.PreferenceManager;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class JobListViewModel extends ViewModel {

    private final JobRepository mJobRepository;
    private final PreferenceManager mPreferenceManager;
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    private final MutableLiveData<List<JobEntity>> mJobsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mLoadingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<String> mErrorLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> mOperationSuccess = new MutableLiveData<>();

    private String mCurrentSearchQuery = "";
    private JobStatus mCurrentFilter = null;
    private SortOption mCurrentSort = SortOption.DATE_NEWEST;

    public JobListViewModel(JobRepository jobRepository, PreferenceManager preferenceManager) {
        this.mJobRepository = jobRepository;
        this.mPreferenceManager = preferenceManager;
    }

    public LiveData<List<JobEntity>> getJobs() { return mJobsLiveData; }
    public LiveData<Boolean> getLoading() { return mLoadingLiveData; }
    public LiveData<String> getError() { return mErrorLiveData; }
    public LiveData<String> getOperationSuccess() { return mOperationSuccess; }

    public void loadJobs() {
        String userId = mPreferenceManager.getUserId();
        if (userId == null) return;

        mDisposables.clear();

        if (mCurrentSearchQuery != null && !mCurrentSearchQuery.isEmpty()) {
            searchJobs(userId, mCurrentSearchQuery);
        } else if (mCurrentFilter != null) {
            filterByStatus(userId, mCurrentFilter);
        } else {
            loadSorted(userId);
        }
    }

    private void loadSorted(String userId) {
        Flowable<List<JobEntity>> flowable;
        switch (mCurrentSort) {
            case COMPANY_AZ:
                flowable = mJobRepository.getJobsSortedByCompany(userId);
                break;
            case STATUS:
                flowable = mJobRepository.getJobsSortedByStatus(userId);
                break;
            default:
                flowable = mJobRepository.getActiveJobs(userId);
                break;
        }

        mDisposables.add(
            flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jobs -> {
                    mJobsLiveData.setValue(jobs);
                    mLoadingLiveData.setValue(false);
                }, error -> mErrorLiveData.setValue(error.getMessage()))
        );
    }

    private void searchJobs(String userId, String query) {
        mDisposables.add(
            mJobRepository.searchJobs(userId, query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    jobs -> mJobsLiveData.setValue(jobs),
                    error -> mErrorLiveData.setValue(error.getMessage())
                )
        );
    }

    private void filterByStatus(String userId, JobStatus status) {
        mDisposables.add(
            mJobRepository.getJobsByStatus(userId, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    jobs -> mJobsLiveData.setValue(jobs),
                    error -> mErrorLiveData.setValue(error.getMessage())
                )
        );
    }

    public void setSearchQuery(String query) {
        mCurrentSearchQuery = query;
        loadJobs();
    }

    public void setFilter(JobStatus status) {
        mCurrentFilter = status;
        mCurrentSearchQuery = "";
        loadJobs();
    }

    public void setSort(SortOption sortOption) {
        mCurrentSort = sortOption;
        loadJobs();
    }

    public void deleteJob(JobEntity job) {
        mDisposables.add(
            mJobRepository.deleteJob(job)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> mOperationSuccess.setValue("Job deleted"),
                    error -> mErrorLiveData.setValue(error.getMessage())
                )
        );
    }

    public void archiveJob(String jobId, boolean archive) {
        mDisposables.add(
            mJobRepository.setArchived(jobId, archive)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> mOperationSuccess.setValue(archive ? "Job archived" : "Job restored"),
                    error -> mErrorLiveData.setValue(error.getMessage())
                )
        );
    }

    public void duplicateJob(JobEntity original) {
        JobEntity copy = original.duplicate();
        mDisposables.add(
            mJobRepository.insertJob(copy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> mOperationSuccess.setValue("Job duplicated"),
                    error -> mErrorLiveData.setValue(error.getMessage())
                )
        );
    }

    public void restoreJob(JobEntity job) {
        mDisposables.add(
            mJobRepository.insertJob(job)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {},
                    error -> mErrorLiveData.setValue(error.getMessage())
                )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposables.clear();
    }
}
