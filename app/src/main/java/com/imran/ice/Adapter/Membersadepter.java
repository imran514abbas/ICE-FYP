package com.imran.ice.Adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imran.ice.Activity.circle_members_map;
import com.imran.ice.R;

import java.util.ArrayList;

public class Membersadepter extends RecyclerView.Adapter<Membersadepter.membersadepterViewHolder> {

    ArrayList<String> namelist, idlist;
    Context context;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    public Membersadepter() {
        //default constructor
    }

    public Membersadepter(ArrayList<String> namelist, ArrayList<String> idlist, Context context) {
        this.namelist = namelist;
        this.idlist = idlist;
        this.context = context;
    }

    @NonNull
    @Override
    public membersadepterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardrecyclerview, parent, false);
        membersadepterViewHolder membersadepterView = new membersadepterViewHolder(v, context, namelist, idlist);
        return membersadepterView;

    }

    @Override
    public void onBindViewHolder(@NonNull membersadepterViewHolder holder, int position) {
        //users userobj = namelist.get(position);
        //holder.nametxt.setText(userobj.getFirstname());
        String strobj = namelist.get(position);
        holder.nametxt.setText(strobj);

        final String strobj1 = idlist.get(position);


        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, circle_members_map.class);
                intent.putExtra("joined_uid", strobj1);
                context.startActivity(intent);
                //Toast.makeText(context,"response:"+strobj1,Toast.LENGTH_LONG).show();
            }
        });
        holder.Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("users").child(strobj1);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String PhoneNumber = snapshot.child("phone_number").getValue(String.class);

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + PhoneNumber));
                        context.startActivity(callIntent);
                        // Toast.makeText(context.getApplicationContext(), "Phone " + PhoneNumber, Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });


            }
        });
        holder.SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("users").child(strobj1);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String PhoneNumber = snapshot.child("phone_number").getValue(String.class);
                        double lat = snapshot.child("latitude").getValue(Double.class);
                        double log = snapshot.child("longitude").getValue(Double.class);
                        String sMessage = "Please Help me I am in Emergency..!" + "\n" + "Latitude : " + lat + "\n" + "Longitude : " + log;
                        try {
                            SmsManager smsManager = SmsManager.getDefault();

                            smsManager.sendTextMessage(PhoneNumber, null, sMessage, null, null);
                            Toast.makeText(context.getApplicationContext(), "SMS Sent from App!",
                                    Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Toast.makeText(context.getApplicationContext(),
                                    "SMS faild, please try again later!",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.MAIL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("users").child(strobj1);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Email = snapshot.child("email").getValue(String.class);
                        double lat = snapshot.child("latitude").getValue(Double.class);
                        double log = snapshot.child("longitude").getValue(Double.class);
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL, new String[]{Email});
                        i.putExtra(Intent.EXTRA_SUBJECT, "In Case of Emergency");
                        i.putExtra(Intent.EXTRA_TEXT, "Please Help me I am in Emergency..!" + "\n" + "Latitude : " + lat + "\n" + "Longitude : " + log);
                        try {
                            context.startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(context.getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        holder.RemoveMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();
                final String getuid = firebaseUser.getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference("users").child(getuid);
                databaseReference.child("circle_members").child(strobj1).removeValue();

            }
        });
    }

    @Override
    public int getItemCount() {
        int list1 = namelist.size();
        int list2 = idlist.size();
        return list1 | list2;
    }

    public class membersadepterViewHolder extends RecyclerView.ViewHolder {
        TextView nametxt, location, Call, SMS, MAIL;
        ImageButton RemoveMember;
        Context c;
        ArrayList<String> namearraylist, idarraylist;
        FirebaseUser firebaseUser;
        FirebaseAuth firebaseAuth;

        public membersadepterViewHolder(@NonNull View itemView, Context c, ArrayList<String> namearraylist, ArrayList<String> idarraylist) {
            super(itemView);
            this.c = c;
            this.namearraylist = namearraylist;
            this.idarraylist = idarraylist;

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();

            nametxt = itemView.findViewById(R.id.textview_name);
            location = itemView.findViewById(R.id.textview_location);
            Call = itemView.findViewById(R.id.textview_phone);
            SMS = itemView.findViewById(R.id.textview_sms);
            MAIL = itemView.findViewById(R.id.textview_email);
            RemoveMember = itemView.findViewById(R.id.removemember);

        }
    }
}

