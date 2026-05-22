package com.jobread.app;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.jobread.app.di.AppComponent;
import com.jobread.app.di.AppModule;
import com.jobread.app.di.DaggerAppComponent;
import com.jobread.app.di.NetworkModule;
import com.jobread.app.utils.NotificationUtils;
import com.jobread.app.utils.PreferenceManager;

public class JobReadApplication extends Application {

    private AppComponent mAppComponent;
    private static JobReadApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        MultiDex.install(this);

        // Build Dagger component
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();

        // Create notification channels
        NotificationUtils.createNotificationChannels(this);

        // Apply saved theme
        PreferenceManager prefs = new PreferenceManager(this);
        AppCompatDelegate.setDefaultNightMode(prefs.getThemeMode());
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static JobReadApplication getInstance() {
        return sInstance;
    }
}
