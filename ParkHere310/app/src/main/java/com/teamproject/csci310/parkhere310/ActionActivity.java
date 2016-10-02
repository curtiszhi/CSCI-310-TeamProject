package com.teamproject.csci310.parkhere310;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by seanyuan on 9/28/16.
 */
public class ActionActivity extends AppCompatActivity {
    Button signOutButton;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    TextView user;
    TabHost host;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //TODO: add back button action - should not be able to return to register screen
    /*
    String uName = mFirebaseUser.getEmail();
    signOutButton.setText("Log-Out of: " + uName);signOutButton = (Button) findViewById(R.id.signOutButton);
    signOutButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFirebaseAuth.signOut();
            Intent intent = new Intent(ActionActivity.this, SignInActivity.class);
            startActivity(intent);
        }
    });*/

}
