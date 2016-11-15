package com.csci310.ParkHere;

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

    private float rateHost;
    private float rateSpot;
    private String commentHost;
    private String commentSpot;

    private TextView hostText,addressText;
    private EditText commentHostText, commentSpotText;
    private RatingBar userRateHost,userRateSpot;
    private Button rate;

    private static DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;

    private ArrayList<Integer> originalSpot;
    private ArrayList<Integer> originalHost;
   // private Integer originalHost;
    private Vector<String> originalSpotComment;
    private Vector<String> originalHostComment;


    private ArrayList<String> rate_list;
    private int position;
    private String spot_Identifier;



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
        originalSpot=new ArrayList<Integer>();

        getInfo(spot_Identifier);

        userRateHost = (RatingBar) findViewById(R.id.RateHost);
        userRateSpot = (RatingBar) findViewById(R.id.RateSpot);
        commentHostText=(EditText) findViewById(R.id.review_host);
        commentSpotText=(EditText) findViewById(R.id.review_spot);
        rate=(Button) findViewById(R.id.rateButton);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateHost=userRateHost.getRating();
                rateSpot=userRateSpot.getRating();
                commentHost=commentHostText.getText().toString().trim();
                commentSpot=commentSpotText.getText().toString().trim();
                update();

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
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        DatabaseReference ref1=mDatabase.child("users").child(host_ID).child("userName");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    host_name = (String) dataSnapshot.getValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        hostText= (TextView) findViewById(R.id.host_name);
        hostText.setText(host_name);

        addressText= (TextView) findViewById(R.id.spot_address);
        addressText.setText(address);


    }
    private void update(){
        DatabaseReference ref=mDatabase.child("parking-spots-hosting").child(spot_Identifier).child("rating");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                originalSpot= (ArrayList)dataSnapshot.getValue();
                originalSpot.add(rateSpot);}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        ref.setValue(originalSpot);

        DatabaseReference ref1=mDatabase.child("users").child(host_ID).child("rating");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                originalHost= dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        ref1.setValue(originalHost);

        if(commentSpot.length()!=0) {
            DatabaseReference ref2=mDatabase.child("parking-spots-hosting").child(spot_Identifier).child("review");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    originalSpotComment= dataSnapshot.getValue(Vector.class);
                    originalSpotComment.add(commentSpot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
            ref2.setValue(originalSpotComment);

        }

        if(commentHost.length()!=0) {
            DatabaseReference ref3=mDatabase.child("users").child(host_ID).child("review");
            ref3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    originalHostComment= dataSnapshot.getValue(Vector.class);
                    originalHostComment.add(commentHost);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
            ref3.setValue(originalHostComment);

        }



    }
}
