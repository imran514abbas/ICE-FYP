package com.imran.ice.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.imran.ice.R;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyLocation extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng latLng;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String current_uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);


        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        current_uid = firebaseUser.getUid();

        callpermissionlistener();
        update_location();

    }


    private void update_location() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
            fusedLocationProviderClient = new FusedLocationProviderClient(this);
            locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(5000) //update interval
                    .setFastestInterval(5000); //fastest interval
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        mMap.clear();
                        final double lat = locationResult.getLastLocation().getLatitude();
                        final double log = locationResult.getLastLocation().getLongitude();
                        latLng = new LatLng(lat, log);
                        mMap.addMarker(new MarkerOptions().position(latLng).title("You Are Here"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


                        //update latitude and longitude
                        Map<String, Object> update = new HashMap<>();
                        update.put("latitude", lat);
                        update.put("longitude", log);
                        databaseReference.child(current_uid).updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               
                            }
                        });
                    } else {
                        Toast.makeText(MyLocation.this, "location not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }, getMainLooper());
        } else {
            callpermissionlistener();
        }
    }

    private void callpermissionlistener() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        String rationale = "Please Provide Location Permission ";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Location Permission")
                .setSettingsDialogTitle("Warrning");

        Permissions.check(this, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.Gmap);
                mapFragment.getMapAsync(MyLocation.this);
                update_location();
                locationEnabled();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                callpermissionlistener();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
            new AlertDialog.Builder(MyLocation.this)
                    .setTitle("Enable GPS Service")
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

}

