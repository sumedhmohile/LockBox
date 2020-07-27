package com.sumedh.lockbox;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String userId = preferences.getString(Constants.USER_ID, Constants.NONE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.fcm_fallback_notification_channel_label);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        if(Constants.NONE.equals(userId)) {
            LoginFragment loginFragment = LoginFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, loginFragment)
                    .commit();
        }
        else {
            User user = new User(preferences.getString(Constants.USERNAME, ""), preferences.getString(Constants.EMAIL, ""));
            user.setUserId(userId);
            LandingFragment landingFragment = LandingFragment.newInstance(user);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, landingFragment)
                    .commit();
        }
    }
}
