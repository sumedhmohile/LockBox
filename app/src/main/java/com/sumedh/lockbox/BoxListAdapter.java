package com.sumedh.lockbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

public class BoxListAdapter extends BaseAdapter {

    protected List<Box> boxes;
    protected LayoutInflater layoutInflater;
    private String TAG = "BoxListAdapter";
    protected FragmentManager fragmentManager;

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
            TextView boxunlockDate = convertView.findViewById(R.id.checkin_deadline);
            View cardBackground = convertView.findViewById(R.id.box_card_background);

            final Box box = boxes.get(position);

            boxNameTextView.setText(box.getName());
            boxOwnerTextView.setText(layoutInflater.getContext().getResources().getString(R.string.box_owner_name, box.getOwnerName()));
            boxCheckinFrequencyTextView.setText(layoutInflater.getContext().getResources().getString(R.string.box_checkin_frequency, box.getCheckInFrequency().name()));
            boxFileCountTextView.setText(layoutInflater.getContext().getResources().getQuantityString(R.plurals.file_label, box.getFiles().size(), String.format("%d", box.getFiles().size())));

            CardView boxCard = convertView.findViewById(R.id.box_card);

            boxCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BoxViewFragment boxViewFragment = BoxViewFragment.newInstance(box);
                    boxViewFragment.show(fragmentManager, "BoxViewFragment");
                }
            });


            if(box.getLockStatus().equals(LockStatusType.Warning)) {
                cardBackground.setBackgroundResource(R.drawable.warning_gradient);
            }
            else if(box.getLockStatus().equals(LockStatusType.Unlocked)) {
                cardBackground.setBackgroundResource(R.drawable.unlocked_gradient);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            boxLastCheckIn.setText(layoutInflater.getContext().getResources().getString(R.string.box_last_checkin_date, sdf.format(box.getLastCheckInDate())));
            boxunlockDate.setText(layoutInflater.getContext().getResources().getString(R.string.box_unlock_date, sdf.format(box.getUnlockDate())));

            setUpActions(convertView, box);

        }
        return convertView;
    }

    protected void setUpActions(View view, Box box) {
    }
}
