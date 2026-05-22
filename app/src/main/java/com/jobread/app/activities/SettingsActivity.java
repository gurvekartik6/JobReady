package com.jobread.app.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.snackbar.Snackbar;
import com.jobread.app.JobReadApplication;
import com.jobread.app.databinding.ActivitySettingsBinding;
import com.jobread.app.repositories.JobRepository;
import com.jobread.app.utils.CsvUtils;
import com.jobread.app.utils.PreferenceManager;
import com.jobread.app.utils.WorkManagerUtils;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding mBinding;
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    @Inject
    PreferenceManager mPreferenceManager;

    @Inject
    JobRepository mJobRepository;

    private ActivityResultLauncher<String> mImportLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((JobReadApplication) getApplication()).getAppComponent().inject(this);
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setSupportActionBar(mBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }

        setupImportLauncher();
        populateSettings();
        setupClickListeners();
    }

    private void setupImportLauncher() {
        mImportLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                this::importFromUri
        );
    }

    private void populateSettings() {
        int themeMode = mPreferenceManager.getThemeMode();
        mBinding.switchDarkMode.setChecked(themeMode == AppCompatDelegate.MODE_NIGHT_YES);
        mBinding.switchNotifications.setChecked(mPreferenceManager.areNotificationsEnabled());
        mBinding.cardNotificationTime.setVisibility(
                mPreferenceManager.areNotificationsEnabled() ? View.VISIBLE : View.GONE);
        updateNotificationTimeDisplay();
    }

    private void updateNotificationTimeDisplay() {
        int hour = mPreferenceManager.getNotificationHour();
        int minute = mPreferenceManager.getNotificationMinute();
        String amPm = hour >= 12 ? "PM" : "AM";
        int displayHour = hour > 12 ? hour - 12 : (hour == 0 ? 12 : hour);
        mBinding.tvNotificationTime.setText(
                String.format("%d:%02d %s", displayHour, minute, amPm));
    }

    private void setupClickListeners() {
        mBinding.switchDarkMode.setOnCheckedChangeListener((btn, checked) -> {
            int mode = checked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
            mPreferenceManager.setThemeMode(mode);
            AppCompatDelegate.setDefaultNightMode(mode);
        });

        mBinding.switchNotifications.setOnCheckedChangeListener((btn, checked) -> {
            mPreferenceManager.setNotificationsEnabled(checked);
            mBinding.cardNotificationTime.setVisibility(checked ? View.VISIBLE : View.GONE);
            if (checked) {
                WorkManagerUtils.scheduleFollowUpNotification(
                        this,
                        mPreferenceManager.getNotificationHour(),
                        mPreferenceManager.getNotificationMinute()
                );
            } else {
                WorkManagerUtils.cancelFollowUpNotification(this);
            }
        });

        mBinding.btnSetNotificationTime.setOnClickListener(v -> showTimePicker());
        mBinding.btnExportCsv.setOnClickListener(v -> exportCsv());
        mBinding.btnImportCsv.setOnClickListener(v -> mImportLauncher.launch("text/*"));
    }

    private void showTimePicker() {
        int hour = mPreferenceManager.getNotificationHour();
        int minute = mPreferenceManager.getNotificationMinute();
        new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            mPreferenceManager.setNotificationTime(hourOfDay, minuteOfHour);
            updateNotificationTimeDisplay();
            WorkManagerUtils.scheduleFollowUpNotification(this, hourOfDay, minuteOfHour);
        }, hour, minute, false).show();
    }

    private void exportCsv() {
        String userId = mPreferenceManager.getUserId();
        if (userId == null) return;

        mBinding.progressBar.setVisibility(View.VISIBLE);
        mDisposables.add(
            mJobRepository.getAllJobsOnce(userId)
                .subscribeOn(Schedulers.io())
                .map(jobs -> CsvUtils.exportToCsv(this, jobs))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uri -> {
                    mBinding.progressBar.setVisibility(View.GONE);
                    Snackbar.make(mBinding.getRoot(),
                            "Exported to Downloads folder", Snackbar.LENGTH_LONG)
                            .setAction("Open", v -> openFile(uri))
                            .show();
                }, error -> {
                    mBinding.progressBar.setVisibility(View.GONE);
                    Snackbar.make(mBinding.getRoot(),
                            "Export failed: " + error.getMessage(),
                            Snackbar.LENGTH_LONG).show();
                })
        );
    }

    private void importFromUri(Uri uri) {
        if (uri == null) return;
        String userId = mPreferenceManager.getUserId();
        if (userId == null) return;

        mBinding.progressBar.setVisibility(View.VISIBLE);
        mDisposables.add(
            Single.fromCallable(() -> CsvUtils.importFromCsv(this, uri, userId))
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(jobs -> mJobRepository.importJobs(jobs))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    mBinding.progressBar.setVisibility(View.GONE);
                    Snackbar.make(mBinding.getRoot(),
                            "Import successful!", Snackbar.LENGTH_SHORT).show();
                }, error -> {
                    mBinding.progressBar.setVisibility(View.GONE);
                    Snackbar.make(mBinding.getRoot(),
                            "Import failed: " + error.getMessage(),
                            Snackbar.LENGTH_LONG).show();
                })
        );
    }

    private void openFile(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "text/csv");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Open CSV"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposables.clear();
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
