package com.siukit.raspconn;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by siukit on 25/03/2017.
 */

public class MyFirebaseInstanceIDService  extends FirebaseInstanceIdService {

    private static final String TAG = "Firebase_MSG";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}