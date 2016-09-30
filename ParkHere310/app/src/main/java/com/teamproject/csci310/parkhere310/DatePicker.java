package com.teamproject.csci310.parkhere310;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * File created by seanyuan on 9/29/16.
 * Source: http://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
 */

public class DatePicker  implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    EditText dateEditText;
    private int _day;
    private int _month;
    private int _birthYear;
    private Context _context;

    public DatePicker(Context context, int editTextViewID) {
        Activity act = (Activity)context;
        this.dateEditText = (EditText) act.findViewById(editTextViewID);
        this.dateEditText.setOnClickListener(this);
        this._context = context;
    }
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        _birthYear = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();
    }
    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(_context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    // updates the date in the birth date EditText
    private void updateDisplay() {

        dateEditText.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(_day).append("/").append(_month + 1).append("/").append(_birthYear).append(" "));
    }
}

