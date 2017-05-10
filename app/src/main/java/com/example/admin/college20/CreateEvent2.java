package com.example.admin.college20;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateEvent2 extends Activity implements View.OnClickListener {

    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button fromDateBttn, toDateBttn, fromTimeBttn, toTimeBttn, nextBttn;
    TextView startDate, endDate, startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event2);

        fromDateBttn = (Button) findViewById(R.id.fromDateBttn);
        toDateBttn = (Button) findViewById(R.id.toDateBttn);
        fromTimeBttn = (Button) findViewById(R.id.toTimeBttn);
        toTimeBttn = (Button) findViewById(R.id.fromTimeBttn);
        nextBttn = (Button) findViewById(R.id.nextButton1);


        //TextView
        endTime = (TextView) findViewById(R.id.endTime);
        startTime = (TextView) findViewById(R.id.startTime);
        endDate = (TextView) findViewById(R.id.endDate);
        startDate = (TextView) findViewById(R.id.startDate);

        fromDateBttn.setBackgroundColor(Color.TRANSPARENT);
        toDateBttn.setBackgroundColor(Color.TRANSPARENT);
        fromTimeBttn.setBackgroundColor(Color.TRANSPARENT);
        toTimeBttn.setBackgroundColor(Color.TRANSPARENT);

        fromDateBttn.setOnClickListener(this);
        toDateBttn.setOnClickListener(this);
        fromTimeBttn.setOnClickListener(this);
        toTimeBttn.setOnClickListener(this);
        nextBttn.setOnClickListener(this);

        nextBttn.setBackgroundColor(getResources().getColor(R.color.light_orange));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fromDateBttn:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDate.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
                break;
            case R.id.toDateBttn:
                final Calendar c1 = Calendar.getInstance();
                mYear = c1.get(Calendar.YEAR);
                mMonth = c1.get(Calendar.MONTH);
                mDay = c1.get(Calendar.DAY_OF_MONTH);

                 datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDate.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
                break;
            case R.id.toTimeBttn:
                final Calendar t = Calendar.getInstance();

                mHour = t.get(Calendar.HOUR);
                mMinute = t.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int hour = hourOfDay % 12;
                        if (hour == 0)
                            hour = 12;
                        endTime.setText(String.format("%02d:%02d %s", hour, minute,
                                hourOfDay < 12 ? "am" : "pm"));
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.fromTimeBttn:
                final Calendar t1 = Calendar.getInstance();

                mHour = t1.get(Calendar.HOUR);
                mMinute = t1.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int hour = hourOfDay % 12;
                        if (hour == 0)
                            hour = 12;
                        startTime.setText(String.format("%02d:%02d %s", hour, minute,
                                hourOfDay < 12 ? "am" : "pm"));
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
                break;

            case R.id.nextButton1:

                final String startDate1 = startDate.getText().toString().trim();
                final String endDate1 = endDate.getText().toString().trim();
                final String startTime1 = startTime.getText().toString().trim();
                final String endTime1 = endTime.getText().toString().trim();

                if (!TextUtils.isEmpty(startDate1)
                        && !TextUtils.isEmpty(endDate1)
                        && !TextUtils.isEmpty(startTime1)
                        && !TextUtils.isEmpty(endTime1)) {
                    SharedPreferences sp = getSharedPreferences("preference", Context.MODE_PRIVATE);
                    SharedPreferences.Editor preferences = sp.edit();

                    preferences.putString("start_date", startDate1);
                    preferences.putString("end_date", endDate1);

                    preferences.putString("start_time", startTime1);
                    preferences.putString("end_time", endTime1);
                    preferences.commit();
                    startActivity(new Intent(CreateEvent2.this, CreateEvent3.class));
                }
                else{
                    Toast.makeText(CreateEvent2.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
