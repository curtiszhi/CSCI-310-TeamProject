package com.teamproject.csci310.parkhere310;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        String email = emailEditText.getText().toString();
        String password = passEditText.getText().toString();
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("message", "signinUserWithEmail:onComplete:" + task.isSuccessful());
                Intent intent = new Intent(SignInActivity.this, ActionActivity.class);
                startActivity(intent);
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(SignInActivity.this).create();
                    alertDialog.setTitle("Sign-In Error");
                    alertDialog.setMessage("Invalid Email/Password");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });
        /*if(.isSuccessful()){

        } */
        /*else{
            AlertDialog alertDialog = new AlertDialog.Builder(SignInActivity.this).create();
            alertDialog.setTitle("Sign-In Error");
            alertDialog.setMessage("Passwords did not match.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }*/
    }
}
