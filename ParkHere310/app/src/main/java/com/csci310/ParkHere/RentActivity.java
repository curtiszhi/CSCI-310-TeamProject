package com.csci310.ParkHere;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static android.text.TextUtils.isEmpty;

public class RentActivity extends AppCompatActivity {

    private FeedItem fd;
    private Vector<String> spotPhoto;
    private String name;

    private Button hostPublic;
    private Button rent,wishlist;
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
    private Map<String, Vector<String>> renterRentTime;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static DatabaseReference mDatabase;
    private String total_price;
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;
    String value;
    private String start;
    private String end;
    int position;

    private Map<String, ArrayList<String>> rentList;
    private Map<String, ArrayList<String>> renter_rentedlist;
    private Map<String, ArrayList<String>> host_rentedlist;
    private DatabaseReference ref1;
    private DatabaseReference ref2;
    private boolean check_data=true;

    private int wish_check=0;
    private boolean dismiss=true;

    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(paypalConfig.PAYPAL_CLIENT_ID);
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();


        Bundle bundle = getIntent().getExtras();
        value = bundle.getString("ItemPosition");
        start = bundle.getString("start");
        end = bundle.getString("end");
        wish_check=bundle.getInt("wish");
        System.out.println(start + "rent");
        System.out.println(end + "rent");
        position = Integer.parseInt(value);
        fd = MyRecyclerAdapter.feedItemList.get(position);

        /*rentedTime=new Vector<>();
        rentedTime.add(0,fd.getStartDates()+" "+fd.getStartTime());
        rentedTime.add(1,fd.getEndDates()+" "+fd.getEndTime());
        renterRentTime.put(fd.getHost(),rentedTime);*/

        image_view = (ImageView) findViewById(R.id.image);
        image_label = (TextView) findViewById(R.id.image_label);
        rent = (Button) findViewById(R.id.rentButton);
        wishlist = (Button) findViewById(R.id.wishButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        address = (TextView) findViewById(R.id.address);
        price = (TextView) findViewById(R.id.price);
        time = (TextView) findViewById(R.id.time);
        filters = (TextView) findViewById(R.id.filters);
        description = (TextView) findViewById(R.id.description);
        cancel = (TextView) findViewById(R.id.cancel);
        review_layout = (LinearLayout) findViewById(R.id.review);
        hostPublic = (Button) findViewById(R.id.host_public_button);

        count = 0;

        setUp();
        downloadPhoto();
        display();

        if(wish_check==1){
          popup();
        }

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);


        image_view.setOnTouchListener(new OnSwipeTouchListener(RentActivity.this) {

            public void onSwipeRight() {
                if (count > 0) {
                    System.out.println("swiped right" + count);
                    count--;
                    String temp_p = spotPhoto.get(count);
                    byte[] decodedString = Base64.decode(temp_p, Base64.DEFAULT);
                    Bitmap b = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_view.setImageBitmap(b);
                    int temp = count + 1;
                    image_label.setText(temp + " of " + spotPhoto.size() + " images");
                }

            }

            public void onSwipeLeft() {

                if (count < spotPhoto.size() - 1) {
                    System.out.println("swiped left" + count);
                    count++;
                    String temp_p = spotPhoto.get(count);
                    byte[] decodedString = Base64.decode(temp_p, Base64.DEFAULT);
                    Bitmap b = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_view.setImageBitmap(b);
                    int temp = count + 1;
                    image_label.setText(temp + " of " + spotPhoto.size() + " images");
                }

            }


        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*try {
                    new SplitParkingSpot(fd, start, end);
                }
                catch (Exception e) {e.printStackTrace();}
                finally {*/
                    getPayment();
               // }
            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("users").child(mFirebaseUser.getUid()).child("wishlist").child(fd.getIdentifier()).setValue(fd.getIdentifier());
            }
        });
        hostPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RentActivity.this, publicActivity.class);
                intent.putExtra("ID", fd.getHost());
                System.out.println(fd.getHost());
                startActivity(intent);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    private void getPayment() {
        //Getting the amount from editText
        total_price = price.getText().toString();
        double price_real = Double.parseDouble(total_price.replace("$", ""));

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(price_real)), "USD", "Parking fee",
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    //update activity of spot
//                    mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("activity").setValue(false);

                    rentList = new HashMap<String, ArrayList<String>>();
                    ArrayList<String> startend = new ArrayList<String>();
                    startend.add(start);
                    startend.add(end);
                    rentList.put(mFirebaseUser.getUid(), startend);

                    renter_rentedlist = new HashMap<String, ArrayList<String>>();
                    ArrayList<String> startend1 = new ArrayList<>();
                    startend1.add(start);
                    startend1.add(end);
                    renter_rentedlist.put(mFirebaseUser.getUid(), startend1);

                    host_rentedlist = new HashMap<String, ArrayList<String>>();
                    host_rentedlist = rentList;
                    //update spot renttime
                    ref1 = mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("rentedTime");
                    ref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int counter_1=0;
                                HashMap<String, ArrayList<String>> tempList = (HashMap) dataSnapshot.getValue();
                                for (HashMap.Entry<String, ArrayList<String>> entry : tempList.entrySet()) {
                                    counter_1++;
                                    String key = entry.getKey();
                                    ArrayList<String> value = entry.getValue();
                                    rentList.put(key, value);
                                    if(counter_1==tempList.size()){
                                        ref1.setValue(rentList);
                                    }
                                }


                            }else{
                                ref1.setValue(rentList);

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read  failed: " + databaseError.getCode());
                        }
                    });

                    ref2 = mDatabase.child("parking-spots-hosting").child(fd.getIdentifier()).child("bookings");
                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(check_data) {
                                if (dataSnapshot.exists()) {
                                    Object temp_1 = (Object) dataSnapshot.getValue();

                                    int original_bookings = Integer.parseInt((String) (temp_1 + ""));
                                    ref2.setValue(1 + original_bookings);
                                    check_data = false;

                                } else {
                                    ref2.setValue(1);
                                    check_data = false;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read  failed: " + databaseError.getCode());
                        }
                    });



                    //update user renting list
                    fd.setRentedTime(renter_rentedlist);
                    fd.setActivity(false);
                    mDatabase.child("users").child(mFirebaseUser.getUid()).child("renting").child(fd.getIdentifier()).setValue(fd.getIdentifier());

                    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    String today = getToday(df);
                    double price_t = Double.parseDouble(total_price.replace("$", ""));
                    mDatabase.child("payment").child(mFirebaseUser.getUid()).child("To Parkhere").child(today).setValue(price_t);

                    if(wish_check==1){
                        mDatabase.child("users").child(mFirebaseUser.getUid()).child("wishlist").child(fd.getIdentifier()).setValue(null);
                    }


                    System.out.print("go to user activity");
                    Intent intent = new Intent(RentActivity.this, ActionActivity.class);//change to ActionActivity.class
                    startActivity(intent);

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }
    public String getToday(java.text.SimpleDateFormat  dformat){
        Date date = new Date();
        return dformat.format(date);
    }

    private void setUp() {

        DatabaseReference database = mDatabase.child("users/").child(fd.getHost()).child("userName");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = (String) dataSnapshot.getValue();
                System.out.println(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        hostPublic.post(new Runnable() {
            @Override
            public void run() {
                hostPublic.setText(name);
            }
        });
        //hostPublic.setText(name);
        ratingBar.setRating(fd.calculateRate());
        address.setText(fd.getAddress());
        if(wish_check==0){
        String start1 = start.substring(0,start.length()-2);
        String end1 = end.substring(0,end.length()-2);
        System.out.println(start1);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date time1 = null;
        Date time2 = null;
        try {
            time1 = df.parse(start1+":00");
            time2 = df.parse(end1+":00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = time2.getTime() - time1.getTime();
        long diffHours = diff / (60 * 60 * 1000);
        System.out.println(fd.getPrice() + " " + diffHours);

        price.setText("$" + Double.toString(fd.getPrice() * (diffHours+1)));
        String time_frame = start + " to " + end;
        time.setText(time_frame);}

        String filter_spot = "";
        if (fd.getFilter() != null) {
            for (int i = 0; i < fd.getFilter().size(); i++) {
                if (i != fd.getFilter().size() - 1) {
                    filter_spot = filter_spot + fd.getFilter().get(i) + ", ";
                } else {
                    filter_spot = filter_spot + fd.getFilter().get(i);
                }
            }
        }
        filters.setText(filter_spot);
        description.setText(fd.getDescription());
        cancel.setText(fd.getCancel());
        for (int i = 0; i < fd.getReview().size(); i++) {
            TextView review_text = new TextView(this);
            review_text.setText(fd.getReview().get(i));
            review_text.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
            ((LinearLayout) review_layout).addView(review_text);
        }
    }

    private void downloadPhoto() {
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

    public void display() {
        if (spotPhoto.size() > 0) {
            byte[] decodedString = Base64.decode(spotPhoto.get(0), Base64.DEFAULT);
            Bitmap b = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image_view.setImageBitmap(b);
            image_label.setText("1 of " + spotPhoto.size() + " images");
        }else{
            image_label.setText("0 image");
        }
    }

    private void popup(){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.custom, null);

        AlertDialog.Builder alertDialogBuilder  = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setView(promptsView);




        final TextView  rentedTime_period;
        final EditText startTime, endTime, startDate, endDate;
        final Context context=promptsView.getContext();

        startTime = (EditText) promptsView.findViewById(R.id.startTimeEditText);
        endTime = (EditText) promptsView.findViewById(R.id.endTimeEditText);
        startDate = (EditText) promptsView.findViewById(R.id.startDateEditText);
        endDate = (EditText) promptsView.findViewById(R.id.endDateEditText);
        System.out.println((startTime==null)+"jhklkjghkjl");
        /*new DatePicker(RentActivity.this, R.id.startDateEditText);
        new DatePicker(RentActivity.this, R.id.endDateEditText);
        new TimePicker(RentActivity.this, R.id.startTimeEditText);
        new TimePicker(RentActivity.this, R.id.endTimeEditText);*/

        rentedTime_period=(TextView) promptsView.findViewById(R.id.rentedtime_period);

        String all="";
        for (HashMap.Entry<String, ArrayList<String>> entry : fd.getRentedTime().entrySet()) {
            ArrayList<String> value = entry.getValue();
            String start=value.get(0);
            String end=value.get(0);
            String total=start+" to "+end;
            all=all+total+", ";
        }
        rentedTime_period.setText(all);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String starttime = startTime.getText().toString().trim();
                                String endtime = endTime.getText().toString().trim();
                                String startdate = startDate.getText().toString().trim();
                                String enddate = endDate.getText().toString().trim();
                                if(!validateFields(starttime, endtime, startdate,enddate)){
                                    rent.setEnabled(false);
                                    AlertDialog alertDialog = new AlertDialog.Builder(RentActivity.this).create();
                                    alertDialog.setTitle("Wait!");
                                    alertDialog.setMessage("Please make sure your date/time is not earlier than the current date/time");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();

                                }else{
                                    if(!compareTime(starttime, endtime, startdate,enddate)){
                                        rent.setEnabled(false);
                                        AlertDialog alertDialog = new AlertDialog.Builder(RentActivity.this).create();
                                        alertDialog.setTitle("Wait!");
                                        alertDialog.setMessage("Please make sure the time you entered dosen't conflict with the rented ones");
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        alertDialog.show();

                                    }else{
                                        String start_w=startdate+" "+starttime;
                                        String end_w=enddate+" "+endtime;
                                        String start1 = start_w.substring(0,start_w.length()-2);
                                        String end1 = end_w.substring(0,end_w.length()-2);
                                        System.out.println(start1);
                                        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                                        Date time1 = null;
                                        Date time2 = null;
                                        try {
                                            time1 = df.parse(start1+":00");
                                            time2 = df.parse(end1+":00");
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        long diff = time2.getTime() - time1.getTime();
                                        long diffHours = diff / (60 * 60 * 1000);
                                        System.out.println(fd.getPrice() + " " + diffHours);

                                        price.setText("$" + Double.toString(fd.getPrice() * (diffHours+1)));
                                        String time_frame = start_w + " to " + end_w;
                                        time.setText(time_frame);
                                    }

                                }




                                dialog.dismiss();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                                rent.setEnabled(false);
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Rent Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    private boolean validateFields(String starttime, String endtime, String startdate, String enddate){
        boolean checkdate=true;

        if (starttime == null || endtime == null || startdate==null || enddate==null){

            return false;
        }


        try{

            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            String start_s=startdate+" "+starttime;
            String end_s=enddate+" "+endtime;
            Date time1 = df.parse(start_s.substring(0,start_s.length()-2)+":00");
            Date time2 = df.parse(end_s.substring(0,end_s.length()-2)+":00");
            long diff = time2.getTime() - time1.getTime();

            if(diff>=/*3600000*/0){
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

    private boolean compareTime(String starttime, String endtime, String startdate, String enddate){
        boolean compare=true;


        try{

            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            String start_s=startdate+" "+starttime;
            String end_s=enddate+" "+endtime;
            Date time1 = df.parse(start_s.substring(0,start_s.length()-2)+":00");
            Date time2 = df.parse(end_s.substring(0,end_s.length()-2)+":00");
            if(fd.getRentedTime().size()!=0) {
                for (HashMap.Entry<String, ArrayList<String>> entry : fd.getRentedTime().entrySet()) {
                    ArrayList<String> value = entry.getValue();
                    String start=value.get(0);
                    String end=value.get(0);
                    Date time_s = df.parse(start.substring(0,start.length()-2)+":00");
                    Date time_e = df.parse(end.substring(0,end.length()-2)+":00");

                    if(time1.getTime() >= time_s.getTime() && time1.getTime() < time_e.getTime()){
                        compare=false;
                    }
                    if(time2.getTime() >= time_s.getTime() && time2.getTime() < time_e.getTime()){
                        compare=false;
                    }
                }
            }


        }catch(ParseException ex){
            ex.printStackTrace();
        }
        return compare;
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
