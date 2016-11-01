package com.csci310.ParkHere;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class publicActivity extends AppCompatActivity {
    private String name;
    private Float rating=(float)0;
    private String profiel_pic;
    private Vector<String> review;
    private TextView name_text;
    private RatingBar ratingBar;
    private ImageView pic_image;
    private LinearLayout review_layout;
    private static DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        Bundle bundle = getIntent().getExtras();
        ID = bundle.getString("ID");
        review=new Vector<String>();
        pull_info();


    }

    private void pull_info(){
        DatabaseReference database = mDatabase.child("users/").child(ID).child("userName");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = (String) dataSnapshot.getValue();
                System.out.println(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        DatabaseReference database1 = mDatabase.child("users/").child(ID).child("rating");
        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                ArrayList<Integer> tempList = (ArrayList) dataSnapshot.getValue();
                if(tempList.size()!=0){
                int total=0;
                for(int i=0;i<tempList.size();i++){
                    total=total+tempList.get(i);
                }
                    rating=(float)total/(float)tempList.size();
                }
                else{
                    rating=(float)0;
                }}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        DatabaseReference database2 = mDatabase.child("users/").child(ID).child("photo");
        database2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profiel_pic = (String) dataSnapshot.getValue();
                byte[] decodedString = Base64.decode(profiel_pic, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                pic_image.setImageBitmap(decodedByte);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        DatabaseReference database3 = mDatabase.child("users/").child(ID).child("review");
        database3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                ArrayList<String> tempList = (ArrayList) dataSnapshot.getValue();
                if(tempList.size()!=0){
                    for(int i=0;i<tempList.size();i++) {
                        review.add(tempList.get(i));
                    }
                }}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        name_text=(TextView) findViewById(R.id.public_name);
        ratingBar=(RatingBar) findViewById(R.id.ratingBar);
        pic_image=(ImageView) findViewById(R.id.Public_image);
        review_layout=(LinearLayout) findViewById(R.id.review);

        name_text.post(new Runnable(){
            @Override
            public void run(){
                name_text.setText("name: "+name);
            }
        });
        ratingBar.post(new Runnable(){
            @Override
            public void run(){
                ratingBar.setRating(rating);
            }
        });
        

        for(int i=0;i<review.size();i++){
            TextView review_text = new TextView(this);
            review_text.setText(review.get(i));
            review_text.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
            ((LinearLayout) review_layout).addView(review_text);
        }
    }
}
