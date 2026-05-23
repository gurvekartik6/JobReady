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
public final class AddEditJobActivity_MembersInjector implements MembersInjector<AddEditJobActivity> {
  private final Provider<ViewModelFactory> mViewModelFactoryProvider;

  public AddEditJobActivity_MembersInjector(Provider<ViewModelFactory> mViewModelFactoryProvider) {
    this.mViewModelFactoryProvider = mViewModelFactoryProvider;
  }

  public static MembersInjector<AddEditJobActivity> create(
      Provider<ViewModelFactory> mViewModelFactoryProvider) {
    return new AddEditJobActivity_MembersInjector(mViewModelFactoryProvider);
  }

  @Override
  public void injectMembers(AddEditJobActivity instance) {
    injectMViewModelFactory(instance, mViewModelFactoryProvider.get());
  }

  @InjectedFieldSignature("com.jobread.app.activities.AddEditJobActivity.mViewModelFactory")
  public static void injectMViewModelFactory(AddEditJobActivity instance,
      ViewModelFactory mViewModelFactory) {
    instance.mViewModelFactory = mViewModelFactory;
  }
}
