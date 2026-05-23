package com.jobread.app.repositories;

import com.jobread.app.database.dao.JobDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class JobRepository_Factory implements Factory<JobRepository> {
  private final Provider<JobDao> jobDaoProvider;

  public JobRepository_Factory(Provider<JobDao> jobDaoProvider) {
    this.jobDaoProvider = jobDaoProvider;
  }

  @Override
  public JobRepository get() {
    return newInstance(jobDaoProvider.get());
  }

  public static JobRepository_Factory create(Provider<JobDao> jobDaoProvider) {
    return new JobRepository_Factory(jobDaoProvider);
  }

  public static JobRepository newInstance(JobDao jobDao) {
    return new JobRepository(jobDao);
  }
}
