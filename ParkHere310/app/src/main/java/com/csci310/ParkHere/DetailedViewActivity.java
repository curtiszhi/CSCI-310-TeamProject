package com.csci310.ParkHere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

    private int count;
    FeedItem fd;
    private Vector<String> spotPhoto;
    private Button editButton,confirmButton,cancelButton;
    int position;
    String value;
    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser_universal;
    public DatabaseReference mDatabase;
    private Vector<String> rateList;
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
        confirmButton=(Button) findViewById(R.id.confirmButton);
        cancelButton=(Button) findViewById(R.id.cancelButton);

        count=0;

        setUp();
        downloadPhoto();
        display();
        /*
            public void onItemSelected(AdapterView<?>spinner,View container,
                                       int position,long id) {

                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy hh:mmaa");

            }

           */


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
                else if(mFirebaseUser_universal.getUid().equals(specific_renterID)){
                    Intent intent = new Intent(DetailedViewActivity.this, publicActivity.class);
                    intent.putExtra("ID", fd.getHost());
                    startActivity(intent);
                }


            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"yingchew@usc.edu"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Payout Request");
                i.putExtra(Intent.EXTRA_TEXT   , "Hi! I would like to request a payment for a successful hosting at" + fd.getAddress()
                + "My PayPal email is: [INSERT EMAIL HERE]");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DetailedViewActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                DatabaseReference ref=mDatabase.child("users").child(specific_renterID).child("rateList");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        rateList= dataSnapshot.getValue(Vector.class);
                        rateList.add(fd.getIdentifier());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
                ref.setValue(rateList);
                Intent intent = new Intent(DetailedViewActivity.this, UserActivity.class);//change to UserActivity.class
                startActivity(intent);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"yingchew@usc.edu"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Refund Request");
                i.putExtra(Intent.EXTRA_TEXT   , "Hi! I would like to request a refund. I agree with the cancellation policy of: " + fd.getCancel());
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DetailedViewActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                if(mFirebaseUser_universal.getUid().equals(fd.getHost())) {
                    mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).setValue(null);
                    mDatabase.child("users").child(fd.getHost()).child(fd.getIdentifier()).setValue(null);
                    if(specific_renterID!=null) {
                        mDatabase.child("users").child(specific_renterID).child("renting").child(fd.getIdentifier()).setValue(null);
                        mDatabase.child("users").child(specific_renterID).child("rateList").child(fd.getIdentifier()).setValue(null);
                    }
                }
                else if(specific_renterID!=null) {
                    if (specific_renterID.equals(mFirebaseUser_universal.getUid())) {
                        mDatabase.child("users").child(specific_renterID).child("renting").child(fd.getIdentifier()).setValue(null);
                        mDatabase.child("users").child(specific_renterID).child("rateList").child(fd.getIdentifier()).setValue(null);
                    }
                }


                Intent intent = new Intent(DetailedViewActivity.this, UserActivity.class);//change to UserActivity.class
                startActivity(intent);
            }
        });
    }
    public String getToday(java.text.SimpleDateFormat  dformat){
        Date date = new Date();
        return dformat.format(date);
    }
    private void setUp(){
        renterTime=new ArrayList<String>();
        if(fd.getRentedTime().size()!=0){
        for (HashMap.Entry<String, ArrayList<String>> innerEntry : fd.getRentedTimeArray().entrySet()) {
            String key = innerEntry.getKey();
            ArrayList<String> value = innerEntry.getValue();
            renterTime=value;
            specific_renterID=key;
        }}

        if((specific_renterID!=null)&&(specific_renterID.equals(mFirebaseUser_universal.getUid()))){
            DatabaseReference database = mDatabase.child("users").child(specific_renterID);
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name = (String) dataSnapshot.getValue();
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
        else{
            if(specific_renterID!=null) {
                DatabaseReference database = mDatabase.child("users").child(specific_renterID);
                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        name = (String) dataSnapshot.getValue();
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

        if (renterTime.size() != 0) {
            String endTime = renterTime.get(1);
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy hh:mmaa");
            String today = getToday(df);
            Date end;
            Date d = null;
            try {
                d = df.parse(today);
                end = df.parse(endTime);
                if (d.getTime() < end.getTime()) {
                    confirmButton.setEnabled(false);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }else{
            confirmButton.setEnabled(false);
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
            confirmButton.setVisibility(Button.GONE);
            editButton.setVisibility(Button.GONE);
        }
        time.post(new Runnable(){
            @Override
            public void run(){
                if(renterTime.size()!=0){
                time.setText(renterTime.get(0)+" to "+renterTime.get(1));}
            }
        });

        ratingBar.setRating(fd.calculateRate());

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
        }
    }

}

