package com.sumedh.lockbox;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LandingFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing, container, false);


        getFragmentManager().beginTransaction()
                .replace(R.id.landing_screen_container, MyBoxesFragment.newInstance(user))
                .commit();
        return view;
    }
}