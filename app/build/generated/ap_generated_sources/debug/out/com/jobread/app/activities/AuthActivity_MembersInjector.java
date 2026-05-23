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
public final class AuthActivity_MembersInjector implements MembersInjector<AuthActivity> {
  private final Provider<PreferenceManager> mPreferenceManagerProvider;

  public AuthActivity_MembersInjector(Provider<PreferenceManager> mPreferenceManagerProvider) {
    this.mPreferenceManagerProvider = mPreferenceManagerProvider;
  }

  public static MembersInjector<AuthActivity> create(
      Provider<PreferenceManager> mPreferenceManagerProvider) {
    return new AuthActivity_MembersInjector(mPreferenceManagerProvider);
  }

  @Override
  public void injectMembers(AuthActivity instance) {
    injectMPreferenceManager(instance, mPreferenceManagerProvider.get());
  }

  @InjectedFieldSignature("com.jobread.app.activities.AuthActivity.mPreferenceManager")
  public static void injectMPreferenceManager(AuthActivity instance,
      PreferenceManager mPreferenceManager) {
    instance.mPreferenceManager = mPreferenceManager;
  }
}
