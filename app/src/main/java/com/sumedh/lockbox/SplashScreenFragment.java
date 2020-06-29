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
                String username = preferences.getString(Constants.USER_ID, "NONE");

                if(username.equalsIgnoreCase("NONE")) {
                    LoginFragment loginFragment = LoginFragment.newInstance();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, loginFragment)
                            .commit();
                }
                else {
                    LandingFragment landingFragment = LandingFragment.newInstance();
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
