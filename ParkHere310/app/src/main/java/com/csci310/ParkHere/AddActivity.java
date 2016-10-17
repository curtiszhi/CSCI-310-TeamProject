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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.cast.framework.media.ImagePicker;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by seanyuan on 10/7/16.
 */

public class AddActivity extends AppCompatActivity {
    private TextView location,description,price,startTime, endTime, startDate, endDate;
    private RadioButton c1,c2,c3,c4;
    private Button post, photo;
    private MultiSelectionSpinner spinner;
    private String[] items={"handicap","Compact", "SUV", "Truck", "covered parking"};
    private static final int selected_p=1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String spotID;
    private Vector<Bitmap> photos;
    private int cancel_policy;
    private List<Integer> filter;
    private Bitmap s_image;
    private StorageReference spot_image;
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

        photos=new Vector<Bitmap>();
        photo=(Button) findViewById(R.id.photo);
        post=(Button) findViewById(R.id.postButton);
        location=(TextView) findViewById(R.id.Address);
        description=(TextView) findViewById(R.id.description);
        price=(TextView) findViewById(R.id.price);
        startTime = (TextView) findViewById(R.id.startTimeEditText);
        endTime = (TextView) findViewById(R.id.endTimeEditText);
        startDate = (TextView) findViewById(R.id.startDateEditText);
        endDate = (TextView) findViewById(R.id.endDateEditText);
        c1=(RadioButton) findViewById(R.id.radio_norefund);
        c2=(RadioButton) findViewById(R.id.radio_80refund);
        c3=(RadioButton) findViewById(R.id.radio_full_50);
        c4=(RadioButton) findViewById(R.id.radio_full_0);

        new DatePicker(AddActivity.this, R.id.startDateEditText);
        new DatePicker(AddActivity.this, R.id.endDateEditText);
        new TimePicker(AddActivity.this, R.id.startTimeEditText);
        new TimePicker(AddActivity.this, R.id.endTimeEditText);

        spotID="emma"+Long.toString(System.currentTimeMillis());

        post.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        filter=spinner.getSelectedIndicies();
                                        String starttime = startTime.getText().toString().trim();
                                        String endtime = endTime.getText().toString().trim();
                                        String startdate = startDate.getText().toString().trim();
                                        String enddate = endDate.getText().toString().trim();
                                        String address = location.getText().toString().trim();
                                        String description_parking = description.getText().toString().trim();
                                        int price_parking=Integer.parseInt(price.getText().toString().trim());
                                        boolean checked_c1 = c1.isChecked();
                                        boolean checked_c2 = c2.isChecked();
                                        boolean checked_c3 = c3.isChecked();
                                        boolean checked_c4 = c4.isChecked();
                                        if(checked_c1){
                                            cancel_policy=1;
                                        }
                                        if(checked_c2){
                                            cancel_policy=2;
                                        }
                                        if(checked_c3){
                                            cancel_policy=3;
                                        }
                                        if(checked_c4){
                                            cancel_policy=4;
                                        }

                                        upload(s_image, spot_image);
                                    }
        });
    }

    public void write_new_spot(){
        mDatabase.child("users").child("Fq2XZx5727XQ8U06fjQJN1jyzCA3").child("hosting");
        //mDatabase.child("parking-spots").child(spotID).setValue();

    }





    public void p1Click(View v){
        Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,selected_p);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case selected_p:
                if(requestCode==RESULT_OK){
                    Uri uri=data.getData();
                    String[] projection={MediaStore.Images.Media.DATA};
                    Cursor cursor=getContentResolver().query(uri,projection,null,null,null);
                    cursor.moveToFirst();
                    int column=cursor.getColumnIndex(projection[0]);
                    String filepath=cursor.getString(column);
                    cursor.close();
                    s_image= BitmapFactory.decodeFile(filepath);
                    photos.add(s_image);

                    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.photoLayout);
                    TextView valueTV = new TextView(this);
                    valueTV.setText("hallo hallo");
                    valueTV.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
                    ((LinearLayout) linearLayout).addView(valueTV);

                    StorageReference imagesRef = storageRef.child(spotID);
                    spot_image = imagesRef.child(spotID+"/image.jpg");

                }
                break;
            default:
                break;
        }


    }

    public void upload(Bitmap image,StorageReference spotImage ){
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
                mFirebaseUser = mFirebaseAuth.getCurrentUser();

                FeedItem item = new FeedItem();
                item.setTitle(name.getText().toString());
                item.setThumbnail(R.drawable.common_google_signin_btn_icon_dark);
                item.setType(type.getText().toString());
                item.setPrice(price.getText().toString());
                item.setQuantity(amount.getText().toString());
                item.setRatingValue(txtRatingValue);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("users/" + mFirebaseUser.getUid() + "/humidor/", item);
                Intent intent = new Intent(AddActivity.this, ListingActivity.class);
                mDatabase.updateChildren(childUpdates);
                startActivity(intent);
            }
        });

    }
**/


