package com.csci310.ParkHere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class RatingActivity extends AppCompatActivity {
    private String host_name;
    private float rateHost;
    private float rateSpot;
    private String commentHost;
    private String commentSpot;
    private TextView hostText;
    private EditText commentHostText, commentSpotText;
    private RatingBar userRateHost,userRateSpot;
    private Button rate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        getInfo();
        hostText= (TextView) findViewById(R.id.host_name);
        hostText.setText(host_name);
        userRateHost = (RatingBar) findViewById(R.id.RateHost);
        userRateSpot = (RatingBar) findViewById(R.id.RateSpot);
        commentHostText=(EditText) findViewById(R.id.review_host);
        commentSpotText=(EditText) findViewById(R.id.review_spot);
        rate=(Button) findViewById(R.id.rateButton);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateHost=userRateHost.getRating();
                rateSpot=userRateSpot.getRating();
                commentHost=commentHostText.getText().toString();
                commentSpot=commentSpotText.getText().toString();
                update();

            }
        });

    }
    private void getInfo(){

    }
    private void update(){

    }
}
