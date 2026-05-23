package com.jobread.app.di;

import com.jobread.app.repositories.JobRepository;
import com.jobread.app.utils.PreferenceManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class ViewModelFactory_Factory implements Factory<ViewModelFactory> {
  private final Provider<JobRepository> jobRepositoryProvider;

  private final Provider<PreferenceManager> preferenceManagerProvider;

  public ViewModelFactory_Factory(Provider<JobRepository> jobRepositoryProvider,
      Provider<PreferenceManager> preferenceManagerProvider) {
    this.jobRepositoryProvider = jobRepositoryProvider;
    this.preferenceManagerProvider = preferenceManagerProvider;
  }

  @Override
  public ViewModelFactory get() {
    return newInstance(jobRepositoryProvider.get(), preferenceManagerProvider.get());
  }

  public static ViewModelFactory_Factory create(Provider<JobRepository> jobRepositoryProvider,
      Provider<PreferenceManager> preferenceManagerProvider) {
    return new ViewModelFactory_Factory(jobRepositoryProvider, preferenceManagerProvider);
  }

  public static ViewModelFactory newInstance(JobRepository jobRepository,
      PreferenceManager preferenceManager) {
    return new ViewModelFactory(jobRepository, preferenceManager);
  }
}
