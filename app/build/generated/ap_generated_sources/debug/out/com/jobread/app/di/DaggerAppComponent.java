package com.jobread.app.di;

import android.content.Context;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.jobread.app.activities.AddEditJobActivity;
import com.jobread.app.activities.AddEditJobActivity_MembersInjector;
import com.jobread.app.activities.AuthActivity;
import com.jobread.app.activities.AuthActivity_MembersInjector;
import com.jobread.app.activities.JobDetailActivity;
import com.jobread.app.activities.JobDetailActivity_MembersInjector;
import com.jobread.app.activities.MainActivity;
import com.jobread.app.activities.MainActivity_MembersInjector;
import com.jobread.app.activities.SettingsActivity;
import com.jobread.app.activities.SettingsActivity_MembersInjector;
import com.jobread.app.activities.SplashActivity;
import com.jobread.app.activities.SplashActivity_MembersInjector;
import com.jobread.app.database.AppDatabase;
import com.jobread.app.database.dao.JobDao;
import com.jobread.app.fragments.DashboardFragment;
import com.jobread.app.fragments.DashboardFragment_MembersInjector;
import com.jobread.app.fragments.JobListFragment;
import com.jobread.app.fragments.JobListFragment_MembersInjector;
import com.jobread.app.repositories.JobRepository;
import com.jobread.app.utils.PreferenceManager;
import com.jobread.app.workers.FollowUpNotificationWorker;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import javax.annotation.processing.Generated;

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
public final class DaggerAppComponent {
  private DaggerAppComponent() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private AppModule appModule;

    private Builder() {
    }

    public Builder appModule(AppModule appModule) {
      this.appModule = Preconditions.checkNotNull(appModule);
      return this;
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder networkModule(NetworkModule networkModule) {
      Preconditions.checkNotNull(networkModule);
      return this;
    }

    public AppComponent build() {
      Preconditions.checkBuilderRequirement(appModule, AppModule.class);
      return new AppComponentImpl(appModule);
    }
  }

  private static final class AppComponentImpl implements AppComponent {
    private final AppComponentImpl appComponentImpl = this;

    private Provider<Context> provideContextProvider;

    private Provider<PreferenceManager> providePreferenceManagerProvider;

    private Provider<AppDatabase> provideDatabaseProvider;

    private Provider<JobDao> provideJobDaoProvider;

    private Provider<JobRepository> provideJobRepositoryProvider;

    private Provider<ViewModelFactory> provideViewModelFactoryProvider;

    private AppComponentImpl(AppModule appModuleParam) {

      initialize(appModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final AppModule appModuleParam) {
      this.provideContextProvider = DoubleCheck.provider(AppModule_ProvideContextFactory.create(appModuleParam));
      this.providePreferenceManagerProvider = DoubleCheck.provider(AppModule_ProvidePreferenceManagerFactory.create(appModuleParam, provideContextProvider));
      this.provideDatabaseProvider = DoubleCheck.provider(AppModule_ProvideDatabaseFactory.create(appModuleParam, provideContextProvider));
      this.provideJobDaoProvider = DoubleCheck.provider(AppModule_ProvideJobDaoFactory.create(appModuleParam, provideDatabaseProvider));
      this.provideJobRepositoryProvider = DoubleCheck.provider(AppModule_ProvideJobRepositoryFactory.create(appModuleParam, provideJobDaoProvider));
      this.provideViewModelFactoryProvider = DoubleCheck.provider(AppModule_ProvideViewModelFactoryFactory.create(appModuleParam, provideJobRepositoryProvider, providePreferenceManagerProvider));
    }

    @Override
    public void inject(SplashActivity activity) {
      injectSplashActivity(activity);
    }

    @Override
    public void inject(AuthActivity activity) {
      injectAuthActivity(activity);
    }

    @Override
    public void inject(MainActivity activity) {
      injectMainActivity(activity);
    }

    @Override
    public void inject(AddEditJobActivity activity) {
      injectAddEditJobActivity(activity);
    }

    @Override
    public void inject(JobDetailActivity activity) {
      injectJobDetailActivity(activity);
    }

    @Override
    public void inject(SettingsActivity activity) {
      injectSettingsActivity(activity);
    }

    @Override
    public void inject(DashboardFragment fragment) {
      injectDashboardFragment(fragment);
    }

    @Override
    public void inject(JobListFragment fragment) {
      injectJobListFragment(fragment);
    }

    @Override
    public void inject(FollowUpNotificationWorker worker) {
    }

    @Override
    public ViewModelFactory viewModelFactory() {
      return provideViewModelFactoryProvider.get();
    }

    @CanIgnoreReturnValue
    private SplashActivity injectSplashActivity(SplashActivity instance) {
      SplashActivity_MembersInjector.injectMPreferenceManager(instance, providePreferenceManagerProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private AuthActivity injectAuthActivity(AuthActivity instance) {
      AuthActivity_MembersInjector.injectMPreferenceManager(instance, providePreferenceManagerProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity(MainActivity instance) {
      MainActivity_MembersInjector.injectMPreferenceManager(instance, providePreferenceManagerProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private AddEditJobActivity injectAddEditJobActivity(AddEditJobActivity instance) {
      AddEditJobActivity_MembersInjector.injectMViewModelFactory(instance, provideViewModelFactoryProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private JobDetailActivity injectJobDetailActivity(JobDetailActivity instance) {
      JobDetailActivity_MembersInjector.injectMViewModelFactory(instance, provideViewModelFactoryProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private SettingsActivity injectSettingsActivity(SettingsActivity instance) {
      SettingsActivity_MembersInjector.injectMPreferenceManager(instance, providePreferenceManagerProvider.get());
      SettingsActivity_MembersInjector.injectMJobRepository(instance, provideJobRepositoryProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private DashboardFragment injectDashboardFragment(DashboardFragment instance) {
      DashboardFragment_MembersInjector.injectMViewModelFactory(instance, provideViewModelFactoryProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private JobListFragment injectJobListFragment(JobListFragment instance) {
      JobListFragment_MembersInjector.injectMViewModelFactory(instance, provideViewModelFactoryProvider.get());
      return instance;
    }
  }
}
