package com.imran.ice.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.imran.ice.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity {
    Button UpdateName, UpdatePhone;
    EditText edtName, edtNumber;
    CircleImageView DP;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        UpdateName = findViewById(R.id.updatename);
        UpdatePhone = findViewById(R.id.updateNumber);
        edtName = findViewById(R.id.Edit_Name);
        edtNumber = findViewById(R.id.Edit_Number);
        DP = findViewById(R.id.dp);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String getuid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(getuid);
        UpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = edtName.getText().toString();
                if (Name.isEmpty()) {
                    edtName.setError("Enter Name");
                    edtName.requestFocus();
                    return;
                } else {

                    databaseReference.child("first_name").setValue(Name);
                    Toast.makeText(getApplicationContext(), "Name Updated Sucessfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        UpdatePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Phone = edtNumber.getText().toString();
                if (Phone.isEmpty()) {
                    edtNumber.setError("Enter Number");
                    edtNumber.requestFocus();
                    return;
                } else {

                    databaseReference.child("phone_number").setValue(Phone);
                    Toast.makeText(getApplicationContext(), "Phone Number Updated Sucessfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
                Picasso.get().load(uri).into(DP);
            }
        });
    }
}

