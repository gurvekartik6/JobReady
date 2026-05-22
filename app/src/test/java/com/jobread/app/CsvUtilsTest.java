package com.jobread.app;

import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.JobStatus;
import com.jobread.app.utils.DateUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CsvUtilsTest {

    @Test
    public void testJobStatusEnum_allValuesHaveDisplayNames() {
        for (JobStatus status : JobStatus.values()) {
            assertNotNull(status.getDisplayName());
            assertFalse(status.getDisplayName().isEmpty());
        }
    }

    @Test
    public void testJobStatusEnum_fromDisplayName() {
        assertEquals(JobStatus.APPLIED, JobStatus.fromDisplayName("Applied"));
        assertEquals(JobStatus.INTERVIEW_SCHEDULED, JobStatus.fromDisplayName("Interview Scheduled"));
        assertEquals(JobStatus.OFFER_RECEIVED, JobStatus.fromDisplayName("Offer Received"));
        assertEquals(JobStatus.REJECTED, JobStatus.fromDisplayName("Rejected"));
        assertEquals(JobStatus.ACCEPTED, JobStatus.fromDisplayName("Accepted"));
    }

    @Test
    public void testJobStatusEnum_fromDisplayName_unknownDefaultsToApplied() {
        assertEquals(JobStatus.APPLIED, JobStatus.fromDisplayName("Unknown Status"));
    }

    @Test
    public void testDateUtils_formatDisplay_notNull() {
        Date date = new Date();
        String formatted = DateUtils.formatDisplay(date);
        assertNotNull(formatted);
        assertFalse(formatted.isEmpty());
    }

    @Test
    public void testDateUtils_formatDisplay_nullDate_returnsEmpty() {
        String result = DateUtils.formatDisplay(null);
        assertEquals("", result);
    }

    @Test
    public void testDateUtils_isInFuture_withFutureDate() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_YEAR, 1);
        assertTrue(DateUtils.isInFuture(cal.getTime()));
    }

    @Test
    public void testDateUtils_isInFuture_withPastDate() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_YEAR, -1);
        assertFalse(DateUtils.isInFuture(cal.getTime()));
    }

    @Test
    public void testDateUtils_isOlderThanDays() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_YEAR, -10);
        assertTrue(DateUtils.isOlderThanDays(cal.getTime(), 7));
        assertFalse(DateUtils.isOlderThanDays(cal.getTime(), 14));
    }

    @Test
    public void testDateUtils_daysAgo() {
        Date sevenDaysAgo = DateUtils.daysAgo(7);
        assertNotNull(sevenDaysAgo);
        assertTrue(sevenDaysAgo.before(new Date()));
    }

    @Test
    public void testDateUtils_getWeekLabel() {
        assertEquals("This Week", DateUtils.getWeekLabel(0));
        assertEquals("Last Week", DateUtils.getWeekLabel(1));
        assertEquals("2 Weeks Ago", DateUtils.getWeekLabel(2));
        assertEquals("3 Weeks Ago", DateUtils.getWeekLabel(3));
    }

    @Test
    public void testJobEntity_duplicate_resetStatusToApplied() {
        JobEntity original = JobEntity.create("OpenAI", "ML Eng", new Date(),
                JobStatus.OFFER_RECEIVED, "user1");
        JobEntity copy = original.duplicate();

        // Duplicates reset to APPLIED
        assertEquals(JobStatus.APPLIED, copy.getStatus());
        assertFalse(copy.isArchived());
        assertNotEquals(original.getId(), copy.getId());
    }
}
