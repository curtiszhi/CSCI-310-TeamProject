package com.teamproject.csci310.parkhere310;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by seanyuan on 9/28/16.
 */
public class RegisterActivity extends AppCompatActivity {
    Button facebookButton,googleButton, registerButton, cancelButton;
    EditText nameEditText, emailEditText, passEditText, passConfEditText, phoneEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton = (Button) findViewById(R.id.registerButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passEditText = (EditText) findViewById(R.id.passwordEditText);
        passConfEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = passEditText.getText().toString();
                String conf = passConfEditText.getText().toString();
                if(pass.equals(conf)) {
                    if(validatePassword(pass)){
                        //good to go
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
}


