package com.sumedh.lockbox;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class FirebaseHandlerService extends FirebaseMessagingService {
    private final String TAG = "FirebaseHandlerService";

    @Override
    public void onNewToken(@NonNull String token) {
        Log.i(TAG, "Refreshed token: " + token);
        AccountManager.handleFCMTokenRefresh(getApplicationContext(), token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.fcm_fallback_notification_channel_label))
                .setSmallIcon(R.drawable.app_logo_small)
                .setContentTitle(remoteMessage.getData().get(Constants.NOTIFICATION_MAP_KEY))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(1, builder.build());
    }

}
