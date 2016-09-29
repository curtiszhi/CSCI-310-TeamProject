package com.teamproject.csci310.parkhere310;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by seanyuan on 9/29/16.
 */
public class SignInActivity extends AppCompatActivity {
    Button loginButton,registerButton, forgotButton;
    EditText emailEditText,passEditText;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailEditText=(EditText)findViewById(R.id.emailEditText);
        passEditText=(EditText)findViewById(R.id.passwordEditText);
        loginButton=(Button)findViewById(R.id.loginButton);
        registerButton=(Button)findViewById(R.id.registerButton);
        forgotButton = (Button) findViewById(R.id.forgotButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

        });

    }
    private void signIn() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        String email = emailEditText.toString();
        String password = passEditText.toString();
        if(mFirebaseAuth.signInWithEmailAndPassword(email, password).isSuccessful()){
            Intent intent = new Intent(SignInActivity.this, ActionActivity.class);
            startActivity(intent);
        } else{
            AlertDialog alertDialog = new AlertDialog.Builder(SignInActivity.this).create();
            alertDialog.setTitle("Sign-In Error");
            alertDialog.setMessage("Passwords did not match.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }
}
