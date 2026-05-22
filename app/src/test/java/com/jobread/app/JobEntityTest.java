package com.jobread.app;

import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.JobStatus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class JobEntityTest {

    private static final String TEST_USER_ID = "user_test_001";

    @Test
    public void testCreate_populatesRequiredFields() {
        // Given
        String company = "Google";
        String role = "SWE";
        Date date = new Date();
        JobStatus status = JobStatus.APPLIED;

        // When
        JobEntity entity = JobEntity.create(company, role, date, status, TEST_USER_ID);

        // Then
        assertNotNull(entity.getId());
        assertEquals(company, entity.getCompany());
        assertEquals(role, entity.getRole());
        assertEquals(date, entity.getDateApplied());
        assertEquals(status, entity.getStatus());
        assertEquals(TEST_USER_ID, entity.getUserId());
        assertFalse(entity.isArchived());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    public void testCreate_generatesUniqueIds() {
        // When
        JobEntity job1 = JobEntity.create("Co1", "Role1", new Date(), JobStatus.APPLIED, TEST_USER_ID);
        JobEntity job2 = JobEntity.create("Co2", "Role2", new Date(), JobStatus.APPLIED, TEST_USER_ID);

        // Then
        assertNotEquals(job1.getId(), job2.getId());
    }

    @Test
    public void testDuplicate_createsNewJobWithDifferentId() {
        // Given
        JobEntity original = JobEntity.create("Facebook", "PM", new Date(), JobStatus.APPLIED, TEST_USER_ID);

        // When
        JobEntity copy = original.duplicate();

        // Then
        assertNotEquals(original.getId(), copy.getId());
        assertEquals(JobStatus.APPLIED, copy.getStatus());
        assertFalse(copy.isArchived());
        assertNotNull(copy.getId());
    }

    @Test
    public void testDuplicate_appendsCopyToCompanyName() {
        // Given
        JobEntity original = JobEntity.create("Tesla", "SRE", new Date(), JobStatus.OFFER_RECEIVED, TEST_USER_ID);

        // When
        JobEntity copy = original.duplicate();

        // Then
        assertTrue(copy.getCompany().contains("Copy"));
        assertEquals(original.getRole(), copy.getRole());
    }

    @Test
    public void testSetStatus_updatesUpdatedAt() throws InterruptedException {
        // Given
        JobEntity entity = JobEntity.create("Amazon", "DevOps", new Date(), JobStatus.APPLIED, TEST_USER_ID);
        Date originalUpdatedAt = entity.getUpdatedAt();

        // Small delay
        Thread.sleep(10);

        // When
        entity.setStatus(JobStatus.INTERVIEW_SCHEDULED);

        // Then
        assertEquals(JobStatus.INTERVIEW_SCHEDULED, entity.getStatus());
        assertTrue(entity.getUpdatedAt().getTime() >= originalUpdatedAt.getTime());
    }

    @Test
    public void testSetArchived() {
        // Given
        JobEntity entity = JobEntity.create("Netflix", "iOS", new Date(), JobStatus.APPLIED, TEST_USER_ID);
        assertFalse(entity.isArchived());

        // When
        entity.setArchived(true);

        // Then
        assertTrue(entity.isArchived());
    }

    @Test
    public void testAllFields_gettersAndSetters() {
        // Given
        JobEntity entity = new JobEntity();
        Date now = new Date();

        // When
        entity.setId("test_id");
        entity.setCompany("Microsoft");
        entity.setRole("Principal Engineer");
        entity.setDateApplied(now);
        entity.setStatus(JobStatus.REJECTED);
        entity.setNotes("Good culture");
        entity.setContactPerson("Jane Smith");
        entity.setContactPhone("555-0000");
        entity.setContactEmail("jane@ms.com");
        entity.setSalaryRange("$150k-$180k");
        entity.setArchived(false);
        entity.setUserId(TEST_USER_ID);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        // Then
        assertEquals("test_id", entity.getId());
        assertEquals("Microsoft", entity.getCompany());
        assertEquals("Principal Engineer", entity.getRole());
        assertEquals(now, entity.getDateApplied());
        assertEquals(JobStatus.REJECTED, entity.getStatus());
        assertEquals("Good culture", entity.getNotes());
        assertEquals("Jane Smith", entity.getContactPerson());
        assertEquals("555-0000", entity.getContactPhone());
        assertEquals("jane@ms.com", entity.getContactEmail());
        assertEquals("$150k-$180k", entity.getSalaryRange());
        assertFalse(entity.isArchived());
        assertEquals(TEST_USER_ID, entity.getUserId());
    }
}
