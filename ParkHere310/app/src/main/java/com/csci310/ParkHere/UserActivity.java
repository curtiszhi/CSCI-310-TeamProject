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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference userRef = mDatabase.child("users/" + mFirebaseUser.getUid() + "/userName/");
        DatabaseReference phoneRef = mDatabase.child("users/" + mFirebaseUser.getUid() + "/phone/");
        String name = userRef.getKey();
        String email = mFirebaseUser.getEmail();
        String phone = phoneRef.getKey();
        Uri uri = mFirebaseUser.getPhotoUrl();

        // Doesn't work
        /*
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                nameEditText.setText(user.userName, TextView.BufferType.EDITABLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */

        //Set User Info
        nameEditText.setText(name,TextView.BufferType.EDITABLE);
        emailEditText.setText(email,TextView.BufferType.EDITABLE);
        phoneEditText.setText(phone,TextView.BufferType.EDITABLE);
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
