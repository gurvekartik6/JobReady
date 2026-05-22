package com.jobread.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jobread.app.JobReadApplication;
import com.jobread.app.R;
import com.jobread.app.utils.PreferenceManager;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 1500L;

    @Inject
    PreferenceManager mPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((JobReadApplication) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(this::checkAuthAndNavigate, SPLASH_DELAY_MS);
    }

    private void checkAuthAndNavigate() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String savedUserId = mPreferenceManager.getUserId();

        if (currentUser != null && savedUserId != null) {
            navigateToMain();
        } else {
            navigateToAuth();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToAuth() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
