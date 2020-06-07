package com.sumedh.lockbox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;


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
                Log.d(TAG, "Done with fragment wait");
            }
        };

        timer.schedule(splashScreenWaitTask, Constants.SPLASH_SCREEN_DURATION);

        return view;
    }

}
