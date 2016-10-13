package com.csci310.ParkHere;

/**
 * Created by seanyuan on 10/13/16.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TabHost;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by seanyuan on 9/28/16.
 */
public class ActionActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    TextView user;
    TabHost host;
    private Toolbar toolbar;
    private TextView startTime, endTime, startDate, endDate, location;
    private Button search;
    private ProgressDialog progressDiag;
    private CheckBox compact, cover, handy;
//khjvg
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
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
        progressDiag = new ProgressDialog(this);
        startTime = (TextView) findViewById(R.id.startTimeText);
        endTime = (TextView) findViewById(R.id.endTimeText);
        startDate = (TextView) findViewById(R.id.startDateEditText);
        endDate = (TextView) findViewById(R.id.endDateEditText);
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
                Boolean requestCompact = compact.isChecked();
                Boolean requestCover = cover.isChecked();
                Boolean handicapped = handy.isChecked();
                validateFields(starttime, endtime, startdate,enddate);
                progressDiag.setMessage("Searching...");
                progressDiag.show();
                ///do search
            }
        });

    }

    private void validateFields(String starttime, String endtime, String startdate, String enddate){
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
            Intent intent = new Intent(ActionActivity.this, ListingActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.signOut) {
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseAuth.signOut();
            Intent intent = new Intent(ActionActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    //TODO: add back button action - should not be able to return to register screen

}
