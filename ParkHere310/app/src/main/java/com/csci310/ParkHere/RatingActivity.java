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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

   // private ArrayList<String> originalSpot;
    private ArrayList<String> originalHost;
   // private ArrayList<String> originalSpotComment;
    private ArrayList<String> originalHostComment;


    private String rate_list;
    private String spot_Identifier;

    //private DatabaseReference ref;
    private DatabaseReference ref1;
    //private DatabaseReference ref2;
    private DatabaseReference ref3;

    //private boolean count1=true;
    private boolean count2=true;
    //private boolean count3=true;
    private boolean count4=true;

    private double single_price=0;
    private Map<String, ArrayList<String>> rented_Time;
    private ArrayList<String> time_frame=new ArrayList<String>();
    private double total_price;


    private int count_spots=0;
    private int count_host_spots=0;
    private ArrayList<String> host_all_spot;
    private FeedItem this_spot;
    private ArrayList<FeedItem> all_spot;
    private ArrayList<String> all_similar_spot;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Bundle bundle = getIntent().getExtras();
        rate_list = bundle.getString("rate");
        spot_Identifier=rate_list;
        System.out.println(spot_Identifier+"   ooooooo size");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();




        originalHost=new ArrayList<String>();
        originalHostComment=new ArrayList<String>();

        host_all_spot=new ArrayList<String>();
        all_spot=new ArrayList<FeedItem>();
        all_similar_spot=new ArrayList<String>();

        getInfo(spot_Identifier);

        userRateHost = (RatingBar) findViewById(R.id.RateHost);
        userRateSpot = (RatingBar) findViewById(R.id.RateSpot);
        commentHostText=(EditText) findViewById(R.id.review_host);
        commentSpotText=(EditText) findViewById(R.id.review_spot);
        rate=(Button) findViewById(R.id.rateButton);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_rate()) {
                    rateHost = (int) userRateHost.getRating() + "";
                    rateSpot = (int) userRateSpot.getRating() + "";
                    commentHost = commentHostText.getText().toString().trim();
                    commentSpot = commentSpotText.getText().toString().trim();
                    update();

                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(RatingActivity.this).create();
                    alertDialog.setTitle("Wait!");
                    alertDialog.setMessage("Please rate both spot and host");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });

    }

    private boolean check_rate(){
        boolean check=true;
        if((int)userRateHost.getRating()==0){
            check=false;
        }
        if((int)userRateSpot.getRating()==0){
            check=false;
        }
        return check;

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

                                hostText.post(new Runnable(){
                                    @Override
                                    public void run(){
                                        hostText.setText(host_name);
                                    }
                                });

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });

                    DatabaseReference ref5=mDatabase.child("users").child(host_ID).child("hosting");
                    ref5.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                int allspot_count=(int)dataSnapshot.getChildrenCount();
                                System.out.println(allspot_count+"   count spots");
                                for (DataSnapshot child : dataSnapshot.getChildren())
                                {
                                    count_spots++;
                                    host_all_spot.add(child.getValue(String.class));
                                    System.out.println(child.getValue(String.class)+"   hosting spots");
                                    if(count_spots==allspot_count){
                                        get_all_spot();
                                    }

                                }
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

                    addressText.post(new Runnable(){
                        @Override
                        public void run(){
                            addressText.setText(address);
                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        DatabaseReference ref3=mDatabase.child("parking-spots-hosting").child(parkID).child("price");
        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Object temp_2=(Object)dataSnapshot.getValue();

                    single_price=Double.parseDouble((String) (temp_2 + ""));

                    System.out.println(single_price+"   per hourrrrrrrrrrrrr");


                    DatabaseReference ref4=mDatabase.child("parking-spots-hosting").child(spot_Identifier).child("rentedTime");
                    ref4.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                rented_Time=(Map<String, ArrayList<String>>)dataSnapshot.getValue();
                                if(rented_Time.size() != 0){
                                    for (HashMap.Entry<String, ArrayList<String>> innerEntry : rented_Time.entrySet()) {

                                        ArrayList<String> value = innerEntry.getValue();
                                        time_frame=value;
                                    }
                                    String start2 = time_frame.get(0).substring(0,time_frame.get(0).length()-2);
                                    String end2 = time_frame.get(1).substring(0,time_frame.get(1).length()-2);
                                    SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                                    Date time1 = null;
                                    Date time2 = null;
                                    try {
                                        time1 = df.parse(start2+":00");
                                        time2 = df.parse(end2+":00");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    long diff = time2.getTime() - time1.getTime();
                                    long diffHours = diff / (60 * 60 * 1000);
                                    System.out.println(diffHours+"   hour differenttttttt");
                                    total_price=(diffHours+1.0)*single_price*0.8;
                                    System.out.println(total_price+"   price to getttttttttt");
                                }


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

        DatabaseReference ref6=mDatabase.child("parking-spots-hosting").child(parkID);
        ref6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                this_spot=dataSnapshot.getValue(FeedItem.class);
                System.out.println(this_spot.getAddress() +"   address");
                System.out.println(this_spot.getCancel() +"   cancel");
                System.out.println(this_spot.getPrice() +"   price");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });


    }
    @Override
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

    private void get_all_spot(){
        for(int i=0;i<host_all_spot.size();i++){
            DatabaseReference ref=mDatabase.child("parking-spots-hosting").child(host_all_spot.get(i));
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    all_spot.add(dataSnapshot.getValue(FeedItem.class));
                    System.out.println(dataSnapshot.getValue(FeedItem.class).getAddress() +"   duplicate");
                    count_host_spots++;

                    if(count_host_spots==host_all_spot.size()){
                        compare();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());
                }
            });
        }
    }

    private void compare(){
        for(int i=0;i<all_spot.size();i++){
            //address
            if(!all_spot.get(i).getAddress().equals(this_spot.getAddress())){
              return;
            }
           /* //cancellation policy
            if(!all_spot.get(i).getCancel().equals(this_spot.getCancel())){
                return;
            }
            //description
            if(!all_spot.get(i).getDescription().equals(this_spot.getDescription())){
                return;
            }
            //photo
            if(all_spot.get(i).getPhotos().size()!=this_spot.getPhotos().size()){
                return;
            }*/
            System.out.println(all_spot.get(i).getIdentifier()+ "     id");
            System.out.println(all_spot.get(i).getAddress()+ "     adresssssssss");
            all_similar_spot.add(all_spot.get(i).getIdentifier());
        }
       // all_similar_spot.add(this_spot.getIdentifier());
    }





    private void update(){
        for(int i=0;i<all_similar_spot.size();i++) {

            System.out.println(all_similar_spot.get(i)+ "     id finally");
            final DatabaseReference ref=mDatabase.child("parking-spots-hosting").child(all_similar_spot.get(i)).child("rating");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<String> originalSpot=new ArrayList<String>();
                    if(dataSnapshot.exists()){
                        originalSpot= (ArrayList)dataSnapshot.getValue();
                        originalSpot.add(rateSpot);
                        ref.setValue(originalSpot);
                    }
                    else{
                        System.out.println("rate the spot");
                        originalSpot.add(rateSpot);
                        ref.setValue(originalSpot);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

            System.out.println(rateSpot);
            if(commentSpot.length()!=0) {
                final DatabaseReference ref2=mDatabase.child("parking-spots-hosting").child(all_similar_spot.get(i)).child("review");
                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> originalSpotComment=new ArrayList<String>();
                        if(dataSnapshot.exists()){
                            originalSpotComment= (ArrayList)dataSnapshot.getValue();
                            originalSpotComment.add(commentSpot);
                            ref2.setValue(originalSpotComment);}
                        else{
                            originalSpotComment.add(commentSpot);
                            System.out.println("comment the spot");
                            ref2.setValue(originalSpotComment);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });

                System.out.println(commentSpot);

            }

        }




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
        mDatabase.child("parking-spots-hosting").child(spot_Identifier).child("rentedTime").child(mFirebaseUser.getUid()).setValue(null);


        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String today = getToday(df);
        mDatabase.child("payment").child(host_ID).child("Get Payment").child(today).setValue(total_price);



        Intent intent = new Intent(RatingActivity.this, ActionActivity.class);
        startActivity(intent);



    }
    public String getToday(java.text.SimpleDateFormat  dformat){
        Date date = new Date();
        return dformat.format(date);
    }

}
