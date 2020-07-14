package com.sumedh.lockbox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Period;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class BoxListAdapter extends BaseAdapter {

    private List<Box> boxes;
    private LayoutInflater layoutInflater;
    private String TAG = "BoxListAdapter";
    private FragmentManager fragmentManager;

    public BoxListAdapter(Context context, List<Box> boxes, FragmentManager fragmentManager) {
        this.boxes = boxes;
        this.layoutInflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return boxes.size();
    }

    @Override
    public Object getItem(int position) {
        return boxes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.box_card, null);

            TextView boxNameTextView = convertView.findViewById(R.id.box_name_text);
            TextView boxOwnerTextView = convertView.findViewById(R.id.box_owner);
            TextView boxCheckinFrequencyTextView = convertView.findViewById(R.id.box_checkin_frequency);
            TextView boxFileCountTextView = convertView.findViewById(R.id.box_file_count);
            TextView boxLastCheckIn = convertView.findViewById(R.id.box_last_checkin);
            TextView boxCheckinDeadline = convertView.findViewById(R.id.checkin_deadline);
            TextView boxFileLabel = convertView.findViewById(R.id.file_label);
            CardView boxCard = convertView.findViewById(R.id.box_card);

            final Box box = boxes.get(position);

            boxNameTextView.setText(box.getName());
            boxOwnerTextView.setText(box.getOwnerName());
            boxCheckinFrequencyTextView.setText(box.getCheckInFrequency().name());
            boxFileCountTextView.setText(String.format("%d", box.getFiles().size()));

            if(box.getFiles().size() > 1) {
                boxFileLabel.setText(layoutInflater.getContext().getResources().getString(R.string.files));
            }
            else {
                boxFileLabel.setText(layoutInflater.getContext().getResources().getString(R.string.file));
            }


            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            boxLastCheckIn.setText(sdf.format(box.getLastCheckInDate()));

            boxCheckinDeadline.setText(TimeUnit.HOURS.convert(box.getUnlockDate().getTime() - box.getLastCheckInDate().getTime(), TimeUnit.MILLISECONDS) + "");

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
        return convertView;
    }
}
