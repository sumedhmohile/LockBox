package com.sumedh.lockbox;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class FirebaseHandlerService extends FirebaseMessagingService {
    private final String TAG = "FirebaseHandlerService";

    @Override
    public void onNewToken(String token) {
        Log.i(TAG, "Refreshed token: " + token);
        AccountManager.handleFCMTokenRefresh(getApplicationContext(), token);
    }

}
