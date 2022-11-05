package com.example.familyplanning;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG="NotificationBuilder";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG,"From: "+remoteMessage.getFrom());
        Log.d(TAG,"Notification title: "+ Objects.requireNonNull(remoteMessage.getNotification()).getTitle());
        Log.d(TAG,"Notification body: "+remoteMessage.getNotification().getBody());
        notifyUser(remoteMessage.getFrom(),remoteMessage.getNotification().getBody());
    }

    public void notifyUser(String from,String notification){
        MyNotificationManager myNotificationManager =new MyNotificationManager(getApplicationContext());
        myNotificationManager.showNotification(from,notification,new Intent(getApplicationContext(),MainActivity.class));
    }
}
