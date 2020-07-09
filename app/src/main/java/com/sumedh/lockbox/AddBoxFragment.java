package com.sumedh.lockbox;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddBoxFragment extends DialogFragment {

    private final String TAG = "AddBoxFragment";
    private List<Uri> files = new ArrayList<>();
    private User user;


    public AddBoxFragment() {
        // Required empty public constructor
    }

    public static AddBoxFragment newInstance(User user) {
        AddBoxFragment fragment = new AddBoxFragment();
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
            user = new User(getArguments().getString(Constants.USERNAME), getArguments().getString(Constants.EMAIL));
            user.setUserId(getArguments().getString(Constants.USER_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_box, container, false);

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button addFilesButton = view.findViewById(R.id.add_boxes_file_button);
        Button addBoxButton = view.findViewById(R.id.add_boxes_button);
        final TextInputEditText boxNameEditText = view.findViewById(R.id.add_box_name_text);
        final Spinner frequencySpinner = view.findViewById(R.id.add_box_frequency_spinner);


        addFilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, 1);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.frequency_items, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(adapter);

        addBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validator.validateNotEmpty(boxNameEditText, getContext()) && Validator.validateLength(boxNameEditText, Constants.TEXT_FIELD_MIN_LENGTH, getContext())) {
                    if(files.size() > 0) {
                        Log.i(TAG, "User: " + user.toString());
                        ProgressBarManager.showProgressBar(getResources().getString(R.string.adding_box), getFragmentManager());
                        Box box = BoxManager.createBox(user, boxNameEditText.getText().toString(), files, CheckInFrequency.DAILY, getContext());
                        dismiss();
                    }
                    else {
                        Toast.makeText(getContext(), getResources().getString(R.string.not_enough_files), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == -1) {
                if(data.getData() != null) {
                    files.add(data.getData());
                }
                else if(data.getClipData() != null) {
                    for(int i = 0 ; i < data.getClipData().getItemCount() ; i++) {
                        files.add(data.getClipData().getItemAt(i).getUri());
                    }
                }
            }
        }
    }
}