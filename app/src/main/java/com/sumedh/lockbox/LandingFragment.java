package com.sumedh.lockbox;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LandingFragment extends Fragment {

    private final String TAG = "LandingFragment";

    private User user;

    public LandingFragment() {
        // Required empty public constructor
    }

    public static LandingFragment newInstance(User user) {
        LandingFragment fragment = new LandingFragment();
        Bundle args = new Bundle();
        args.putString(Constants.USERNAME, user.getUsername());
        args.putString(Constants.EMAIL, user.getEmail());
        args.putString(Constants.USER_ID, user.getUserId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String username = getArguments().getString(Constants.USERNAME);
            String email = getArguments().getString(Constants.EMAIL);
            String userId = getArguments().getString(Constants.USER_ID);

            user = new User(username, email);
            user.setUserId(userId);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing, container, false);


        getFragmentManager().beginTransaction()
                .replace(R.id.landing_screen_container, MyBoxesFragment.newInstance(user))
                .commit();

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.my_boxes_navigation_button: getFragmentManager().beginTransaction()
                                                            .replace(R.id.landing_screen_container, MyBoxesFragment.newInstance(user))
                                                            .commit(); break;
                    case R.id.recently_added_navigation_button: getFragmentManager().beginTransaction()
                                                                .replace(R.id.landing_screen_container, PendingBoxesFragment.newInstance(user))
                                                                .commit(); break;
                    case R.id.popular_boxes_navigation_button: Log.i(TAG, "POP"); break;
                }
                return true;
            }
        });

        return view;
    }
}