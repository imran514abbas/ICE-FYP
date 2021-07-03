package com.imran.ice.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.imran.ice.R;

public class ResetPassword extends AppCompatActivity {
    private EditText email;
    private Button reset;
    FirebaseAuth firebaseAuth;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        email = findViewById(R.id.edittext_reset);
        reset = findViewById(R.id.Reset_button);
        firebaseAuth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsound();
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String Email = email.getText().toString().trim();
        if (Email.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError("Please Provide Valid Email");
            email.requestFocus();
            return;

        }
        firebaseAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Check Your Email to reset password", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Try Again ! Something wrong", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void buttonsound() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sample);
        mediaPlayer.start();
    }
}