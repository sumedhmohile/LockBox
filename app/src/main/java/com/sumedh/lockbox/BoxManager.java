package com.sumedh.lockbox;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class BoxManager {

    static String TAG = "BoxManager";

    public static Box createBox(final User owner, String name, CheckInFrequency checkInFrequency) {
        final Box box = new Box(owner.getUsername(), name, checkInFrequency);
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child(Constants.BOXES).child(owner.getUserId()).child(box.getBoxId()).setValue(box).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.i(TAG, "Successfully added box");
                }
                else {
                    Log.e(TAG, "Error when adding box " + task.getException());
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
}
