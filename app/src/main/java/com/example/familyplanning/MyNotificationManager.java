package com.example.familyplanning;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class MyNotificationManager {
    private final Context ctx;
    public static  final int NOTIFICATION_ID=234;
    public MyNotificationManager(Context ctx){
        this.ctx=ctx;

    }
    public void showNotification(String from, String notification, Intent intent){
        String NOTIFICATION_CHANNEL_ID = "mychannel12";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent pendingIntent=PendingIntent.getActivity(
                    ctx,
                    NOTIFICATION_ID,
                    intent,
                   PendingIntent.FLAG_MUTABLE
            );
            NotificationCompat.Builder builder=new NotificationCompat.Builder(ctx, NOTIFICATION_CHANNEL_ID);
            Notification mNotification=builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(from)
                    .setContentText(notification)
                    .build();
            mNotification.flags|=Notification.FLAG_AUTO_CANCEL;
            NotificationManager notificationManager=(NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID,mNotification);
        }
        else{
            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent=PendingIntent.getActivity(
                    ctx,
                    NOTIFICATION_ID,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            NotificationCompat.Builder builder=new NotificationCompat.Builder(ctx, NOTIFICATION_CHANNEL_ID);
            Notification mNotification=builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(from)
                    .setContentText(notification)
                    .build();
            mNotification.flags|=Notification.FLAG_AUTO_CANCEL;
            NotificationManager notificationManager=(NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID,mNotification);
        }


    }
}
