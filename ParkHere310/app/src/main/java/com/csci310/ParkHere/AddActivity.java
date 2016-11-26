package com.csci310.ParkHere;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Vector;


/**
 * Created by seanyuan on 10/7/16.
 */

public class AddActivity extends AppCompatActivity {
    private List<FeedItem> hostList;
    private String identifier;
    private EditText location,city,postcode, description, price, startTime, endTime, startDate, endDate;
    private Button post, photoButton;
    private MultiSelectionSpinner spinner;
    private String[] items = {"handicap", "compact", "covered parking"};
    private static final int selected_p = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private DatabaseReference ref;
    private String spotID;
    private Vector<String> photos;
    private String state;
    private String cancel_policy;
    private List<String> filter;
    private Bitmap s_image;
    private FeedItem fd;
    private AddActivity self;
    private Spinner dropdown;
    ProgressBar viewProgressBar;
    private static String[] state_list=new String[]{"AL",
            "AK",
            "AZ",
            "AR",
            "CA",
            "CO",
            "CT",
            "DE",
            "GA",
            "HI",
            "ID",
            "IL",
            "IN",
            "IA",
            "KS",
            "KY",
            "LA",
            "ME",
            "MD",
            "MA",
            "MI",
            "MN",
            "MS",
            "MO",
            "MT",
            "NE",
            "NV",
            "NH",
            "NJ",
            "NM",
            "NY",
            "NC",
            "ND",
            "OH",
            "OK",
            "OR",
            "PA",
            "RI",
            "SC",
            "SD",
            "TN",
            "TX",
            "UT",
            "VT",
            "VA",
            "WA",
            "WV",
            "WI",
            "WY"};
    String value;
    int position;
    Boolean isEdit;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("New Listing");
        self = this;


        setContentView(R.layout.activity_create);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        spinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner1);
        spinner.setItems(items);
        hostList = new ArrayList<>();
        photos = new Vector<String>();
        photoButton = (Button) findViewById(R.id.photo);
        post = (Button) findViewById(R.id.postButton);
        location = (EditText) findViewById(R.id.Address);
        description = (EditText) findViewById(R.id.description);
        price = (EditText) findViewById(R.id.price);
        startTime = (EditText) findViewById(R.id.startTimeEditText);
        endTime = (EditText) findViewById(R.id.endTimeEditText);
        startDate = (EditText) findViewById(R.id.startDateEditText);
        endDate = (EditText) findViewById(R.id.endDateEditText);
        city=(EditText)findViewById(R.id.city);
        postcode=(EditText)findViewById(R.id.postcode);
        dropdown=(Spinner)findViewById(R.id.state);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(AddActivity.this,android.R.layout.simple_spinner_dropdown_item,state_list);
        dropdown.setAdapter(adapter);
        viewProgressBar = (ProgressBar) findViewById(R.id.progressBar1);


        new DatePicker(AddActivity.this, R.id.startDateEditText);
        new DatePicker(AddActivity.this, R.id.endDateEditText);
        new TimePicker(AddActivity.this, R.id.startTimeEditText);
        new TimePicker(AddActivity.this, R.id.endTimeEditText);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            value = bundle.getString("ItemPosition");
            String past = bundle.getString("isPast");
            if(past.equals("true")){
                getItemsHosting();
                isEdit = false;
                position = Integer.parseInt(value);
                FeedItem fd2 = MyRecyclerAdapter.feedItemList.get(position);
                fd = new FeedItem();
                String addressfull = fd2.getAddress();
                String [] info = addressfull.split(",");
                location.setEnabled(false);
                location.setText(info[0].trim());
                setTitle(info[0].trim());
                System.out.println(info[0].trim());
                city.setEnabled(false);
                city.setText(info[1].trim());
                System.out.println(info[1].trim());
                postcode.setEnabled(false);
                String zip2 = info[2].trim().replaceAll("\\D+","");
                System.out.println(zip2);
                postcode.setText(zip2);
                String state2 =  info[2].trim().replaceAll("[0-9]","");
                System.out.println(state2);
                dropdown.setEnabled(false);
                int index = -1;
                for (int i=0;i<state_list.length;i++) {
                    System.out.println(state_list[i] + "==" + state2);
                    if (state_list[i].trim().equals(state2.trim())) {
                        index = i;
                        System.out.println("equals");
                        break;
                    }
                }
                dropdown.setSelection(index);
                photos = fd2.photos;
                description.setText(fd2.getDescription());
                price.setText(Double.toString(fd2.getPrice()));
                startTime.setText(fd2.getStartTime());
                endTime.setText(fd2.getEndTime());
                startDate.setText(fd2.getStartDates());
                endDate.setText(fd2.getEndDates());
                spinner.setSelection(fd2.getFilter());
                if(fd2.getCancel().equals("No refund")) {
                    RadioButton no=(RadioButton)findViewById(R.id.radio_norefund);
                    cancel_policy="No refund";
                    no.setChecked(true);
                }
                if(fd2.getCancel().equals("80% refund rate at any time")) {
                    RadioButton no=(RadioButton)findViewById(R.id.radio_80refund);
                    cancel_policy="80% refund rate at any time";
                    no.setChecked(true);
                }
                if(fd2.getCancel().equals("Full refund if cancel before 7 days, 50% refund if cancel less than 7 days")) {
                    RadioButton no=(RadioButton)findViewById(R.id.radio_full_50);
                    cancel_policy="Full refund if cancel before 7 days, 50% refund if cancel less than 7 days";
                    no.setChecked(true);
                }
                if(fd2.getCancel().equals("Full refund if cancel before 7 days, no refund if cancel less than 7 days")) {
                    RadioButton no=(RadioButton)findViewById(R.id.radio_full_0);
                    cancel_policy="Full refund if cancel before 7 days, no refund if cancel less than 7 days";
                    no.setChecked(true);
                }
            }else{
                position = Integer.parseInt(value);
                fd = MyRecyclerAdapter.feedItemList.get(position);
                isEdit = true;
                location.setVisibility(View.GONE);
                city.setVisibility(View.GONE);
                postcode.setVisibility(View.GONE);
                dropdown.setVisibility(View.GONE);
                photos = fd.photos;
                description.setText(fd.getDescription());
                price.setText(Double.toString(fd.getPrice()));
                startTime.setText(fd.getStartTime());
                endTime.setText(fd.getEndTime());
                startDate.setText(fd.getStartDates());
                endDate.setText(fd.getEndDates());
                spinner.setSelection(fd.getFilter());
                if(fd.getCancel().equals("No refund")) {
                    RadioButton no=(RadioButton)findViewById(R.id.radio_norefund);
                    cancel_policy="No refund";
                    no.setChecked(true);
                }
                if(fd.getCancel().equals("80% refund rate at any time")) {
                    RadioButton no=(RadioButton)findViewById(R.id.radio_80refund);
                    cancel_policy="80% refund rate at any time";
                    no.setChecked(true);
                }
                if(fd.getCancel().equals("Full refund if cancel before 7 days, 50% refund if cancel less than 7 days")) {
                    RadioButton no=(RadioButton)findViewById(R.id.radio_full_50);
                    cancel_policy="Full refund if cancel before 7 days, 50% refund if cancel less than 7 days";
                    no.setChecked(true);
                }
                if(fd.getCancel().equals("Full refund if cancel before 7 days, no refund if cancel less than 7 days")) {
                    RadioButton no=(RadioButton)findViewById(R.id.radio_full_0);
                    cancel_policy="Full refund if cancel before 7 days, no refund if cancel less than 7 days";
                    no.setChecked(true);
                }
            }

        } else{
            isEdit = false;
            fd = new FeedItem();
        }


        AdapterView.OnItemSelectedListener statelistener=new AdapterView.OnItemSelectedListener(){

            @Override
                    public void onItemSelected(AdapterView<?>spinner,View container,
                    int position,long id){
                state=state_list[position];
            }

            @Override
                    public void onNothingSelected(AdapterView<?>arg0){
                state=null;
            }
        };
        dropdown.setOnItemSelectedListener(statelistener);

        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), selected_p);

            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit){
                    filter=spinner.getSelectedStrings();
                    String starttime = startTime.getText().toString().trim();
                    String endtime = endTime.getText().toString().trim();
                    String startdate = startDate.getText().toString().trim();
                    String enddate = endDate.getText().toString().trim();
                    String description_parking = description.getText().toString().trim();
                    double price_parking=Double.parseDouble(price.getText().toString().trim());
                    String renter=null;
                    if(fd.getRentedTime().size()!=0){
                        for (HashMap.Entry<String, ArrayList<String>> innerEntry : fd.getRentedTime().entrySet()) {
                            renter = innerEntry.getKey();
                        }
                    }
                    if(isEmpty(description)||isEmpty(price)||isEmpty(startTime)||isEmpty(startDate)||isEmpty(endTime)||isEmpty(endDate)){
                        AlertDialog alertDialog = new AlertDialog.Builder(AddActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Please fill all the Text field");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else{

                        if(check(starttime,endtime,startdate,enddate)){
                            viewProgressBar.setVisibility(View.VISIBLE);

                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("filter").setValue(filter);
                           // mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("filter").setValue(filter);
                            //System.out.println(mFirebaseUser.getUid());
                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("startTime").setValue(starttime);
                            //mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("startTime").setValue(starttime);

                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("endTime").setValue(endtime);
                            //mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("endTime").setValue(endtime);

                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("startDates").setValue(startdate);
                           // mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("startDates").setValue(startdate);

                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("endDates").setValue(enddate);
                           // mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("endDates").setValue(enddate);

                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("description").setValue(description_parking);
                            //mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("description").setValue(description_parking);


                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("price").setValue(price_parking);
                           // mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("price").setValue(price_parking);

                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("cancelpolicy").setValue(cancel_policy);
                            //mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("cancelpolicy").setValue(cancel_policy);

                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("photos").setValue(photos);
                            //mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("photos").setValue(photos);

                           /* if(renter!=null){
                                mDatabase.child("users").child(renter).child("renting").child(fd.getIdentifier()).child("filter").setValue(filter);
                                mDatabase.child("users").child(renter).child("renting").child(fd.getIdentifier()).child("startTime").setValue(starttime);
                                mDatabase.child("users").child(renter).child("renting").child(fd.getIdentifier()).child("endTime").setValue(endtime);
                                mDatabase.child("users").child(renter).child("renting").child(fd.getIdentifier()).child("startDates").setValue(startdate);
                                mDatabase.child("users").child(renter).child("renting").child(fd.getIdentifier()).child("endDates").setValue(enddate);
                                mDatabase.child("users").child(renter).child("renting").child(fd.getIdentifier()).child("description").setValue(description_parking);
                                mDatabase.child("users").child(renter).child("renting").child(fd.getIdentifier()).child("cancelpolicy").setValue(cancel_policy);
                                mDatabase.child("users").child(renter).child("renting").child(fd.getIdentifier()).child("photos").setValue(photos);
                                mDatabase.child("users").child(renter).child("renting").child(fd.getIdentifier()).child("price").setValue(price_parking);
                            }*/



                            viewProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddActivity.this, "Spot Added!",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddActivity.this, ActionActivity.class);//change to UserActivity.class
                            startActivity(intent);


                        }else{
                            AlertDialog alertDialog = new AlertDialog.Builder(AddActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Please make sure time difference is larger than 1 hour");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();

                        }
                    }

                }else {
                    filter = spinner.getSelectedStrings();
                    String starttime = startTime.getText().toString().trim();
                    String endtime = endTime.getText().toString().trim();
                    String startdate = startDate.getText().toString().trim();
                    String enddate = endDate.getText().toString().trim();
                    String address = location.getText().toString().trim();
                    String description_parking = description.getText().toString().trim();
                    double price_parking=0;
                    if(price.getText().toString().length()!=0){
                    price_parking = Double.parseDouble(price.getText().toString().trim());}
                    String city_input = city.getText().toString().trim();
                    String postcode_input = postcode.getText().toString().trim();


                    if ((state == null) || isEmpty(postcode) || isEmpty(city) || isEmpty(location) || isEmpty(description) || isEmpty(price) || isEmpty(startTime) || isEmpty(startDate) || isEmpty(endTime) || isEmpty(endDate)) {
                        AlertDialog alertDialog = new AlertDialog.Builder(AddActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Please fill all the Text field");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    } else {

                        if (check(starttime, endtime, startdate, enddate)) {
                            identifier = mFirebaseAuth.getCurrentUser().getUid() + Long.toString(System.currentTimeMillis());
                            String full_address = address + "," + city_input + "," + state + postcode_input;
                            fd.setIdentifier(identifier);
                            fd.setActivity(true);
                            fd.setCancel(cancel_policy);
                            fd.setDescription(description_parking);


                            fd.setHost(mFirebaseUser.getUid());

                            fd.setStartDates(startdate);
                            fd.setEndDates(enddate);
                            fd.setStartTime(starttime);
                            fd.setEndTime(endtime);
                            fd.setPrice(price_parking);
                            fd.setFilter(filter);
                            System.out.println("checking past bookings now");
                            if(!checkPast(fd)){
                                AlertDialog alertDialog = new AlertDialog.Builder(AddActivity.this).create();
                                alertDialog.setTitle("Alert");
                                alertDialog.setMessage("Please make sure there are no date/time conflicts with this existing spot.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Help",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                AlertDialog alertDialog = new AlertDialog.Builder(AddActivity.this).create();
                                                alertDialog.setTitle("Help");
                                                alertDialog.setMessage("Remember: Check to make sure the spot you are trying to add does not already exist in this same time frame. ");
                                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                alertDialog.show();
                                            }
                                        });
                                alertDialog.show();
                            }else{
                                new AddressOperation(self).execute(full_address);
                                Intent intent = new Intent(AddActivity.this, MainActivity.class);//change to UserActivity.class
                                startActivity(intent);
                            }

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(AddActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Please make sure time-slot is larger than 1 hour and the starting/ending credentials are valid.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Help",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            AlertDialog alertDialog = new AlertDialog.Builder(AddActivity.this).create();
                                            alertDialog.setTitle("Help");
                                            alertDialog.setMessage("Remember: your start/end can't be before the current date/time, your end date/time cannot be before the start. Spots must be added in 1 hour increments. ");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            alertDialog.show();
                                        }
                                    });
                            alertDialog.show();

                        }
                    }


                }

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private Boolean checkPast(final FeedItem feeder){
        System.out.println("inside past checker");
        final Boolean[] resulter = {true};
        if(!hostList.isEmpty()) {
            for (int i = 0; i < hostList.size(); i++) {
                System.out.println("spot match found" + feeder.getAddress());
                long newpoststart = Long.parseLong(feeder.getStartDates().replace("-", "") + feeder.getStartTime().replaceAll("\\D+", ""));
                long newpostend = Long.parseLong(feeder.getEndDates().replace("-", "") + feeder.getEndTime().replaceAll("\\D+", ""));

                long onlinestart = Long.parseLong(hostList.get(i).getStartDates().replace("-", "") + hostList.get(i).getStartTime().replaceAll("\\D+", ""));
                long onlineend = Long.parseLong(hostList.get(i).getEndDates().replace("-", "") + hostList.get(i).getEndTime().replaceAll("\\D+", ""));

                if (newpoststart < onlinestart && newpostend > onlinestart) {
                    resulter[0] = false;
                }
                if (newpoststart == onlinestart) {
                    resulter[0] = false;
                }
                if (newpoststart > onlinestart && newpoststart < onlineend) {
                    resulter[0] = false;
                }
            }
        }
        return resulter[0];
    }

    public void getItemsHosting(){
        DatabaseReference database = mDatabase.child("users/"+mFirebaseUser.getUid()+"/hosting");
        //DatabaseReference database = mDatabase.child("parking-spots-hosting");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    HashMap<String,String> user_map= (HashMap<String,String>)dataSnapshot.getValue();
                    getSpot_host(user_map);}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void getSpot_host(HashMap<String,String> spot_map){
        Vector<String> names=new Vector<String>();
        for (HashMap.Entry<String, String> entry : spot_map.entrySet()) {
            names.add(entry.getKey());
        }
        for(int i=0;i<names.size();i++){
            DatabaseReference database_p = mDatabase.child("parking-spots-hosting").child(names.get(i));
            database_p.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Object> user_map = (HashMap<String,Object>) dataSnapshot.getValue();

                    if (user_map != null) {
                        FeedItem user_all = new FeedItem();
                        for (HashMap.Entry<String, Object> innerEntry : user_map.entrySet()) {
                            String key = innerEntry.getKey();
                            Object value = innerEntry.getValue();
                            if (key.equals("latitude")) {
                                user_all.setLatitude((double) value);
                            }
                            if (key.equals("address")) {
                                user_all.setAddress((String) value);
                            }

                            if (key.equals("longitude")) {
                                user_all.setLongitude((double) value);
                            }
                            if (key.equals("spotID")) {
                                user_all.setSpotID((String) value);
                            }
                            if (key.equals("startDates")) {
                                user_all.setStartDates((String) value);
                            }
                            if (key.equals("endDates")) {
                                user_all.setEndDates((String) value);
                            }
                            if (key.equals("startTime")) {
                                user_all.setStartTime((String) value);
                            }
                            if (key.equals("endTime")) {
                                user_all.setEndTime((String) value);
                            }
                            if (key.equals("price")) {
                                user_all.setPrice(Double.parseDouble( (value + "")));
                            }
                            if (key.equals("bookings")) {
                                user_all.setBookings(Integer.parseInt((String) (value + "")));
                            }
                            if (key.equals("cancel")) {
                                user_all.setCancel((String) value);
                            }
                            if (key.equals("description")) {
                                user_all.setDescription((String) value);
                            }
                            if (key.equals("rating")) {
                                ArrayList<String> temp=(ArrayList<String>) value;
                                user_all.setRating(temp);
                            }
                            if (key.equals("activity")) {
                                user_all.setActivity((Boolean) value);
                            }
                            if (key.equals("filter")) {
                                Vector v = new Vector((ArrayList<String>) value);
                                user_all.setFilter(v);
                            }
                            if (key.equals("host")) {
                                user_all.setHost((String) value);
                            }
                            if (key.equals("photos")) {
                                user_all.setPhotos((ArrayList<String>) value);
                            }
                            if (key.equals("rentedTime")) {
                                user_all.setRentedTime((Map<String, ArrayList<String>>) value);
                            }
                            if (key.equals("identifier")) {
                                user_all.setIdentifier((String) value);
                            }
                            if (key.equals("review")) {
                                user_all.setReview((ArrayList<String>) value);
                            }
                            if (key.equals("currentRenter")) {
                                user_all.setCurrentRenter((String) value);
                            }
                        }
                        hostList.add(user_all);
                    }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_norefund:
                if (checked)
                    cancel_policy="No refund";
                    break;
            case R.id.radio_80refund:
                if (checked)
                    cancel_policy="80% refund rate at any time";
                    break;
            case R.id.radio_full_50:
                if (checked)
                    cancel_policy="Full refund if cancel before 7 days, 50% refund if cancel less than 7 days";
                    break;
            case R.id.radio_full_0:
                if (checked)
                    cancel_policy="Full refund if cancel before 7 days, no refund if cancel less than 7 days";
                    break;
        }
    }
    public void write_new_spot(FeedItem Fd) {
        mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting").child(fd.getIdentifier()).setValue(fd.getIdentifier());
       /* ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ref.setValue(fd.getIdentifier());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });*/

        mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).setValue(Fd);
    }

    public void setFeedItem(String jsonString)
    {
        try
        {
            double[] latlng = AddressOperation.getCoordinatesFromJSON(jsonString);
            spotID = AddressOperation.getIDfromJSON(jsonString);
            fd.setSpotID(spotID);
            fd.setAddress(AddressOperation.getFormattedAddressFromJSON(jsonString));
            fd.setLatitude(latlng[0]);
            fd.setLongitude(latlng[1]);

            write_new_spot(fd);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            AddressOperation.showAddressFaultDialog(self);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case selected_p:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        s_image = BitmapFactory.decodeStream(imageStream);

                        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
                        s_image.compress(Bitmap.CompressFormat.PNG, 100, bYtE);

                        byte[] byteArray = bYtE.toByteArray();
                        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        fd.photos.add(imageFile);
                        photos.add(imageFile);
                        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.photoLayout);
                        TextView valueTV = new TextView(this);
                        valueTV.setText("image"+fd.photos.size());
                        valueTV.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
                        ((LinearLayout) linearLayout).addView(valueTV);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }


    }

    private boolean check(String starttime,String endtime,String startdate,String enddate){
        boolean checkdate=true;
        try{

            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            String start_s=startdate+" "+starttime;
            String end_s=enddate+" "+endtime;
            Date time1 = df.parse(start_s.substring(0,start_s.length()-2)+":00");
            Date time2 = df.parse(end_s.substring(0,end_s.length()-2)+":00");
            long diff = time2.getTime() - time1.getTime();

            if(diff>=0/*3600000*/){
                checkdate=true;
            }else{
                checkdate=false;
                return checkdate;
            }

            String today=getToday(df);
            Date d = df.parse(today);
            if(time1.getTime() > d.getTime()){
                checkdate=true;
            }else{
                checkdate=false;
                return checkdate;
            }


        }catch(ParseException ex){
            ex.printStackTrace();
        }

        return checkdate;
    }
    public String getToday(java.text.SimpleDateFormat  dformat){
        Date date = new Date();
        return dformat.format(date);
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

   /* public void upload(Bitmap image, StorageReference spotImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = spotImage.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                //pop-up
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }*/

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Add Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}


