package com.sumedh.lockbox;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UnlockedBoxesFragment extends Fragment {

    private User user;
    public UnlockedBoxesFragment() {
        // Required empty public constructor
    }

    public static UnlockedBoxesFragment newInstance(User user) {
        UnlockedBoxesFragment fragment = new UnlockedBoxesFragment();
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
        final View view = inflater.inflate(R.layout.fragment_unlocked_boxes, container, false);

        ProgressBarManager.showProgressBar(getResources().getString(R.string.loading_unlocked_boxes), getFragmentManager());
        BoxManager.loadPublicBoxes(view, getFragmentManager());

        SwipeRefreshLayout unlockedBoxesLayout = view.findViewById(R.id.unlocked_boxes_layout);
        unlockedBoxesLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        unlockedBoxesLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BoxManager.loadPublicBoxes(view, getFragmentManager());
            }
        });

        return view;
    }
}