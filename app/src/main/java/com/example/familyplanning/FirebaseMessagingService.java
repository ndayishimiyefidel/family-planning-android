package com.example.familyplanning;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("MyLog","From"+remoteMessage.getFrom());
        Log.d("MyLog","Notification title"+remoteMessage.getNotification().getTitle());
        Log.d("MyLog","Notification body"+remoteMessage.getNotification().getBody());
    }
}
