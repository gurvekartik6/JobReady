package com.jobread.app.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {

    private final FirebaseAuth mAuth;
    private final MutableLiveData<AuthState> mAuthStateLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> mErrorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mLoadingLiveData = new MutableLiveData<>(false);

    public AuthViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public LiveData<AuthState> getAuthState() { return mAuthStateLiveData; }
    public LiveData<String> getError() { return mErrorLiveData; }
    public LiveData<Boolean> getLoading() { return mLoadingLiveData; }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void login(String email, String password) {
        if (!validateInput(email, password)) return;

        mLoadingLiveData.setValue(true);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    mLoadingLiveData.setValue(false);
                    mAuthStateLiveData.setValue(AuthState.LOGGED_IN);
                })
                .addOnFailureListener(e -> {
                    mLoadingLiveData.setValue(false);
                    mErrorLiveData.setValue(e.getMessage());
                });
    }

    public void register(String email, String password) {
        if (!validateInput(email, password)) return;

        mLoadingLiveData.setValue(true);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    mLoadingLiveData.setValue(false);
                    mAuthStateLiveData.setValue(AuthState.REGISTERED);
                })
                .addOnFailureListener(e -> {
                    mLoadingLiveData.setValue(false);
                    mErrorLiveData.setValue(e.getMessage());
                });
    }

    public void logout() {
        mAuth.signOut();
        mAuthStateLiveData.setValue(AuthState.LOGGED_OUT);
    }

    private boolean validateInput(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            mErrorLiveData.setValue("Email is required");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mErrorLiveData.setValue("Invalid email format");
            return false;
        }
        if (password == null || password.length() < 6) {
            mErrorLiveData.setValue("Password must be at least 6 characters");
            return false;
        }
        return true;
    }

    public enum AuthState {
        LOGGED_IN, LOGGED_OUT, REGISTERED
    }
}
