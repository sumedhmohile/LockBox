package com.sumedh.lockbox;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SplashScreenFragment splashScreenFragment = new SplashScreenFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, splashScreenFragment)
                .commit();
    }
}
