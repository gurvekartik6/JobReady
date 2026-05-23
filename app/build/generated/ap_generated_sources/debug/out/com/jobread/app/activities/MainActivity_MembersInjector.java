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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<PreferenceManager> mPreferenceManagerProvider;

  public MainActivity_MembersInjector(Provider<PreferenceManager> mPreferenceManagerProvider) {
    this.mPreferenceManagerProvider = mPreferenceManagerProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<PreferenceManager> mPreferenceManagerProvider) {
    return new MainActivity_MembersInjector(mPreferenceManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectMPreferenceManager(instance, mPreferenceManagerProvider.get());
  }

  @InjectedFieldSignature("com.jobread.app.activities.MainActivity.mPreferenceManager")
  public static void injectMPreferenceManager(MainActivity instance,
      PreferenceManager mPreferenceManager) {
    instance.mPreferenceManager = mPreferenceManager;
  }
}
