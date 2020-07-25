package com.sumedh.lockbox;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class BoxManager {

    static String TAG = "BoxManager";

    public static Box createBox(final User owner, String name, final List<Uri> fileList, CheckInFrequency checkInFrequency, final Context context) {
        final Box box = new Box(owner.getUsername(), name, checkInFrequency, owner.getUserId());
        ArrayList<String> strings = new ArrayList<>();
        for(Uri uri : fileList) {
            strings.add(uri.toString());
        }
        box.setFiles(strings);
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child(Constants.BOXES).child(owner.getUserId()).child(box.getBoxId()).setValue(box).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.i(TAG, "Successfully added box");
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference().child(Constants.BOXES).child(owner.getUserId()).child(box.getBoxId());
                    final ArrayList<String> fileUrls = new ArrayList<>();
                    for(final Uri fileUri : fileList) {
                        UploadTask uploadTask = storageRef.child(UUID.randomUUID().toString()).putFile(fileUri);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Upload failed for file: " + fileUri);
                                database.child(Constants.BOXES).child(owner.getUserId()).child(box.getBoxId()).removeValue();
                                ProgressBarManager.dismissProgressBar();
                                Toast.makeText(context, context.getResources().getString(R.string.add_box_failed), Toast.LENGTH_LONG).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.i(TAG, "Uploaded file successfully " + fileUri);
                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        fileUrls.add(uri.toString());
                                        if(fileUrls.size() == box.getFiles().size()) {
                                            box.setFiles(fileUrls);
                                            database.child(Constants.BOXES).child(owner.getUserId()).child(box.getBoxId()).child(Constants.FILES_LIST).setValue(box.getFiles()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.i(TAG, "Updated box");
                                                    ProgressBarManager.dismissProgressBar();
                                                    Toast.makeText(context, context.getResources().getString(R.string.add_box_success), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }

                }
                else {
                    Log.e(TAG, "Error when adding box " + task.getException());
                    ProgressBarManager.dismissProgressBar();
                    Toast.makeText(context, context.getResources().getString(R.string.add_box_failed), Toast.LENGTH_LONG).show();
                }
            }
        });
        return box;
    }

    public static void deleteBox(final Box box) {
        final String boxId = box.getBoxId();

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(Constants.BOXES).child(box.getOwnerId()).child(box.getBoxId());
        Log.i(TAG, "Location: " + storageRef.getPath());

        storageRef.listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                for (StorageReference ref : task.getResult().getItems()) {
                    Log.i(TAG, "Going to delete: " + ref.getPath());
                    ref.delete();
                }
            }
        });

        Query deleteQuery = database.child(Constants.BOXES).child(box.getOwnerId()).orderByChild(Constants.BOX_ID).equalTo(boxId);

        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    snapshot.getRef().child(boxId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i(TAG, "Deleted box " + boxId + " successfully");
                                ProgressBarManager.dismissProgressBar();
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "No such box");
                    ProgressBarManager.dismissProgressBar();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Deletion cancelled");
                ProgressBarManager.dismissProgressBar();
            }
        });
    }

    public static void loadBoxesForUser(User user, final View view, final FragmentManager fragmentManager) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final List<Box> boxes = new ArrayList<>();
        Query userBoxesQuery = database.child(Constants.BOXES).child(user.getUserId());

        userBoxesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()) {
                    boxes.add(snap.getValue(Box.class));
                }
                ListView listView = view.findViewById(R.id.my_boxes_screen_listview);
                MutableBoxListAdapter boxListAdapter = new MutableBoxListAdapter(view.getContext(), boxes, fragmentManager);
                listView.setAdapter(boxListAdapter);
                ProgressBarManager.dismissProgressBar();
                if (view.findViewById(R.id.my_boxes_layout) != null) {
                    SwipeRefreshLayout layout = view.findViewById(R.id.my_boxes_layout);
                    layout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "Error: " + error);
                ProgressBarManager.dismissProgressBar();
            }
        });
    }

    public static void checkinBox(Box box, final Context context) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("lastCheckInDate", new Date());
        updateMap.put(Constants.LOCK_STATUS, LockStatusType.Locked);

        database.child(Constants.BOXES).child(box.getOwnerId()).child(box.getBoxId()).updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.i(TAG, "Updated checkin");
                    ProgressBarManager.dismissProgressBar();
                    Toast.makeText(context, context.getResources().getString(R.string.checkin_box_success), Toast.LENGTH_LONG).show();
                }
                else {
                    Log.i(TAG, "Update checkin failed: " + task.getException());
                    ProgressBarManager.dismissProgressBar();
                    Toast.makeText(context, context.getResources().getString(R.string.checkin_box_failed), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static void loadPendingBoxesForUser(User user, final View view, final FragmentManager fragmentManager) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final List<Box> boxes = new ArrayList<>();
        Query userBoxesQuery = database.child(Constants.BOXES).child(user.getUserId());

        userBoxesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()) {
                    Box box = snap.getValue(Box.class);
                    if(box.getLockStatus().equals(LockStatusType.Warning)) {
                        boxes.add(box);
                    }
                }
                ListView listView = view.findViewById(R.id.pending_boxes_screen_listview);
                MutableBoxListAdapter boxListAdapter = new MutableBoxListAdapter(view.getContext(), boxes, fragmentManager);
                listView.setAdapter(boxListAdapter);
                ProgressBarManager.dismissProgressBar();
                if (view.findViewById(R.id.pending_boxes_layout) != null) {
                    SwipeRefreshLayout layout = view.findViewById(R.id.pending_boxes_layout);
                    layout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "Error: " + error);
                ProgressBarManager.dismissProgressBar();
            }
        });
    }
}
