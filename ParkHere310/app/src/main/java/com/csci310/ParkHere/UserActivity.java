package com.csci310.ParkHere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class UserActivity extends AppCompatActivity {

    private Button viewHostHistoryButton, viewRentHistoryButton, returnHomeScreenButton;
    private ToggleButton editToggleButton;
    private EditText nameEditText, emailEditText, phoneEditText;
    private ImageView profilePicImageView;
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        profilePicImageView = (ImageView) findViewById(R.id.profilePicImageView);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        editToggleButton = (ToggleButton) findViewById(R.id.editUserDetailsToggleButton);
        viewHostHistoryButton = (Button) findViewById(R.id.viewHostHistoryButton);
        viewRentHistoryButton = (Button) findViewById(R.id.viewRentHistoryButton);
        returnHomeScreenButton = (Button) findViewById(R.id.returnHomeScreenButton);

        //Pull User Info
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        String userID = mFirebaseUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String name = mDatabase.child("users").child(userID).toString();

        String email = mFirebaseUser.getEmail();
        Uri uri = mFirebaseUser.getPhotoUrl();

        nameEditText.setText(name,TextView.BufferType.EDITABLE);
        emailEditText.setText(email,TextView.BufferType.EDITABLE);
        profilePicImageView.setImageURI(uri);

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

                    //Update User Info

                    mFirebaseUser.updateEmail(emailEditText.getText().toString());
                    UserProfileChangeRequest updateName = new UserProfileChangeRequest.Builder().setDisplayName(nameEditText.getText().toString()).build();
                    mFirebaseUser.updateProfile(updateName);
                }
            }
        });

        viewHostHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Go to Host History
            }
        });

        viewRentHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Go to Rent History
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
