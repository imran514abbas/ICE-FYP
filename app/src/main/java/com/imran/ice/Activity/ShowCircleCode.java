package com.imran.ice.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imran.ice.R;

public class ShowCircleCode extends AppCompatActivity {

    TextView showid, done;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser fuser;
    Button send;
    String currentcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcirclecode);

        showid = findViewById(R.id.txtcircle_id);
        done = findViewById(R.id.button_done);
        send = findViewById(R.id.sendbutton);

        firebaseAuth = FirebaseAuth.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        String current = fuser.getUid();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendbuttonlistener();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentcode = dataSnapshot.child("code").getValue(String.class);
                showid.setText(currentcode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donetohomelistener();
            }
        });
    }

    private void sendbuttonlistener() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String sharebody = "circle code: " + currentcode;
        String sharesub = "Women Safe invitation code";
        intent.putExtra(Intent.EXTRA_SUBJECT, sharesub);
        intent.putExtra(Intent.EXTRA_TEXT, sharebody);
        startActivity(Intent.createChooser(intent, "sharing code"));
    }

    private void donetohomelistener() {

        startActivity(new Intent(ShowCircleCode.this, Home.class));
        finish();

    }
}
