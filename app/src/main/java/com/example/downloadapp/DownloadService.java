package com.example.downloadapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DownloadService extends Service {
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;
    private Context mContext;
    Download download;

    private final class ServiceHandler extends Handler{
        public ServiceHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg){
                download = new Download(mContext);
                download.execute(msg.obj.toString());

        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
        super.onCreate();
        mContext = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent.getStringExtra("URL");


        serviceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        if (download.getStatus() == AsyncTask.Status.RUNNING){
            download.cancel(true);
            Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        }
    }
}
