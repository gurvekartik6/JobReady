package com.jobread.app.repositories;

import com.jobread.app.database.dao.JobDao;
import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.JobStatus;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Singleton
public class JobRepository {

    private final JobDao mJobDao;

    @Inject
    public JobRepository(JobDao jobDao) {
        this.mJobDao = jobDao;
    }

    public Completable insertJob(JobEntity job) {
        return mJobDao.insert(job);
    }

    public Completable updateJob(JobEntity job) {
        job.setUpdatedAt(new Date());
        return mJobDao.update(job);
    }

    public Completable deleteJob(JobEntity job) {
        return mJobDao.delete(job);
    }

    public Completable deleteAllForUser(String userId) {
        return mJobDao.deleteAllForUser(userId);
    }

    public Flowable<List<JobEntity>> getActiveJobs(String userId) {
        return mJobDao.getAllActiveJobs(userId);
    }

    public Flowable<List<JobEntity>> getArchivedJobs(String userId) {
        return mJobDao.getArchivedJobs(userId);
    }

    public Single<JobEntity> getJobById(String jobId) {
        return mJobDao.getJobById(jobId);
    }

    public Flowable<List<JobEntity>> getJobsByStatus(String userId, JobStatus status) {
        return mJobDao.getJobsByStatus(userId, status);
    }

    public Flowable<List<JobEntity>> searchJobs(String userId, String query) {
        return mJobDao.searchJobs(userId, query);
    }

    public Single<Integer> getTotalCount(String userId) {
        return mJobDao.getTotalCount(userId);
    }

    public Single<Integer> getCountByStatus(String userId, JobStatus status) {
        return mJobDao.getCountByStatus(userId, status);
    }

    public Single<List<JobEntity>> getStalePendingJobs(String userId, Date cutoffDate) {
        return mJobDao.getStalePendingJobs(userId, cutoffDate);
    }

    public Flowable<List<JobEntity>> getJobsSortedByCompany(String userId) {
        return mJobDao.getAllJobsSortedByCompany(userId);
    }

    public Flowable<List<JobEntity>> getJobsSortedByStatus(String userId) {
        return mJobDao.getAllJobsSortedByStatus(userId);
    }

    public Single<List<JobEntity>> getAllJobsOnce(String userId) {
        return mJobDao.getAllJobsOnce(userId);
    }

    public Completable setArchived(String jobId, boolean archived) {
        return mJobDao.setArchived(jobId, archived, new Date());
    }

    public Completable updateStatus(String jobId, JobStatus status) {
        return mJobDao.updateStatus(jobId, status, new Date());
    }

    public Completable importJobs(List<JobEntity> jobs) {
        return mJobDao.insertAll(jobs);
    }
}
