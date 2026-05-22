package com.jobread.app;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.jobread.app.database.AppDatabase;
import com.jobread.app.database.dao.JobDao;
import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.JobStatus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.subscribers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class JobDaoTest {

    private AppDatabase mDatabase;
    private JobDao mJobDao;

    private static final String TEST_USER_ID = "instrumentation_user_001";

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        mJobDao = mDatabase.jobDao();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    @Test
    public void insertJob_andGetById_returnsCorrectJob() {
        JobEntity job = createJob("Google", "SWE", JobStatus.APPLIED);
        mJobDao.insert(job).blockingAwait();

        JobEntity retrieved = mJobDao.getJobById(job.getId()).blockingGet();

        assertNotNull(retrieved);
        assertEquals(job.getId(), retrieved.getId());
        assertEquals("Google", retrieved.getCompany());
        assertEquals("SWE", retrieved.getRole());
        assertEquals(JobStatus.APPLIED, retrieved.getStatus());
    }

    @Test
    public void insertMultipleJobs_getAllActiveJobs_returnsAll() {
        mJobDao.insert(createJob("Apple", "iOS Dev", JobStatus.APPLIED)).blockingAwait();
        mJobDao.insert(createJob("Meta", "PM", JobStatus.INTERVIEW_SCHEDULED)).blockingAwait();
        mJobDao.insert(createJob("Amazon", "DevOps", JobStatus.OFFER_RECEIVED)).blockingAwait();

        TestSubscriber<List<JobEntity>> subscriber = mJobDao
                .getAllActiveJobs(TEST_USER_ID)
                .take(1)
                .test();

        subscriber.awaitCount(1, () -> {}, 3000);
        subscriber.assertNoErrors();

        List<JobEntity> jobs = subscriber.values().get(0);
        assertEquals(3, jobs.size());
    }

    @Test
    public void updateJob_changesStatusCorrectly() {
        JobEntity job = createJob("Netflix", "SRE", JobStatus.APPLIED);
        mJobDao.insert(job).blockingAwait();

        job.setStatus(JobStatus.INTERVIEW_SCHEDULED);
        mJobDao.update(job).blockingAwait();

        JobEntity updated = mJobDao.getJobById(job.getId()).blockingGet();
        assertEquals(JobStatus.INTERVIEW_SCHEDULED, updated.getStatus());
    }

    @Test
    public void deleteJob_removesFromDatabase() {
        JobEntity job = createJob("Twitter", "Backend", JobStatus.REJECTED);
        mJobDao.insert(job).blockingAwait();

        // Verify it exists
        JobEntity inserted = mJobDao.getJobById(job.getId()).blockingGet();
        assertNotNull(inserted);

        // Delete it
        mJobDao.delete(job).blockingAwait();

        // Verify it throws EmptyResultSetException (Room Single behaviour)
        TestObserver<JobEntity> observer = mJobDao.getJobById(job.getId()).test();
        observer.awaitTerminalEvent(3, TimeUnit.SECONDS);
        observer.assertError(e -> e != null); // EmptyResultSetException expected
    }

    @Test
    public void archiveJob_doesNotAppearInActiveJobs() {
        JobEntity job1 = createJob("Stripe", "SWE", JobStatus.APPLIED);
        JobEntity job2 = createJob("Shopify", "Backend", JobStatus.APPLIED);
        mJobDao.insert(job1).blockingAwait();
        mJobDao.insert(job2).blockingAwait();

        mJobDao.setArchived(job1.getId(), true, new Date()).blockingAwait();

        TestSubscriber<List<JobEntity>> subscriber = mJobDao
                .getAllActiveJobs(TEST_USER_ID)
                .take(1)
                .test();
        subscriber.awaitCount(1, () -> {}, 3000);

        List<JobEntity> activeJobs = subscriber.values().get(0);
        assertEquals(1, activeJobs.size());
        assertEquals("Shopify", activeJobs.get(0).getCompany());
    }

    @Test
    public void archivedJob_appearsInArchivedList() {
        JobEntity job = createJob("Lyft", "Data Eng", JobStatus.APPLIED);
        mJobDao.insert(job).blockingAwait();
        mJobDao.setArchived(job.getId(), true, new Date()).blockingAwait();

        TestSubscriber<List<JobEntity>> subscriber = mJobDao
                .getArchivedJobs(TEST_USER_ID)
                .take(1)
                .test();
        subscriber.awaitCount(1, () -> {}, 3000);

        List<JobEntity> archivedJobs = subscriber.values().get(0);
        assertEquals(1, archivedJobs.size());
        assertTrue(archivedJobs.get(0).isArchived());
    }

    @Test
    public void searchJobs_byCompanyName_returnsMatchingJobs() {
        mJobDao.insert(createJob("Google", "SWE", JobStatus.APPLIED)).blockingAwait();
        mJobDao.insert(createJob("Google Deepmind", "ML", JobStatus.APPLIED)).blockingAwait();
        mJobDao.insert(createJob("Amazon", "SDE", JobStatus.APPLIED)).blockingAwait();

        TestSubscriber<List<JobEntity>> subscriber = mJobDao
                .searchJobs(TEST_USER_ID, "Google")
                .take(1)
                .test();
        subscriber.awaitCount(1, () -> {}, 3000);

        List<JobEntity> results = subscriber.values().get(0);
        assertEquals(2, results.size());
    }

    @Test
    public void getTotalCount_returnsCorrectCount() {
        mJobDao.insert(createJob("Co1", "R1", JobStatus.APPLIED)).blockingAwait();
        mJobDao.insert(createJob("Co2", "R2", JobStatus.APPLIED)).blockingAwait();
        mJobDao.insert(createJob("Co3", "R3", JobStatus.APPLIED)).blockingAwait();

        int count = mJobDao.getTotalCount(TEST_USER_ID).blockingGet();
        assertEquals(3, count);
    }

    @Test
    public void getCountByStatus_returnsCorrectCount() {
        mJobDao.insert(createJob("Co1", "R1", JobStatus.APPLIED)).blockingAwait();
        mJobDao.insert(createJob("Co2", "R2", JobStatus.INTERVIEW_SCHEDULED)).blockingAwait();
        mJobDao.insert(createJob("Co3", "R3", JobStatus.INTERVIEW_SCHEDULED)).blockingAwait();

        int interviewCount = mJobDao.getCountByStatus(TEST_USER_ID, JobStatus.INTERVIEW_SCHEDULED)
                .blockingGet();
        int appliedCount = mJobDao.getCountByStatus(TEST_USER_ID, JobStatus.APPLIED).blockingGet();

        assertEquals(2, interviewCount);
        assertEquals(1, appliedCount);
    }

    @Test
    public void updateStatus_changesJobStatus() {
        JobEntity job = createJob("Uber", "Driver App Eng", JobStatus.APPLIED);
        mJobDao.insert(job).blockingAwait();

        mJobDao.updateStatus(job.getId(), JobStatus.REJECTED, new Date()).blockingAwait();
        JobEntity updated = mJobDao.getJobById(job.getId()).blockingGet();

        assertEquals(JobStatus.REJECTED, updated.getStatus());
    }

    @Test
    public void deleteAllForUser_removesOnlyThatUsersJobs() {
        String otherUserId = "other_user_999";
        JobEntity myJob = JobEntity.create("MyCompany", "MyRole", new Date(),
                JobStatus.APPLIED, TEST_USER_ID);
        JobEntity otherJob = JobEntity.create("OtherCo", "OtherRole", new Date(),
                JobStatus.APPLIED, otherUserId);

        mJobDao.insert(myJob).blockingAwait();
        mJobDao.insert(otherJob).blockingAwait();

        mJobDao.deleteAllForUser(TEST_USER_ID).blockingAwait();

        int myCount = mJobDao.getTotalCount(TEST_USER_ID).blockingGet();
        assertEquals(0, myCount);

        List<JobEntity> otherJobs = mJobDao.getAllJobsOnce(otherUserId).blockingGet();
        assertEquals(1, otherJobs.size());
    }

    @Test
    public void getStalePendingJobs_returnsJobsOlderThan7Days() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_YEAR, -10);
        Date oldDate = cal.getTime();

        JobEntity staleJob = JobEntity.create("OldCo", "Role", oldDate, JobStatus.APPLIED, TEST_USER_ID);
        JobEntity freshJob = JobEntity.create("NewCo", "Role", new Date(), JobStatus.APPLIED, TEST_USER_ID);
        mJobDao.insert(staleJob).blockingAwait();
        mJobDao.insert(freshJob).blockingAwait();

        cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_YEAR, -7);
        Date cutoff = cal.getTime();

        List<JobEntity> staleJobs = mJobDao.getStalePendingJobs(TEST_USER_ID, cutoff).blockingGet();
        assertEquals(1, staleJobs.size());
        assertEquals("OldCo", staleJobs.get(0).getCompany());
    }

    private JobEntity createJob(String company, String role, JobStatus status) {
        return JobEntity.create(company, role, new Date(), status, TEST_USER_ID);
    }
}
