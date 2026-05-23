package com.jobread.app.di;

import com.jobread.app.repositories.JobRepository;
import com.jobread.app.utils.PreferenceManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideViewModelFactoryFactory implements Factory<ViewModelFactory> {
  private final AppModule module;

  private final Provider<JobRepository> jobRepositoryProvider;

  private final Provider<PreferenceManager> preferenceManagerProvider;

  public AppModule_ProvideViewModelFactoryFactory(AppModule module,
      Provider<JobRepository> jobRepositoryProvider,
      Provider<PreferenceManager> preferenceManagerProvider) {
    this.module = module;
    this.jobRepositoryProvider = jobRepositoryProvider;
    this.preferenceManagerProvider = preferenceManagerProvider;
  }

  @Override
  public ViewModelFactory get() {
    return provideViewModelFactory(module, jobRepositoryProvider.get(), preferenceManagerProvider.get());
  }

  public static AppModule_ProvideViewModelFactoryFactory create(AppModule module,
      Provider<JobRepository> jobRepositoryProvider,
      Provider<PreferenceManager> preferenceManagerProvider) {
    return new AppModule_ProvideViewModelFactoryFactory(module, jobRepositoryProvider, preferenceManagerProvider);
  }

  public static ViewModelFactory provideViewModelFactory(AppModule instance,
      JobRepository jobRepository, PreferenceManager preferenceManager) {
    return Preconditions.checkNotNullFromProvides(instance.provideViewModelFactory(jobRepository, preferenceManager));
  }
}
