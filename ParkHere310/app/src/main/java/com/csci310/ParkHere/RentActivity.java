package com.csci310.ParkHere;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class RentActivity extends AppCompatActivity {

    private FeedItem fd;
    private Vector<String> spotPhoto;


    private Button hostPublic;
    private Button rent;
    private RatingBar ratingBar;
    private TextView address;
    private TextView price;
    private TextView time;
    private TextView filters;
    private TextView description;
    private TextView cancel;
    private DatabaseReference ref;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference imagesRef;
    private ImageView image_view;
    private TextView image_label;
    private LinearLayout review_layout;
    private int count;
    private Vector<String> rentedTime;
    private Map<String,Vector<String>> renterRentTime;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static DatabaseReference mDatabase;
    private String total_price;
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;
    String value;
    int position;

    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(paypalConfig.PAYPAL_CLIENT_ID);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        Bundle bundle = getIntent().getExtras();
        value = bundle.getString("ItemPosition");
        position = Integer.parseInt(value);
        fd = MyRecyclerAdapter.feedItemList.get(position);

        /*rentedTime=new Vector<>();
        rentedTime.add(0,fd.getStartDates()+" "+fd.getStartTime());
        rentedTime.add(1,fd.getEndDates()+" "+fd.getEndTime());
        renterRentTime.put(fd.getHost(),rentedTime);*/

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
        review_layout=(LinearLayout) findViewById(R.id.review);

        count=0;

        setUp();
        downloadPhoto();
        display();

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);



        image_view.setOnTouchListener(new OnSwipeTouchListener(RentActivity.this) {

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

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayment();
            }
        });
    }
    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    private void getPayment() {
        //Getting the amount from editText
        total_price = price.getText().toString();

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(total_price)), "USD", "Parking fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    ref=mDatabase.child("users").child(mFirebaseUser.getUid()).child("renting/" + fd.getSpotID());
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

                    mDatabase.child("parking-spots-renting").child(fd.getSpotID()).setValue(fd);
                    Intent intent = new Intent(RentActivity.this, MainActivity.class);//change to UserActivity.class
                    startActivity(intent);

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    private void setUp(){
        hostPublic.setText(fd.getHost());
       // ratingBar.setRating(fd.getRating());
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
        for(int i=0;i<fd.getReview().size();i++){
            TextView review_text = new TextView(this);
            review_text.setText(fd.getReview().get(i));
            review_text.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
            ((LinearLayout) review_layout).addView(review_text);
        }
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
        byte[] decodedString = Base64.decode(spotPhoto.get(0), Base64.DEFAULT);
        Bitmap b = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        image_view.setImageBitmap(b);
        image_label.setText("1 of "+spotPhoto.size()+" images");
    }


}
