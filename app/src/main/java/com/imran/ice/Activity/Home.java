package com.imran.ice.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.imran.ice.Fragments.InviteCodeFragment;
import com.imran.ice.Fragments.JoinCircleFragment;
import com.imran.ice.Fragments.MyCircleFragment;
import com.imran.ice.Fragments.ProfileFagment;
import com.imran.ice.R;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity implements LocationListener, NavigationView.OnNavigationItemSelectedListener {
    TextView Text_location, LatitudeText, LongitudeText;
    private Button vLocation;
    CircleImageView Profile_Picture;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    TextView header_name, header_email;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String current_uid;
    MediaPlayer mediaPlayer;
    public LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getLocation();
        drawerLayout = findViewById(R.id.drawerlayout);
        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nev_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        Profile_Picture = header.findViewById(R.id.profile_image);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        locationEnabled();
        LatitudeText = findViewById(R.id.text_ll);
        LongitudeText = findViewById(R.id.text_lll);
        Text_location = findViewById(R.id.text_adress);
        vLocation = findViewById(R.id.location);
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {

            }
        });
        vLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsound();
                locationEnabled();
                getLocation();
                Intent intent = new Intent(getApplicationContext(), MyLocation.class);
                startActivity(intent);

            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        current_uid = firebaseUser.getUid();


        dynamicheaderlistener();
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, Home.this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatitudeText.setText(String.valueOf(location.getLatitude()));
        LatitudeText.setVisibility(View.VISIBLE);
        LongitudeText.setText(String.valueOf(location.getLongitude()));
        LongitudeText.setVisibility(View.VISIBLE);

        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            Text_location.setText(address);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void dynamicheaderlistener() {

        View header = navigationView.getHeaderView(0);

        header_name = header.findViewById(R.id.header_name_text);
        header_email = header.findViewById(R.id.header_email_text);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String current_name = dataSnapshot.child(current_uid).child("first_name").getValue(String.class);
                String current_email = dataSnapshot.child(current_uid).child("email").getValue(String.class);

                String s1 = current_name;
                header_name.setText(s1);
                header_email.setText(current_email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nev_home:
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                break;
            case R.id.nev_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.emergency,
                        new ProfileFagment()).commit();
                break;
            case R.id.nev_joiningc:
                getSupportFragmentManager().beginTransaction().replace(R.id.emergency,
                        new JoinCircleFragment()).commit();
                break;
            case R.id.nev_invite:
                getSupportFragmentManager().beginTransaction().replace(R.id.emergency,
                        new InviteCodeFragment()).commit();
                break;
            case R.id.nev_mycircle:
                getSupportFragmentManager().beginTransaction().replace(R.id.emergency,
                        new MyCircleFragment()).commit();
                break;
            case R.id.nev_Exit:
                this.finishAffinity();
                break;
            case R.id.nev_logout:

                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    firebaseAuth.signOut();
                    startActivity(new Intent(Home.this, Login_Screen.class));
                    finish();
                }
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(Home.this)
                    .setTitle("Enable GPS Service ")
                    .setMessage("We need your GPS location")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        super.onBackPressed();

    }


    public void LifeSavingRules(View view) {
        buttonsound();
        Intent intent = new Intent(Home.this, LifeSavingRules.class);
        startActivity(intent);
    }

    public void buttonsound() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sample);
        mediaPlayer.start();
    }


    public void SendTHome(View view) {
        buttonsound();
        getSupportFragmentManager().beginTransaction().replace(R.id.emergency,
                new MyCircleFragment()).commit();

    }

    public void SendToPolice(View view) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Police Emergency");

        buttonsound();
        String latitude = LatitudeText.getText().toString();
        databaseReference.child("PLatitude").setValue(latitude);
        String longitude = LongitudeText.getText().toString();
        databaseReference.child("PLongitude").setValue(longitude);
        String address = Text_location.getText().toString();
        databaseReference.child("Paddress").setValue(address);
        databaseReference.child("PStopEmergency").setValue("Please Help me I need Your Help");
        if (ActivityCompat.checkSelfPermission(Home.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            sendMessage();
        } else {
            ActivityCompat.requestPermissions(Home.this,
                    new String[]{Manifest.permission.SEND_SMS}
                    , 100);
        }
        Intent intent = new Intent(getApplicationContext(), PoliceCall.class);
        startActivity(intent);

    }

    public void SendToFirebrigade(View view) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Firebrigade Emergency");

        buttonsound();
        String latitude = LatitudeText.getText().toString();
        databaseReference.child("FLatitude").setValue(latitude);
        String longitude = LongitudeText.getText().toString();
        databaseReference.child("FLongitude").setValue(longitude);
        String address = Text_location.getText().toString();
        databaseReference.child("Faddress").setValue(address);
        databaseReference.child("FStopEmergency").setValue("Please Help me I need Your Help");
        if (ActivityCompat.checkSelfPermission(Home.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            sendMessage();
        } else {
            ActivityCompat.requestPermissions(Home.this,
                    new String[]{Manifest.permission.SEND_SMS}
                    , 100);
        }

        Intent intent = new Intent(getApplicationContext(), Firebrigade.class);
        startActivity(intent);
    }

    public void SendToResque(View view) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Resque Emergency");

        buttonsound();
        String latitude = LatitudeText.getText().toString();
        databaseReference.child("RLatitude").setValue(latitude);
        String longitude = LongitudeText.getText().toString();
        databaseReference.child("RLongitude").setValue(longitude);
        String address = Text_location.getText().toString();
        databaseReference.child("Raddress").setValue(address);
        databaseReference.child("RStopEmergency").setValue("Please Help me I need Your Help");

        if (ActivityCompat.checkSelfPermission(Home.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            sendMessage();
        } else {
            ActivityCompat.requestPermissions(Home.this,
                    new String[]{Manifest.permission.SEND_SMS}
                    , 100);
        }

        Intent intent = new Intent(getApplicationContext(), Resque1122.class);
        startActivity(intent);
    }

    private void sendMessage() {
        String s1 = Text_location.getText().toString();
        String s2 = LatitudeText.getText().toString();
        String s3 = LongitudeText.getText().toString();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String sPhone = "03015536392";
            String sMessage = "Please Help me I am in Emergency" + "\n" + "Address :" + s1 + "\n" + "Latitude :" + s2 + "\n" + "Longitude :" + s3;
            if (!s2.isEmpty() && !s3.isEmpty() && !sPhone.isEmpty() && !sMessage.isEmpty()) {
                smsManager.sendTextMessage(sPhone, null, sMessage, null, null);
                Toast.makeText(getApplicationContext(), "Location and SMS Has Been Sent", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(),
                        "SMS faild, and Location has been not sent please try again otherwise Call ",
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void StopEmergency(View view) {
        buttonsound();
        Intent intent = new Intent(getApplicationContext(), StopEmergency.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("upload");
        StorageReference profileRef = storageReference.child("USERS/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(Profile_Picture);
            }
        });
    }
}
