package com.jobread.app.utils;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.jobread.app.workers.FollowUpNotificationWorker;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class WorkManagerUtils {

    private static final String WORK_TAG_FOLLOW_UP = "follow_up_notification";

    public static void scheduleFollowUpNotification(Context context, int hour, int minute) {
        long initialDelay = calculateInitialDelay(hour, minute);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                FollowUpNotificationWorker.class,
                24, TimeUnit.HOURS
        )
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .addTag(WORK_TAG_FOLLOW_UP)
        .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_TAG_FOLLOW_UP,
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
        );
    }

    public static void cancelFollowUpNotification(Context context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG_FOLLOW_UP);
    }

    private static long calculateInitialDelay(int hour, int minute) {
        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        target.set(Calendar.HOUR_OF_DAY, hour);
        target.set(Calendar.MINUTE, minute);
        target.set(Calendar.SECOND, 0);
        target.set(Calendar.MILLISECOND, 0);

        if (target.before(now)) {
            target.add(Calendar.DAY_OF_YEAR, 1);
        }

        return target.getTimeInMillis() - now.getTimeInMillis();
    }
}
