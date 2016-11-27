package com.csci310.ParkHere;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static java.lang.System.currentTimeMillis;

/**
 * Created by Sean Yuan on 11/23/2016.
 */

public class AddPastActivity extends AppCompatActivity {
    private static DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;
    static List<FeedItem> hostingActualList;
    public static ArrayList<FeedItem> hostList;
    static MySecondRecyclerAdapter adapter;
    List<String> selected = new ArrayList<String>();
    long startTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        hostList = new ArrayList<>();
        getItemsHosting();
        hostingActualList = new ArrayList<>();
        initActionBar();
    }


    public void getItemsHosting(){
        startTime = currentTimeMillis();
        DatabaseReference database = mDatabase.child("users/"+mFirebaseUser.getUid()+"/hosting");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    HashMap<String,String> user_map= (HashMap<String,String>)dataSnapshot.getValue();
                    getSpot_host(user_map);}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void getSpot_host(HashMap<String,String> spot_map){
        Vector<String> names=new Vector<String>();
        for (HashMap.Entry<String, String> entry : spot_map.entrySet()) {
            if (!names.contains(entry.getKey())) {
                names.add(entry.getKey());
            }
        }
        for(int i=0;i<names.size();i++){
            DatabaseReference database_p = mDatabase.child("parking-spots-hosting").child(names.get(i));
            database_p.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Object> user_map = (HashMap<String,Object>) dataSnapshot.getValue();

                    if (user_map != null) {
                        FeedItem user_all = new FeedItem();
                        String spotaddy = (String) user_map.get("address");
                        if (!selected.contains(spotaddy)) {
                            for (HashMap.Entry<String, Object> innerEntry : user_map.entrySet()) {
                                String key = innerEntry.getKey();
                                Object value = innerEntry.getValue();
                                if (key.equals("latitude")) {
                                    user_all.setLatitude((double) value);
                                }
                                if (key.equals("address")) {
                                    user_all.setAddress((String) value);
                                    selected.add((String) value);
                                }

                                if (key.equals("longitude")) {
                                    user_all.setLongitude((double) value);
                                }
                                if (key.equals("spotID")) {
                                    user_all.setSpotID((String) value);
                                }
                                if (key.equals("startDates")) {
                                    user_all.setStartDates((String) value);
                                }
                                if (key.equals("endDates")) {
                                    user_all.setEndDates((String) value);
                                }
                                if (key.equals("startTime")) {
                                    user_all.setStartTime((String) value);
                                }
                                if (key.equals("endTime")) {
                                    user_all.setEndTime((String) value);
                                }
                                if (key.equals("price")) {
                                    user_all.setPrice(Double.parseDouble((value + "")));
                                }
                                if (key.equals("bookings")) {
                                    user_all.setBookings(Integer.parseInt((String) (value + "")));
                                }
                                if (key.equals("cancel")) {
                                    user_all.setCancel((String) value);
                                }
                                if (key.equals("description")) {
                                    user_all.setDescription((String) value);
                                }
                                if (key.equals("rating")) {
                                    ArrayList<String> temp = (ArrayList<String>) value;
                                    user_all.setRating(temp);
                                }
                                if (key.equals("activity")) {
                                    user_all.setActivity((Boolean) value);
                                }
                                if (key.equals("filter")) {
                                    Vector v = new Vector((ArrayList<String>) value);
                                    user_all.setFilter(v);
                                }
                                if (key.equals("host")) {
                                    user_all.setHost((String) value);
                                }
                                if (key.equals("photos")) {
                                    user_all.setPhotos((ArrayList<String>) value);
                                }
                                if (key.equals("rentedTime")) {
                                    user_all.setRentedTime((Map<String, ArrayList<String>>) value);
                                }
                                if (key.equals("identifier")) {
                                    user_all.setIdentifier((String) value);
                                }
                                if (key.equals("review")) {
                                    user_all.setReview((ArrayList<String>) value);
                                }
                                if (key.equals("currentRenter")) {
                                    user_all.setCurrentRenter((String) value);
                                }
                            }
                            hostList.add(user_all);
                        }
                    }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
        long finalTime = currentTimeMillis() - startTime;
        System.out.println("Total runtime: " + finalTime);
    }


    @SuppressWarnings("deprecation")
    private void initActionBar() {
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.addTab(actionBar.newTab()
                    .setText("New Hosting")
                    .setTabListener(new ActionBar.TabListener() {
                        @Override
                        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                            fragmentTransaction.replace(android.R.id.content, new RecyclerViewFragmentNewHosting());
                        }

                        @Override
                        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        }

                        @Override
                        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        }
                    }));
            actionBar.addTab(actionBar.newTab()
                    .setText("Previous Hostings")
                    .setTabListener(new ActionBar.TabListener() {
                        @Override
                        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                            fragmentTransaction.replace(android.R.id.content, new RecyclerViewFragmentHosting());
                        }

                        @Override
                        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        }

                        @Override
                        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        }
                    }));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
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
        if (item.getItemId() == R.id.signOut) {
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseAuth.signOut();
            Intent intent = new Intent(AddPastActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    public static class RecyclerViewFragmentHosting extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_recyclerview, container, false);
            RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.myList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new MySecondRecyclerAdapter(getActivity(), "past");
            recyclerView.setAdapter(adapter);
            return root;
        }
    }

    public static class RecyclerViewFragmentNewHosting extends Fragment {
        @SuppressLint("InflateParams")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.chat, container, false);
            Button newbutton = (Button) root.findViewById(R.id.newpostButton);
            newbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AddActivity.class);
                    startActivity(intent);
                }
            });
            return root;
        }
    }
}