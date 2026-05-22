package com.jobread.app.di;

import android.content.Context;

import com.jobread.app.database.AppDatabase;
import com.jobread.app.database.dao.JobDao;
import com.jobread.app.repositories.JobRepository;
import com.jobread.app.utils.NetworkUtils;
import com.jobread.app.utils.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context mContext;

    public AppModule(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    public AppDatabase provideDatabase(Context context) {
        return AppDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public JobDao provideJobDao(AppDatabase database) {
        return database.jobDao();
    }

    @Provides
    @Singleton
    public JobRepository provideJobRepository(JobDao jobDao) {
        return new JobRepository(jobDao);
    }

    @Provides
    @Singleton
    public PreferenceManager providePreferenceManager(Context context) {
        return new PreferenceManager(context);
    }

    @Provides
    @Singleton
    public NetworkUtils provideNetworkUtils(Context context) {
        return new NetworkUtils(context);
    }

    @Provides
    @Singleton
    public ViewModelFactory provideViewModelFactory(JobRepository jobRepository,
                                                     PreferenceManager preferenceManager) {
        return new ViewModelFactory(jobRepository, preferenceManager);
    }
}
