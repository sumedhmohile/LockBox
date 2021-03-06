package com.sumedh.lockbox;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MyBoxesFragment extends Fragment {

    public final String TAG = "MyBoxesFragment";

    private User user;

    public MyBoxesFragment() {
        // Required empty public constructor
    }

    public static MyBoxesFragment newInstance(User user) {
        MyBoxesFragment fragment = new MyBoxesFragment();
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
        final View view = inflater.inflate(R.layout.fragment_my_boxes, container, false);

        ProgressBarManager.showProgressBar(getResources().getString(R.string.loading_boxes), getFragmentManager());
        BoxManager.loadBoxesForUser(user, view, getFragmentManager());

        Toolbar toolbar = view.findViewById(R.id.my_boxes_toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getContext(), R.drawable.toobar_more));
        setHasOptionsMenu(true);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        FloatingActionButton addBoxButton = view.findViewById(R.id.add_box_fab);
        addBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBoxFragment addBoxFragment = AddBoxFragment.newInstance(user);
                addBoxFragment.show(getFragmentManager(), "AddBoxFragment");
            }
        });

        SwipeRefreshLayout myBoxesLayout = view.findViewById(R.id.my_boxes_layout);
        myBoxesLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        myBoxesLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BoxManager.loadBoxesForUser(user, view, getFragmentManager());
            }
        });

        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logout_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == (R.id.toolbar_logout)) {
            AccountManager.logout(getActivity());
        }
        return true;
    }
}