package com.csci310.ParkHere;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by seanyuan on 11/28/16.
 */

public class PasswordReset extends AppCompatActivity {
    private ProgressBar progressDiag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passwordresetview);
        final TextView recemail = (TextView) findViewById(R.id.recemailEditText);
        Button recbutton = (Button) findViewById(R.id.recoveryButton);
        recbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = recemail.getText().toString().trim();
                progressDiag.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Recovery email: ", "Email successfully sent.");
                                    progressDiag.setVisibility(View.GONE);
                                    finish();
                                }
                                if (!task.isSuccessful()) {
                                    progressDiag.setVisibility(View.GONE);
                                    AlertDialog alertDialog = new AlertDialog.Builder(PasswordReset.this).create();
                                    alertDialog.setTitle("Recovery Error");
                                    alertDialog.setMessage("Invalid email, please try again.");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();
                                }
                            }
                        });
            }
        });
    }
}
