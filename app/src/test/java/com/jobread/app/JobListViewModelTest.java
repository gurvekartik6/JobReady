package com.jobread.app;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.JobStatus;
import com.jobread.app.repositories.JobRepository;
import com.jobread.app.utils.PreferenceManager;
import com.jobread.app.viewmodels.JobListViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.TestScheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobListViewModelTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private JobRepository mJobRepository;

    @Mock
    private PreferenceManager mPreferenceManager;

    private JobListViewModel mViewModel;
    private TestScheduler mTestScheduler;

    private static final String TEST_USER_ID = "test_user_123";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mTestScheduler = new TestScheduler();

        when(mPreferenceManager.getUserId()).thenReturn(TEST_USER_ID);

        // Default: return empty list flowable
        when(mJobRepository.getActiveJobs(TEST_USER_ID))
                .thenReturn(Flowable.just(Arrays.asList()));
        when(mJobRepository.getJobsSortedByCompany(TEST_USER_ID))
                .thenReturn(Flowable.just(Arrays.asList()));
        when(mJobRepository.getJobsSortedByStatus(TEST_USER_ID))
                .thenReturn(Flowable.just(Arrays.asList()));

        mViewModel = new JobListViewModel(mJobRepository, mPreferenceManager);
    }

    @Test
    public void testLoadJobs_returnsJobList() {
        // Given
        List<JobEntity> expectedJobs = createTestJobs();
        when(mJobRepository.getActiveJobs(TEST_USER_ID))
                .thenReturn(Flowable.just(expectedJobs));

        // When
        mViewModel.loadJobs();

        // Then
        assertNotNull(mViewModel.getJobs().getValue());
        assertEquals(expectedJobs.size(), mViewModel.getJobs().getValue().size());
    }

    @Test
    public void testLoadJobs_withNullUserId_doesNotCrash() {
        // Given
        when(mPreferenceManager.getUserId()).thenReturn(null);
        mViewModel = new JobListViewModel(mJobRepository, mPreferenceManager);

        // When - Should not throw
        mViewModel.loadJobs();

        // Then - No crash, jobs list is null (not loaded)
        // (If we had loaded before, it would keep old value)
    }

    @Test
    public void testDeleteJob_callsRepository() {
        // Given
        JobEntity job = createTestJob("Google", "Engineer");
        when(mJobRepository.deleteJob(any(JobEntity.class)))
                .thenReturn(Completable.complete());

        // When
        mViewModel.deleteJob(job);

        // Then
        verify(mJobRepository).deleteJob(job);
    }

    @Test
    public void testArchiveJob_callsRepository() {
        // Given
        String jobId = "job_id_123";
        when(mJobRepository.setArchived(anyString(), anyBoolean()))
                .thenReturn(Completable.complete());

        // When
        mViewModel.archiveJob(jobId, true);

        // Then
        verify(mJobRepository).setArchived(jobId, true);
    }

    @Test
    public void testDuplicateJob_callsRepositoryWithCopy() {
        // Given
        JobEntity original = createTestJob("Apple", "iOS Developer");
        when(mJobRepository.insertJob(any(JobEntity.class)))
                .thenReturn(Completable.complete());

        // When
        mViewModel.duplicateJob(original);

        // Then
        verify(mJobRepository).insertJob(any(JobEntity.class));
    }

    @Test
    public void testSearchJobs_callsSearchRepository() {
        // Given
        String query = "Google";
        when(mJobRepository.searchJobs(TEST_USER_ID, query))
                .thenReturn(Flowable.just(Arrays.asList()));

        // When
        mViewModel.setSearchQuery(query);

        // Then
        verify(mJobRepository).searchJobs(TEST_USER_ID, query);
    }

    @Test
    public void testFilterByStatus_callsFilteredRepository() {
        // Given
        JobStatus status = JobStatus.INTERVIEW_SCHEDULED;
        when(mJobRepository.getJobsByStatus(TEST_USER_ID, status))
                .thenReturn(Flowable.just(Arrays.asList()));

        // When
        mViewModel.setFilter(status);

        // Then
        verify(mJobRepository).getJobsByStatus(TEST_USER_ID, status);
    }

    @Test
    public void testDeleteJob_onError_setsErrorLiveData() {
        // Given
        JobEntity job = createTestJob("Microsoft", "PM");
        when(mJobRepository.deleteJob(any(JobEntity.class)))
                .thenReturn(Completable.error(new RuntimeException("DB Error")));

        // When
        mViewModel.deleteJob(job);

        // Then
        assertNotNull(mViewModel.getError().getValue());
        assertEquals("DB Error", mViewModel.getError().getValue());
    }

    // ---- Helpers ----

    private List<JobEntity> createTestJobs() {
        return Arrays.asList(
                createTestJob("Google", "Software Engineer"),
                createTestJob("Meta", "Product Manager"),
                createTestJob("Amazon", "Data Scientist")
        );
    }

    private JobEntity createTestJob(String company, String role) {
        return JobEntity.create(company, role, new Date(), JobStatus.APPLIED, TEST_USER_ID);
    }
}
