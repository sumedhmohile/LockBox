package com.sumedh.lockbox;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BoxImageFragment extends DialogFragment {

    private Uri imageUri;

    public BoxImageFragment() {
        // Required empty public constructor
    }

    public static BoxImageFragment newInstance(Uri imageUri) {
        BoxImageFragment fragment = new BoxImageFragment();
        Bundle args = new Bundle();
        args.putString(Constants.IMAGE_URI, imageUri.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.imageUri = Uri.parse(getArguments().getString(Constants.IMAGE_URI));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_box_image, container, false);

        ImageView imageView = view.findViewById(R.id.box_image_full_screen);

        imageView.setImageURI(imageUri);

        return view;
    }
}