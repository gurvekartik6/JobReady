package com.jobread.app.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.JobStatus;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface JobDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(JobEntity job);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<JobEntity> jobs);

    @Update
    Completable update(JobEntity job);

    @Delete
    Completable delete(JobEntity job);

    @Query("DELETE FROM jobs WHERE user_id = :userId")
    Completable deleteAllForUser(String userId);

    @Query("SELECT * FROM jobs WHERE user_id = :userId AND is_archived = 0 ORDER BY date_applied DESC")
    Flowable<List<JobEntity>> getAllActiveJobs(String userId);

    @Query("SELECT * FROM jobs WHERE user_id = :userId AND is_archived = 1 ORDER BY date_applied DESC")
    Flowable<List<JobEntity>> getArchivedJobs(String userId);

    @Query("SELECT * FROM jobs WHERE id = :jobId")
    Single<JobEntity> getJobById(String jobId);

    @Query("SELECT * FROM jobs WHERE user_id = :userId AND status = :status AND is_archived = 0")
    Flowable<List<JobEntity>> getJobsByStatus(String userId, JobStatus status);

    @Query("SELECT * FROM jobs WHERE user_id = :userId AND (company LIKE '%' || :query || '%' OR role LIKE '%' || :query || '%') AND is_archived = 0")
    Flowable<List<JobEntity>> searchJobs(String userId, String query);

    @Query("SELECT COUNT(*) FROM jobs WHERE user_id = :userId AND is_archived = 0")
    Single<Integer> getTotalCount(String userId);

    @Query("SELECT COUNT(*) FROM jobs WHERE user_id = :userId AND status = :status AND is_archived = 0")
    Single<Integer> getCountByStatus(String userId, JobStatus status);

    @Query("SELECT * FROM jobs WHERE user_id = :userId AND status = 'APPLIED' AND date_applied < :cutoffDate AND is_archived = 0")
    Single<List<JobEntity>> getStalePendingJobs(String userId, Date cutoffDate);

    @Query("SELECT * FROM jobs WHERE user_id = :userId AND is_archived = 0 ORDER BY company ASC")
    Flowable<List<JobEntity>> getAllJobsSortedByCompany(String userId);

    @Query("SELECT * FROM jobs WHERE user_id = :userId AND is_archived = 0 ORDER BY status ASC")
    Flowable<List<JobEntity>> getAllJobsSortedByStatus(String userId);

    @Query("SELECT * FROM jobs WHERE user_id = :userId")
    Single<List<JobEntity>> getAllJobsOnce(String userId);

    @Query("UPDATE jobs SET is_archived = :archived, updated_at = :updatedAt WHERE id = :jobId")
    Completable setArchived(String jobId, boolean archived, Date updatedAt);

    @Query("UPDATE jobs SET status = :status, updated_at = :updatedAt WHERE id = :jobId")
    Completable updateStatus(String jobId, JobStatus status, Date updatedAt);
}
