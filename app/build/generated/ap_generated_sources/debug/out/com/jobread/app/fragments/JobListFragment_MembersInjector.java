package com.jobread.app.fragments;

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
public final class JobListFragment_MembersInjector implements MembersInjector<JobListFragment> {
  private final Provider<ViewModelFactory> mViewModelFactoryProvider;

  public JobListFragment_MembersInjector(Provider<ViewModelFactory> mViewModelFactoryProvider) {
    this.mViewModelFactoryProvider = mViewModelFactoryProvider;
  }

  public static MembersInjector<JobListFragment> create(
      Provider<ViewModelFactory> mViewModelFactoryProvider) {
    return new JobListFragment_MembersInjector(mViewModelFactoryProvider);
  }

  @Override
  public void injectMembers(JobListFragment instance) {
    injectMViewModelFactory(instance, mViewModelFactoryProvider.get());
  }

  @InjectedFieldSignature("com.jobread.app.fragments.JobListFragment.mViewModelFactory")
  public static void injectMViewModelFactory(JobListFragment instance,
      ViewModelFactory mViewModelFactory) {
    instance.mViewModelFactory = mViewModelFactory;
  }
}
