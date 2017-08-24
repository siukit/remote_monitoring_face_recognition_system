package com.siukit.raspconn;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Firebase_MSG";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //if the app was opened when the notification was being sent, then go to ViewIntruderActivity automatically
        Intent streamIntent = new Intent(MyFirebaseMessagingService.this, ViewIntrudersActivity.class);
        startActivity(streamIntent);


        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }



    }

}
