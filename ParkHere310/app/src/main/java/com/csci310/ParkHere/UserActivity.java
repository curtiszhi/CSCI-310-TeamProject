package com.csci310.ParkHere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class UserActivity extends AppCompatActivity {

    private Button viewHostHistoryButton, viewRentHistoryButton, returnHomeScreenButton;
    private ToggleButton editToggleButton;
    private EditText nameEditText, emailEditText, phoneEditText;
    private ImageView profilePicImageView;
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private final static int SELECT_PHOTO = 1;

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

        // Edit Profile Pic (Not Tested Yet)
        profilePicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent pickPhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pickPhotoIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(pickPhotoIntent,"Select Picture"), SELECT_PHOTO);
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
                    emailEditText.setKeyListener((KeyListener) emailEditText.getTag());
                    phoneEditText.setKeyListener((KeyListener) phoneEditText.getTag());
                } else {
                    nameEditText.setTag(nameEditText.getKeyListener());
                    nameEditText.setKeyListener(null);

                    emailEditText.setTag(emailEditText.getKeyListener());
                    emailEditText.setKeyListener(null);

                    phoneEditText.setTag(phoneEditText.getKeyListener());
                    phoneEditText.setKeyListener(null);

                    //Update User Info (Not Complete)
                    mFirebaseUser.updateEmail(emailEditText.getText().toString());
                    UserProfileChangeRequest updateName = new UserProfileChangeRequest.Builder().setDisplayName(nameEditText.getText().toString()).build();
                    mFirebaseUser.updateProfile(updateName);
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

        returnHomeScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    //Update Profile Pic (Not Tested Yet)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                profilePicImageView.setImageBitmap(bitmap);
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
