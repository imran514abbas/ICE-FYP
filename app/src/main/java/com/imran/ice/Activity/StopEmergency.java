package com.imran.ice.Activity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.imran.ice.R;

public class StopEmergency extends AppCompatActivity {
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_emergency);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

    }

    public void StopPolice(View view) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Police Emergency");
        databaseReference.child("PStopEmergency").setValue("I am Ok No need of Your Help");
        sendMessage();
        Toast.makeText(getApplicationContext(), "Emergency has been stoped", Toast.LENGTH_SHORT).show();
    }

    private void sendMessage() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String sPhone = "03015536392";
            String sMessage = "I am ok no Need of help thank you..!";

            smsManager.sendTextMessage(sPhone, null, sMessage, null, null);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void StopFirebrigade(View view) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Firebrigade Emergency");
        databaseReference.child("FStopEmergency").setValue("I am Ok No need of Your Help");
        sendMessage();
        Toast.makeText(getApplicationContext(), "Emergency has been stoped", Toast.LENGTH_SHORT).show();
    }

    public void StopResque(View view) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Resque Emergency");
        databaseReference.child("RStopEmergency").setValue("I am Ok No need of Your Help");
        sendMessage();
        Toast.makeText(getApplicationContext(), "Emergency has been stoped", Toast.LENGTH_SHORT).show();
    }
}