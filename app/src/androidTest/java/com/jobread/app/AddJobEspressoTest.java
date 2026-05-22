package com.jobread.app;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.jobread.app.R;
import com.jobread.app.activities.AddEditJobActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddJobEspressoTest {

    @Rule
    public ActivityTestRule<AddEditJobActivity> mActivityRule =
            new ActivityTestRule<>(AddEditJobActivity.class, false, false);

    private void launchActivity() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(),
                AddEditJobActivity.class);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void testAddJobActivity_toolbarIsDisplayed() {
        launchActivity();
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void testCompanyField_isDisplayed() {
        launchActivity();
        onView(withId(R.id.et_company)).check(matches(isDisplayed()));
    }

    @Test
    public void testRoleField_isDisplayed() {
        launchActivity();
        onView(withId(R.id.et_role)).check(matches(isDisplayed()));
    }

    @Test
    public void testSaveButton_isDisplayed() {
        launchActivity();
        onView(withId(R.id.btn_save)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void testSaveWithEmptyCompany_showsValidationError() {
        launchActivity();

        // Only fill role, leave company empty
        onView(withId(R.id.et_role))
                .perform(click(), typeText("Software Engineer"), closeSoftKeyboard());

        onView(withId(R.id.btn_save)).perform(scrollTo(), click());

        // Expect snackbar with validation message
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(org.hamcrest.Matchers.containsString("Company"))));
    }

    @Test
    public void testSaveWithEmptyRole_showsValidationError() {
        launchActivity();

        // Fill company, leave role empty
        onView(withId(R.id.et_company))
                .perform(click(), typeText("Google"), closeSoftKeyboard());

        onView(withId(R.id.btn_save)).perform(scrollTo(), click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(org.hamcrest.Matchers.containsString("Role"))));
    }

    @Test
    public void testFillingRequiredFields_enablesSave() {
        launchActivity();

        onView(withId(R.id.et_company))
                .perform(click(), typeText("Apple"), closeSoftKeyboard());

        onView(withId(R.id.et_role))
                .perform(click(), typeText("iOS Developer"), closeSoftKeyboard());

        onView(withId(R.id.btn_save)).perform(scrollTo()).check(matches(isEnabled()));
    }

    @Test
    public void testOptionalFields_fillWithoutError() {
        launchActivity();

        onView(withId(R.id.et_company))
                .perform(click(), typeText("Microsoft"), closeSoftKeyboard());

        onView(withId(R.id.et_role))
                .perform(click(), typeText("Principal SWE"), closeSoftKeyboard());

        onView(withId(R.id.et_notes))
                .perform(scrollTo(), click(),
                        typeText("Great company"), closeSoftKeyboard());

        onView(withId(R.id.et_salary_range))
                .perform(scrollTo(), click(), typeText("$180k+"), closeSoftKeyboard());

        onView(withId(R.id.btn_save)).perform(scrollTo()).check(matches(isEnabled()));
    }

    @Test
    public void testStatusSpinner_isDisplayed() {
        launchActivity();
        onView(withId(R.id.spinner_status)).check(matches(isDisplayed()));
    }

    @Test
    public void testDateField_hasNonEmptyText() {
        launchActivity();
        onView(withId(R.id.et_date_applied))
                .check(matches(withText(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.isEmptyString()))));
    }
}
