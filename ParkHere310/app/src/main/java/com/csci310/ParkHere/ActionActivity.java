package com.csci310.ParkHere;

/**
 * Created by seanyuan on 10/13/16.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import static android.text.TextUtils.isEmpty;

/**
 * Created by seanyuan on 9/28/16.
 */
public class ActionActivity extends AppCompatActivity {
    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser_universal;
    public DatabaseReference mDatabase;
    private DatabaseReference spotsDatabase;
    private HashMap<FeedItem, double[]> tempSpots;
    public static ArrayList<FeedItem> searchResult;
    TextView user;
    TabHost host;
    private TextView startTime, endTime, startDate, endDate, location;
    private Button search;
    private CheckBox compact, cover, handy;
    public static User user_all;
    private ActionActivity self;
    public static double[] latlng;
    private Map<String,String> spot_rent;
    private String rate_list=null;
    private String temp_spot_identifier;
    private Map<String,ArrayList<String>> Time_list;
    private int check_size=0;


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
        tempSpots = new HashMap<FeedItem, double[]>();
        initUserListener();
        searchResult = new ArrayList<FeedItem>();
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

        for(int i=0;i<host.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.WHITE);
        }

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


        checkRate();



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
                    AlertDialog alertDialog = new AlertDialog.Builder(ActionActivity.this).create();
                    alertDialog.setTitle("Wait!");
                    alertDialog.setMessage("Please make sure the address is not empty or your date/time is not earlier than the current date/time");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }else{

                    tempSpots.clear();
                    searchResult.clear();
                    getListWithOptions(starttime, endtime, startdate, enddate, requestCompact, requestCover, handicapped, address);

                }
            }
        });
    }
    private void checkRate() {
        DatabaseReference ref=mDatabase.child("users").child(mFirebaseUser_universal.getUid()).child("renting");

        spot_rent=new HashMap<String,String>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    spot_rent = (HashMap<String,String>) dataSnapshot.getValue();
                    if(spot_rent.size()!=0) {

                        for (HashMap.Entry<String, String> entry_1 : spot_rent.entrySet()) {
                            String spot_name=entry_1.getKey();
                            System.out.println(spot_name+"   name");
                            String spot_status=entry_1.getValue();
                            System.out.println(spot_status+"   status");
                            if(!spot_status.equals("rated")) {
                                DatabaseReference ref1 = mDatabase.child("parking-spots-hosting").child(spot_name).child("rentedTime");
                                temp_spot_identifier = spot_name;
                                Time_list = new HashMap<String, ArrayList<String>>();

                                ref1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            System.out.println(Time_list.size()+"   rentedTime.size");
                                            Time_list = (HashMap<String, ArrayList<String>>) dataSnapshot.getValue();
                                            String endTime_c = "";

                                            ArrayList<String> value = Time_list.get(mFirebaseUser_universal.getUid());
                                            endTime_c = value.get(1);

                                            System.out.println(endTime_c+"   endTime");
                                            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                                            String today = getToday(df);
                                            Date time1 = null;
                                            Date d = null;
                                            try {
                                                time1 = df.parse(endTime_c.substring(0, endTime_c.length() - 2) + ":00");
                                                System.out.println(endTime_c.substring(0, endTime_c.length() - 2) + ":00"+"   format endTime");
                                                d = df.parse(today);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            if (time1.getTime() < d.getTime()) {
                                                System.out.println("smaller");
                                                rate_list=temp_spot_identifier;
                                                check_size=check_size+1;
                                                if(check_size==1) {
                                                    goRate();
                                                }

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.out.println("The read failed: " + databaseError.getCode());
                                    }
                                });
                            }

                           /* if(counter==spot_rent.size()){
                            goRate();
                            }*/

                        }
                        System.out.println(rate_list+"   rateList size");


                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });




    }

    private void goRate(){


        if(rate_list!=null ){

            AlertDialog alertDialog = new AlertDialog.Builder(ActionActivity.this).create();
            alertDialog.setTitle("Spot to Rate");

            alertDialog.setMessage("Please go to Rate this spot");

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(ActionActivity.this, RatingActivity.class);
                    intent.putExtra("rate", rate_list);
                    startActivity(intent);

                }
            });
            alertDialog.show();
        }

    }


    private void getListWithOptions(final String starttime, final String endtime, final String startdate, final String enddate,
                                    final boolean requestCompact, final boolean requestCover, final boolean handicapped, final String address)
    {
        spotsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    if (child.child("activity").getValue().toString().equals("true") &&
//                        child.child("rentedTime").getValue() == null &&
                            isValidDT(startdate, enddate, child.child("startDates").getValue().toString(), child.child("endDates").getValue().toString(),
                                    starttime, endtime, child.child("startTime").getValue().toString(), child.child("endTime").getValue().toString()) &&
                            isValidFilters(requestCompact, requestCover, handicapped, child.child("filter")))
                    {
                        boolean isValid = true;
                        if (child.child("rentedTime").exists())
                        {
                            for (DataSnapshot timeSlot : child.child("rentedTime").getChildren())
                            {
                                if (!isSeparateDT(startdate + " " + starttime, enddate + " " + endtime,
                                                timeSlot.child("0").getValue().toString(), timeSlot.child("1").getValue().toString()))
                                    isValid = false;
                            }
                        }
                        if (isValid)
                            tempSpots.put(child.getValue(FeedItem.class), new double[]
                                    {Double.parseDouble(child.child("latitude").getValue().toString()),
                                            Double.parseDouble(child.child("longitude").getValue().toString())});
                    }
                }
                new AddressOperation(self).execute(address);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
    }

    public void search(String jsonString)
    {
        try
        {
            latlng = AddressOperation.getCoordinatesFromJSON(jsonString);

            if (!tempSpots.isEmpty()) {
                for (Map.Entry<FeedItem, double[]> entry : tempSpots.entrySet()) {
                    double[] tmplatlng = entry.getValue();
                    if (distance(latlng[0], latlng[1], tmplatlng[0], tmplatlng[1]) < 3.0) {
                        searchResult.add(entry.getKey());
                        System.out.println("Spot: " + entry.getKey().getIdentifier());
                    }
                }
                //go to resultview
                Intent intent = new Intent(ActionActivity.this, ListingResultActivity.class);
                intent.putExtra("start", startDate.getText().toString().trim()+" "+startTime.getText().toString().trim() );
                intent.putExtra("end", endDate.getText().toString().trim()+" "+endTime.getText().toString().trim() );
                startActivity(intent);
            } else {
                System.out.println("Empty tempSpots!");
                Toast.makeText(ActionActivity.this, "No results found", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            AddressOperation.showAddressFaultDialog(self);
        }
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2)
    {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg)
    {
        return (deg * Math.PI / 180.0);
    }
    private static double rad2deg(double rad)
    {
        return (rad * 180.0 / Math.PI);
    }

    private boolean isValidDT (String sDate1str, String eDate1str, String sDate2str, String eDate2str,
                               String sTime1str, String eTime1str, String sTime2str, String eTime2str)
    {
        try
        {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            long userStartTime = sdf.parse(sDate1str + " " + sTime1str.substring(0,sTime1str.length()-2)+":00").getTime();
            long userEndTime = sdf.parse(eDate1str + " " + eTime1str.substring(0,sTime1str.length()-2)+":00").getTime();
            long spotStartTime = sdf.parse(sDate2str + " " + sTime2str.substring(0,sTime1str.length()-2)+":00").getTime();
            long spotEndTime = sdf.parse(eDate2str + " " + eTime2str.substring(0,sTime1str.length()-2)+":00").getTime();
            if (userStartTime >= spotStartTime && userEndTime <= spotEndTime)
                return true;
        }
        catch (ParseException parseException) {parseException.printStackTrace();}
        return false;
    }

    private boolean isSeparateDT (String sDT1, String eDT1,
                                  String sDT2, String eDT2)
    {
        try
        {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm");
            long userStartTime = sdf.parse(sDT1.substring(0, sDT1.length()-2)).getTime();
            long userEndTime = sdf.parse(eDT1.substring(0, eDT1.length()-2)).getTime();
            long spotStartTime = sdf.parse(sDT2.substring(0, sDT2.length()-2)).getTime();
            long spotEndTime = sdf.parse(eDT2.substring(0, eDT2.length()-2)).getTime();
            if (userStartTime >= spotEndTime || userEndTime <= spotStartTime)
                return true;
        }
        catch (ParseException parseException) {parseException.printStackTrace();}
        return false;
    }

    private String tryConvertTimeFormat(String time)
    {
        String[] hhmm = time.split(":");
        int tmp;

        if ((tmp = Integer.parseInt(hhmm[0])) > 12)
        {
            tmp -= 12;
            return Integer.toString(tmp) + ":" + hhmm[1];
        }
        else
            return time;
    }

    private boolean isValidFilters(boolean requestCompact, boolean requestCover, boolean requestHandicap, DataSnapshot filterNode)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        for (DataSnapshot filter : filterNode.getChildren())
            arrayList.add(filter.getValue().toString().toLowerCase());
        if ((requestCompact && !arrayList.contains("compact")) ||
                (requestCover && !arrayList.contains("covered parking")) ||
                (requestHandicap && !arrayList.contains("handicap")))
            return false;
        return true;
    }

    private void initUserListener(){
        DatabaseReference database = mDatabase.child("users/").child(mFirebaseUser_universal.getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,Object> user_map= (HashMap)dataSnapshot.getValue();
                if(user_map !=null){
                    System.out.println("got the user");
                    for (HashMap.Entry<String, Object> entry : user_map.entrySet()) {
                        String key = entry.getKey();
                        if(key.equals("email")){
                            String value = (String)entry.getValue();
                            user_all.setEmail(value);

                            System.out.println((String)value);
                        }
                        if(key.equals("userName")){
                            String value = (String)entry.getValue();
                            user_all.setUserName(value);
                        }
                        if(key.equals("phone")){
                            String value = (String)entry.getValue();
                            user_all.setPhone(value);
                        }
                        if(key.equals("host")){
                            Boolean value = (Boolean)entry.getValue();
                            user_all.setHost(value);
                        }
                        if(key.equals("review")){
                            ArrayList<String> value = ( ArrayList<String>)entry.getValue();

                            user_all.setReview(value);
                        }

                        if(key.equals("rating")){
                            ArrayList<String> value = ( ArrayList<String>)entry.getValue();

                            user_all.setRating(value);
                        }
                        if(key.equals("renting")){
                            Vector<String> temp = new Vector<String>();
                            HashMap<String,String> temp_map= (HashMap<String,String>)entry.getValue();
                            for (HashMap.Entry<String, String> entry1 : temp_map.entrySet()) {
                                String itemKey = entry1.getKey();
                                temp.add(itemKey);
                            }
                            user_all.setRenting(temp);
                        }
                        if(key.equals("hosting")){
                            Vector<String> temp = new Vector<String>();
                            HashMap<String,String> temp_map= (HashMap<String,String>)entry.getValue();
                            for (HashMap.Entry<String, String> entry1 : temp_map.entrySet()) {
                                String itemKey = entry1.getKey();
                                temp.add(itemKey);
                            }
                            user_all.setHosting(temp);
                        }
                        if(key.equals("photo")){
                            String value = (String)entry.getValue();
                            user_all.setPhoto(value);
                        }

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
        boolean checkdate=true;

        if (address == null || isEmpty(address)){

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
    public String getToday(java.text.SimpleDateFormat  dformat){
        Date date = new Date();
        return dformat.format(date);
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
           //if(user_all.getHost()){
                Intent intent = new Intent(ActionActivity.this, AddPastActivity.class);
                startActivity(intent);
           /* }else{
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ActionActivity.this);
                builder1.setMessage("You are currently not a host. Would you like to enable host features?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                user_all.setHost(true);
                                mDatabase.child("users").child(mFirebaseUser_universal.getUid()).child("host").setValue(true);
                                Intent intent = new Intent(ActionActivity.this, AddActivity.class);
                                startActivity(intent);
                            }
                        });

                builder1.setNegativeButton(
                        "Nope",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }*/

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
    @Override
    protected void onResume() {
           super.onResume();
                checkRate();
    }
}
