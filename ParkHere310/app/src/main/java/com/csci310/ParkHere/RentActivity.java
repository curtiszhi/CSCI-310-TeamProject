package com.csci310.ParkHere;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Vector;

public class RentActivity extends AppCompatActivity {

    private FeedItem spot;
    private Vector<Bitmap> spotPhoto;

    private Button hostPublic;
    private RatingBar ratingBar;
    private TextView address;
    private TextView price;
    private TextView time;
    private TextView filters;
    private TextView description;
    private TextView cancel;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference imagesRef;
    private ImageView image_view;
    private TextView image_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://parkhere310-3701d.appspot.com");
        imagesRef = storageRef.child(spot.getSpotID());
        spotPhoto= new Vector<Bitmap>();

        image_view=(ImageView) findViewById(R.id.image);
        image_label=(TextView) findViewById(R.id.image_label);
        hostPublic = (Button) findViewById(R.id.rentButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        address= (TextView) findViewById(R.id.address);
        price= (TextView) findViewById(R.id.price);
        time= (TextView) findViewById(R.id.time);
        filters= (TextView) findViewById(R.id.filters);
        description= (TextView) findViewById(R.id.description);
        cancel= (TextView) findViewById(R.id.cancel);

        setUp();
        downloadPhoto();
        image_view.setOnTouchListener(new OnSwipeTouchListener(RentActivity.this) {

            public void onSwipeRight() {

            }
            public void onSwipeLeft() {
                
            }


        });

        hostPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //public profile!!!!!
            }
        });
    }

    private void setUp(){
        hostPublic.setText(spot.getHost());
        ratingBar.setRating(spot.getRating());
        address.setText(spot.getAddress());
        price.setText(Double.toString(spot.getPrice()));
        String time_frame=spot.getStartDates()+ " "+ spot.getStartTime()+" to "+spot.getEndDates()+" "+spot.getEndTime();
        time.setText(time_frame);
        String filter_spot="";
        for(int i=0;i<spot.getFilter().size();i++){
            if(i!=spot.getFilter().size()-1){
            filter_spot=filter_spot+spot.getFilter().get(i)+", ";}
            else{
                filter_spot=filter_spot+spot.getFilter().get(i);
            }
        }
        filters.setText(filter_spot);
        description.setText(spot.getDescription());
        cancel.setText(spot.getCancel());
    }

    private void downloadPhoto(){
        //get all files inside imagesref
        StorageReference islandRef = imagesRef.child("");

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                spotPhoto.add(bm);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
               //pop up: fail to download images
            }
        });

    }

}
