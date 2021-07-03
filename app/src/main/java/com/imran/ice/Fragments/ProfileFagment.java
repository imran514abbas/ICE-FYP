
package com.imran.ice.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.imran.ice.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFagment extends Fragment {
    TextView txtfirst, txtlast, txtemail;
    CircleImageView DP;
    ImageButton UploadDP;
    Button UpdateProfile;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    StorageReference storageReference;
    private Uri imageUri;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        txtfirst = view.findViewById(R.id.firstname_textview);
        txtlast = view.findViewById(R.id.lastname_textview);
        txtemail = view.findViewById(R.id.email_textview);
        UpdateProfile = view.findViewById(R.id.update);
        DP = view.findViewById(R.id.dp);
        UploadDP = view.findViewById(R.id.upload_dp);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String getuid = firebaseUser.getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("upload");
        StorageReference profileRef = storageReference.child("USERS/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(DP);
            }
        });
        UploadDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1000);
            }
        });
        UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), com.imran.ice.Activity.UpdateProfile.class);
                startActivity(intent);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(getuid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstname = dataSnapshot.child("first_name").getValue(String.class);
                String lastname = dataSnapshot.child("phone_number").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);

                txtfirst.setText(firstname);
                txtlast.setText(lastname);
                txtemail.setText(email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getData();
                DP.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);
            }

        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("File Uploader");
        pd.show();
        final StorageReference fileRef = storageReference.child("USERS/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                        Picasso.get().load(uri).into(DP);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), "Failed ", Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {


            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                pd.setMessage("Uploaded :" + (int) percent + "%");

            }
        });

    }
}