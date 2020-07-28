package com.sumedh.lockbox;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


public class BoxViewFragment extends Fragment {

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
        args.putStringArrayList(Constants.FILES_LIST, box.getFiles());
        args.putString(Constants.LOCK_STATUS, box.getLockStatus().name());

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
            ArrayList filesList = getArguments().getStringArrayList(Constants.FILES_LIST);
            LockStatusType lockStatusType = LockStatusType.valueOf(getArguments().getString(Constants.LOCK_STATUS));

            box = new Box(ownerName, name, CheckInFrequency.valueOf(checkinFrequency), ownerId);
            box.setCreationDate(new Date(getArguments().getLong(Constants.BOX_CREATION_DATE)));
            box.setLastCheckInDate(new Date(getArguments().getLong(Constants.BOX_CHECKIN_DATE)));
            box.setBoxId(boxId);
            box.setFiles(filesList);
            box.setLockStatus(lockStatusType);
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
        ConstraintLayout boxHeaderLayout = view.findViewById(R.id.box_viewer_header);
        final ViewPager2 imageViewPager = view.findViewById(R.id.box_viewer_image_pager);

        boxNameTextView.setText(box.getName());
        ownerNameTextView.setText(box.getOwnerName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        boxCreationDateTextView.setText(sdf.format(box.getCreationDate()));
        boxCheckinDateTextView.setText(sdf.format(box.getLastCheckInDate()));

        if(box.getLockStatus().equals(LockStatusType.Locked)) {
            boxHeaderLayout.setBackgroundResource(R.drawable.locked_gradient);
        } else if (box.getLockStatus().equals(LockStatusType.Warning)) {
            boxHeaderLayout.setBackgroundResource(R.drawable.warning_gradient);
        } else {
            boxHeaderLayout.setBackgroundResource(R.drawable.unlocked_gradient);
        }

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        ImagePagerAdapter adapter = new ImagePagerAdapter(getContext(), box, new ArrayList<Uri>(), getFragmentManager());
        imageViewPager.setAdapter(adapter);

        database.child(Constants.BOXES).child(box.getOwnerId()).child(box.getBoxId()).child(Constants.FILES_LIST).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                final List<Uri> files = new ArrayList<>();
                while(iterator.hasNext()) {
                    String url = iterator.next().getValue().toString();
                    StorageReference ref = storage.getReferenceFromUrl(url);
                    final File file;
                    try {
                        file = File.createTempFile(UUID.randomUUID().toString(), ".jpg");
                        ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                files.add(Uri.fromFile(file));
                                if (files.size() == box.getFiles().size()) {
                                    ImagePagerAdapter adapter = new ImagePagerAdapter(getContext(), box, files, getFragmentManager());
                                    imageViewPager.setAdapter(adapter);
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}