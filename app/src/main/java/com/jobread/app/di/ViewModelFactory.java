package com.jobread.app.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.jobread.app.repositories.JobRepository;
import com.jobread.app.utils.PreferenceManager;
import com.jobread.app.viewmodels.AddEditJobViewModel;
import com.jobread.app.viewmodels.AuthViewModel;
import com.jobread.app.viewmodels.DashboardViewModel;
import com.jobread.app.viewmodels.JobDetailViewModel;
import com.jobread.app.viewmodels.JobListViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final JobRepository mJobRepository;
    private final PreferenceManager mPreferenceManager;

    @Inject
    public ViewModelFactory(JobRepository jobRepository, PreferenceManager preferenceManager) {
        this.mJobRepository = jobRepository;
        this.mPreferenceManager = preferenceManager;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DashboardViewModel.class)) {
            return (T) new DashboardViewModel(mJobRepository, mPreferenceManager);
        } else if (modelClass.isAssignableFrom(JobListViewModel.class)) {
            return (T) new JobListViewModel(mJobRepository, mPreferenceManager);
        } else if (modelClass.isAssignableFrom(AddEditJobViewModel.class)) {
            return (T) new AddEditJobViewModel(mJobRepository, mPreferenceManager);
        } else if (modelClass.isAssignableFrom(JobDetailViewModel.class)) {
            return (T) new JobDetailViewModel(mJobRepository, mPreferenceManager);
        } else if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
