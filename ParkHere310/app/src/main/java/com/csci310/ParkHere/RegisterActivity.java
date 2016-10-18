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
                validateFields(email, name, pass, phone);
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
                                            String name = nameEditText.getText().toString().trim();
                                            String phone = phoneEditText.getText().toString().trim();
                                            mFirebaseAuth = FirebaseAuth.getInstance();
                                            mFirebaseUser = mFirebaseAuth.getCurrentUser();
                                            writeNewUser(mFirebaseUser.getUid(), name, mFirebaseUser.getEmail(), phone, defaultHost);
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
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }

    private void writeNewUser(String userId, String userName, String email, String phone, Boolean isHost) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        List<FeedItem> renting = new ArrayList<>();
            FeedItem item = new FeedItem();
            item.setAddress("Add your first renting spot!");
            //item.setThumbnail(R.drawable.common_google_signin_btn_icon_dark);
            //item.setDates("10/02 to 10/05");
            item.setPrice(9);
            item.setActivity(true);
            item.setRating("3");
            item.setSpotID(Integer.toString(item.hashCode()));

        FeedItem item2 = new FeedItem();
        item2.setAddress("Add your first host spot!");
        //item2.setThumbnail(R.drawable.common_google_signin_btn_icon_dark);
        //item2.setDates("10/02 to 10/05");
        item2.setPrice(9);
        item2.setActivity(true);
        item2.setRating("3");
        item2.setSpotID(Integer.toString(item.hashCode()));
        renting.add(item);
        List<FeedItem> hosting = new ArrayList<>();
        hosting.add(item2);
        User database_user = new User(userName, email, phone, isHost, renting, hosting);
        mDatabase.child("users").child(userId).setValue(database_user);
        mDatabase.child("parking-spots").child(item.getSpotID()).setValue(item);
        mDatabase.child("parking-spots").child(item2.getSpotID()).setValue(item2);
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
    public void validateFields(String email, String name,  String pass, String phone){
        if(TextUtils.isEmpty(email)){
            Toast.makeText(RegisterActivity.this, "Please enter email",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(RegisterActivity.this, "Please enter name",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(RegisterActivity.this, "Please enter password",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(RegisterActivity.this, "Please enter phone number",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }
}


