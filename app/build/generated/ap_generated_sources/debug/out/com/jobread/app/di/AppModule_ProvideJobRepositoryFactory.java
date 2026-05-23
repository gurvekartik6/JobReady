package com.jobread.app.di;

import com.jobread.app.database.dao.JobDao;
import com.jobread.app.repositories.JobRepository;
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
public final class AppModule_ProvideJobRepositoryFactory implements Factory<JobRepository> {
  private final AppModule module;

  private final Provider<JobDao> jobDaoProvider;

  public AppModule_ProvideJobRepositoryFactory(AppModule module, Provider<JobDao> jobDaoProvider) {
    this.module = module;
    this.jobDaoProvider = jobDaoProvider;
  }

  @Override
  public JobRepository get() {
    return provideJobRepository(module, jobDaoProvider.get());
  }

  public static AppModule_ProvideJobRepositoryFactory create(AppModule module,
      Provider<JobDao> jobDaoProvider) {
    return new AppModule_ProvideJobRepositoryFactory(module, jobDaoProvider);
  }

  public static JobRepository provideJobRepository(AppModule instance, JobDao jobDao) {
    return Preconditions.checkNotNullFromProvides(instance.provideJobRepository(jobDao));
  }
}
