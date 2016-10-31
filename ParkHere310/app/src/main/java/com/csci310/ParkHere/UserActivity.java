package com.csci310.ParkHere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class UserActivity extends AppCompatActivity {

    private Button viewHostHistoryButton, viewRentHistoryButton, returnHomeScreenButton;
    private ToggleButton editToggleButton;
    private EditText nameEditText, emailEditText, phoneEditText;
    private ImageView profilePicImageView;
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private Bitmap s_image;
    private static final int selected_p = 1;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        profilePicImageView = (ImageView) findViewById(R.id.profilePicImageView);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        editToggleButton = (ToggleButton) findViewById(R.id.editUserDetailsToggleButton);
        viewHostHistoryButton = (Button) findViewById(R.id.viewHostHistoryButton);
        viewRentHistoryButton = (Button) findViewById(R.id.viewRentHistoryButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        // Edit Profile Pic (Not Tested Yet)

        profilePicImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(editToggleButton.isChecked()){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), selected_p);
                }
            }
        });


        //Get User Info
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String email = mFirebaseUser.getEmail();
        String phone = ActionActivity.user_all.getPhone();
        String name = ActionActivity.user_all.getUserName();
        Uri uri = mFirebaseUser.getPhotoUrl();



        //Set User Info
        nameEditText.setText(name.trim(),TextView.BufferType.EDITABLE);
        emailEditText.setText(email.trim(),TextView.BufferType.EDITABLE);
        phoneEditText.setText(phone.trim(),TextView.BufferType.EDITABLE);
        profilePicImageView.setImageURI(uri);


        //Set EditText Not Editable
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
                    //emailEditText.setKeyListener((KeyListener) emailEditText.getTag());
                    phoneEditText.setKeyListener((KeyListener) phoneEditText.getTag());
                } else {
                    nameEditText.setTag(nameEditText.getKeyListener());
                    nameEditText.setKeyListener(null);

                    //emailEditText.setTag(emailEditText.getKeyListener());
                    //emailEditText.setKeyListener(null);

                    phoneEditText.setTag(phoneEditText.getKeyListener());
                    phoneEditText.setKeyListener(null);

                    //Update User Info
                    mFirebaseUser.updateEmail(emailEditText.getText().toString());
                    UserProfileChangeRequest updateName = new UserProfileChangeRequest.Builder().setDisplayName(nameEditText.getText().toString()).build();
                    mFirebaseUser.updateProfile(updateName);
                    ActionActivity.user_all.setUserName(nameEditText.getText().toString().trim());
                    //ActionActivity.user_all.setEmail(emailEditText.getText().toString().trim());
                    ActionActivity.user_all.setPhone(phoneEditText.getText().toString().trim());
                    //update all
                }
            }
        });

        viewHostHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(UserActivity.this, ListingActivity.class);
                startActivity(intent);
            }
        });

        /*viewRentHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Go to Rent History
            }
        });*/




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case selected_p:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        s_image = BitmapFactory.decodeStream(imageStream);
                        profilePicImageView.setImageBitmap(s_image);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }


    }



}
