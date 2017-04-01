package com.example.admin.college20;


import android.app.ProgressDialog;

import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
   private DatabaseReference mDatabaseUsers;

    SignInButton googleSignIn;
    private static final int RC_SIGN_IN =1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private static final String TAG = "Main Activity";
    private FirebaseAuth.AuthStateListener authStateListener;

    private LoginButton loginButton;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                progressDialog.setMessage("Logging In");
                progressDialog.show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
       mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        googleSignIn = (SignInButton) findViewById(R.id.googleSignInBttn);
        TextView textView = (TextView) googleSignIn.getChildAt(0);
        final String v = "Sign In with Google";
        googleSignIn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        googleSignIn.setColorScheme(SignInButton.COLOR_LIGHT);

        textView.setText(v);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        googleSignIn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() !=null){
                    progressDialog.dismiss();
                    Intent i = new Intent(MainActivity.this, MainPage1.class);
                    startActivity(i);
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
                        Toast.makeText(MainActivity.this, "You got an Error", Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    private void signIn() {
        progressDialog.setMessage("Signing In..");
        progressDialog.show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            final String personName =  account.getDisplayName();
                            final String personEmail = account.getEmail();
                            final String personGivenName = account.getGivenName();
                            final String personId = account.getId();
                            Uri personPhotoUrl = account.getPhotoUrl();
                            String personPhoto = personPhotoUrl.toString();

                            //SharedPreferences user_details = getSharedPreferences("user_details", MODE_PRIVATE);
                            //SharedPreferences.Editor editor = user_details.edit();
                            ///editor.putString("name", personName);
                            //editor.putString("email", personEmail);
                            //    editor.putString("givenName", personGivenName);
                            //editor.putString("id", personId);
                            // editor.putString("imageUrl", personPhoto);
                            //editor.commit();

                            final String uid1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference current_user_db =  mDatabaseUsers.child(uid1);
                            current_user_db.child("name").setValue(personName);
                            current_user_db.child("imageUrl").setValue(personPhoto);
                            current_user_db.child("email").setValue(personEmail);
                            current_user_db.child("user_id").setValue(personId);
                        }
                    }
                });
    }
    private void handleFacebookAccessToken(final AccessToken accessToken) {
        Log.d(TAG, "handleFacebookAccessToken:" + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    for (UserInfo profile : user.getProviderData()) {
                        // Id of the provider (ex: google.com)
                        final String providerId = profile.getProviderId();

                        // UID specific to the provider
                        final String uid = profile.getUid();

                        // Name, email address, and profile photo Url
                        final String name = profile.getDisplayName();
                        final Uri photoUrl = profile.getPhotoUrl();
                        final String url = photoUrl.toString();
                        final String email = profile.getEmail();
                        final String uid1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference current_user_db =  mDatabaseUsers.child(uid1);
                        current_user_db.child("name").setValue(name);

                        current_user_db.child("imageUrl").setValue(url);
                        current_user_db.child("user_id").setValue(uid);
                        if (email != null) {
                            current_user_db.child("email").setValue(email);
                        }
                        System.out.println(email);
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }
}
