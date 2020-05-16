package com.example.downloadapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class BroadcastListener extends BroadcastReceiver {
    private static final int ID = 112;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("DOWNLOAD_COMPLETE")){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Download Complete")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
            notificationManager.notify( ID ,builder.build());
        }

    }
}
