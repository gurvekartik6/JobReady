package com.jobread.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferenceManager {

    private static final String PREFS_NAME = "jobread_secure_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_THEME_MODE = "theme_mode";
    private static final String KEY_NOTIFICATION_HOUR = "notification_hour";
    private static final String KEY_NOTIFICATION_MINUTE = "notification_minute";
    private static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";

    private static final int DEFAULT_NOTIFICATION_HOUR = 9;
    private static final int DEFAULT_NOTIFICATION_MINUTE = 0;

    private SharedPreferences mPreferences;
    private final Context mContext;

    @Inject
    public PreferenceManager(Context context) {
        this.mContext = context;
        initPreferences();
    }

    private void initPreferences() {
        try {
            MasterKey masterKey = new MasterKey.Builder(mContext)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            mPreferences = EncryptedSharedPreferences.create(
                    mContext,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            // Fallback to regular shared preferences if encryption fails
            mPreferences = mContext.getSharedPreferences(PREFS_NAME + "_fallback", Context.MODE_PRIVATE);
        }
    }

    public void saveUserId(String userId) {
        mPreferences.edit().putString(KEY_USER_ID, userId).apply();
    }

    public String getUserId() {
        return mPreferences.getString(KEY_USER_ID, null);
    }

    public void saveUserEmail(String email) {
        mPreferences.edit().putString(KEY_USER_EMAIL, email).apply();
    }

    public String getUserEmail() {
        return mPreferences.getString(KEY_USER_EMAIL, null);
    }

    public void clearUser() {
        mPreferences.edit()
                .remove(KEY_USER_ID)
                .remove(KEY_USER_EMAIL)
                .apply();
    }

    public boolean isLoggedIn() {
        return getUserId() != null && !getUserId().isEmpty();
    }

    public void setThemeMode(int mode) {
        mPreferences.edit().putInt(KEY_THEME_MODE, mode).apply();
    }

    public int getThemeMode() {
        // AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM = -1
        return mPreferences.getInt(KEY_THEME_MODE, -1);
    }

    public void setNotificationTime(int hour, int minute) {
        mPreferences.edit()
                .putInt(KEY_NOTIFICATION_HOUR, hour)
                .putInt(KEY_NOTIFICATION_MINUTE, minute)
                .apply();
    }

    public int getNotificationHour() {
        return mPreferences.getInt(KEY_NOTIFICATION_HOUR, DEFAULT_NOTIFICATION_HOUR);
    }

    public int getNotificationMinute() {
        return mPreferences.getInt(KEY_NOTIFICATION_MINUTE, DEFAULT_NOTIFICATION_MINUTE);
    }

    public void setNotificationsEnabled(boolean enabled) {
        mPreferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply();
    }

    public boolean areNotificationsEnabled() {
        return mPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true);
    }
}
