package com.example.admin.college20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateEvent1 extends AppCompatActivity {
    private EditText mEventTitle, mEventClub, mContact, mPrice;
    Spinner spinner;

    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event1);

        spinner = (Spinner) findViewById(R.id.spinnerCategory);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Categories, android.R.layout.simple_spinner_item);

        //Specify the layout to use when the spinner appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        mEventTitle = (EditText) findViewById(R.id.event_title);
        mEventClub = (EditText) findViewById(R.id.event_club);
        mContact = (EditText) findViewById(R.id.contact_info);
        mPrice = (EditText) findViewById(R.id.event_price);
        next = (Button) findViewById(R.id.nextButton0);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String event_title = mEventTitle.getText().toString().trim();
                final String contact = mContact.getText().toString().trim();
                final String event_cat = spinner.getSelectedItem().toString();
                final String club = mEventClub.getText().toString().trim();
                final String price = mPrice.getText().toString().trim();
                Log.d("event_cat", event_cat);

                if (!TextUtils.isEmpty(event_title)
                        && !TextUtils.isEmpty(contact)
                        && !TextUtils.isEmpty(event_cat)
                        && !TextUtils.isEmpty(price)
                        && !event_cat.equals("Select Item")) {
                    Intent i = new Intent(CreateEvent1.this, CreateEvent2.class);

                        SharedPreferences sp = getSharedPreferences("preference", Context.MODE_PRIVATE);
                    SharedPreferences.Editor preferences = sp.edit();
                    preferences.putString("title", event_title);
                    preferences.putString("event_cat", event_cat);
                    preferences.putString("contact", contact);
                    preferences.putString("club", club);
                    preferences.putString("price", price);
                    preferences.commit();

                    startActivity(i);
                }
                else{
                    Toast.makeText(CreateEvent1.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
