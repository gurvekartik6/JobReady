package com.jobread.app.activities;

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
public final class SplashActivity_MembersInjector implements MembersInjector<SplashActivity> {
  private final Provider<PreferenceManager> mPreferenceManagerProvider;

  public SplashActivity_MembersInjector(Provider<PreferenceManager> mPreferenceManagerProvider) {
    this.mPreferenceManagerProvider = mPreferenceManagerProvider;
  }

  public static MembersInjector<SplashActivity> create(
      Provider<PreferenceManager> mPreferenceManagerProvider) {
    return new SplashActivity_MembersInjector(mPreferenceManagerProvider);
  }

  @Override
  public void injectMembers(SplashActivity instance) {
    injectMPreferenceManager(instance, mPreferenceManagerProvider.get());
  }

  @InjectedFieldSignature("com.jobread.app.activities.SplashActivity.mPreferenceManager")
  public static void injectMPreferenceManager(SplashActivity instance,
      PreferenceManager mPreferenceManager) {
    instance.mPreferenceManager = mPreferenceManager;
  }
}
