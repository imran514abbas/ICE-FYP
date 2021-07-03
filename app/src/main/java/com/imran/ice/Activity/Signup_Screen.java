package com.imran.ice.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.imran.ice.ProgressbarLoader;
import com.imran.ice.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Signup_Screen extends AppCompatActivity {

    EditText fname, phone, eemail, ppassword;
    Button signupbtn;
    TextView signtolog, signanim;
    FirebaseAuth firebaseAuth;
    ProgressbarLoader loader;
    Animation animation;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        fname = findViewById(R.id.edittext_firstname);
        phone = findViewById(R.id.edittext_lastname);
        eemail = findViewById(R.id.edittext_signemail);
        ppassword = findViewById(R.id.edittext_signpassword);
        signupbtn = findViewById(R.id.signup_button);
        signtolog = findViewById(R.id.signtolog_txt);
        signanim = findViewById(R.id.faded_signup_text);

        //set animation
        animation = AnimationUtils.loadAnimation(Signup_Screen.this, R.anim.fade_anim);
        animation.setDuration(1000);
        signanim.setAnimation(animation);

        loader = new ProgressbarLoader(Signup_Screen.this);
        firebaseAuth = FirebaseAuth.getInstance();

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsound();
                signuplistner();
            }
        });

        signtolog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsound();
                signintologinlistener();
            }
        });
    }

    private void signintologinlistener() {

        startActivity(new Intent(Signup_Screen.this, Login_Screen.class));
        finish();
    }

    private void signuplistner() {


        String first_name = fname.getText().toString().trim();
        String phone_number = phone.getText().toString().trim();
        String email = eemail.getText().toString().trim();
        String password = ppassword.getText().toString().trim();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        final String strdate = dateFormat.format(date);
        if (first_name.isEmpty()) {
            fname.setError("Name is required");
            fname.requestFocus();
            return;

        }
        if (first_name.isEmpty()) {
            fname.setError("Name is required");
            fname.requestFocus();
            return;

        }
        if (phone_number.isEmpty()) {
            phone.setError(" Phone No is required");
            phone.requestFocus();
            return;

        }
        if (!Patterns.PHONE.matcher(phone_number).matches()) {
            phone.setError(" Phone No is required");
            phone.requestFocus();
            return;

        }
        if (password.isEmpty()) {
            ppassword.setError("Password is required");
            ppassword.requestFocus();
            return;

        }
        if (email.isEmpty()) {
            eemail.setError("Email is required");
            eemail.requestFocus();
            return;

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            eemail.setError("Email is not valid ");
            eemail.requestFocus();
            return;

        }
        if (password.length() < 6) {
            ppassword.setError("Minimum Password length should be 6 characters");
            ppassword.requestFocus();
            return;

        }
        loader.showloader();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userid = firebaseAuth.getCurrentUser().getUid();
                            User user = new User(userid, first_name, phone_number, email, strdate, generatecode(), 0, 0);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                        loader.dismissloader();
                                        Intent intent = new Intent(getApplicationContext(), Login_Screen.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failed to Register ! Try again ", Toast.LENGTH_SHORT).show();
                                        loader.dismissloader();

                                    }
                                }
                            });


                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to Register ! Try again ", Toast.LENGTH_SHORT).show();
                            loader.dismissloader();


                        }
                    }
                });
    }


    private String generatecode() {
        Random r = new Random();
        int intcode = 100000 + r.nextInt(900000);
        String code = String.valueOf(intcode);
        return code;
    }
    public void buttonsound() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sample);
        mediaPlayer.start();
    }
}
