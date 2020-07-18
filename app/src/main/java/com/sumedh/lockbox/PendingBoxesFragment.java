package com.sumedh.lockbox;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PendingBoxesFragment extends Fragment {

    private User user;

    public PendingBoxesFragment() {
        // Required empty public constructor
    }


    public static PendingBoxesFragment newInstance(User user) {
        PendingBoxesFragment fragment = new PendingBoxesFragment();
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
        final View view = inflater.inflate(R.layout.fragment_pending_boxes, container, false);

        ProgressBarManager.showProgressBar(getResources().getString(R.string.loading_pending_boxes), getFragmentManager());
        BoxManager.loadPendingBoxesForUser(user, view, getFragmentManager());

        SwipeRefreshLayout myBoxesLayout = view.findViewById(R.id.pending_boxes_layout);
        myBoxesLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BoxManager.loadPendingBoxesForUser(user, view, getFragmentManager());
            }
        });

        return view;
    }
}