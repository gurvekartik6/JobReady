package com.jobread.app.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.DashboardStats;
import com.jobread.app.models.JobStatus;
import com.jobread.app.repositories.JobRepository;
import com.jobread.app.utils.DateUtils;
import com.jobread.app.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DashboardViewModel extends ViewModel {

    private final JobRepository mJobRepository;
    private final PreferenceManager mPreferenceManager;
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    private final MutableLiveData<DashboardStats> mStatsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Float>> mWeeklyCountsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mLoadingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<String> mErrorLiveData = new MutableLiveData<>();

    public DashboardViewModel(JobRepository jobRepository, PreferenceManager preferenceManager) {
        this.mJobRepository = jobRepository;
        this.mPreferenceManager = preferenceManager;
    }

    public LiveData<DashboardStats> getStats() { return mStatsLiveData; }
    public LiveData<List<Float>> getWeeklyCounts() { return mWeeklyCountsLiveData; }
    public LiveData<Boolean> getLoading() { return mLoadingLiveData; }
    public LiveData<String> getError() { return mErrorLiveData; }

    public void loadDashboard() {
        String userId = mPreferenceManager.getUserId();
        if (userId == null) return;

        mLoadingLiveData.setValue(true);

        mDisposables.add(
            Single.zip(
                mJobRepository.getTotalCount(userId),
                mJobRepository.getCountByStatus(userId, JobStatus.INTERVIEW_SCHEDULED),
                mJobRepository.getCountByStatus(userId, JobStatus.OFFER_RECEIVED),
                mJobRepository.getCountByStatus(userId, JobStatus.REJECTED),
                mJobRepository.getCountByStatus(userId, JobStatus.ACCEPTED),
                (total, interviews, offers, rejections, accepted) ->
                        new DashboardStats(total, interviews, offers, rejections, accepted)
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(stats -> {
                mStatsLiveData.setValue(stats);
                mLoadingLiveData.setValue(false);
            }, error -> {
                mErrorLiveData.setValue(error.getMessage());
                mLoadingLiveData.setValue(false);
            })
        );

        loadWeeklyStats(userId);
    }

    private void loadWeeklyStats(String userId) {
        mDisposables.add(
            mJobRepository.getAllJobsOnce(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jobs -> {
                    List<Float> weeklyCounts = calculateWeeklyCounts(jobs);
                    mWeeklyCountsLiveData.setValue(weeklyCounts);
                }, error -> mErrorLiveData.setValue(error.getMessage()))
        );
    }

    private List<Float> calculateWeeklyCounts(List<JobEntity> jobs) {
        List<Float> counts = new ArrayList<>();
        for (int weeksAgo = 3; weeksAgo >= 0; weeksAgo--) {
            final int week = weeksAgo;
            long count = jobs.stream()
                    .filter(job -> job.getDateApplied() != null &&
                            job.getDateApplied().after(DateUtils.startOfWeekOffset(week)) &&
                            job.getDateApplied().before(DateUtils.endOfWeekOffset(week)))
                    .count();
            counts.add((float) count);
        }
        return counts;
    }

    public void refresh() {
        loadDashboard();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposables.clear();
    }
}
