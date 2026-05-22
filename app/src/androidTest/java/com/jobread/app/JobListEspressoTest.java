package com.jobread.app;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.jobread.app.R;
import com.jobread.app.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class JobListEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    private void launchActivity() {
        mActivityRule.launchActivity(
                new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class));
    }

    @Test
    public void testMainActivity_bottomNavigationVisible() {
        launchActivity();
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToJobsTab_showsFab() {
        launchActivity();
        onView(withId(R.id.nav_jobs)).perform(click());
        onView(withId(R.id.fab_add_job)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToDashboard_showsStats() {
        launchActivity();
        onView(withId(R.id.nav_dashboard)).perform(click());
        onView(withId(R.id.tv_total_count)).check(matches(isDisplayed()));
    }

    @Test
    public void testJobListFilterSpinner_isDisplayed() {
        launchActivity();
        onView(withId(R.id.nav_jobs)).perform(click());
        onView(withId(R.id.spinner_filter)).check(matches(isDisplayed()));
    }

    @Test
    public void testFabAddJob_opensAddEditActivity() {
        launchActivity();
        onView(withId(R.id.nav_jobs)).perform(click());
        onView(withId(R.id.fab_add_job)).perform(click());
        onView(withId(R.id.et_company)).check(matches(isDisplayed()));
    }
}
