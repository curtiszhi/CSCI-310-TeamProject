package com.teamproject.csci310.parkhere310;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
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
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        TimePickerDialog tp1 = new TimePickerDialog(_context, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        tp1.show();
    }

    // updates the date in the birth date EditText
    private void updateDisplay() {

        timeEditText.setText(new StringBuilder()
                .append(hour).append(":").append(minute));
    }
}
