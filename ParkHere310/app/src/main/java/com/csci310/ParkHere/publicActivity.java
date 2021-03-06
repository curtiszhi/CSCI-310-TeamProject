package com.csci310.ParkHere;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
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
import java.util.HashMap;
import java.util.Vector;

public class publicActivity extends AppCompatActivity {
    private String name;
    private Float rating=(float)0;
    private String profile_pic;
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
        //pull_info();
        query_data();
    }

    private void query_data(){
        long startTime = System.currentTimeMillis();

        name_text=(TextView) findViewById(R.id.public_name);
        ratingBar=(RatingBar) findViewById(R.id.ratingBar);
        pic_image=(ImageView) findViewById(R.id.Public_image);
        review_layout=(LinearLayout) findViewById(R.id.review);


        DatabaseReference database_p = mDatabase.child("users").child(ID);
        database_p.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> user_map = (HashMap<String,Object>) dataSnapshot.getValue();

                if (user_map != null) {
                    for (HashMap.Entry<String, Object> innerEntry : user_map.entrySet()) {
                        String key = innerEntry.getKey();
                        Object value = innerEntry.getValue();

                        if (key.equals("userName")) {
                            name = (String) value;

                            name_text.post(new Runnable(){
                                @Override
                                public void run(){
                                    name_text.setText("Name: "+name);
                                }
                            });
                        }

                        if (key.equals("rating")){
                            if (value != null){
                                ArrayList<String> tempList = (ArrayList<String>) value;
                                if(tempList.size()!=0){
                                    float total=0;
                                    for(int i=0;i<tempList.size();i++){
                                        total+=Double.parseDouble((tempList.get(i).trim() + ""));
                                    }
                                    rating=total/(float)tempList.size();
                                }
                                else{
                                    rating=(float)0;
                                }

                                ratingBar.post(new Runnable(){
                                    @Override
                                    public void run(){
                                        ratingBar.setRating(rating);
                                    }
                                });
                            }
                        }

                        if (key.equals("photo")){
                            if (value != null) {
                                profile_pic = dataSnapshot.getValue().toString();
                                byte[] decodedString = Base64.decode(profile_pic, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                pic_image.setImageBitmap(decodedByte);
                            }
                        }

                        if (key.equals("review")){
                            if(value != null){
                                ArrayList<String> tempList = (ArrayList) value;
                                if(tempList.size()!=0){
                                    for(int i=0;i<tempList.size();i++) {
                                        review.add(tempList.get(i));
                                    }
                                }
                            }
                            for(int i=0;i<review.size();i++){
                                TextView review_text = new TextView(publicActivity.this);
                                review_text.setText(review.get(i));
                                review_text.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
                                ((LinearLayout) review_layout).addView(review_text);
                            }
                        }
                    }
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        long endTime = System.currentTimeMillis();
        long dif = endTime - startTime;
        System.out.println("After Improvement");
        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Time Elapsed: " + dif);
    }


    private void pull_info(){
        long startTime = System.currentTimeMillis();
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
                    ArrayList<String> tempList = (ArrayList) dataSnapshot.getValue();
                    if(tempList.size()!=0){
                    float total=0;
                    for(int i=0;i<tempList.size();i++){
                        total+=Double.parseDouble((tempList.get(i).trim() + ""));
                    }
                        rating=total/(float)tempList.size();
                    }
                    else{
                        rating=(float)0;
                    }
                }
                ratingBar.post(new Runnable(){
                    @Override
                    public void run(){
                        ratingBar.setRating(rating);
                    }
                });

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

                if (dataSnapshot.getValue() != null) {
                    profile_pic = dataSnapshot.getValue().toString();
                    byte[] decodedString = Base64.decode(profile_pic, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    pic_image.setImageBitmap(decodedByte);
                }

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
                for(int i=0;i<review.size();i++){
                    TextView review_text = new TextView(publicActivity.this);
                    review_text.setText(review.get(i));
                    review_text.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
                    ((LinearLayout) review_layout).addView(review_text);
                }

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

        long endTime = System.currentTimeMillis();
        long dif = endTime - startTime;
        System.out.println("Before Improvement");
        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Time Elapsed: " + dif);
    }
}
