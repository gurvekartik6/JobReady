package com.jobread.app.di;

import com.jobread.app.activities.AddEditJobActivity;
import com.jobread.app.activities.AuthActivity;
import com.jobread.app.activities.JobDetailActivity;
import com.jobread.app.activities.MainActivity;
import com.jobread.app.activities.SettingsActivity;
import com.jobread.app.activities.SplashActivity;
import com.jobread.app.fragments.DashboardFragment;
import com.jobread.app.fragments.JobListFragment;
import com.jobread.app.workers.FollowUpNotificationWorker;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    // Activities
    void inject(SplashActivity activity);
    void inject(AuthActivity activity);
    void inject(MainActivity activity);
    void inject(AddEditJobActivity activity);
    void inject(JobDetailActivity activity);
    void inject(SettingsActivity activity);

    // Fragments
    void inject(DashboardFragment fragment);
    void inject(JobListFragment fragment);

    // Workers
    void inject(FollowUpNotificationWorker worker);

    // Expose for use in activities/fragments
    ViewModelFactory viewModelFactory();
}
