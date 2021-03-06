package com.sumedh.lockbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class AccountManager {
    private static final String TAG = "AccountManager";

    public static void registerUser(final User user, final String password, final FragmentActivity activity) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query userNameQuery = database.child(Constants.USERS).orderByChild(Constants.USERNAME).equalTo(user.getUsername());

        SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        if(StringUtils.isNotBlank(sharedPreferences.getString(Constants.FCM_TOKEN, Constants.BLANK_SPACE))) {
            Log.i(TAG, "Setting FCM token before login");
            user.setFcmToken(sharedPreferences.getString(Constants.FCM_TOKEN, Constants.BLANK_SPACE));
        }

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
                                database.child(Constants.USERS).child(user.getUserId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.i(TAG, "Successfully registered User " + user.toString());
                                        saveUserToSharedPreference(user, activity);
                                        ProgressBarManager.dismissProgressBar();
                                        Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                        routeToLanding(activity, user);
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

    public static void loginUser(final User user, final String password, final FragmentActivity activity) {
        Log.i(TAG, "Attempting login for user: " + user.toString());

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        if(StringUtils.isNotBlank(sharedPreferences.getString(Constants.FCM_TOKEN, Constants.BLANK_SPACE))) {
            Log.i(TAG, "Setting FCM token before login");
            user.setFcmToken(sharedPreferences.getString(Constants.FCM_TOKEN, Constants.BLANK_SPACE));
        }

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query userNameQuery = database.child(Constants.USERS).orderByChild(Constants.USERNAME).equalTo(user.getUsername());
        userNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    Log.e(TAG, "Invalid username");
                    Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show();
                    ProgressBarManager.dismissProgressBar();
                }
                else {
                    Log.i(TAG, "Results of username fetch: " + snapshot.getValue());
                    HashMap userArray = (HashMap) snapshot.getValue();
                    try {
                        Map.Entry<String, Map> data = (Map.Entry<String, Map>) userArray.entrySet().iterator().next();
                        String email = (String) data.getValue().get(Constants.EMAIL);

                        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.i(TAG, "Successfully logged in");
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    user.setUserId(firebaseUser.getUid());
                                    database.child(Constants.USERS).child(user.getUserId()).child(Constants.FCM_TOKEN).setValue(user.getFcmToken());
                                    saveUserToSharedPreference(user, activity);
                                    Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.login_success), Toast.LENGTH_LONG).show();
                                    routeToLanding(activity, user);
                                }
                                else {
                                    Log.e(TAG, "Error when logging in. Exception: " + task.getException());
                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.generic_error), Toast.LENGTH_LONG).show();
                                    }
                                }
                                ProgressBarManager.dismissProgressBar();
                            }
                        });

                    } catch (Exception e) {
                        Log.e(TAG, "Error when parsing username response: " + Arrays.toString(e.getStackTrace()));
                        ProgressBarManager.dismissProgressBar();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ProgressBarManager.dismissProgressBar();
            }
        });
    }

    private static void saveUserToSharedPreference(User user, Activity activity) {
        SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USERNAME, user.getUsername());
        editor.putString(Constants.EMAIL, user.getEmail());
        editor.putString(Constants.USER_ID, user.getUserId());
        editor.apply();
    }

    private static void routeToLanding(FragmentActivity activity, User user) {
        Log.i(TAG, "Routing to landing screen");
        LandingFragment landingFragment = LandingFragment.newInstance(user);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, landingFragment)
                .commit();
    }

    public static void handleFCMTokenRefresh(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString(Constants.USER_ID, Constants.BLANK_SPACE);
        if(StringUtils.isNotBlank(userId)) {
            final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            database.child(Constants.USERS).child(userId).child(Constants.FCM_TOKEN).setValue(token);
        }
        else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.FCM_TOKEN, token);
            editor.apply();
        }
    }

    public static void logout(Activity activity) {
        Log.i(TAG, "Logging out");
        SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear()
              .commit();

        Intent intent = new Intent(activity, SplashScreenActivity.class);
        activity.startActivity(intent);
        activity.finish();

    }
}
