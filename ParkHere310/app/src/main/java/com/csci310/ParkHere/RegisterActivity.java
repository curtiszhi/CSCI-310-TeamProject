package com.csci310.ParkHere;

/**
 * Created by seanyuan on 9/30/16.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton, cancelButton;
    private EditText nameEditText, emailEditText, passEditText, passConfEditText, phoneEditText;
    private ToggleButton toggleButton;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDiag;
    private Boolean defaultHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFirebaseAuth = FirebaseAuth.getInstance();
        registerButton = (Button) findViewById(R.id.registerButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passEditText = (EditText) findViewById(R.id.passwordEditText);
        passConfEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        toggleButton = (ToggleButton) findViewById(R.id.togglebutton);
        progressDiag = new ProgressDialog(this);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (toggleButton.isChecked()) {
                    defaultHost = true;
                } else {
                    defaultHost = false;
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = passEditText.getText().toString().trim();
                String conf = passConfEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String name = nameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                Boolean valid = validateFields(email, name, pass, phone);
                if(valid){
                if(pass.equals(conf)) {
                    if(validatePassword(pass)){
                        progressDiag.setMessage("Registering User...");
                        progressDiag.show();
                        mFirebaseAuth.createUserWithEmailAndPassword(emailEditText.getText().toString().trim(), pass)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "failed" + task.getException(),
                                                    Toast.LENGTH_SHORT).show();
                                            progressDiag.hide();
                                            return;
                                        }else{
                                            String namey = nameEditText.getText().toString().trim();
                                            String phoney = phoneEditText.getText().toString().trim();
                                            writeNewUser(namey, phoney, defaultHost);
                                            Intent intent = new Intent(RegisterActivity.this, ActionActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                    }
                    else{
                        passwordAlert("invalid");
                    }
                }
                else{
                    passwordAlert("match");
                }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }

    private void writeNewUser( String userName, String phone, Boolean isHost) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        String userID = mFirebaseUser.getUid();
        String email = mFirebaseUser.getEmail();
        List<String> renting = new ArrayList<>();
        List<String> hosting = new ArrayList<>();
        User database_user = new User();
        database_user.setEmail(email);
        database_user.setHost(isHost);
        database_user.setPhone(phone);
        database_user.setUserName(userName);
        mDatabase.child("users").child(userID).setValue(database_user);
    }

    public static boolean validatePassword(String unhashedPassword) {
        boolean hasUppercase = !unhashedPassword.equals(unhashedPassword.toLowerCase());
        boolean hasNumber = unhashedPassword.matches(".*\\d+.*");
        boolean longEnough = unhashedPassword.length() >= 6;
        return hasUppercase && hasNumber && longEnough;
    }
    public void passwordAlert(String error){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        alertDialog.setTitle("Wait!");
        if(error.equals("invalid")){
            alertDialog.setMessage("Passwords must be at least 6 characters long and contain at least: 1-number 1-uppercase letter");
        } else{
            alertDialog.setMessage("Passwords did not match.");
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    public Boolean validateFields(String email, String name,  String pass, String phone){
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(phone)){
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Wait!");
            alertDialog.setMessage("Please fill all fields");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
            return false;
        }
        if(!email.contains("@")){
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Wait!");
            alertDialog.setMessage("Please enter valid email");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
            return false;
        }else{
            return true;
        }
    }
}


