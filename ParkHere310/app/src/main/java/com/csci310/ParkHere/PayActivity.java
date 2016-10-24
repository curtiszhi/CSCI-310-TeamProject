package com.csci310.ParkHere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PayActivity extends AppCompatActivity {
    private TextView total_fee;
    private Button paypal;
    private Button creditCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        String value;
        Bundle bundle = getIntent().getExtras();
        value = bundle.getString("ItemPosition");
        int price = Integer.parseInt(value);

        total_fee=(TextView) findViewById(R.id.totalmoney);
        total_fee.setText(value);
        paypal=(Button) findViewById(R.id.paypalButton);
        creditCard=(Button) findViewById(R.id.cardButton);

        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
