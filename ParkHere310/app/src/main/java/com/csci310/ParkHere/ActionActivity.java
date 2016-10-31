package com.csci310.ParkHere;

/**
 * Created by seanyuan on 10/13/16.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static android.text.TextUtils.isEmpty;

/**
 * Created by seanyuan on 9/28/16.
 */
public class ActionActivity extends AppCompatActivity {
    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser_universal;
    public DatabaseReference mDatabase;
    private DatabaseReference spotsDatabase;
    private java.text.SimpleDateFormat sdf;
    private HashMap<String, double[]> tempSpots;
    private ArrayList<String> searchResult;
    TextView user;
    TabHost host;
    private TextView startTime, endTime, startDate, endDate, location;
    private Button search;
    private CheckBox compact, cover, handy;
    public static User user_all;
    private ActionActivity self;

//khjvg
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.action_activity);
        user_all=new User();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser_universal = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        spotsDatabase = mDatabase.child("parking-spots-hosting");
        sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
        tempSpots = new HashMap<String, double[]>();
        searchResult = new ArrayList<String>();
        initUserListener();
        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();
        TabHost.TabSpec spec = host.newTabSpec("Basics");
        spec.setContent(R.id.basics);
        spec.setIndicator("Basics");
        host.addTab(spec);
        spec = host.newTabSpec("Filters");
        spec.setContent(R.id.filters);
        spec.setIndicator("Filters");
        host.addTab(spec);
        new DatePicker(ActionActivity.this, R.id.startDateEditText);
        new DatePicker(ActionActivity.this, R.id.endDateEditText);
        new TimePicker(ActionActivity.this, R.id.startTimeText);
        new TimePicker(ActionActivity.this, R.id.endTimeText);
        startTime = (TextView) findViewById(R.id.startTimeText);
        endTime = (TextView) findViewById(R.id.endTimeText);
        startDate = (TextView) findViewById(R.id.startDateEditText);
        endDate = (TextView) findViewById(R.id.endDateEditText);
        location = (TextView) findViewById(R.id.locationEditText);
        compact = (CheckBox) findViewById(R.id.compactBox);
        cover = (CheckBox) findViewById(R.id.coverBox);
        handy = (CheckBox) findViewById(R.id.handicappedBox);
        search = (Button) findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String starttime = startTime.getText().toString().trim();
                String endtime = endTime.getText().toString().trim();
                String startdate = startDate.getText().toString().trim();
                String enddate = endDate.getText().toString().trim();
                String address = location.getText().toString().trim();

                boolean requestCompact = compact.isChecked();
                boolean requestCover = cover.isChecked();
                boolean handicapped = handy.isChecked();
                if(!validateFields(starttime, endtime, startdate,enddate, address)){
                    Toast.makeText(ActionActivity.this, "Please enter an address",
                            Toast.LENGTH_SHORT).show();
                }else{

                getListWithOptions(starttime, endtime, startdate, enddate, requestCompact, requestCover, handicapped);
                new AddressOperation(self).execute(address);}
            }
        });
    }

    private void getListWithOptions(final String starttime, final String endtime, final String startdate, final String enddate, boolean requestCompact, boolean requestCover, boolean handicapped)
    {
        spotsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    if (child.child("activity").getValue().toString().equals("true") &&
                            isValidDT(startdate, enddate, child.child("startDates").getValue().toString(), child.child("endDates").getValue().toString(),
                                    starttime, endtime, child.child("startTime").getValue().toString(), child.child("endTime").getValue().toString()))
                    {
                        tempSpots.put(child.getKey(), new double[]{Double.parseDouble(child.child("latitude").getValue().toString()),
                                Double.parseDouble(child.child("longitude").getValue().toString())});
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
    }

    public void search(String jsonString)
    {
        double[] latlng = AddressOperation.getCoordinatesFromJSON(jsonString);
        if (!tempSpots.isEmpty())
        {
            for (Map.Entry<String, double[]> entry : tempSpots.entrySet())
            {
                double[] tmplatlng = entry.getValue();
                if (distance(latlng[0], latlng[1], tmplatlng[0], tmplatlng[1]) < 3.0)
                {
                    searchResult.add(entry.getKey());
                    System.out.println(entry.getKey());
                }
            }
        }
        else
        {
            System.out.println("Empty tempSpots!");
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2)
    {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg)
    {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad)
    {
        return (rad * 180.0 / Math.PI);
    }

    private boolean isValidDT (String sDate1str, String eDate1str, String sDate2str, String eDate2str,
                               String sTime1str, String eTime1str, String sTime2str, String eTime2str)
    {
        if (dateWithinRange(sDate1str, eDate1str, sDate2str, eDate2str) == 1)
            return true;
        else if (dateWithinRange(sDate1str, eDate1str, sDate2str, eDate2str) == 2 &&
                timeWithinRange(sTime1str, eTime1str, sTime2str, eTime2str))
            return true;
        return false;
    }

    private int dateWithinRange(String sDate1str, String eDate1str, String sDate2str, String eDate2str)
    {
        try
        {
            Date sDate1 = sdf.parse(sDate1str);
            Date sDate2 = sdf.parse(sDate2str);
            Date eDate1 = sdf.parse(eDate1str);
            Date eDate2 = sdf.parse(eDate2str);

            //Within range:
            if (sDate1.compareTo(sDate2) >= 0 && eDate1.compareTo(eDate2) <= 0)
            {
                //On a different day:
                if (sDate1.compareTo(eDate1) < 0)
                    return 1;
                //On the same day:
                else if (sDate1.compareTo(eDate1) == 0)
                    return 2;
            }
        }
        catch (ParseException parseException) {parseException.printStackTrace();}
        return 0;
    }

    private boolean timeWithinRange(String sTime1str, String eTime1str, String sTime2str, String eTime2str)
    {
        return true;
    }

    private void initUserListener(){
        DatabaseReference database = mDatabase.child("users/").child(mFirebaseUser_universal.getUid());
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,Object> user_map= (HashMap)dataSnapshot.getValue();

                for (HashMap.Entry<String, Object> entry : user_map.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if(key.equals("email")){
                        user_all.setEmail((String)value);
                        System.out.println((String)value);
                    }
                    if(key.equals("userName")){
                        user_all.setUserName((String)value);
                    }
                    if(key.equals("phone")){
                        user_all.setPhone((String)value);
                    }
                    if(key.equals("host")){
                        user_all.setHost((Boolean)value);
                    }
                    if(key.equals("review")){
                        user_all.setReview((Vector<String>)value);
                    }
                    if(key.equals("rateList")){
                        user_all.setRateList((Vector<String>)value);
                    }
                    if(key.equals("rating")){
                        user_all.setRating((Vector<Integer>)value);
                    }
                    if(key.equals("renting")){
                        user_all.setRenting((List<String>)value);
                    }
                    if(key.equals("hosting")){
                        user_all.setHosting((List<String>)value);
                    }
                    if(key.equals("photo")){
                        user_all.setPhoto((Bitmap) value);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private boolean validateFields(String starttime, String endtime, String startdate, String enddate, String address){


        if (address == null || isEmpty(address)){

            return false;
        }
        boolean checkdate=true;
        try{

            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy hh:mma");
            Date time1 = df.parse(startdate+" "+starttime);
            Date time2 = df.parse(enddate+" "+endtime);
            long diff = time2.getTime() - time1.getTime();
            long diffHours = diff / (60 * 60 * 1000) % 24;
            if(diffHours>=1){
                checkdate=true;
            }else{
                checkdate=false;
            }
            Date date = new Date();
            if(time1.after(date)){
                checkdate=true;
            }else{
                checkdate=false;
            }


        }catch(ParseException ex){
            ex.printStackTrace();
        }

        return checkdate;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about) {
            TextView content = (TextView) getLayoutInflater().inflate(R.layout.about_view, null);
            content.setMovementMethod(LinkMovementMethod.getInstance());
            String stuff = getString(R.string.about_body);
            content.setText(Html.fromHtml(stuff));
            new AlertDialog.Builder(this)
                    .setTitle(R.string.about)
                    .setView(content)
                    .setInverseBackgroundForced(true)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }
        if (item.getItemId() == R.id.action_user) {
            Intent intent = new Intent(ActionActivity.this, UserActivity.class);//change to UserActivity.class
            startActivity(intent);
        }
        if (item.getItemId() == R.id.signOut) {
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseAuth.signOut();
            Intent intent = new Intent(ActionActivity.this, MainActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.newPosting){
            Intent intent = new Intent(ActionActivity.this, AddActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    //TODO: add back button action - should not be able to return to register screen

}
