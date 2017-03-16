package com.example.admin.college20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateEvent4 extends AppCompatActivity {
    private EditText mEventDesc;

    private Button upload_image_button, doneButton;

    private ImageView eventImage;
    private static final int GALLERY_INTENT = 1;
    private Uri imageUri = null;
    private ProgressDialog progressDialog;

    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event4);

        progressDialog = new ProgressDialog(this);

        mEventDesc = (EditText) findViewById(R.id.event_desc);
        upload_image_button = (Button) findViewById(R.id.upload_image_button);
        doneButton = (Button) findViewById(R.id.doneButton);
        eventImage = (ImageView) findViewById(R.id.eventImage);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Event");

        upload_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Uploading Data");
                progressDialog.show();
                startPosting();
            }
        });
    }
    public void startPosting() {

        Intent in = getIntent();
        final String event_location = in.getStringExtra("location");
        final String fblink = in.getStringExtra("fblink");
        final String weblink = in.getStringExtra("weblink");


        SharedPreferences preferences = getSharedPreferences("preference", Context.MODE_PRIVATE);
        final String event_title = preferences.getString("title", "");
        final String event_category = preferences.getString("event_cat","");

        final String event_contact = preferences.getString("contact","");
        final String club = preferences.getString("club", "");
        final String price = preferences.getString("price", "");
        final String start_date = preferences.getString("start_date", "");
        final String end_date = preferences.getString("end_date","");
        final String start_time = preferences.getString("start_time", "");
        final String end_time = preferences.getString("end_time", "");


        if (!TextUtils.isEmpty(mEventDesc.getText().toString())
                && imageUri != null) {

            StorageReference filepath = mStorageRef.child("Images").child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri uri = taskSnapshot.getDownloadUrl();

                    DatabaseReference newEvent = databaseReference.push();

                    newEvent.child("title").setValue(event_title);
                    newEvent.child("category").setValue(event_category);
                    newEvent.child("club").setValue(club);
                    newEvent.child("contact").setValue(event_contact);
                    newEvent.child("price").setValue(price);

                    newEvent.child("start_date").setValue(start_date);
                    newEvent.child("end_date").setValue(end_date);
                    newEvent.child("start_time").setValue(start_time);
                    newEvent.child("end_time").setValue(end_time);

                    newEvent.child("location").setValue(event_location);
                    newEvent.child("fblink").setValue(fblink);
                    newEvent.child("weblink").setValue(weblink);

                    newEvent.child("desc").setValue(mEventDesc.getText().toString());
                    newEvent.child("imageUrl").setValue(uri.toString());

                    progressDialog.dismiss();
                    startActivity(new Intent(CreateEvent4.this, MainPage1.class));

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateEvent4.this, "Upload Failed :(", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this, "Not Uploaded", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            imageUri = data.getData();
            eventImage.setImageURI(imageUri);
        }
    }

}

