package com.jobread.app.activities;

import com.jobread.app.repositories.JobRepository;
import com.jobread.app.utils.PreferenceManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class SettingsActivity_MembersInjector implements MembersInjector<SettingsActivity> {
  private final Provider<PreferenceManager> mPreferenceManagerProvider;

  private final Provider<JobRepository> mJobRepositoryProvider;

  public SettingsActivity_MembersInjector(Provider<PreferenceManager> mPreferenceManagerProvider,
      Provider<JobRepository> mJobRepositoryProvider) {
    this.mPreferenceManagerProvider = mPreferenceManagerProvider;
    this.mJobRepositoryProvider = mJobRepositoryProvider;
  }

  public static MembersInjector<SettingsActivity> create(
      Provider<PreferenceManager> mPreferenceManagerProvider,
      Provider<JobRepository> mJobRepositoryProvider) {
    return new SettingsActivity_MembersInjector(mPreferenceManagerProvider, mJobRepositoryProvider);
  }

  @Override
  public void injectMembers(SettingsActivity instance) {
    injectMPreferenceManager(instance, mPreferenceManagerProvider.get());
    injectMJobRepository(instance, mJobRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.jobread.app.activities.SettingsActivity.mPreferenceManager")
  public static void injectMPreferenceManager(SettingsActivity instance,
      PreferenceManager mPreferenceManager) {
    instance.mPreferenceManager = mPreferenceManager;
  }

  @InjectedFieldSignature("com.jobread.app.activities.SettingsActivity.mJobRepository")
  public static void injectMJobRepository(SettingsActivity instance, JobRepository mJobRepository) {
    instance.mJobRepository = mJobRepository;
  }
}
