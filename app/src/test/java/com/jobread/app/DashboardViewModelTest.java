package com.jobread.app;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.DashboardStats;
import com.jobread.app.models.JobStatus;
import com.jobread.app.repositories.JobRepository;
import com.jobread.app.utils.PreferenceManager;
import com.jobread.app.viewmodels.DashboardViewModel;

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

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.schedulers.TestScheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DashboardViewModelTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private JobRepository mJobRepository;

    @Mock
    private PreferenceManager mPreferenceManager;

    private DashboardViewModel mViewModel;
    private TestScheduler mTestScheduler;

    private static final String TEST_USER_ID = "test_user_abc";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mTestScheduler = new TestScheduler();

        // Override schedulers so RxJava runs synchronously in tests
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

        when(mPreferenceManager.getUserId()).thenReturn(TEST_USER_ID);

        // Default repository stubs
        when(mJobRepository.getTotalCount(TEST_USER_ID)).thenReturn(Single.just(5));
        when(mJobRepository.getCountByStatus(TEST_USER_ID, JobStatus.INTERVIEW_SCHEDULED))
                .thenReturn(Single.just(2));
        when(mJobRepository.getCountByStatus(TEST_USER_ID, JobStatus.OFFER_RECEIVED))
                .thenReturn(Single.just(1));
        when(mJobRepository.getCountByStatus(TEST_USER_ID, JobStatus.REJECTED))
                .thenReturn(Single.just(1));
        when(mJobRepository.getCountByStatus(TEST_USER_ID, JobStatus.ACCEPTED))
                .thenReturn(Single.just(1));
        when(mJobRepository.getAllJobsOnce(TEST_USER_ID))
                .thenReturn(Single.just(createTestJobs()));

        mViewModel = new DashboardViewModel(mJobRepository, mPreferenceManager);
    }

    @org.junit.After
    public void tearDown() {
        RxJavaPlugins.reset();
    }

    @Test
    public void testLoadDashboard_setsCorrectStats() {
        // When
        mViewModel.loadDashboard();

        // Then
        DashboardStats stats = mViewModel.getStats().getValue();
        assertNotNull(stats);
        assertEquals(5, stats.getTotal());
        assertEquals(2, stats.getInterviews());
        assertEquals(1, stats.getOffers());
        assertEquals(1, stats.getRejections());
    }

    @Test
    public void testLoadDashboard_withZeroCounts_showsZeroStats() {
        // Given
        when(mJobRepository.getTotalCount(TEST_USER_ID)).thenReturn(Single.just(0));
        when(mJobRepository.getCountByStatus(TEST_USER_ID, JobStatus.INTERVIEW_SCHEDULED))
                .thenReturn(Single.just(0));
        when(mJobRepository.getCountByStatus(TEST_USER_ID, JobStatus.OFFER_RECEIVED))
                .thenReturn(Single.just(0));
        when(mJobRepository.getCountByStatus(TEST_USER_ID, JobStatus.REJECTED))
                .thenReturn(Single.just(0));
        when(mJobRepository.getCountByStatus(TEST_USER_ID, JobStatus.ACCEPTED))
                .thenReturn(Single.just(0));

        // When
        mViewModel.loadDashboard();

        // Then
        DashboardStats stats = mViewModel.getStats().getValue();
        assertNotNull(stats);
        assertEquals(0, stats.getTotal());
        assertEquals(0, stats.getInterviews());
    }

    @Test
    public void testLoadDashboard_setsWeeklyCounts() {
        // When
        mViewModel.loadDashboard();

        // Then
        List<Float> weeklyCounts = mViewModel.getWeeklyCounts().getValue();
        assertNotNull(weeklyCounts);
        assertEquals(4, weeklyCounts.size());
    }

    @Test
    public void testLoadDashboard_withNullUser_doesNothing() {
        // Given
        when(mPreferenceManager.getUserId()).thenReturn(null);
        mViewModel = new DashboardViewModel(mJobRepository, mPreferenceManager);

        // When
        mViewModel.loadDashboard();

        // Then - stats remain null, no crash
        // Just verifying no exception thrown
    }

    @Test
    public void testRefresh_reloadsDashboard() {
        // Given
        mViewModel.loadDashboard();
        DashboardStats initialStats = mViewModel.getStats().getValue();

        // Update mock to return different values
        when(mJobRepository.getTotalCount(TEST_USER_ID)).thenReturn(Single.just(10));

        // When
        mViewModel.refresh();

        // Then
        DashboardStats refreshedStats = mViewModel.getStats().getValue();
        assertNotNull(refreshedStats);
        assertEquals(10, refreshedStats.getTotal());
    }

    @Test
    public void testLoadDashboard_onError_setsErrorLiveData() {
        // Given
        when(mJobRepository.getTotalCount(TEST_USER_ID))
                .thenReturn(Single.error(new RuntimeException("Network error")));

        // When
        mViewModel.loadDashboard();

        // Then
        assertNotNull(mViewModel.getError().getValue());
    }

    // ---- Helpers ----

    private List<JobEntity> createTestJobs() {
        return Arrays.asList(
                JobEntity.create("Google", "SWE", new Date(), JobStatus.APPLIED, TEST_USER_ID),
                JobEntity.create("Meta", "PM", new Date(), JobStatus.INTERVIEW_SCHEDULED, TEST_USER_ID),
                JobEntity.create("Amazon", "SDE", new Date(), JobStatus.OFFER_RECEIVED, TEST_USER_ID),
                JobEntity.create("Netflix", "SRE", new Date(), JobStatus.REJECTED, TEST_USER_ID),
                JobEntity.create("Apple", "iOS", new Date(), JobStatus.ACCEPTED, TEST_USER_ID)
        );
    }
}
