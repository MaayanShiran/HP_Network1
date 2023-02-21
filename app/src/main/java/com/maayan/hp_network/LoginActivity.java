package com.maayan.hp_network;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GameManager gameManager;
    private EditText nickname;
    private Button submitButton;
    private FirebaseUser currentUser;
    private String name;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Set direction on all devices from LEFT to RIGHT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        DatabaseReference duelsRef = FirebaseDatabase.getInstance().getReference("GameManager").child("users");
        MapsInitializer.initialize(this);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }


       /*
        HashMap <String, User> users = new HashMap<>();
        users.put("111", new User());

        gameManager.setUsers(users);
          duelsRef.setValue(gameManager.getUsers());

        */
        gameManager = GameManager.set();

        readUsersFromDB();
        nickname = (EditText) findViewById(R.id.nick);
        submitButton = (Button) findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();


        currentUser = mAuth.getCurrentUser();

        onAuth();


        if (currentUser == null) {
            login();
        } else {

        }


    }

    private void onAuth() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                if (currentUser != null) {

                    currentUser.updateProfile(
                            new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setDisplayName("NE11")
                                    .build());
                    currentUser.reload();


                }


            }
        };
    }

    private void updateUser() {


        User us = new User(currentUser.getPhoneNumber());


        us.setRandomHouse();//set random house
        us.setNickname("MA");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        us.setLatitude(location.getLatitude());
        us.setLongitude(location.getLongitude());


        gameManager.getUsers().put(us.getNumber(), us);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                name = String.valueOf(nickname.getText());
                us.setNickname(name);

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setDisplayName("HEYY")
                        .build();

                onAuth();

                currentUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {


                    @Override

                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
                currentUser.reload();

                gameManager.getUsers().put(us.getNumber(), us);
                FirebaseDatabase.getInstance().getReference("GameManager").child("users").child(us.getNumber()).setValue(us);

                readUsersFromDB();

                Intent intent = new Intent(LoginActivity.this, PlayerMain.class);
                startActivity(intent);
                finish();
            }
        });


        FirebaseDatabase.getInstance().getReference("GameManager").child("users").setValue(gameManager.getUsers());


    }

    private void readUsersFromDB() {
        FirebaseDatabase.getInstance().getReference("GameManager").child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String, User> users = (HashMap<String, User>) snapshot.getValue();
                if (currentUser != null) {

                    currentUser.updateProfile(
                            new UserProfileChangeRequest.Builder()

                                    .setDisplayName(name)
                                    .build());
                    mAuth.getCurrentUser().reload();

                }

                gameManager.setUsers(users);
                gameManager.showUsersOnMap();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void login() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );


        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build();


        signInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {


                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .build();
                    onSignInResult(result);

                }
            }
    );

    @SuppressLint("MissingPermission")
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {


        currentUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("newDisplayName")
                .build();

        currentUser.updateProfile(profileUpdates);


        if (currentUser != null) {

            updateUser();
        }


        DatabaseReference duelsRef = FirebaseDatabase.getInstance().getReference("GameManager").child("users");

        duelsRef.setValue(gameManager.getUsers());

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}