package com.csci310.ParkHere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by seanyuan on 10/17/16.
 */
public class DetailedViewActivity extends AppCompatActivity{
    private Button hostPublic;
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
    private Vector<Bitmap> spotPhoto;
    private Button editButton,confirmButton,cancelButton;
    int position;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Bundle bundle = getIntent().getExtras();
        value = bundle.getString("ItemPosition");
        position = Integer.parseInt(value);
        fd = MyRecyclerAdapter.feedItemList.get(position);
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
        editButton= (Button) findViewById(R.id.editButton);
        confirmButton=(Button) findViewById(R.id.confirmButton);
        cancelButton=(Button) findViewById(R.id.cancelButton);;
        count=0;

        setUp();
        downloadPhoto();
        display();
        image_view.setOnTouchListener(new OnSwipeTouchListener(DetailedViewActivity.this) {
            public void onSwipeRight() {
                if(count>0){
                    System.out.println("swiped right" + count);
                    count--;
                    Bitmap b = spotPhoto.get(count);
                    image_view.setImageBitmap(b);
                    int temp=count+1;
                    image_label.setText(temp+" of "+spotPhoto.size()+" images");
                }

            }
            public void onSwipeLeft() {
                if(count<spotPhoto.size()-1){
                    System.out.println("swiped left" + count);
                    count++;
                    Bitmap b = spotPhoto.get(count);
                    image_view.setImageBitmap(b);
                    int temp=count+1;
                    image_label.setText(temp+" of "+spotPhoto.size()+" images");
                }

            }
        });

        hostPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //public profile!!!!!
            }
        });
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedViewActivity.this, AddActivity.class);
                intent.putExtra("ItemPosition", value);
                startActivity(intent);
            }
        });
    }

    private void setUp(){
        hostPublic.setText(fd.getHost());
        ratingBar.setRating(fd.getRating());
        address.setText(fd.getAddress());
        price.setText(Double.toString(fd.getPrice()));
        String time_frame=fd.getStartDates()+ " "+ fd.getStartTime()+" to "+fd.getEndDates()+" "+fd.getEndTime();
        time.setText(time_frame);
        String filter_spot="";
        for(int i=0;i<fd.getFilter().size();i++){
            if(i!=fd.getFilter().size()-1){
                filter_spot=filter_spot+fd.getFilter().get(i)+", ";}
            else{
                filter_spot=filter_spot+fd.getFilter().get(i);
            }
        }
        filters.setText(filter_spot);
        description.setText(fd.getDescription());
        cancel.setText(fd.getCancel());
    }

    private void downloadPhoto(){
        spotPhoto = fd.photos;
    }

    public void display(){
        image_view.setImageBitmap(spotPhoto.get(0));
        image_label.setText("1 of "+spotPhoto.size()+" images");
    }

}

