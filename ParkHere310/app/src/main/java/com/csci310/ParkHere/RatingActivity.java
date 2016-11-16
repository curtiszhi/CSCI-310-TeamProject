package com.csci310.ParkHere;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class RatingActivity extends AppCompatActivity {
    private String host_name;
    private String host_ID;
    private String address;

    private String rateHost;
    private String rateSpot;
    private String commentHost;
    private String commentSpot;

    private TextView hostText,addressText;
    private EditText commentHostText, commentSpotText;
    private RatingBar userRateHost,userRateSpot;
    private Button rate;

    private static DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;

    private ArrayList<String> originalSpot;
    private ArrayList<String> originalHost;
    private ArrayList<String> originalSpotComment;
    private ArrayList<String> originalHostComment;


    private ArrayList<String> rate_list;
    private int position;
    private String spot_Identifier;

    private DatabaseReference ref;
    private DatabaseReference ref1;
    private DatabaseReference ref2;
    private DatabaseReference ref3;

    private boolean count1=true;
    private boolean count2=true;
    private boolean count3=true;
    private boolean count4=true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        rate_list=new ArrayList<String>();
        Bundle bundle = getIntent().getExtras();
        rate_list = bundle.getStringArrayList("rate");
        position=bundle.getInt("position");
        spot_Identifier=rate_list.get(position);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        originalSpot=new ArrayList<String>();
        originalHost=new ArrayList<String>();
        originalSpotComment=new ArrayList<String>();
        originalHostComment=new ArrayList<String>();

        getInfo(spot_Identifier);

        userRateHost = (RatingBar) findViewById(R.id.RateHost);
        userRateSpot = (RatingBar) findViewById(R.id.RateSpot);
        commentHostText=(EditText) findViewById(R.id.review_host);
        commentSpotText=(EditText) findViewById(R.id.review_spot);
        rate=(Button) findViewById(R.id.rateButton);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateHost=(int)userRateHost.getRating()+"";
                rateSpot=(int)userRateSpot.getRating()+"";
                commentHost=commentHostText.getText().toString().trim();
                commentSpot=commentSpotText.getText().toString().trim();
                update();
                check();

            }
        });

    }
    private void getInfo(String parkID){
        DatabaseReference ref=mDatabase.child("parking-spots-hosting").child(parkID).child("host");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    host_ID = (String) dataSnapshot.getValue();
                    DatabaseReference ref1=mDatabase.child("users").child(host_ID).child("userName");
                    ref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                host_name = (String) dataSnapshot.getValue();

                                hostText= (TextView) findViewById(R.id.host_name);
                                hostText.setText(host_name);

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        DatabaseReference ref2=mDatabase.child("parking-spots-hosting").child(parkID).child("address");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    address = (String) dataSnapshot.getValue();
                    addressText= (TextView) findViewById(R.id.spot_address);
                    addressText.setText(address);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });





    }
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        AlertDialog alertDialog = new AlertDialog.Builder(RatingActivity.this).create();
        alertDialog.setTitle("Wait!");
        alertDialog.setMessage("Please finish all ratings before moving on. Thanks!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void update(){
        ref=mDatabase.child("parking-spots-hosting").child(spot_Identifier).child("rating");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                originalSpot= (ArrayList)dataSnapshot.getValue();
                originalSpot.add(rateSpot);
                }
                else{
                    originalSpot.add(rateSpot);
                }
                if(count1==true){
                ref.setValue(originalSpot);
                    count1=false;}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        System.out.println(rateSpot);


        ref1=mDatabase.child("users").child(host_ID).child("rating");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                originalHost= (ArrayList)dataSnapshot.getValue();
                originalHost.add(rateHost);}
                else{
                    originalHost.add(rateHost);
                }
                if(count2==true){
                ref1.setValue(originalHost);
                count2=false;}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        System.out.println(rateHost);

        if(commentSpot.length()!=0) {
            ref2=mDatabase.child("parking-spots-hosting").child(spot_Identifier).child("review");
            ref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                    originalSpotComment= (ArrayList)dataSnapshot.getValue();
                    originalSpotComment.add(commentSpot);}
                    else{
                        originalSpotComment.add(commentSpot);
                    }
                    if(count3==true){
                    ref2.setValue(originalSpotComment);
                    count3=false;}
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

            System.out.println(commentSpot);

        }

        if(commentHost.length()!=0) {
            ref3=mDatabase.child("users").child(host_ID).child("review");
            ref3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        originalHostComment = (ArrayList)dataSnapshot.getValue();
                        originalHostComment.add(commentHost);
                    }else{
                        originalHostComment.add(commentHost);
                    }
                    if(count4==true){
                    ref3.setValue(originalHostComment);
                    count4=false;}
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
            System.out.println(commentHost);

        }
        mDatabase.child("users").child(mFirebaseUser.getUid()).child("renting").child(spot_Identifier).setValue("rated");
        mDatabase.child("parking-spots-hosting").child(spot_Identifier).child("activity").setValue(true);
        mDatabase.child("parking-spots-hosting").child(spot_Identifier).child("rentedTime").setValue(null);


    }
    private void check(){
        if(rate_list.size()==position+1){
            Intent intent = new Intent(RatingActivity.this, ActionActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(RatingActivity.this, RatingActivity.class);
            intent.putExtra("rate", rate_list);
            intent.putExtra("position", position+1);
            startActivity(intent);

        }
    }
}
