package com.sumedh.lockbox;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;

public class AccountManager {
    private static final String TAG = "AccountManager";

    public static void registerUser(String email, String password, final Activity activity) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            Log.i(TAG, "Successfully registered User " + firebaseUser.getEmail());
                        }
                        else {
                            Log.e(TAG, "Registration failed: " + task.getException());
                            Toast.makeText(activity.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                        ProgressBarManager.dismissProgressBar();
                    }
                });
    }
}
