package com.imran.ice.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imran.ice.Adapter.Membersadepter;
import com.imran.ice.R;

import java.util.ArrayList;

public class MyCircleFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    //users cuser;
    ArrayList<String> namelist, idlist;

    DatabaseReference databaseReference, currentreference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_circle_fragment, container, false);
        recyclerView = view.findViewById(R.id.view_recyclerview);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String useruid = firebaseUser.getUid();

        namelist = new ArrayList<>();
        idlist = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        currentreference = FirebaseDatabase.getInstance().getReference("users").child(useruid).child("circle_members");

        currentreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namelist.clear();
                idlist.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        String circleid = dss.child("circlememberid").getValue(String.class);

                        databaseReference.child(circleid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String fatchname = dataSnapshot.child("first_name").getValue(String.class);
                                String fatchid = dataSnapshot.child("userid").getValue(String.class);
                                namelist.add(fatchname);
                                idlist.add(fatchid);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter = new Membersadepter(namelist, idlist, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    public void callpermission() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
