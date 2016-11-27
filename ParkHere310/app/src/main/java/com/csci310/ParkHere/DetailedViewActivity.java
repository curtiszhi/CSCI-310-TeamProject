package com.csci310.ParkHere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
    private LinearLayout time;
    private TextView filters;
    private TextView description;
    private TextView cancel;
    private ImageView image_view;
    private TextView image_label;
    private LinearLayout review_layout;
    private Spinner sp;

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

    private ArrayList<String> all_renter;
    private ArrayList<ArrayList<String>> all_rented_time;
    private ArrayList<String> all_renter_name;
    private ArrayList<TextView> rent_time;
    private int count_name=0;
    private  ArrayList<String> spinnerArray = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        all_renter=new ArrayList<String>();
        all_rented_time=new ArrayList<ArrayList<String>>();
        renterTime=new ArrayList<String>();
        all_renter_name=new ArrayList<String>();
        rent_time=new ArrayList<TextView>();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser_universal = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Bundle bundle = getIntent().getExtras();
        value = bundle.getString("ItemPosition");


        position = Integer.parseInt(value);
        fd = MyRecyclerAdapter.feedItemList.get(position);


        image_view=(ImageView) findViewById(R.id.image);
        image_label=(TextView) findViewById(R.id.image_label);
        sp = (Spinner) findViewById(R.id.spinner1);

        if(mFirebaseUser_universal.getUid().equals(fd.getHost())){
            System.out.println("yes, hosttttttt");
            addSpinner();
        }else{
            sp.setVisibility(View.GONE);
        }
        viewButton = (Button) findViewById(R.id.viewButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        address= (TextView) findViewById(R.id.address);
        price= (TextView) findViewById(R.id.price);
        time= (LinearLayout) findViewById(R.id.Time);
        filters= (TextView) findViewById(R.id.filters);
        description= (TextView) findViewById(R.id.description);
        cancel= (TextView) findViewById(R.id.cancel);
        editButton= (Button) findViewById(R.id.editButton);
        cancelButton=(Button) findViewById(R.id.cancelButton);
        review_layout = (LinearLayout) findViewById(R.id.review);

        count=0;
        if(!mFirebaseUser_universal.getUid().equals(fd.getHost())){
        setUp();
        downloadPhoto();
        display();}

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(all_rented_time.size()!=0){
                    final int temp_i=i;
                    renterTime=all_rented_time.get(i);
                    specific_renterID=all_renter.get(i);
                    viewButton.post(new Runnable() {
                        @Override
                        public void run() {
                            viewButton.setText("Renter:" + all_renter_name.get(temp_i));
                        }
                    });
                    for(int j=0;j<rent_time.size();j++){
                        rent_time.get(j).setTypeface(null, Typeface.NORMAL);
                        if(j==i){
                           rent_time.get(j).setTypeface(null, Typeface.BOLD);
                        }
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


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
                }else if((specific_renterID==null)&&!fd.getHost().equals(mFirebaseUser_universal.getUid())){
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
                    if(all_renter.size()!=0) {
                        for(int i=0;i<all_renter.size();i++) {
                            mDatabase.child("users").child(all_renter.get(i)).child("renting").child(fd.getIdentifier()).setValue(null);

                            double price_total = calculateTotalPrice(i);
                            if (price_total != 0.0) {

                                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                                String today = getToday(df);
                                mDatabase.child("payment").child(all_renter.get(i)).child("Get Refund_Host_Cancel").child(today).setValue(price_total);


                                Intent intent = new Intent(DetailedViewActivity.this, ActionActivity.class);//change to UserActivity.class
                                startActivity(intent);

                            }
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
                        mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("rentedTime").child(specific_renterID).setValue(null);
                        double price_total= calculate_TotalPrice();
                        double total_price=price_total;
                        String startTime = renterTime.get(0);
                        System.out.println(total_price+"   xxxxxxxxx");
                        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                        String today = getToday(df);
                        Date start=null;
                        Date d = null;
                        try {
                            d = df.parse(today);
                            start = df.parse(startTime.substring(0,startTime.length()-2)+":00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long diff= start.getTime()-d.getTime();
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

    private void addSpinner(){

        for (HashMap.Entry<String, ArrayList<String>> innerEntry : fd.getRentedTime().entrySet()) {
            String key = innerEntry.getKey();
            ArrayList<String> value = innerEntry.getValue();
            all_renter.add(key);
            all_rented_time.add(value);
        }


        System.out.println(fd.getRentedTime().size()+"  sizeeeeeee");
        System.out.println(all_renter.size()+"  1sizeeeeeee");
        System.out.println(all_rented_time.size()+"  2sizeeeeeee");
        //get all names
        if(all_renter.size()!=0){
            for(int i=0;i<all_renter.size();i++){
                System.out.println(all_renter.get(i)+"  idddddddd");

                DatabaseReference database = mDatabase.child("users").child(all_renter.get(i)).child("userName");
                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        count_name++;
                        String rent_name = (String) dataSnapshot.getValue();
                        spinnerArray.add(rent_name);
                        all_renter_name.add(rent_name);
                        if(count_name==all_renter.size()){
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(DetailedViewActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                            sp.setAdapter(spinnerArrayAdapter);
                            setUp();
                            downloadPhoto();
                            display();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
            }
        }



    }

    private double calculateTotalPrice(int index){
        double price_t=0.0;
        ArrayList<String> temp_renterTime=all_rented_time.get(index);
        if(temp_renterTime.size() != 0){
        String start2 = temp_renterTime.get(0).substring(0,temp_renterTime.get(0).length()-2);
        String end2 = temp_renterTime.get(1).substring(0,temp_renterTime.get(1).length()-2);
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

    private double calculate_TotalPrice(){
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
        if(mFirebaseUser_universal.getUid().equals(fd.getHost())){//is host
            if(all_rented_time.size()!=0){//if there are renters
                renterTime=all_rented_time.get(0);
                specific_renterID=all_renter.get(0);}
            //if not ID=null, renterTime size=0
        }else {//not host

            //current renting
            if(fd.getRentedTime().containsKey(mFirebaseUser_universal.getUid()))
            {
                specific_renterID=mFirebaseUser_universal.getUid();
                renterTime=fd.getRentedTime().get(specific_renterID);
            }//if not current renting, renting specific renter Id=null, renterTime size=0
        }


        //if current renter
        if((specific_renterID!=null)&&(specific_renterID.equals(mFirebaseUser_universal.getUid()))){
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

        // if host
        else if(fd.getHost().equals(mFirebaseUser_universal.getUid())){
                viewButton.post(new Runnable() {
                    @Override
                    public void run() {
                        if(all_renter_name.size()!=0){
                        viewButton.setText("Renter:" + all_renter_name.get(0));}
                    }
                });
        }

        //old renter. not current renting
        else if(specific_renterID==null && !fd.getHost().equals(mFirebaseUser_universal.getUid())){
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

        //cancel button
        //if host always can cancel
        if(!fd.getHost().equals(mFirebaseUser_universal.getUid())){ //if not host
            if(specific_renterID!=null){//current renting
                String startTime = renterTime.get(0);
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                String today = getToday(df);
                Date start;
                Date d = null;
                try {
                    d = df.parse(today);
                    start = df.parse(startTime.substring(0, startTime.length() - 2) + ":00");
                    if (d.getTime() > start.getTime()) {
                            cancelButton.setEnabled(false);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else{//not current renting, specificID=null
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



        if(!fd.getHost().equals(mFirebaseUser_universal.getUid())){ //if not host
            if(specific_renterID!=null){//current renting
                TextView time_text = new TextView(this);
                time_text.setText(renterTime.get(0)+" to "+renterTime.get(1));
                time_text.setTextSize(20);
                time_text.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
                ((LinearLayout) time).addView(time_text);
            }else{//not current renting, old renter, specificID=null
                TextView time_text = new TextView(this);
                time_text.setText(fd.getStartDates()+" "+fd.getStartTime()+" to "+fd.getEndDates()+" "+fd.getEndTime());
                time_text.setTextSize(20);
                time_text.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
                ((LinearLayout) time).addView(time_text);
            }
        }else{//if host
            for(int i=0;i<all_rented_time.size();i++){
                TextView time_text = new TextView(this);
                time_text.setText(all_rented_time.get(i).get(0)+" to "+all_rented_time.get(i).get(1));
                time_text.setTextSize(20);
                time_text.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
                ((LinearLayout) time).addView(time_text);
                rent_time.add(time_text);
            }
            rent_time.get(0).setTypeface(null, Typeface.BOLD);

        }


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

