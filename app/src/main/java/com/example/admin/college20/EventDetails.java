package com.example.admin.college20;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

public class EventDetails extends AppCompatActivity implements View.OnClickListener {
    ImageView eventImage;
    TextView title1, desc1, location1, price1,
            start_to_end1, club1;
    Button fblink1, weblink1, contact1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        eventImage = (ImageView) findViewById(R.id.eventImage_detailsPage);
        desc1 = (TextView) findViewById(R.id.request_desc);
        title1 = (TextView) findViewById(R.id.request_title_d);
        location1 = (TextView) findViewById(R.id.request_location);
       // price1 = (TextView) findViewById(R.id.request_price);
        club1 = (TextView) findViewById(R.id.request_club_d);
        weblink1 = (Button) findViewById(R.id.request_weblink);
        fblink1 = (Button) findViewById(R.id.request_fblink);
        contact1 = (Button) findViewById(R.id.request_contact);
        start_to_end1 = (TextView) findViewById(R.id.from_and_to_text);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEventDetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        final String desc = intent.getStringExtra("desc");
        final String imageUrl = intent.getStringExtra("imageUrl");
        final String title = intent.getStringExtra("title");
        final String start_date = intent.getStringExtra("start_date");
        final String end_date = intent.getStringExtra("end_date");
        final String start_time = intent.getStringExtra("start_time");
        final String end_time = intent.getStringExtra("end_time");
        final String club = intent.getStringExtra("club");
        final String category = intent.getStringExtra("category");
      //  final String price = intent.getStringExtra("price");
        final String location = intent.getStringExtra("location");

        final String src_start_date = "<b>" + start_date + "</b>";
        final String src_end_date = "<b>" + end_date  + "</b>";
        desc1.setText(desc);
        title1.setText(title);
        location1.setText(location);
//        price1.setText(price);
        club1.setText(club);

        fblink1.setOnClickListener(this);
        weblink1.setOnClickListener(this);
        contact1.setOnClickListener(this);

        start_to_end1.setText(Html.fromHtml(start_time+", " + src_start_date + " - " +
                end_time + ", " + src_end_date));
        toolbar.setTitle(category);

        Picasso.with(getApplicationContext()).load(imageUrl).into(eventImage);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.eventdetails, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_back:
                Intent action_back = new Intent(EventDetails.this, EventView.class);
                startActivity(action_back);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        final String fblink = intent.getStringExtra("fblink");
        final String weblink = intent.getStringExtra("weblink");
        final String contact = intent.getStringExtra("contact");
        switch(v.getId()){
            case R.id.request_fblink:
                Uri uri = Uri.parse(fblink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(i);

                } catch (Exception e) {
                    Toast.makeText(this,"Invalid Details", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.request_weblink:
                Uri uri1 = Uri.parse(weblink);
                Intent j = new Intent(Intent.ACTION_VIEW, uri1);
                try {
                    startActivity(j);
                } catch (Exception e) {
                    Toast.makeText(this,"Invalid Details", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.request_contact:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                Log.d("call", contact);
                String p = "tel:" + contact;
                callIntent.setData(Uri.parse(p));
                try {
                    startActivity(callIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "Invalid Number", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
