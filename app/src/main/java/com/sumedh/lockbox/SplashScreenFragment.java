package com.sumedh.lockbox;

import android.content.Context;
import android.content.SharedPreferences;
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
