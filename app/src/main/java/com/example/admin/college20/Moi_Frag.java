package com.example.admin.college20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Moi_Frag extends Fragment {
    CircleImageView imageView;
    FloatingActionButton floatingActionButton;
    private static final int GALLERY_INTENT = 1;
    private Uri imageUri = null;
    TextView text_name, text_email;
    ProgressDialog progressDialog;
    DatabaseReference event_user_image_ref;

    private String uid;
    StorageReference storageReference;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mView = inflater.inflate(R.layout.activity_moi__frag, container, false);

            imageView = (CircleImageView) mView.findViewById(R.id.register_image);
            floatingActionButton = (FloatingActionButton) mView.findViewById(R.id.floatingActionButton);
            text_name = (TextView) mView.findViewById(R.id.moi_name);
            text_email = (TextView) mView.findViewById(R.id.moi_email);

            progressDialog = new ProgressDialog(getContext());

            storageReference = FirebaseStorage.getInstance().getReference();


            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference().child("Users").child(uid);


        ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null){
                        progressDialog.setMessage("Updating Information");
                        progressDialog.show();
                        User user = dataSnapshot.getValue(User.class);
                        final String name = user.getName();
                        final String email  = user.getEmail();
                        final String imageUrl = user.getImageUrl();

                        SharedPreferences sp1 = getActivity().getSharedPreferences("moi_frag", Context.MODE_PRIVATE);
                        SharedPreferences.Editor preferences = sp1.edit();
                        preferences.putString("name", name);
                        preferences.putString("imageUrl", imageUrl);
                        preferences.commit();

                        text_name.setText(name);
                        text_email.setText(email);

                        Picasso.with(getContext()).load(imageUrl).into(imageView);
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        SharedPreferences preferences = getActivity().getSharedPreferences("moi_frag", Context.MODE_PRIVATE);
        final String name = preferences.getString("name", "");
        final String imageUrl = preferences.getString("imageUrl", "");
        final DatabaseReference dr = database.getReference().child("ApprovedEvents");

        Query query = dr.orderByChild("event_username").equalTo(name);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot!=null) {
                    progressDialog.setMessage("Updating attributes");
                    progressDialog.show();
                    dr.child(dataSnapshot.getKey()).child("event_user_image")
                            .setValue(imageUrl);
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        return mView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            imageUri = data.getData();
            if(imageUri!=null){
                imageUploading();
            }
        }
    }
    private void imageUploading() {
        progressDialog.setMessage("Changing Your Pic");
        progressDialog.show();

        StorageReference filepath = storageReference.child("Images").child(imageUri.getLastPathSegment());
        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                Uri uri = taskSnapshot.getDownloadUrl();
                final String uid1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Map<String,Object> taskMap = new HashMap<String,Object>();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid1);

                taskMap.put("imageUrl", uri.toString());

                databaseReference.updateChildren(taskMap);
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Try Again!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
