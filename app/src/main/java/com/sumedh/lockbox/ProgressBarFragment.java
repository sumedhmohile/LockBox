package com.sumedh.lockbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressBarFragment extends DialogFragment {

    private static final String MESSAGE = "message";

    private String message;

    public ProgressBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param message
     * @return A new instance of fragment ProgressBarFragment.
     */
    public static ProgressBarFragment newInstance(String message) {
        ProgressBarFragment fragment = new ProgressBarFragment();
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString(MESSAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_progress_bar, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_progress_bar, null);
        alertDialogBuilder.setView(view);

        TextView progressbarText = view.findViewById(R.id.progress_bar_text);


        progressbarText.setText(message);

        return alertDialogBuilder.create();
    }
}