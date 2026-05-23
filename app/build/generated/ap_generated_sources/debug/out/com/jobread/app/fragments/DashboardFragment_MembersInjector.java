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
public final class DashboardFragment_MembersInjector implements MembersInjector<DashboardFragment> {
  private final Provider<ViewModelFactory> mViewModelFactoryProvider;

  public DashboardFragment_MembersInjector(Provider<ViewModelFactory> mViewModelFactoryProvider) {
    this.mViewModelFactoryProvider = mViewModelFactoryProvider;
  }

  public static MembersInjector<DashboardFragment> create(
      Provider<ViewModelFactory> mViewModelFactoryProvider) {
    return new DashboardFragment_MembersInjector(mViewModelFactoryProvider);
  }

  @Override
  public void injectMembers(DashboardFragment instance) {
    injectMViewModelFactory(instance, mViewModelFactoryProvider.get());
  }

  @InjectedFieldSignature("com.jobread.app.fragments.DashboardFragment.mViewModelFactory")
  public static void injectMViewModelFactory(DashboardFragment instance,
      ViewModelFactory mViewModelFactory) {
    instance.mViewModelFactory = mViewModelFactory;
  }
}
