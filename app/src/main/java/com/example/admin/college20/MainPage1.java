package com.example.admin.college20;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainPage1 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    NavigationView mp1NavigationView;
    DrawerLayout mp1NavigationLayout;
    FragmentManager mp1FragmentManager;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDatabaseUsers;
    FragmentTransaction mp1FragmentTransaction;
    Toolbar mp1_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page1);
        if(SaveSharedPreference.getUserName(MainPage1.this).length()!=0){
            Intent intent = new Intent(MainPage1.this, MainActivity.class);
            startActivity(intent);
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    //if returns null then user is not logged in
                    Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainPage1.this, "You got an Error", Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        final SharedPreferences user = getSharedPreferences("user", Context.MODE_PRIVATE);
        final String uid = user.getString("id", "");

        Log.d("userId", uid);

        mp1_toolbar = (Toolbar) findViewById(R.id.toolbar_mp1);
        mp1NavigationLayout = (DrawerLayout) findViewById(R.id.mainPage1DrawerLayout);
        mp1NavigationView = (NavigationView) findViewById(R.id.mainPage1DrawerView);


        /**
         * Get Fragment Manager and replace your frame layout with the fragment that you wish to
         replace it with
         */

        mp1FragmentManager = getSupportFragmentManager();
        mp1FragmentTransaction = mp1FragmentManager.beginTransaction();
        mp1FragmentTransaction.replace(R.id.containerToBeFilled, new mp1_SwipeTab()).commit();


        /** After getting the particular fragment you can now set your Navigation View Adapter
         * and Listener
         */
        mp1NavigationView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                getMenuInflater().inflate(R.menu.navigation_menu, menu);
            }
        });

        mp1NavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mp1NavigationLayout.closeDrawers();
                if (item.getItemId() == R.id.Home) {
                    FragmentTransaction fragmentTransaction = mp1FragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerToBeFilled, new mp1_SwipeTab()).commit();
                }
                if (item.getItemId() == R.id.OP) ;
                {
                    Intent i = new Intent();
                    i.setClass(getApplicationContext(), uMainPage.class);
                    startActivity(i);
                }
                if( item.getItemId() == R.id.action_logout){
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    mAuth.signOut();
                }
                if( item.getItemId() == R.id.event_list){
                    Intent i = new Intent(getApplicationContext(), EventView.class);
                    startActivity(i);
                }
                return false;
            }
        });

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_mp1);
        ActionBarDrawerToggle mp1_DrawerToggle = new ActionBarDrawerToggle(this, mp1NavigationLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mp1NavigationLayout.setDrawerListener(mp1_DrawerToggle);

        mp1_DrawerToggle.syncState();

        mp1_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(), SearchUniActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
