package com.example.downloadapp;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download extends AsyncTask<String, String, String> {
    BroadcastListener broadcastListener;
    private Context context;
    IntentFilter intentFilter;

    public Download(Context context) {

        this.context = context;
        broadcastListener = new BroadcastListener();
        intentFilter = new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE");
        context.registerReceiver(broadcastListener, intentFilter);
    }

    @Override
    protected String doInBackground(String... sUrl) {
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        try {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream("/sdcard/Download/" + stringSlicing(url.toString()));


                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled())
                        return null;
                    total += count;
                    // publishing the progress....
                    output.write(data, 0, count);
                }

            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                }
                catch (IOException ignored) { }
                if (connection != null)
                    connection.disconnect();
            }
        } finally {
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            Log.e("DOWNLOAD ERR", result);
        }
        else{
            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("DOWNLOAD_COMPLETE");
            context.sendBroadcast(intent);
        }


    }

    private String stringSlicing(String url){
        return url.substring(url.lastIndexOf("/", url.length()));
    }

}