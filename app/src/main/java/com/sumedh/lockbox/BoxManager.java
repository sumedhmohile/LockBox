package com.sumedh.lockbox;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;

public class BoxManager {

    static String TAG = "BoxManager";

    public static Box createBox(final User owner, String name, final List<Uri> fileList, CheckInFrequency checkInFrequency, final Context context) {
        final Box box = new Box(owner.getUsername(), name, checkInFrequency);
        List<String> strings = new ArrayList<>();
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
                                ProgressBarManager.dismissProgressBar();
                                Toast.makeText(context, context.getResources().getString(R.string.add_box_success), Toast.LENGTH_LONG).show();
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

    public static void deleteBox(User owner, final String boxId) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query deleteQuery = database.child(Constants.BOXES).child(owner.getUserId()).orderByChild(Constants.BOX_ID).equalTo(boxId);

        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    snapshot.getRef().child(boxId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Log.i(TAG, "Deleted box " + boxId + " successfully");
                            }
                        }
                    });
                }
                else {
                    Log.e(TAG, "No such box");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Deletion cancelled");
            }
        });
    }

    public static void loadBoxesForUser(User user, final View view) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final List<Box> boxes = new ArrayList<>();
        Query userBoxesQuery = database.child(Constants.BOXES).child(user.getUserId()).orderByChild(Constants.USERNAME);

        userBoxesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()) {
                    boxes.add(snap.getValue(Box.class));
                }
                ListView listView = view.findViewById(R.id.my_boxes_screen_listview);
                BoxListAdapter boxListAdapter = new BoxListAdapter(view.getContext(), boxes);
                listView.setAdapter(boxListAdapter);
                ProgressBarManager.dismissProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "Error: " + error);
                ProgressBarManager.dismissProgressBar();
            }
        });
    }
}
