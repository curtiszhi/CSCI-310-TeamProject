package com.csci310.ParkHere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.csci310.ParkHere.ActionActivity.user_all;
import static com.csci310.ParkHere.ListingActivity.confirm_list;

/**
 * Created by seanyuan on 10/17/16.
 */
public class DetailedViewActivity extends AppCompatActivity{
    private Button viewButton;
    private String renterName;
    private Vector<String> rentTime;
    private String startT;
    private String endT;
    private RatingBar ratingBar;
    private TextView address;
    private TextView price;
    private TextView time;
    private TextView filters;
    private TextView description;
    private TextView cancel;
    private ImageView image_view;
    private TextView image_label;
    private LinearLayout review_layout;

    private int count;
    private int index1=0;
    FeedItem fd;
    private Vector<String> spotPhoto;
    private Button editButton,cancelButton;
    int position;
    String value;
    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser_universal;
    public DatabaseReference mDatabase;
    private String specific_renterID=null;
    private ArrayList<String> renterTime;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser_universal = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Bundle bundle = getIntent().getExtras();
        value = bundle.getString("ItemPosition");


        position = Integer.parseInt(value);
        fd = MyRecyclerAdapter.feedItemList.get(position);


        image_view=(ImageView) findViewById(R.id.image);
        image_label=(TextView) findViewById(R.id.image_label);
        viewButton = (Button) findViewById(R.id.viewButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        address= (TextView) findViewById(R.id.address);
        price= (TextView) findViewById(R.id.price);
        time= (TextView) findViewById(R.id.time);
        filters= (TextView) findViewById(R.id.filters);
        description= (TextView) findViewById(R.id.description);
        cancel= (TextView) findViewById(R.id.cancel);
        editButton= (Button) findViewById(R.id.editButton);
        cancelButton=(Button) findViewById(R.id.cancelButton);
        review_layout = (LinearLayout) findViewById(R.id.review);

        count=0;

        setUp();
        downloadPhoto();
        display();




        image_view.setOnTouchListener(new OnSwipeTouchListener(DetailedViewActivity.this) {
            public void onSwipeRight() {
                if(count>0){
                    System.out.println("swiped right" + count);
                    count--;
                    String temp_p = spotPhoto.get(count);
                    byte[] decodedString = Base64.decode(temp_p, Base64.DEFAULT);
                    Bitmap b = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_view.setImageBitmap(b);
                    int temp=count+1;
                    image_label.setText(temp+" of "+spotPhoto.size()+" images");
                }

            }
            public void onSwipeLeft() {
                if(count<spotPhoto.size()-1){
                    System.out.println("swiped left" + count);
                    count++;
                    String temp_p = spotPhoto.get(count);
                    byte[] decodedString = Base64.decode(temp_p, Base64.DEFAULT);
                    Bitmap b = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_view.setImageBitmap(b);
                    int temp=count+1;
                    image_label.setText(temp+" of "+spotPhoto.size()+" images");
                }

            }
        });


        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedViewActivity.this, AddActivity.class);
                intent.putExtra("ItemPosition", value);
                startActivity(intent);
                //address cannot be change
                //rating cannot be change
                //review cannot be change

            }
        });

        viewButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if((specific_renterID!=null) && (mFirebaseUser_universal.getUid().equals(fd.getHost()))){
                Intent intent = new Intent(DetailedViewActivity.this, publicActivity.class);
                intent.putExtra("ID", specific_renterID);
                startActivity(intent);}
                else if((specific_renterID!=null)&&(mFirebaseUser_universal.getUid().equals(specific_renterID))){
                    Intent intent = new Intent(DetailedViewActivity.this, publicActivity.class);
                    intent.putExtra("ID", fd.getHost());
                    startActivity(intent);
                }else if(!fd.getHost().equals(mFirebaseUser_universal.getUid())){
                    Intent intent = new Intent(DetailedViewActivity.this, publicActivity.class);
                    intent.putExtra("ID", fd.getHost());
                    startActivity(intent);
                }


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mFirebaseUser_universal.getUid().equals(fd.getHost())) {
                    mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).setValue(null);
                    mDatabase.child("users").child(fd.getHost()).child("hosting").child(fd.getIdentifier()).setValue(null);
                    if(specific_renterID!=null) {

                        mDatabase.child("users").child(specific_renterID).child("renting").child(fd.getIdentifier()).setValue(null);

                       double price_total= calculateTotalPrice();
                        if(price_total!=0.0){
                            /*Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("message/rfc822");
                            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"yingchew@usc.edu"});
                            i.putExtra(Intent.EXTRA_SUBJECT, "Refund to the renter");
                            i.putExtra(Intent.EXTRA_TEXT   , "Hi! I would like to cancel my spot and give the renter whole refund "+price_total);
                            try {
                                startActivity(Intent.createChooser(i, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(DetailedViewActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                            }*/
                            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                            String today = getToday(df);
                            mDatabase.child("payment").child(specific_renterID).child("Get Refund_Host_Cancel").child(today).setValue(price_total);


                            Intent intent = new Intent(DetailedViewActivity.this, ActionActivity.class);//change to UserActivity.class
                            startActivity(intent);

                        }

                    }
                }
                System.out.println(specific_renterID+ "      llllllll");
                System.out.println(mFirebaseUser_universal.getUid()+ "      mmmmmmmmmmm");
                if(specific_renterID!=null && (specific_renterID.equals(mFirebaseUser_universal.getUid()))) {
                    if (specific_renterID.equals(mFirebaseUser_universal.getUid())) {
                        System.out.println("get in llllllll");

                        mDatabase.child("users").child(specific_renterID).child("renting").child(fd.getIdentifier()).setValue(null);

                        mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("activity").setValue(true);
                        mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("rentedTime").setValue(null);
                        double price_total= calculateTotalPrice();
                        double total_price=price_total;
                        String endTime = renterTime.get(1);
                        System.out.println(total_price+"   xxxxxxxxx");
                        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                        String today = getToday(df);
                        Date end=null;
                        Date d = null;
                        try {
                            d = df.parse(today);
                            end = df.parse(endTime.substring(0,endTime.length()-2)+":00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long diff= end.getTime()-d.getTime();
                        System.out.println(fd.getCancel()+"     uuuuuuuuuuuuuuuuu");
                        if(fd.getCancel().equals("No refund")){
                            price_total=0.0;
                        }else if(fd.getCancel().equals("80% refund rate at any time")){
                            System.out.println("hehe   ccccccccc");
                            price_total=price_total*0.8;
                        }else if(fd.getCancel().equals("Full refund if cancel before 7 days, 50% refund if cancel less than 7 days")){
                            if(diff>604800000){
                                price_total=price_total;
                            }else{
                                price_total=price_total*0.5;
                            }
                        }else if(fd.getCancel().equals("Full refund if cancel before 7 days, no refund if cancel less than 7 days")){
                            if(diff>604800000){
                                price_total=price_total;
                            }else{
                                price_total=0.0;
                            }
                        }
                        if(price_total!=0.0){
                           /* System.out.print("send emaillllllllllll");
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("message/rfc822");
                            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"yingchew@usc.edu"});
                            i.putExtra(Intent.EXTRA_SUBJECT, "Refund Request");
                            i.putExtra(Intent.EXTRA_TEXT   , "Hi! I would like to request a refund. I agree with the cancellation policy of: " +
                                    fd.getCancel()+"the total refund is: "+price_total+"and the money goes to the host is: "+total_price);
                            try {
                                startActivity(Intent.createChooser(i, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(DetailedViewActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                            }*/

                            java.text.SimpleDateFormat df1 = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                            String today1 = getToday(df);
                            mDatabase.child("payment").child(specific_renterID).child("Get Refund_Renter_Cancel").child(today1).setValue(price_total);

                        }
                        mDatabase.child("payment").child(fd.getHost()).child("Get Refund_Renter_Cancel").child(today).setValue(total_price-price_total);
                    }
                }


                Intent intent = new Intent(DetailedViewActivity.this, ActionActivity.class);//change to UserActivity.class
                startActivity(intent);
            }
        });
    }
    private double calculateTotalPrice(){
        double price_t=0.0;
        if(renterTime.size() != 0){
        String start2 = renterTime.get(0).substring(0,renterTime.get(0).length()-2);
        String end2 = renterTime.get(1).substring(0,renterTime.get(1).length()-2);
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
            price_t=(diffHours+1)*fd.getPrice();
       }
        System.out.print(price_t+"    total priceeeeeeeeeeeee");
        return price_t;
    }
    public String getToday(java.text.SimpleDateFormat  dformat){
        Date date = new Date();
        return dformat.format(date);
    }
    private void setUp(){
        renterTime=new ArrayList<String>();
        System.out.println("11111111111 "+fd.getRentedTime().size());
        if(fd.getRentedTime().size()!=0){
        for (HashMap.Entry<String, ArrayList<String>> innerEntry : fd.getRentedTime().entrySet()) {
            String key = innerEntry.getKey();
            ArrayList<String> value = innerEntry.getValue();
            renterTime=value;
            specific_renterID=key;
            System.out.println(key+"11111111111");
        }}

        if((specific_renterID!=null)&&(specific_renterID.equals(mFirebaseUser_universal.getUid()))){
            System.out.println(specific_renterID+"222222");
            DatabaseReference database = mDatabase.child("users").child(fd.getHost()).child("userName");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name = (String) dataSnapshot.getValue();
                    System.out.println(name+"3333333");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
            viewButton.post(new Runnable(){
                    @Override
                    public void run(){
                        viewButton.setText("Host:"+name);
                    }
                });
            }
        else if(fd.getHost().equals(mFirebaseUser_universal.getUid())){
            if(specific_renterID!=null) {
                System.out.println(specific_renterID+"4444444");
                DatabaseReference database = mDatabase.child("users").child(specific_renterID).child("userName");
                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        name = (String) dataSnapshot.getValue();
                        System.out.println(name+"5555555");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
                viewButton.post(new Runnable() {
                    @Override
                    public void run() {
                        viewButton.setText("Renter:" + name);
                    }
                });
            }
            }
        else if(!fd.getHost().equals(mFirebaseUser_universal.getUid())){
            DatabaseReference database = mDatabase.child("users").child(fd.getHost()).child("userName");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name = (String) dataSnapshot.getValue();
                    System.out.println(name+"3333333");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
            viewButton.post(new Runnable(){
                @Override
                public void run(){
                    viewButton.setText("Host:"+name);
                }
            });
        }

        if (renterTime.size() != 0) {
            if(specific_renterID.equals(mFirebaseUser_universal.getUid())) {
                String startTime = renterTime.get(0);
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                String today = getToday(df);
                Date end;
                Date d = null;
                try {
                    d = df.parse(today);
                    end = df.parse(startTime.substring(0, startTime.length() - 2) + ":00");
                    if (d.getTime() > end.getTime()) {
                        if (specific_renterID != null && (specific_renterID.equals(mFirebaseUser_universal.getUid()))) {
                            cancelButton.setEnabled(false);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else{
                if(!fd.getHost().equals(mFirebaseUser_universal.getUid())){
                cancelButton.setEnabled(false);
                cancelButton.setText("This is an old listing");}
            }


        }else{
            if(!fd.getHost().equals(mFirebaseUser_universal.getUid())){
                cancelButton.setEnabled(false);
                cancelButton.setText("This is an old listing");
            }

        }


        address.setText(fd.getAddress());
        price.setText("$" + Double.toString(fd.getPrice()));

        String filter_spot="";
        if (fd.getFilter() != null)
        {
            for(int i=0;i<fd.getFilter().size();i++){
                if(i!=fd.getFilter().size()-1){
                    filter_spot=filter_spot+fd.getFilter().get(i)+", ";}
                else{
                    filter_spot=filter_spot+fd.getFilter().get(i);
                }
            }
        }
        filters.setText(filter_spot);
        description.setText(fd.getDescription());
        cancel.setText(fd.getCancel());
        if(!fd.getHost().equals((mFirebaseAuth.getCurrentUser().getUid()))){
            System.out.println("sammmmmmmmmeeeeeee");
            editButton.setVisibility(Button.GONE);
        }
        time.post(new Runnable(){
            @Override
            public void run(){
                if(renterTime.size()!=0){
                time.setText(renterTime.get(0)+" to "+renterTime.get(1));}
                else{
                    time.setText(fd.getStartDates()+" "+fd.getStartTime()+" to "+fd.getEndDates()+" "+fd.getEndTime());
                }
            }
        });

        ratingBar.setRating(fd.calculateRate());
        for (int i = 0; i < fd.getReview().size(); i++) {
            TextView review_text = new TextView(this);
            review_text.setText(fd.getReview().get(i));
            review_text.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
            ((LinearLayout) review_layout).addView(review_text);
        }

    }

    private void downloadPhoto(){
        spotPhoto = fd.photos;
    }

    public void display(){
        if (spotPhoto.size() > 0) {
            byte[] decodedString = Base64.decode(spotPhoto.get(0), Base64.DEFAULT);
            Bitmap b = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image_view.setImageBitmap(b);
            image_label.setText("1 of " + spotPhoto.size() + " images");
        }else{
            image_label.setText("0 image");
        }
    }

}

