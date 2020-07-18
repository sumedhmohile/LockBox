package com.sumedh.lockbox;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class SplashScreenFragment extends Fragment {
    String TAG = "SplashScreenFragment";

    public SplashScreenFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        Log.d(TAG, "Starting SplashScreenFragment");

        Timer timer = new Timer();
        TimerTask splashScreenWaitTask = new TimerTask() {
            @Override
            public void run() {

                SharedPreferences preferences = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
                String userId = preferences.getString(Constants.USER_ID, "NONE");


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = getString(R.string.fcm_fallback_notification_channel_label);
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel channel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance);
                        NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                    }

                if(userId.equalsIgnoreCase("NONE")) {
                    LoginFragment loginFragment = LoginFragment.newInstance();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, loginFragment)
                            .commit();
                }
                else {
                    User user = new User(preferences.getString(Constants.USERNAME, ""), preferences.getString(Constants.EMAIL, ""));
                    user.setUserId(userId);
                    LandingFragment landingFragment = LandingFragment.newInstance(user);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, landingFragment)
                            .commit();
                }
            }
        };

        timer.schedule(splashScreenWaitTask, Constants.SPLASH_SCREEN_DURATION);

        return view;
    }

}
