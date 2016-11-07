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


        new DatePicker(AddActivity.this, R.id.startDateEditText);
        new DatePicker(AddActivity.this, R.id.endDateEditText);
        new TimePicker(AddActivity.this, R.id.startTimeEditText);
        new TimePicker(AddActivity.this, R.id.endTimeEditText);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            value = bundle.getString("ItemPosition");
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

                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("filter").setValue(filter);
                            mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("filter").setValue(filter);

                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("startTime").setValue(starttime);
                            mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("startTime").setValue(starttime);

                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("endTime").setValue(endtime);
                            mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("endTime").setValue(endtime);

                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("startDates").setValue(startdate);
                            mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("startDates").setValue(startdate);

                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("endDates").setValue(enddate);
                            mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("endDates").setValue(enddate);

                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("description").setValue(description_parking);
                            mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("description").setValue(description_parking);


                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("price").setValue(price_parking);
                            mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("price").setValue(price_parking);

                            mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("cancelpolicy").setValue(cancel_policy);
                            mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier()).child("cancelpolicy").setValue(cancel_policy);
                            Intent intent = new Intent(AddActivity.this, UserActivity.class);//change to UserActivity.class
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
                           
                            new AddressOperation(self).execute(full_address);
                            Intent intent = new Intent(AddActivity.this, MainActivity.class);//change to UserActivity.class
                            startActivity(intent);

                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(AddActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Please make sure time difference is larger than 1 hour or the starting time is before the current time ");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
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
        ref=mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting/" + fd.getIdentifier());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ref.setValue(fd);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

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
            long diffHours = diff / (60 * 60 * 1000) % 24;
            if(diffHours>=1){
                checkdate=true;
            }else{
                checkdate=false;
            }

            String today=getToday(df);
            Date d = df.parse(today);
            if(time1.getTime() > d.getTime()){
                checkdate=true;
            }else{
                checkdate=false;
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


