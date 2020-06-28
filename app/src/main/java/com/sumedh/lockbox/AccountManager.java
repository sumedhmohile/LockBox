package com.sumedh.lockbox;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class AccountManager {
    private static final String TAG = "AccountManager";

    public static void registerUser(final User user, final String password, final Activity activity) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query userNameQuery = database.child("users").orderByChild("username").equalTo(user.getUsername());

        userNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    Log.i(TAG, "Username does not exist, registering user");
                    firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                Log.i(TAG, "Successfully registered in firebase auth User " + firebaseUser.getEmail());
                                user.setUserId(firebaseUser.getUid());
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                                database.child("users").child(user.getUserId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                        Log.i(TAG, "Successfully registered User " + user.toString());
                                        ProgressBarManager.dismissProgressBar();
                                    }
                                });
                            } else {
                                Log.e(TAG, "Registration failed in firebase: " + task.getException());
                                Toast.makeText(activity.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                ProgressBarManager.dismissProgressBar();
                            }
                        }
                    });
                }
                else {
                    Log.e(TAG, "Username exists");
                    ProgressBarManager.dismissProgressBar();
                    Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.username_exists_error), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "Username fetch cancelled");
                ProgressBarManager.dismissProgressBar();
            }
        });

    }
}
