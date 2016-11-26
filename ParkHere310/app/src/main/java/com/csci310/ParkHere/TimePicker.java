package com.csci310.ParkHere;

/**
 * Created by seanyuan on 10/13/16.
 */

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * File created by seanyuan on 9/30/16.
 * Source: http://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
 */

public class TimePicker implements View.OnClickListener, TimePickerDialog.OnTimeSetListener{
    EditText timeEditText;
    private int hour;
    private int minute;
    private String hour_s;
    private String minute_s;
    private Context _context;

    public TimePicker(Context context, int editTextViewID) {
        Activity act = (Activity)context;
        this.timeEditText = (EditText) act.findViewById(editTextViewID);
        this.timeEditText.setOnClickListener(this);
        this._context = context;
    }


    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        updateDisplay();
    }
    @Override
    public void onClick(View v) {
        hideSoftKeyboard((Activity)_context);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        TimePickerDialog tp1 = new TimePickerDialog(_context, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        tp1.show();
    }

    // updates the date in the birth date EditText
    private void updateDisplay() {
        String AM_PM ;
        if(hour < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }
        if(hour<10){
            hour_s="0"+hour;
        }else{
            hour_s=""+hour;
        }
        if(minute<10){
            minute_s="0"+minute;
        }else{
            minute_s=""+minute;
        }
        timeEditText.setText(new StringBuilder()
                .append(hour_s).append(":").append(minute_s).append(AM_PM));
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
