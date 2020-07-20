package com.sumedh.lockbox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

public class MutableBoxListAdapter extends BoxListAdapter {

    private static final String TAG = "MutableBoxListAdapter";

    public MutableBoxListAdapter(Context context, List<Box> boxes, FragmentManager fragmentManager) {
        super(context, boxes, fragmentManager);
    }

    @Override
    protected void setUpActions(View view, final Box box) {
        CardView boxCard = view.findViewById(R.id.box_card);
        boxCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(layoutInflater.getContext());
                builder.setMessage("Box Action").setPositiveButton("Check In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "Going to check in");
                        AlertDialog.Builder confirmationDialogBuilder = new AlertDialog.Builder(layoutInflater.getContext());
                        confirmationDialogBuilder.setMessage("Are you sure?").setPositiveButton(layoutInflater.getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i(TAG, "Actually checking in");
                                ProgressBarManager.showProgressBar(layoutInflater.getContext().getResources().getString(R.string.checkin_box), fragmentManager);
                                BoxManager.checkinBox(box, layoutInflater.getContext());
                            }
                        }).setNegativeButton(layoutInflater.getContext().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i(TAG, "Not Checking in");
                            }
                        }).show();
                    }
                }).setNegativeButton("Delete Box", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "Going to delete");
                        AlertDialog.Builder confirmationDialogBuilder = new AlertDialog.Builder(layoutInflater.getContext());
                        confirmationDialogBuilder.setMessage("Are you sure?").setPositiveButton(layoutInflater.getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i(TAG, "Actually deleting");
                                ProgressBarManager.showProgressBar(layoutInflater.getContext().getResources().getString(R.string.deleting_box), fragmentManager);
                                BoxManager.deleteBox(box);
                            }
                        }).setNegativeButton(layoutInflater.getContext().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i(TAG, "Not deleting");
                            }
                        }).show();
                    }
                }).show();
                return true;
            }
        });

    }
}
