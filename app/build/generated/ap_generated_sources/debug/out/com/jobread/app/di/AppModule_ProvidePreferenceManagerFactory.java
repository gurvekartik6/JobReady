package com.jobread.app.di;

import android.content.Context;
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
public final class AppModule_ProvidePreferenceManagerFactory implements Factory<PreferenceManager> {
  private final AppModule module;

  private final Provider<Context> contextProvider;

  public AppModule_ProvidePreferenceManagerFactory(AppModule module,
      Provider<Context> contextProvider) {
    this.module = module;
    this.contextProvider = contextProvider;
  }

  @Override
  public PreferenceManager get() {
    return providePreferenceManager(module, contextProvider.get());
  }

  public static AppModule_ProvidePreferenceManagerFactory create(AppModule module,
      Provider<Context> contextProvider) {
    return new AppModule_ProvidePreferenceManagerFactory(module, contextProvider);
  }

  public static PreferenceManager providePreferenceManager(AppModule instance, Context context) {
    return Preconditions.checkNotNullFromProvides(instance.providePreferenceManager(context));
  }
}
