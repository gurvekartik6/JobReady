package com.jobread.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.jobread.app.JobReadApplication;
import com.jobread.app.databinding.ActivityAuthBinding;
import com.jobread.app.utils.PreferenceManager;
import com.jobread.app.viewmodels.AuthViewModel;

import javax.inject.Inject;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding mBinding;
    private AuthViewModel mViewModel;
    private boolean mIsLoginMode = true;

    @Inject
    PreferenceManager mPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((JobReadApplication) getApplication()).getAppComponent().inject(this);
        mBinding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        setupTabs();
        setupObservers();
        setupClickListeners();
    }

    private void setupTabs() {
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText("Login"));
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText("Register"));
        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mIsLoginMode = tab.getPosition() == 0;
                mBinding.btnAuth.setText(mIsLoginMode ? "Login" : "Create Account");
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupObservers() {
        mViewModel.getAuthState().observe(this, state -> {
            switch (state) {
                case LOGGED_IN:
                case REGISTERED:
                    saveUserAndNavigate();
                    break;
                case LOGGED_OUT:
                    break;
            }
        });

        mViewModel.getError().observe(this, error -> {
            if (error != null) {
                Snackbar.make(mBinding.getRoot(), error, Snackbar.LENGTH_LONG).show();
            }
        });

        mViewModel.getLoading().observe(this, loading -> {
            mBinding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            mBinding.btnAuth.setEnabled(!loading);
        });
    }

    private void setupClickListeners() {
        mBinding.btnAuth.setOnClickListener(v -> {
            String email = mBinding.etEmail.getText() != null ?
                    mBinding.etEmail.getText().toString().trim() : "";
            String password = mBinding.etPassword.getText() != null ?
                    mBinding.etPassword.getText().toString() : "";

            if (mIsLoginMode) {
                mViewModel.login(email, password);
            } else {
                mViewModel.register(email, password);
            }
        });
    }

    private void saveUserAndNavigate() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            mPreferenceManager.saveUserId(auth.getCurrentUser().getUid());
            mPreferenceManager.saveUserEmail(auth.getCurrentUser().getEmail());
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
