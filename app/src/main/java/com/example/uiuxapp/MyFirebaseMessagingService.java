package com.example.uiuxapp;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingService";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // Log the new token
        Log.d(TAG, "Refreshed token: " + token);

        // Send the new token to your server
        sendTokenToServer(token);
    }

    private void sendTokenToServer(String deviceToken) {
        // Implement your server request logic here
    }
}
