package com.csci310.ParkHere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.csci310.ParkHere.ActionActivity.user_all;

public class UserActivity extends AppCompatActivity {

    private Button viewHostHistoryButton;
    private ToggleButton editToggleButton;
    private EditText nameEditText, emailEditText, phoneEditText;
    private ImageView profilePicImageView;
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private Bitmap s_image;
    private static final int selected_p = 1;
    private RatingBar ratingBar;
    private boolean check=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.print("gets into user activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle("My Profile");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        profilePicImageView = (ImageView) findViewById(R.id.profilePicImageView);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        editToggleButton = (ToggleButton) findViewById(R.id.editUserDetailsToggleButton);
        viewHostHistoryButton = (Button) findViewById(R.id.viewHostHistoryButton);
        //viewRentHistoryButton = (Button) findViewById(R.id.viewRentHistoryButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);


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
        String email = ActionActivity.user_all.getEmail();
        String phone = ActionActivity.user_all.getPhone();
        String name = ActionActivity.user_all.getUserName();



        //Set User Info
        if (nameEditText != null){
            nameEditText.setText(name.trim(),TextView.BufferType.EDITABLE);
        }
        if (emailEditText != null){
            emailEditText.setText(email.trim(),TextView.BufferType.EDITABLE);
        }
        if (phoneEditText != null){
            phoneEditText.setText(phone.trim(),TextView.BufferType.EDITABLE);
        }

        ratingBar.setRating(user_all.calculate_Rating());

        if(ActionActivity.user_all.getPhoto()!=null) {
            byte[] decodedString = Base64.decode(ActionActivity.user_all.getPhoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profilePicImageView.setImageBitmap(decodedByte);
        }
        //Set EditText Not Editable
        nameEditText.setTag(nameEditText.getKeyListener());
        nameEditText.setKeyListener(null);

        emailEditText.setTag(emailEditText.getKeyListener());
        emailEditText.setKeyListener(null);

        phoneEditText.setTag(phoneEditText.getKeyListener());
        phoneEditText.setKeyListener(null);


        editToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (editToggleButton.isChecked()) {
                    nameEditText.setKeyListener((KeyListener) nameEditText.getTag());
                    phoneEditText.setKeyListener((KeyListener) phoneEditText.getTag());
                } else {
                    nameEditText.setTag(nameEditText.getKeyListener());
                    nameEditText.setKeyListener(null);
                    phoneEditText.setTag(phoneEditText.getKeyListener());
                    phoneEditText.setKeyListener(null);
                    user_all.setUserName(nameEditText.getText().toString().trim());
                    user_all.setPhone(phoneEditText.getText().toString().trim());
                    mDatabase.child("users").child(mFirebaseUser.getUid()).child("userName").setValue(user_all.getUserName());
                    mDatabase.child("users").child(mFirebaseUser.getUid()).child("phone").setValue(user_all.getPhone());
                    mDatabase.child("users").child(mFirebaseUser.getUid()).child("photo").setValue(user_all.getPhoto());
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
                        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
                        s_image.compress(Bitmap.CompressFormat.PNG, 100, bYtE);

                        byte[] byteArray = bYtE.toByteArray();
                        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);


                        user_all.setPhoto(imageFile);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }


    }



}
