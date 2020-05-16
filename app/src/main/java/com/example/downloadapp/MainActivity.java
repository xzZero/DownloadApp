package com.example.downloadapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Button button, button2;
    private EditText editText;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (shouldAskPermissions()) {
            askPermissions();
        }
        button = (Button)findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        editText = findViewById(R.id.txtInput);
        intent = new Intent(getApplication(), DownloadService.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //intent.putExtra("URL", editText.getText().toString());
                intent.putExtra("URL", "http://ipv4.download.thinkbroadband.com/512MB.zip");
                startService(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent != null){
                    stopService(intent);
                }
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @TargetApi(23)

    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
}
