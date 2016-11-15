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
import java.util.Vector;

public class RatingActivity extends AppCompatActivity {
    private String host_name;
    private float rateHost;
    private float rateSpot;
    private String commentHost;
    private String commentSpot;
    private TextView hostText;
    private EditText commentHostText, commentSpotText;
    private RatingBar userRateHost,userRateSpot;
    private Button rate;
    private static DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;

    private Vector<Float> originalSpot;
    private Integer originalHost;
    private Vector<String> originalSpotComment;
    private Vector<String> originalHostComment;
    private Vector<String> rateList;

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

        getInfo(spot_Identifier);
        
        hostText= (TextView) findViewById(R.id.host_name);
        hostText.setText(host_name);
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
                update(fd);

            }
        });

    }
    private void getInfo(String parkID){
        mDatabase.child("parking-spots-renting");
        mDatabase.orderByChild("identifier").equalTo(parkID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("User key", child.getKey());
                    Log.d("User ref", child.getRef().toString());
                    Log.d("User val", child.getValue().toString());
                    fd = (FeedItem) child.getValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void update(FeedItem feeder){
        DatabaseReference ref=mDatabase.child("parking-spots-hosting").child(feeder.getIdentifier()).child("rating");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                originalSpot= dataSnapshot.getValue(Vector.class);
                originalSpot.add(rateSpot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        ref.setValue(originalSpot);

        DatabaseReference ref1=mDatabase.child("users").child(feeder.getHost()).child("rating");
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
            DatabaseReference ref2=mDatabase.child("parking-spots-hosting").child(feeder.getIdentifier()).child("review");
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
            DatabaseReference ref3=mDatabase.child("users").child(feeder.getHost()).child("review");
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

        DatabaseReference ref4=mDatabase.child("users").child(mFirebaseAuth.getCurrentUser().getUid()).child("rateList");
        ref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rateList= dataSnapshot.getValue(Vector.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        Vector<String> newRateList=new Vector<String>();
        for(int i=0;i<rateList.size();i++){
            if(rateList.get(i)!=fd.getIdentifier()){
                newRateList.add(rateList.get(i));
            }
        }
        ref4.setValue(newRateList);

    }
}
