package com.csci310.ParkHere;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 * Created by seanyuan on 10/7/16.
 */

public class AddActivity extends AppCompatActivity {
    private TextView location,description,price,startTime, endTime, startDate, endDate;
    private RadioButton rn;
    private Button post, photo_1,photo2,photo3;
    private MultiSelectionSpinner spinner;
    private String[] items={"handicap","Compact", "SUV", "Truck", "covered parking"};
    private static final int selected_p=1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String spotID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("New Listing");
        setContentView(R.layout.activity_create);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        spinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner1);
        spinner.setItems(items);

        photo_1=(Button) findViewById(R.id.photo);
        post=(Button) findViewById(R.id.postButton);
        new DatePicker(AddActivity.this, R.id.startDateEditText);
        new DatePicker(AddActivity.this, R.id.endDateEditText);
        new TimePicker(AddActivity.this, R.id.startTimeEditText);
        new TimePicker(AddActivity.this, R.id.endTimeEditText);
        spotID="just for test";

        post.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {



                                    }
        });
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
                    Bitmap s_image= BitmapFactory.decodeFile(filepath);

                    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.photoLayout);
                    TextView valueTV = new TextView(this);
                    valueTV.setText("hallo hallo");
                    valueTV.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
                    ((LinearLayout) linearLayout).addView(valueTV);
                }
                break;
            default:
                break;
        }


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


