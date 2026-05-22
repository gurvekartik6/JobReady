package com.jobread.app.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.jobread.app.R;
import com.jobread.app.activities.MainActivity;

public class NotificationUtils {

    public static final String CHANNEL_ID_FOLLOW_UP = "follow_up_channel";
    public static final String CHANNEL_ID_REMINDERS = "reminders_channel";
    public static final int NOTIFICATION_ID_FOLLOW_UP = 1001;

    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager == null) return;

            NotificationChannel followUpChannel = new NotificationChannel(
                    CHANNEL_ID_FOLLOW_UP,
                    "Follow-up Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            followUpChannel.setDescription("Reminds you to follow up on pending applications");

            NotificationChannel remindersChannel = new NotificationChannel(
                    CHANNEL_ID_REMINDERS,
                    "Custom Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            remindersChannel.setDescription("Custom scheduled reminders");

            manager.createNotificationChannel(followUpChannel);
            manager.createNotificationChannel(remindersChannel);
        }
    }

    public static Notification buildFollowUpNotification(Context context, int pendingCount) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String message = "You have " + pendingCount +
                " pending application" + (pendingCount > 1 ? "s" : "") +
                ". Time to follow up?";

        return new NotificationCompat.Builder(context, CHANNEL_ID_FOLLOW_UP)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("JobRead Reminder")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
    }

    public static void showNotification(Context context, int id, Notification notification) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        try {
            manager.notify(id, notification);
        } catch (SecurityException e) {
            // POST_NOTIFICATIONS permission not granted
        }
    }
}
