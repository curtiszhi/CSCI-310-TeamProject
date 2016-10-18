package com.csci310.ParkHere;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Vector;


/**
 * Created by seanyuan on 10/7/16.
 */

public class AddActivity extends AppCompatActivity {
    private TextView location, description, price, startTime, endTime, startDate, endDate;
    private Button post, photo;
    private MultiSelectionSpinner spinner;
    private String[] items = {"handicap", "Compact", "SUV", "Truck", "covered parking"};
    private static final int selected_p = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String spotID;
    private Vector<Bitmap> photos;
    private String cancel_policy;
    private List<String> filter;
    private Bitmap s_image;
    private StorageReference spot_image;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("New Listing");
        setContentView(R.layout.activity_create);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://parkhere310-3701d.appspot.com");
        spinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner1);
        spinner.setItems(items);

        photos = new Vector<Bitmap>();
        photo = (Button) findViewById(R.id.photo);
        post = (Button) findViewById(R.id.postButton);
        location = (TextView) findViewById(R.id.Address);
        description = (TextView) findViewById(R.id.description);
        price = (TextView) findViewById(R.id.price);
        startTime = (TextView) findViewById(R.id.startTimeEditText);
        endTime = (TextView) findViewById(R.id.endTimeEditText);
        startDate = (TextView) findViewById(R.id.startDateEditText);
        endDate = (TextView) findViewById(R.id.endDateEditText);


        new DatePicker(AddActivity.this, R.id.startDateEditText);
        new DatePicker(AddActivity.this, R.id.endDateEditText);
        new TimePicker(AddActivity.this, R.id.startTimeEditText);
        new TimePicker(AddActivity.this, R.id.endTimeEditText);

        spotID = mFirebaseUser.getUid() + Long.toString(System.currentTimeMillis());
//
        photo.setOnClickListener(new View.OnClickListener() {

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
                                        filter=spinner.getSelectedStrings();
                                        String starttime = startTime.getText().toString().trim();
                                        String endtime = endTime.getText().toString().trim();
                                        String startdate = startDate.getText().toString().trim();
                                        String enddate = endDate.getText().toString().trim();
                                        String address = location.getText().toString().trim();
                                        String description_parking = description.getText().toString().trim();
                                        double price_parking=Double.parseDouble(price.getText().toString().trim());


                FeedItem fd=new FeedItem();
                fd.setActivity(true);
                fd.setCancel(cancel_policy);
                fd.setDescription(description_parking);
                fd.setSpotID(spotID);
                fd.setRating(null);
                fd.setHouse(address);
                fd.setStartDates(startdate);
                fd.setEndDates(enddate);
                fd.setStartTime(starttime);
                fd.setEndTime(endtime);
                fd.setPrice(price_parking);
                fd.setFilter(filter);
                fd.setHost(mFirebaseUser.getUid());
                write_new_spot(fd);



                StorageReference imagesRef = storageRef.child(spotID);

                for(int i=0;i<photos.size();i++){
                    spot_image = imagesRef.child("image" + i + ".jpg");
                    upload(photos.get(i), spot_image);
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
                    cancel_policy="Full refund if cancell before 7 days, 50% refund if cancell less than 7 days";
                    break;
            case R.id.radio_full_0:
                if (checked)
                    cancel_policy="Full refund if cancell before 7 days, no refund if cancell less than 7 days";
                    break;
        }
    }
    public void write_new_spot(FeedItem Fd) {
        mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting").setValue(Fd.getSpotID());
        mDatabase.child("parking-spots").child(Fd.getSpotID()).setValue(Fd);


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
                        photos.add(s_image);
                        //LinearLayout linearLayout = (LinearLayout)findViewById(R.id.photoLayout);
                        RelativeLayout relLayout = (RelativeLayout)findViewById(R.id.imageTitleLayout);
                        TextView valueTV = new TextView(this);
                        valueTV.setText("image"+photos.size());
                        valueTV.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
                        ((RelativeLayout) relLayout).addView(valueTV);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }


    }


    public void upload(Bitmap image, StorageReference spotImage) {
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
    }

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








/**
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*house = (EditText) findViewById(R.id.cigarName);
                type = (EditText) findViewById(R.id.cigarType);
                length = (EditText) findViewById(R.id.length);
                gauge = (EditText) findViewById(R.id.gauge);
                amount = (EditText) findViewById(R.id.amount);
                price = (EditText) findViewById(R.id.price);
                location = (EditText) findViewById(R.id.location);
                notes = (EditText) findViewById(R.id.notes);
                ratingBar = (RatingBar) findViewById(R.id.ratingBarSetter);
                //if rating value is changed,
                //display the current rating value in the result (textview) automatically
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser) {

                        txtRatingValue = (String.valueOf(rating));

                    }
                });



                mDatabase = FirebaseDatabase.getInstance().getReference();
                mFirebaseAuth = FirebaseAuth.getInstance();
                mFirebaseUser_universal = mFirebaseAuth.getCurrentUser();

                FeedItem item = new FeedItem();
                item.setTitle(name.getText().toString());
                item.setThumbnail(R.drawable.common_google_signin_btn_icon_dark);
                item.setType(type.getText().toString());
                item.setPrice(price.getText().toString());
                item.setQuantity(amount.getText().toString());
                item.setRatingValue(txtRatingValue);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("users/" + mFirebaseUser_universal.getUid() + "/humidor/", item);
                Intent intent = new Intent(AddActivity.this, ListingActivity.class);
                mDatabase.updateChildren(childUpdates);
                startActivity(intent);
            }
        });

    }
**/


