package com.jobread.app.workers;

import android.app.Notification;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;

import com.jobread.app.database.AppDatabase;
import com.jobread.app.database.dao.JobDao;
import com.jobread.app.utils.DateUtils;
import com.jobread.app.utils.NotificationUtils;
import com.jobread.app.utils.PreferenceManager;

import io.reactivex.rxjava3.core.Single;

public class FollowUpNotificationWorker extends RxWorker {

    private final PreferenceManager mPreferenceManager;
    private final JobDao mJobDao;

    public FollowUpNotificationWorker(@NonNull Context context,
                                       @NonNull WorkerParameters params) {
        super(context, params);
        mPreferenceManager = new PreferenceManager(context);
        mJobDao = AppDatabase.getInstance(context).jobDao();
    }

    @NonNull
    @Override
    public Single<ListenableWorker.Result> createWork() {
        String userId = mPreferenceManager.getUserId();
        if (userId == null || !mPreferenceManager.areNotificationsEnabled()) {
            return Single.just(ListenableWorker.Result.success());
        }

        return mJobDao.getStalePendingJobs(userId, DateUtils.daysAgo(7))
                .map(staleJobs -> {
                    if (!staleJobs.isEmpty()) {
                        Notification notification = NotificationUtils.buildFollowUpNotification(
                                getApplicationContext(), staleJobs.size());
                        NotificationUtils.showNotification(
                                getApplicationContext(),
                                NotificationUtils.NOTIFICATION_ID_FOLLOW_UP,
                                notification
                        );
                    }
                    return ListenableWorker.Result.success();
                })
                .onErrorReturn(throwable -> ListenableWorker.Result.failure());
    }
}
