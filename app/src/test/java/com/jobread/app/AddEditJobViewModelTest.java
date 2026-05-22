package com.jobread.app;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.JobStatus;
import com.jobread.app.repositories.JobRepository;
import com.jobread.app.utils.PreferenceManager;
import com.jobread.app.viewmodels.AddEditJobViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddEditJobViewModelTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private JobRepository mJobRepository;

    @Mock
    private PreferenceManager mPreferenceManager;

    private AddEditJobViewModel mViewModel;

    private static final String TEST_USER_ID = "user_xyz";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        RxJavaPlugins.setIoSchedulerHandler(s -> Schedulers.trampoline());
        RxJavaPlugins.setMainThreadSchedulerHandler(s -> Schedulers.trampoline());

        when(mPreferenceManager.getUserId()).thenReturn(TEST_USER_ID);
        when(mJobRepository.insertJob(any())).thenReturn(Completable.complete());
        when(mJobRepository.updateJob(any())).thenReturn(Completable.complete());

        mViewModel = new AddEditJobViewModel(mJobRepository, mPreferenceManager);
    }

    @org.junit.After
    public void tearDown() {
        RxJavaPlugins.reset();
    }

    @Test
    public void testSaveJob_withValidData_callsInsert() {
        // When
        mViewModel.saveJob(null, "Google", "Engineer", pastDate(),
                JobStatus.APPLIED, "Notes", "John", "555-1234", "j@g.com", "$100k");

        // Then
        verify(mJobRepository).insertJob(any(JobEntity.class));
    }

    @Test
    public void testSaveJob_withEmptyCompany_setsError() {
        // When
        mViewModel.saveJob(null, "", "Engineer", pastDate(),
                JobStatus.APPLIED, "", "", "", "", "");

        // Then
        assertNotNull(mViewModel.getError().getValue());
        assertTrue(mViewModel.getError().getValue().contains("Company"));
        verify(mJobRepository, never()).insertJob(any());
    }

    @Test
    public void testSaveJob_withEmptyRole_setsError() {
        // When
        mViewModel.saveJob(null, "Google", "", pastDate(),
                JobStatus.APPLIED, "", "", "", "", "");

        // Then
        assertNotNull(mViewModel.getError().getValue());
        assertTrue(mViewModel.getError().getValue().contains("Role"));
        verify(mJobRepository, never()).insertJob(any());
    }

    @Test
    public void testSaveJob_withFutureDate_setsError() {
        // When
        mViewModel.saveJob(null, "Google", "SWE", futureDate(),
                JobStatus.APPLIED, "", "", "", "", "");

        // Then
        assertNotNull(mViewModel.getError().getValue());
        assertTrue(mViewModel.getError().getValue().contains("future"));
        verify(mJobRepository, never()).insertJob(any());
    }

    @Test
    public void testSaveJob_withJobId_callsUpdate() {
        // Given
        String existingJobId = "existing_job_123";
        JobEntity existingJob = JobEntity.create("Old Co", "Old Role",
                pastDate(), JobStatus.APPLIED, TEST_USER_ID);
        when(mJobRepository.getJobById(existingJobId)).thenReturn(Single.just(existingJob));

        mViewModel.loadJob(existingJobId);

        // When
        mViewModel.saveJob(existingJobId, "New Co", "New Role", pastDate(),
                JobStatus.INTERVIEW_SCHEDULED, "Updated notes", "", "", "", "");

        // Then
        verify(mJobRepository).updateJob(any(JobEntity.class));
    }

    @Test
    public void testSaveJob_success_setsSaveSuccessTrue() {
        // When
        mViewModel.saveJob(null, "Apple", "iOS Dev", pastDate(),
                JobStatus.APPLIED, "", "", "", "", "");

        // Then
        Boolean success = mViewModel.getSaveSuccess().getValue();
        assertNotNull(success);
        assertTrue(success);
    }

    @Test
    public void testSaveJob_insertError_setsErrorLiveData() {
        // Given
        when(mJobRepository.insertJob(any()))
                .thenReturn(Completable.error(new RuntimeException("Insert failed")));

        // When
        mViewModel.saveJob(null, "Google", "SWE", pastDate(),
                JobStatus.APPLIED, "", "", "", "", "");

        // Then
        assertNotNull(mViewModel.getError().getValue());
        assertEquals("Insert failed", mViewModel.getError().getValue());
    }

    @Test
    public void testSaveJob_correctFieldsPassedToEntity() {
        // Given
        ArgumentCaptor<JobEntity> captor = ArgumentCaptor.forClass(JobEntity.class);

        // When
        mViewModel.saveJob(null, "Tesla", "Autopilot Engineer", pastDate(),
                JobStatus.OFFER_RECEIVED, "Great company", "Elon", "555-9999",
                "elon@tesla.com", "$200k+");

        // Then
        verify(mJobRepository).insertJob(captor.capture());
        JobEntity captured = captor.getValue();
        assertEquals("Tesla", captured.getCompany());
        assertEquals("Autopilot Engineer", captured.getRole());
        assertEquals(JobStatus.OFFER_RECEIVED, captured.getStatus());
        assertEquals("Great company", captured.getNotes());
        assertEquals("Elon", captured.getContactPerson());
        assertEquals("elon@tesla.com", captured.getContactEmail());
        assertEquals(TEST_USER_ID, captured.getUserId());
    }

    @Test
    public void testLoadJob_populatesLiveData() {
        // Given
        String jobId = "load_test_job";
        JobEntity expected = JobEntity.create("Netflix", "SRE", pastDate(),
                JobStatus.INTERVIEW_SCHEDULED, TEST_USER_ID);
        when(mJobRepository.getJobById(jobId)).thenReturn(Single.just(expected));

        // When
        mViewModel.loadJob(jobId);

        // Then
        JobEntity loaded = mViewModel.getJob().getValue();
        assertNotNull(loaded);
        assertEquals("Netflix", loaded.getCompany());
        assertEquals("SRE", loaded.getRole());
    }

    // ---- Helpers ----

    private Date pastDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -10);
        return cal.getTime();
    }

    private Date futureDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 10);
        return cal.getTime();
    }
}
