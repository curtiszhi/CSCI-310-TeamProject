package com.teamproject.csci310.parkhere310;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by seanyuan on 9/28/16.
 */
public class RegisterActivity extends AppCompatActivity  {
    Button facebookButton,googleButton, registerButton, cancelButton;
    EditText nameEditText, emailEditText, passEditText, passConfEditText, phoneEditText;
    Switch ownerSwitch;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDiag;

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
        ownerSwitch = (Switch) findViewById(R.id.ownerSwitch);
        progressDiag = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = passEditText.getText().toString().trim();
                String conf = passConfEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String name = nameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                validateFields(email, name, phone, pass);
                if(pass.equals(conf)) {
                    if(validatePassword(pass)){
                        progressDiag.setMessage("Registering User...");
                        progressDiag.show();
                        mFirebaseAuth.createUserWithEmailAndPassword(emailEditText.getText().toString().trim(), pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "failed",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                    String name = nameEditText.getText().toString().trim();
                                    String phone = phoneEditText.getText().toString().trim();
                                    Boolean isOwner = ownerSwitch.isChecked();
                                    mFirebaseAuth = FirebaseAuth.getInstance();
                                    mFirebaseUser = mFirebaseAuth.getCurrentUser();
                                    writeNewUser(mFirebaseUser.getUid(), name, mFirebaseUser.getEmail(), phone, isOwner);
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

    private void writeNewUser(String userId, String userName, String email, String userPhone, boolean isOwner) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        User user = new User(userName, email, userPhone, isOwner);
        mDatabase.child("users").child(userId).setValue(user);
    }

    public static boolean validatePassword(String unhashedPassword) {
        boolean hasUppercase = !unhashedPassword.equals(unhashedPassword.toLowerCase());
        boolean hasNumber = unhashedPassword.matches(".*\\d+.*");
        return hasUppercase && hasNumber;
    }
    public void passwordAlert(String error){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        alertDialog.setTitle("Wait!");
        if(error.equals("invalid")){
            alertDialog.setMessage("Passwords must contain at least: 1-number 1-uppercase letter");
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
    public void validateFields(String email, String name, String phone, String pass){
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
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(RegisterActivity.this, "Please enter phone number",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(RegisterActivity.this, "Please enter password",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }
}


