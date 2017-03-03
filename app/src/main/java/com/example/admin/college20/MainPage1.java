package com.example.admin.college20;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainPage1 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    NavigationView mp1NavigationView;
    DrawerLayout mp1NavigationLayout;
    FragmentManager mp1FragmentManager;
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
                    Intent loginIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
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

                   logout();
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

    private void logout()
    {
        mAuth.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
