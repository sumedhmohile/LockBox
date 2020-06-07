package com.sumedh.lockbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SplashScreenFragment splashScreenFragment = new SplashScreenFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, splashScreenFragment)
                .commit();
    }
}
