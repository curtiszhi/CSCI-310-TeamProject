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
    FeedItem fd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        Bundle bundle = getIntent().getExtras();
        String value = bundle.getString("parkingID");
        getInfo(value);
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
                commentHost=commentHostText.getText().toString();
                commentSpot=commentSpotText.getText().toString();
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
        mDatabase.child("parking-spots-hosting").child(feeder.getIdentifier()).child("rating").setValue(rateSpot);
        //needs to be calculated, right now it is overwriting
        mDatabase.child("parking-spots-hosting").child(feeder.getIdentifier()).child("review").child(mFirebaseUser.getUid()).setValue(commentSpot);


        mDatabase.child("users/"+feeder.getHost()+"/hosting").child(feeder.getIdentifier()).child("rating").setValue(rateHost);
        //needs to be calculated, right now it is overwriting
        mDatabase.child("users/"+feeder.getHost()+"/hosting").child(feeder.getIdentifier()).child("review").setValue(commentHost);
    }
}
