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
import com.google.firebase.auth.FirebaseUser;
import com.imran.ice.ProgressbarLoader;
import com.imran.ice.R;

public class Login_Screen extends AppCompatActivity {

    EditText logemail, logpass;
    Button loginbtn;
    TextView txtsign, slide_logtxt;
    FirebaseAuth firebaseAuth;
    ProgressbarLoader loader;
    FirebaseUser firebaseUser;
    Animation animation;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        logemail = findViewById(R.id.edittext_email);
        logpass = findViewById(R.id.edittext_password);
        loginbtn = findViewById(R.id.login_button);
        txtsign = findViewById(R.id.logtosign);
        slide_logtxt = findViewById(R.id.slide_login_text);

        //set animation
        animation = AnimationUtils.loadAnimation(Login_Screen.this, R.anim.fade_anim);
        animation.setDuration(1000);
        slide_logtxt.setAnimation(animation);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //initilize progressbar
        loader = new ProgressbarLoader(Login_Screen.this);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsound();
                loader.showloader();
                loginlistner();
                loader.dismissloader();
            }
        });

        txtsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsound();
                txtsignuplistener();
            }
        });

    }

    private void txtsignuplistener() {

        Intent intent = new Intent(Login_Screen.this, Signup_Screen.class);
        startActivity(intent);
    }

    private void loginlistner() {
        String email = logemail.getText().toString().trim();
        String password = logpass.getText().toString().trim();
        if (password.isEmpty()) {
            logpass.setError("Password is required");
            logpass.requestFocus();
            return;

        }
        if (email.isEmpty()) {
            logemail.setError("Email is required");
            logemail.requestFocus();
            return;

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            logemail.setError("Email is not valid ");
            logemail.requestFocus();
            return;

        }
        if (password.length() < 6) {
            logpass.setError("Minimum Password length should be 6 characters");
            logpass.requestFocus();
            return;

        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user.isEmailVerified()) {
                                    loader.dismissloader();
                                    Toast.makeText(Login_Screen.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), ShowCircleCode.class);
                                    startActivity(intent);
                                } else {
                                    user.sendEmailVerification();
                                    Toast.makeText(Login_Screen.this, "Check your Email to varify your account..!", Toast.LENGTH_SHORT).show();
                                    loader.dismissloader();
                                }
                            } else {

                                loader.dismissloader();
                                Toast.makeText(Login_Screen.this, "failed to login! Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    @Override
    protected void onStart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null && user.isEmailVerified()) {
            startActivity(new Intent(Login_Screen.this, Home.class));
            finish();
        }
        super.onStart();
    }

    public void ResetPassword(View view) {
        buttonsound();
        Intent intent = new Intent(getApplicationContext(), ResetPassword.class);
        startActivity(intent);
    }

    public void buttonsound() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sample);
        mediaPlayer.start();
    }
}
