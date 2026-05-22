package com.jobread.app.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.jobread.app.JobReadApplication;
import com.jobread.app.R;
import com.jobread.app.databinding.ActivityAddEditJobBinding;
import com.jobread.app.di.ViewModelFactory;
import com.jobread.app.models.JobStatus;
import com.jobread.app.utils.DateUtils;
import com.jobread.app.viewmodels.AddEditJobViewModel;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

public class AddEditJobActivity extends AppCompatActivity {

    public static final String EXTRA_JOB_ID = "extra_job_id";

    private ActivityAddEditJobBinding mBinding;
    private AddEditJobViewModel mViewModel;
    private Date mSelectedDate = new Date();
    private String mJobId;

    @Inject
    ViewModelFactory mViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((JobReadApplication) getApplication()).getAppComponent().inject(this);
        mBinding = ActivityAddEditJobBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this, mViewModelFactory).get(AddEditJobViewModel.class);

        mJobId = getIntent().getStringExtra(EXTRA_JOB_ID);
        boolean isEditing = mJobId != null;

        setSupportActionBar(mBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditing ? "Edit Job" : "Add Job");
        }

        setupStatusSpinner();
        setupDatePicker();
        setupObservers();
        setupSaveButton();

        if (isEditing) {
            mViewModel.loadJob(mJobId);
        }
    }

    private void setupStatusSpinner() {
        String[] statusNames = new String[JobStatus.values().length];
        for (int i = 0; i < JobStatus.values().length; i++) {
            statusNames[i] = JobStatus.values()[i].getDisplayName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, statusNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerStatus.setAdapter(adapter);
    }

    private void setupDatePicker() {
        mBinding.etDateApplied.setOnClickListener(v -> showDatePicker());
        mBinding.etDateApplied.setText(DateUtils.formatDisplay(mSelectedDate));
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (mSelectedDate != null) calendar.setTime(mSelectedDate);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth);
                    mSelectedDate = selected.getTime();
                    mBinding.etDateApplied.setText(DateUtils.formatDisplay(mSelectedDate));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    private void setupObservers() {
        mViewModel.getJob().observe(this, job -> {
            if (job != null) {
                mBinding.etCompany.setText(job.getCompany());
                mBinding.etRole.setText(job.getRole());
                mBinding.etNotes.setText(job.getNotes());
                mBinding.etContactPerson.setText(job.getContactPerson());
                mBinding.etContactPhone.setText(job.getContactPhone());
                mBinding.etContactEmail.setText(job.getContactEmail());
                mBinding.etSalaryRange.setText(job.getSalaryRange());

                if (job.getDateApplied() != null) {
                    mSelectedDate = job.getDateApplied();
                    mBinding.etDateApplied.setText(DateUtils.formatDisplay(mSelectedDate));
                }

                if (job.getStatus() != null) {
                    JobStatus[] statuses = JobStatus.values();
                    for (int i = 0; i < statuses.length; i++) {
                        if (statuses[i] == job.getStatus()) {
                            mBinding.spinnerStatus.setSelection(i);
                            break;
                        }
                    }
                }
            }
        });

        mViewModel.getSaveSuccess().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                setResult(RESULT_OK);
                finish();
            }
        });

        mViewModel.getError().observe(this, error -> {
            if (error != null) {
                Snackbar.make(mBinding.getRoot(), error, Snackbar.LENGTH_LONG).show();
                clearError(error);
            }
        });

        mViewModel.getLoading().observe(this, loading -> {
            mBinding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            mBinding.btnSave.setEnabled(!loading);
        });
    }

    private void clearError(String error) {
        // Clear field errors based on error message
        if (error.contains("Company")) {
            mBinding.tilCompany.setError(error);
        } else if (error.contains("Role")) {
            mBinding.tilRole.setError(error);
        } else if (error.contains("Date")) {
            mBinding.tilDateApplied.setError(error);
        }
    }

    private void setupSaveButton() {
        mBinding.btnSave.setOnClickListener(v -> {
            // Clear previous errors
            mBinding.tilCompany.setError(null);
            mBinding.tilRole.setError(null);
            mBinding.tilDateApplied.setError(null);

            String company = mBinding.etCompany.getText() != null ?
                    mBinding.etCompany.getText().toString().trim() : "";
            String role = mBinding.etRole.getText() != null ?
                    mBinding.etRole.getText().toString().trim() : "";
            String notes = mBinding.etNotes.getText() != null ?
                    mBinding.etNotes.getText().toString() : "";
            String contactPerson = mBinding.etContactPerson.getText() != null ?
                    mBinding.etContactPerson.getText().toString() : "";
            String contactPhone = mBinding.etContactPhone.getText() != null ?
                    mBinding.etContactPhone.getText().toString() : "";
            String contactEmail = mBinding.etContactEmail.getText() != null ?
                    mBinding.etContactEmail.getText().toString() : "";
            String salaryRange = mBinding.etSalaryRange.getText() != null ?
                    mBinding.etSalaryRange.getText().toString() : "";

            int statusPosition = mBinding.spinnerStatus.getSelectedItemPosition();
            JobStatus status = JobStatus.values()[statusPosition];

            mViewModel.saveJob(mJobId, company, role, mSelectedDate, status,
                    notes, contactPerson, contactPhone, contactEmail, salaryRange);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
