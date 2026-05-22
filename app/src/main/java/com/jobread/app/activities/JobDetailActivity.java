package com.jobread.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.jobread.app.JobReadApplication;
import com.jobread.app.R;
import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.databinding.ActivityJobDetailBinding;
import com.jobread.app.di.ViewModelFactory;
import com.jobread.app.models.JobStatus;
import com.jobread.app.utils.DateUtils;
import com.jobread.app.viewmodels.JobDetailViewModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class JobDetailActivity extends AppCompatActivity {

    public static final String EXTRA_JOB_ID = "extra_job_id";
    public static final String EXTRA_TRANSITION_NAME = "extra_transition_name";

    private ActivityJobDetailBinding mBinding;
    private JobDetailViewModel mViewModel;
    private String mJobId;
    private JobEntity mCurrentJob;

    @Inject
    ViewModelFactory mViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((JobReadApplication) getApplication()).getAppComponent().inject(this);
        mBinding = ActivityJobDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this, mViewModelFactory).get(JobDetailViewModel.class);

        mJobId = getIntent().getStringExtra(EXTRA_JOB_ID);

        setSupportActionBar(mBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Job Details");
        }

        String transitionName = getIntent().getStringExtra(EXTRA_TRANSITION_NAME);
        if (transitionName != null) {
            mBinding.cardHeader.setTransitionName(transitionName);
        }

        setupObservers();
        setupClickListeners();

        if (mJobId != null) {
            mViewModel.loadJob(mJobId);
        }
    }

    private void setupObservers() {
        mViewModel.getJob().observe(this, job -> {
            if (job != null) {
                mCurrentJob = job;
                populateUI(job);
            }
        });

        mViewModel.getError().observe(this, error -> {
            if (error != null) {
                Snackbar.make(mBinding.getRoot(), error, Snackbar.LENGTH_LONG).show();
            }
        });

        mViewModel.getDeleteSuccess().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                setResult(RESULT_OK);
                finish();
            }
        });

        mViewModel.getStatusUpdateMessage().observe(this, message -> {
            if (message != null) {
                Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUI(JobEntity job) {
        mBinding.tvCompany.setText(job.getCompany());
        mBinding.tvRole.setText(job.getRole());
        mBinding.tvDateApplied.setText(DateUtils.formatDisplay(job.getDateApplied()));
        mBinding.tvStatus.setText(job.getStatus() != null ?
                job.getStatus().getDisplayName() : "Applied");

        mBinding.tvNotes.setText(job.getNotes() != null && !job.getNotes().isEmpty()
                ? job.getNotes() : "No notes");
        mBinding.tvSalaryRange.setText(job.getSalaryRange() != null && !job.getSalaryRange().isEmpty()
                ? job.getSalaryRange() : "Not specified");
        mBinding.tvContactPerson.setText(job.getContactPerson() != null &&
                !job.getContactPerson().isEmpty() ? job.getContactPerson() : "Not provided");

        // Show/hide action buttons based on data
        mBinding.btnCallContact.setVisibility(
                job.getContactPhone() != null && !job.getContactPhone().isEmpty()
                        ? View.VISIBLE : View.GONE);
        mBinding.btnEmailContact.setVisibility(
                job.getContactEmail() != null && !job.getContactEmail().isEmpty()
                        ? View.VISIBLE : View.GONE);
        mBinding.btnAddCalendar.setVisibility(
                job.getStatus() == JobStatus.INTERVIEW_SCHEDULED ? View.VISIBLE : View.GONE);
    }

    private void setupClickListeners() {
        mBinding.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditJobActivity.class);
            intent.putExtra(AddEditJobActivity.EXTRA_JOB_ID, mJobId);
            startActivityForResult(intent, 100);
        });

        mBinding.btnChangeStatus.setOnClickListener(v -> showStatusDialog());

        mBinding.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Job")
                    .setMessage("Are you sure you want to delete this job application?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        if (mCurrentJob != null) {
                            mViewModel.deleteJob(mCurrentJob);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        mBinding.btnCallContact.setOnClickListener(v -> {
            if (mCurrentJob != null && mCurrentJob.getContactPhone() != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mCurrentJob.getContactPhone()));
                startActivity(intent);
            }
        });

        mBinding.btnEmailContact.setOnClickListener(v -> {
            if (mCurrentJob != null && mCurrentJob.getContactEmail() != null) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + mCurrentJob.getContactEmail()));
                intent.putExtra(Intent.EXTRA_SUBJECT,
                        "Re: " + (mCurrentJob.getRole() != null ? mCurrentJob.getRole() : "Application"));
                startActivity(intent);
            }
        });

        mBinding.btnAddCalendar.setOnClickListener(v -> {
            if (mCurrentJob != null) addToCalendar(mCurrentJob);
        });
    }

    private void showStatusDialog() {
        String[] statusNames = new String[JobStatus.values().length];
        for (int i = 0; i < JobStatus.values().length; i++) {
            statusNames[i] = JobStatus.values()[i].getDisplayName();
        }

        int currentIndex = 0;
        if (mCurrentJob != null && mCurrentJob.getStatus() != null) {
            JobStatus[] statuses = JobStatus.values();
            for (int i = 0; i < statuses.length; i++) {
                if (statuses[i] == mCurrentJob.getStatus()) {
                    currentIndex = i;
                    break;
                }
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("Change Status")
                .setSingleChoiceItems(statusNames, currentIndex, (dialog, which) -> {
                    JobStatus newStatus = JobStatus.values()[which];
                    mViewModel.updateStatus(mJobId, newStatus);
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addToCalendar(JobEntity job) {
        long startTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        long endTime = startTime + TimeUnit.HOURS.toMillis(1);

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
                .putExtra(CalendarContract.Events.TITLE,
                        "Interview: " + job.getRole() + " at " + job.getCompany())
                .putExtra(CalendarContract.Events.DESCRIPTION,
                        job.getNotes() != null ? job.getNotes() : "")
                .putExtra(CalendarContract.Events.AVAILABILITY,
                        CalendarContract.Events.AVAILABILITY_BUSY);

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            mViewModel.loadJob(mJobId);
        }
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
