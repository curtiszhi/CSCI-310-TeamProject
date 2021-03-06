package com.csci310.ParkHere;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by seanyuan on 9/30/16.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Debug Message:";
    private  Button loginButton,registerButton, forgotButton;
    private EditText emailEditText,passEditText;
    private FirebaseAuth mFirebaseAuth;
    private ProgressBar progressDiag;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailEditText=(EditText)findViewById(R.id.emailEditText);
        passEditText=(EditText)findViewById(R.id.passwordEditText);
        loginButton=(Button)findViewById(R.id.loginButton);
        registerButton=(Button)findViewById(R.id.registerButton);
        forgotButton = (Button) findViewById(R.id.forgotButton);
        progressDiag = (ProgressBar) findViewById(R.id.signInProgress);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent intent = new Intent(MainActivity.this, ActionActivity.class);
                    startActivity(intent);
                } else {
                    // User is signed out, stay here
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

        });
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PasswordReset.class);
                startActivity(intent);
            }
        });
    }
    private boolean check_empty(){
        if(emailEditText.getText().toString().trim().length()==0 || passEditText.getText().toString().trim().length()==0){
            return true;
        }
        return false;
    }
    private void signIn() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        String email = emailEditText.getText().toString();
        String password = passEditText.getText().toString();
        progressDiag.setVisibility(View.VISIBLE);
        //progressDiag.setMessage("Signing In...");
        //progressDiag.show();
        if(!check_empty()) {
            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressDiag.setVisibility(View.GONE);
                                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("Sign-In Error");
                                alertDialog.setMessage("Invalid Email/Password");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alertDialog.show();
                            } else {

                                Log.d(TAG, "signinUserWithEmail:onComplete:" + task.isSuccessful());
                                Intent intent = new Intent(MainActivity.this, ActionActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
            //progressDiag.setVisibility(View.GONE);
        }else{
            progressDiag.setVisibility(View.GONE);
            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Empty");
            alertDialog.setMessage("please fill all textfield");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
        // ...
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
        // ...
    }


}


