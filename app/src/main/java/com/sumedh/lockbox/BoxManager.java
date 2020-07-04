package com.sumedh.lockbox;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;

public class BoxManager {

    static String TAG = "BoxManager";

    public static Box createBox(User owner, String name, CheckInFrequency checkInFrequency) {
        final Box box = new Box(owner.getUsername(), name, checkInFrequency);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("boxes").child(owner.getUserId()).setValue(box).addOnCompleteListener(new OnCompleteListener<Void>() {
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
}
