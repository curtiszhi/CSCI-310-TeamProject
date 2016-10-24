package com.csci310.ParkHere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RentActivity extends AppCompatActivity {

    private FeedItem fd;
    private Vector<Bitmap> spotPhoto;

    private Button hostPublic;
    private Button rent;
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
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        fd = new FeedItem();
        fd.setActivity(true);
        fd.setCancel("test cancel policy");
        fd.setDescription("test description");
        fd.setRating(null);
        fd.setAddress("dummy address");
        fd.setStartDates("10/4");
        fd.setEndDates("10/5");
        fd.setStartTime("9:00");
        fd.setEndTime("8:00");
        fd.setPrice(3.45);
        List<String> dumb = new ArrayList<>();
        dumb.add("Handicapped");
        dumb.add("Shaded");
        fd.setFilter(dumb);
        fd.setRating(Float.valueOf("4.0"));
        Vector<Bitmap> photodata = new Vector<>();
        Drawable myDrawable = getResources().getDrawable(R.drawable.ic_search);
        Bitmap anImage      = ((BitmapDrawable) myDrawable).getBitmap();
        photodata.add(anImage);
        Drawable myDrawable2 = getResources().getDrawable(R.drawable.ic_user);
        Bitmap anImage2      = ((BitmapDrawable) myDrawable2).getBitmap();
        photodata.add(anImage2);
        fd.photos = photodata;



        //storage = FirebaseStorage.getInstance();
        //storageRef = storage.getReferenceFromUrl("gs://parkhere310-3701d.appspot.com");
        //imagesRef = storageRef.child(fd.getSpotID());
        //spotPhoto= new Vector<Bitmap>();

        image_view=(ImageView) findViewById(R.id.image);
        image_label=(TextView) findViewById(R.id.image_label);
        rent = (Button) findViewById(R.id.rentButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        address= (TextView) findViewById(R.id.address);
        price= (TextView) findViewById(R.id.price);
        time= (TextView) findViewById(R.id.time);
        filters= (TextView) findViewById(R.id.filters);
        description= (TextView) findViewById(R.id.description);
        cancel= (TextView) findViewById(R.id.cancel);

        count=0;

        setUp();
        downloadPhoto();
        display();
        image_view.setOnTouchListener(new OnSwipeTouchListener(RentActivity.this) {

            public void onSwipeRight() {
                if(count>0){
                    System.out.println("swiped right" + count);
                    count--;
                    Bitmap b = spotPhoto.get(count);
                    image_view.setImageBitmap(b);
                    //image_view.setImageBitmap(spotPhoto.get(count));
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
                    //image_view.setImageBitmap(spotPhoto.get(count));
                    int temp=count+1;
                    image_label.setText(temp+" of "+spotPhoto.size()+" images");
                }

            }


        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RentActivity.this, PayActivity.class);
                intent.putExtra("ItemPosition", price.getText());
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
        //get all files inside imagesref
        //StorageReference islandRef = imagesRef.child("");
        spotPhoto = fd.photos;

        /*final long ONE_MEGABYTE = 1024 * 1024;
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
        });*/

    }

    public void display(){
        image_view.setImageBitmap(spotPhoto.get(0));
        image_label.setText("1 of "+spotPhoto.size()+" images");
    }

   /* public void initLibrary() {
        PayPal pp = PayPal.getInstance();

        if (pp == null) {  // Test to see if the library is already initialized

            // This main initialization call takes your Context, AppID, and target server
            pp = PayPal.initWithAppID(this, "APP-80W284485P519543T", PayPal.ENV_NONE);

            // Required settings:

            // Set the language for the library
            pp.setLanguage("en_US");

            // Some Optional settings:

            // Sets who pays any transaction fees. Possible values are:
            // FEEPAYER_SENDER, FEEPAYER_PRIMARYRECEIVER, FEEPAYER_EACHRECEIVER, and FEEPAYER_SECONDARYONLY
            pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER);

            // true = transaction requires shipping
            pp.setShippingEnabled(true);

            _paypalLibraryInit = true;
        }
    }
*/
}