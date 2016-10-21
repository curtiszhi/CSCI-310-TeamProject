package com.csci310.ParkHere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {

    private Button viewHostHistoryButton, viewRentHistoryButton, returnHomeScreenButton;
    private ToggleButton editToggleButton;
    private EditText nameEditText, emailEditText, phoneEditText;
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;

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

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        nameEditText.setTag(nameEditText.getKeyListener());
        nameEditText.setKeyListener(null);

        emailEditText.setTag(emailEditText.getKeyListener());
        emailEditText.setKeyListener(null);

        phoneEditText.setTag(phoneEditText.getKeyListener());
        phoneEditText.setKeyListener(null);

        editToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (editToggleButton.isChecked()) {
                    nameEditText.setKeyListener((KeyListener) nameEditText.getTag());
                    emailEditText.setKeyListener((KeyListener) emailEditText.getTag());
                    phoneEditText.setKeyListener((KeyListener) phoneEditText.getTag());
                } else {
                    nameEditText.setTag(nameEditText.getKeyListener());
                    nameEditText.setKeyListener(null);

                    emailEditText.setTag(emailEditText.getKeyListener());
                    emailEditText.setKeyListener(null);

                    phoneEditText.setTag(phoneEditText.getKeyListener());
                    phoneEditText.setKeyListener(null);
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
