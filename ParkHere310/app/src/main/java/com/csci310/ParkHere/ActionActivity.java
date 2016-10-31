package com.csci310.ParkHere;

/**
 * Created by seanyuan on 10/13/16.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.List;
import java.util.Map;

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
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser_universal = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        spotsDatabase = FirebaseDatabase.getInstance().getReference().child("parking-spots");
        sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
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

                System.out.println(starttime);
                System.out.println(endtime);
                System.out.println(startdate);
                System.out.println(enddate);

                boolean requestCompact = compact.isChecked();
                boolean requestCover = cover.isChecked();
                boolean handicapped = handy.isChecked();
                validateFields(starttime, endtime, startdate,enddate, address);

                getListWithOptions(starttime, endtime, startdate, enddate, requestCompact, requestCover, handicapped);
                new AddressOperation(self).execute(address);
            }
        });
    }

    private void getListWithOptions(final String starttime, final String endtime, final String startdate, final String enddate, boolean requestCompact, boolean requestCover, boolean handicapped)
    {
        spotsDatabase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    if (child.child("activity").equals("true") &&
                            isValidDT(child.child("startDates").getKey(), child.child("endDates").getKey(), startdate, enddate,
                                    child.child("startTime").getKey(), child.child("endTime").getKey(), starttime, endtime))
                    {
                        tempSpots.put(child.getKey(), new double[]{Double.parseDouble(child.child("latitude").getKey()),
                                Double.parseDouble(child.child("longitude").getKey())});
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
                    searchResult.add(entry.getKey());
            }
        }
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2)
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
                if (sDate1.compareTo(eDate1) > 0)
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
        DatabaseReference database = mDatabase.child("users/");
        database.orderByChild("email").equalTo(mFirebaseUser_universal.getEmail()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                List<User> userlist = (List<User>)dataSnapshot.getValue();
                user_all = userlist.get(0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            // ...
        });
    }

    private void validateFields(String starttime, String endtime, String startdate, String enddate, String address){
        int compare = starttime.compareTo(endtime);

        if (compare > 0){
            Toast.makeText(ActionActivity.this, "Please choose later end time",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        compare = startdate.compareTo(enddate);

        if (compare > 0){
            Toast.makeText(ActionActivity.this, "Please choose later end date",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (address == null || isEmpty(address)){
            Toast.makeText(ActionActivity.this, "Please enter an address",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (startdate.equals(enddate)){
            String[] time1 = starttime.split(":");
            int hour1 = Integer.parseInt(time1[0]) % 12;
            String[] time2 = endtime.split(":");
            int hour2 = Integer.parseInt(time2[0]) % 12;
            if ((hour2 - hour1) < 1){
                Toast.makeText(ActionActivity.this, "Spots must be rented for at least 1 hour",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
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
