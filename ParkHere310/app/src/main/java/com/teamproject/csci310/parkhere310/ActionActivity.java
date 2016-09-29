package com.teamproject.csci310.parkhere310;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by seanyuan on 9/28/16.
 */
public class ActionActivity extends AppCompatActivity {
    Button signOutButton;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        mFirebaseAuth = FirebaseAuth.getInstance();
        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
                Intent intent = new Intent(ActionActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }
    //TODO: add back button action - should not be able to return to register screen
}
