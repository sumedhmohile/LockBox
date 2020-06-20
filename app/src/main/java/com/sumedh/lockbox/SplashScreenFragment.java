package com.sumedh.lockbox;

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
                RegisterFragment registerFragment = new RegisterFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, registerFragment)
                        .commit();
            }
        };

        timer.schedule(splashScreenWaitTask, Constants.SPLASH_SCREEN_DURATION);

        return view;
    }

}
