package com.csci310.ParkHere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ToggleButton;
/*
public class UserActivity extends AppCompatActivity {

    private Button viewHostHistoryButton, viewRentHistoryButton, returnHomeScreenButton;
    private ToggleButton editToggleButton;
    private EditText nameEditText, emailEditText, phoneEditText;
    KeyListener oldNameKeyListener, oldEmailKeyListener, oldPhoneKeyListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        editToggleButton = (ToggleButton) findViewById(R.id.editUserDetailsToggleButton);
        viewHostHistoryButton = (Button) findViewById(R.id.viewHostHistoryButton);
        viewRentHistoryButton = (Button) findViewById(R.id.viewRentHistoryButton);
        returnHomeScreenButton = (Button) findViewById(R.id.returnHomeScreenButton);

        oldNameKeyListener = nameEditText.getKeyListener();
        oldEmailKeyListener = emailEditText.getKeyListener();
        oldPhoneKeyListener = phoneEditText.getKeyListener();

        nameEditText.setKeyListener(null);
        nameEditText.setFocusable(false);
        nameEditText.setClickable(false);

        emailEditText.setKeyListener(null);
        emailEditText.setFocusable(false);
        emailEditText.setClickable(false);

        phoneEditText.setKeyListener(null);
        phoneEditText.setFocusable(false);
        phoneEditText.setClickable(false);

        editToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (editToggleButton.isChecked()) {
                    nameEditText.setKeyListener(null);
                    nameEditText.setFocusable(false);
                    nameEditText.setClickable(false);

                    emailEditText.setKeyListener(null);
                    emailEditText.setFocusable(false);
                    emailEditText.setClickable(false);

                    phoneEditText.setKeyListener(null);
                    phoneEditText.setFocusable(false);
                    phoneEditText.setClickable(false);
                } else {
                    nameEditText.setKeyListener(oldNameKeyListener);
                    nameEditText.setFocusable(true);
                    nameEditText.setClickable(true);

                    nameEditText.setKeyListener(oldEmailKeyListener);
                    emailEditText.setFocusable(true);
                    emailEditText.setClickable(true);

                    nameEditText.setKeyListener(oldPhoneKeyListener);
                    phoneEditText.setFocusable(true);
                    phoneEditText.setClickable(true);
                }
            }
        });

        viewHostHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //To do
            }
        });

        viewRentHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //To do
            }
        });

        returnHomeScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
*/