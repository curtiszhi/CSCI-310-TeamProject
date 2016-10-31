package com.csci310.ParkHere;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.List;
import java.util.Set;

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
    private static List<FeedItem> hostList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        rentingList = new ArrayList<>();
        hostingList = new ArrayList<>();
        //displayList = new ArrayList<>();
        getItemsHosting();
        rentingActualList = new ArrayList<>();
        hostingActualList = new ArrayList<>();
        //initDataListenerRental();
        //initDataListenerHosting();
        initActionBar();
    }

    private static void getItemsRenting(){
        ref=mDatabase.child("users").child(mFirebaseUser.getUid()).child("renting");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hostList= (List)dataSnapshot.getValue();
                if(hostList==null){
                    //dosomething
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
    public static void getItemsHosting(){
        ref=mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hostList= (List)dataSnapshot.getValue();
                System.out.println("cigars: " + hostList.size());
                if(hostList==null){
                   //dosomething
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        //return hostList;
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
            getItemsRenting();
            MyRecyclerAdapter adapter = new MyRecyclerAdapter(getActivity(), rentingActualList);
            recyclerView.setAdapter(adapter);
            return root;
        }
    }

    public static class RecyclerViewFragmentHosting extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //getItemsHosting();
            View root = inflater.inflate(R.layout.fragment_recyclerview, container, false);
            RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.myList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            MyRecyclerAdapter adapter = new MyRecyclerAdapter(getActivity(), getItemsHosting());
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
