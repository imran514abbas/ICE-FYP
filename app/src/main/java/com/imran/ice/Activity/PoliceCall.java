package com.imran.ice.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.imran.ice.R;

public class PoliceCall extends AppCompatActivity {
    public static final int RINGER_MODE_SILENT = 0;
    Button bt_silent;
    Button PoliceCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_call);
        PoliceCall = findViewById(R.id.policecall);
        bt_silent = findViewById(R.id.bt_silent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }

        bt_silent.setOnClickListener(new View.OnClickListener() {

            final AudioManager aManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            @Override
            public void onClick(View v) {
                aManager.setRingerMode(RINGER_MODE_SILENT);
                Toast.makeText(getApplicationContext(), "Your Mobile is on Silent Now", Toast.LENGTH_SHORT).show();
            }
        });
        PoliceCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneNumber();
            }
        });
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNumber();
            }
        }
    }

    private void callPhoneNumber() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PoliceCall.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "03015536392"));
                startActivity(callIntent);

            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "03015536392"));
                startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}