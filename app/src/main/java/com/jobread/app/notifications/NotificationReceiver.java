package com.jobread.app.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jobread.app.utils.PreferenceManager;
import com.jobread.app.utils.WorkManagerUtils;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            PreferenceManager prefs = new PreferenceManager(context);
            if (prefs.areNotificationsEnabled() && prefs.isLoggedIn()) {
                WorkManagerUtils.scheduleFollowUpNotification(
                        context,
                        prefs.getNotificationHour(),
                        prefs.getNotificationMinute()
                );
            }
        }
    }
}
