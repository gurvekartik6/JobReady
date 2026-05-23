package com.jobread.app.activities;

import com.jobread.app.di.ViewModelFactory;
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
public final class JobDetailActivity_MembersInjector implements MembersInjector<JobDetailActivity> {
  private final Provider<ViewModelFactory> mViewModelFactoryProvider;

  public JobDetailActivity_MembersInjector(Provider<ViewModelFactory> mViewModelFactoryProvider) {
    this.mViewModelFactoryProvider = mViewModelFactoryProvider;
  }

  public static MembersInjector<JobDetailActivity> create(
      Provider<ViewModelFactory> mViewModelFactoryProvider) {
    return new JobDetailActivity_MembersInjector(mViewModelFactoryProvider);
  }

  @Override
  public void injectMembers(JobDetailActivity instance) {
    injectMViewModelFactory(instance, mViewModelFactoryProvider.get());
  }

  @InjectedFieldSignature("com.jobread.app.activities.JobDetailActivity.mViewModelFactory")
  public static void injectMViewModelFactory(JobDetailActivity instance,
      ViewModelFactory mViewModelFactory) {
    instance.mViewModelFactory = mViewModelFactory;
  }
}
