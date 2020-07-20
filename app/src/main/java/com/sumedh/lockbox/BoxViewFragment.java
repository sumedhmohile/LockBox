package com.sumedh.lockbox;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class BoxViewFragment extends DialogFragment {

    private final String TAG = "BoxViewFragment";
    Box box;


    public BoxViewFragment() {
        // Required empty public constructor
    }


    public static BoxViewFragment newInstance(Box box) {
        BoxViewFragment fragment = new BoxViewFragment();
        Bundle args = new Bundle();
        args.putString(Constants.BOX_ID, box.getBoxId());
        args.putString(Constants.BOX_NAME, box.getName());
        args.putString(Constants.BOX_OWNER_NAME, box.getOwnerName());
        args.putString(Constants.BOX_OWNER_ID, box.getOwnerId());
        args.putLong(Constants.BOX_CHECKIN_DATE, box.getLastCheckInDate().getTime());
        args.putLong(Constants.BOX_CREATION_DATE, box.getCreationDate().getTime());
        args.putString(Constants.BOX_CHECKING_FREQUENCY, box.getCheckInFrequency().name());

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String ownerName = getArguments().getString(Constants.BOX_OWNER_NAME);
            String name = getArguments().getString(Constants.BOX_NAME);
            String checkinFrequency = getArguments().getString(Constants.BOX_CHECKING_FREQUENCY);
            String ownerId = getArguments().getString(Constants.BOX_OWNER_ID);
            String boxId = getArguments().getString(Constants.BOX_ID);

            box = new Box(ownerName, name, CheckInFrequency.valueOf(checkinFrequency), ownerId);
            box.setCreationDate(new Date(getArguments().getLong(Constants.BOX_CREATION_DATE)));
            box.setLastCheckInDate(new Date(getArguments().getLong(Constants.BOX_CHECKIN_DATE)));
            box.setBoxId(boxId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_box_view, container, false);

        TextView boxNameTextView = view.findViewById(R.id.box_viewer_box_name);
        TextView ownerNameTextView = view.findViewById(R.id.box_viewer_owner_name);
        TextView boxCreationDateTextView = view.findViewById(R.id.box_viewer_created_date);
        TextView boxCheckinDateTextView = view.findViewById(R.id.box_viewer_checkindate);
        ViewPager2 imageViewPager = view.findViewById(R.id.box_viewer_image_pager);

        boxNameTextView.setText(box.getName());
        ownerNameTextView.setText(box.getOwnerName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        boxCreationDateTextView.setText(sdf.format(box.getCreationDate()));
        boxCheckinDateTextView.setText(sdf.format(box.getLastCheckInDate()));



        return view;
    }
}