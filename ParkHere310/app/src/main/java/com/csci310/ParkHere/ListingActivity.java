package com.csci310.ParkHere;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Created by seanyuan on 9/30/16.
 */

public class ListingActivity extends AppCompatActivity {
    private static DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;
    static List<String> rentingList;
    static List<String> hostingList;
    //static List<FeedItem> displayList;
    static List<FeedItem> rentingActualList;
    static List<FeedItem> hostingActualList;
    private static DatabaseReference ref;
    public static ArrayList<FeedItem> hostList;
    public static ArrayList<FeedItem> rentList;
    public static ArrayList<FeedItem> wishList;
    static MyRecyclerAdapter adapter;
    public static ArrayList<String> confirm_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        Bundle bundle = getIntent().getExtras();
        confirm_list=new ArrayList<String>();
        if(bundle!=null){
            confirm_list=bundle.getStringArrayList("confirm");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        rentList = new ArrayList<>();
        hostList = new ArrayList<>();
        wishList = new ArrayList<>();
        //displayList = new ArrayList<>();
        getItemsHosting();
        getItemsRenting();
        getItemsWish();
        rentingActualList = new ArrayList<>();
        //hostingActualList = new ArrayList<>();
        //initDataListenerRental();
        //initDataListenerHosting();
        initActionBar();
    }

    private  void getItemsRenting(){
        DatabaseReference database = mDatabase.child("users/"+mFirebaseUser.getUid()+"/renting");
        //DatabaseReference database = mDatabase.child("parking-spots-hosting");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                HashMap<String,String> user_map= (HashMap<String,String>)dataSnapshot.getValue();
                getSpot(user_map);}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void getSpot(HashMap<String,String> spot_map){

        Vector<String> names=new Vector<String>();
        for (HashMap.Entry<String, String> entry : spot_map.entrySet()) {
            names.add(entry.getKey());
        }
        for(int i=0;i<names.size();i++){
            DatabaseReference database_p = mDatabase.child("parking-spots-hosting").child(names.get(i));
            database_p.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Object> user_map = (HashMap<String,Object>) dataSnapshot.getValue();

                    if (user_map != null) {
                        FeedItem user_all = new FeedItem();
                            for (HashMap.Entry<String, Object> innerEntry : user_map.entrySet()) {
                                String key = innerEntry.getKey();
                                Object value = innerEntry.getValue();
                                System.out.println("innerkey: " + key + "// innervalue: " + value);

                                if (key.equals("latitude")) {
                                    user_all.setLatitude((double) value);
                                    System.out.println(value);
                                }
                                if (key.equals("address")) {
                                    user_all.setAddress((String) value);
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
                                    user_all.setPrice(Double.parseDouble((String) (value + "")));
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
                                    ArrayList<String> temp=(ArrayList<String>) value;


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
                            rentList.add(user_all);
                        }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

    public void getItemsHosting(){
        DatabaseReference database = mDatabase.child("users/"+mFirebaseUser.getUid()+"/hosting");
        //DatabaseReference database = mDatabase.child("parking-spots-hosting");
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
            names.add(entry.getKey());
        }
        for(int i=0;i<names.size();i++){
            DatabaseReference database_p = mDatabase.child("parking-spots-hosting").child(names.get(i));
            database_p.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Object> user_map = (HashMap<String,Object>) dataSnapshot.getValue();

                    if (user_map != null) {
                        FeedItem user_all = new FeedItem();
                        for (HashMap.Entry<String, Object> innerEntry : user_map.entrySet()) {
                            String key = innerEntry.getKey();
                            Object value = innerEntry.getValue();
                            System.out.println("innerkey: " + key + "// innervalue: " + value);

                            if (key.equals("latitude")) {
                                user_all.setLatitude((double) value);
                                System.out.println(value);
                            }
                            if (key.equals("address")) {
                                user_all.setAddress((String) value);
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
                                user_all.setPrice(Double.parseDouble( (value + "")));
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

                                ArrayList<String> temp=(ArrayList<String>) value;

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


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }


    private  void getItemsWish(){
        DatabaseReference database = mDatabase.child("users/"+mFirebaseUser.getUid()+"/wishlist");
        //DatabaseReference database = mDatabase.child("parking-spots-hosting");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    HashMap<String,String> user_map= (HashMap<String,String>)dataSnapshot.getValue();
                    getWishSpot(user_map);}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void getWishSpot(HashMap<String,String> spot_map){

        Vector<String> names=new Vector<String>();
        for (HashMap.Entry<String, String> entry : spot_map.entrySet()) {
            names.add(entry.getKey());
        }
        for(int i=0;i<names.size();i++){
            DatabaseReference database_p = mDatabase.child("parking-spots-hosting").child(names.get(i));
            database_p.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Object> user_map = (HashMap<String,Object>) dataSnapshot.getValue();

                    if (user_map != null) {
                        FeedItem user_all = new FeedItem();
                        for (HashMap.Entry<String, Object> innerEntry : user_map.entrySet()) {
                            String key = innerEntry.getKey();
                            Object value = innerEntry.getValue();
                            System.out.println("innerkey: " + key + "// innervalue: " + value);

                            if (key.equals("latitude")) {
                                user_all.setLatitude((double) value);
                                System.out.println(value);
                            }
                            if (key.equals("address")) {
                                user_all.setAddress((String) value);
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
                                user_all.setPrice(Double.parseDouble((String) (value + "")));
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
                                ArrayList<String> temp=(ArrayList<String>) value;


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
                        wishList.add(user_all);
                    }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

    @SuppressWarnings("deprecation")
    private void initActionBar() {
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.addTab(actionBar.newTab()
                    .setText("Renting")
                    .setTabListener(new ActionBar.TabListener() {
                        @Override
                        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                            fragmentTransaction.replace(android.R.id.content, new RecyclerViewFragment());
                        }

                        @Override
                        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        }

                        @Override
                        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                        }
                    }));
            actionBar.addTab(actionBar.newTab()
                    .setText("Hosting")
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
            actionBar.addTab(actionBar.newTab()
                    .setText("WishList")
                    .setTabListener(new ActionBar.TabListener() {
                        @Override
                        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                            fragmentTransaction.replace(android.R.id.content, new RecyclerViewFragmentWish());
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
            Intent intent = new Intent(ListingActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    public static class ChatViewFragment extends Fragment {

        @SuppressLint("InflateParams")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.chat, container, false);

            return root;
        }
    }

    public static class RecyclerViewFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_recyclerview, container, false);
            RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.myList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            MyRecyclerAdapter adapter = new MyRecyclerAdapter(getActivity(), "rent");
            recyclerView.setAdapter(adapter);
            return root;
        }
    }

    public static class RecyclerViewFragmentWish extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_recyclerview, container, false);
            RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.myList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new MyRecyclerAdapter(getActivity(), "wish");
            recyclerView.setAdapter(adapter);
            return root;
        }
    }

    public static class RecyclerViewFragmentHosting extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_recyclerview, container, false);
            RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.myList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new MyRecyclerAdapter(getActivity(), "host");
            recyclerView.setAdapter(adapter);
            return root;
        }
    }

    /*private static void getItemsRenting(List<String> rentalIDs){
        mDatabase.child("parking-spots-renting");
        mDatabase.orderByChild("currentRenter").equalTo(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("User key", child.getKey());
                    Log.d("User ref", child.getRef().toString());
                    Log.d("User val", child.getValue().toString());
                    rentingActualList.add((FeedItem)child.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    /*private static void getItemsHosting(List<String> hostalIDs){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("parking-spots-hosting").child(key).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post value
                        Post post = dataSnapshot.getValue(Post.class);
                        // post now has all the values and
                        // can be used to update the UI
                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

    }*/
}
//TODO: add back button action - should not be able to return to register screen
