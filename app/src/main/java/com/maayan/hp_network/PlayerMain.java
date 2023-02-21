package com.maayan.hp_network;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PlayerMain extends AppCompatActivity {

    private ImageView inventoryButton;
    private ImageView highscoresButton;
    private ImageView duellingButton;
    private ImageView mapButton;
    private TextView player_greeting;
    private GameManager gameManager;
    private FirebaseUser user;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_main);
        //Set direction on all devices from LEFT to RIGHT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        MyUtiles.hideSystemUI(this);

        inventoryButton = findViewById(R.id.inventory);
        highscoresButton = findViewById(R.id.highscores);
        duellingButton = findViewById(R.id.duelling);
        player_greeting = findViewById(R.id.player_greeting);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        readUsersFromDB();
        gameManager = GameManager.set();

        user.reload();


        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayerMain.this, Inventory.class);
                startActivity(intent);

            }
        });

        highscoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayerMain.this, Highscores.class);
                startActivity(intent);

            }
        });

        duellingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayerMain.this, DuelWaitingRoom.class);
                startActivity(intent);

            }
        });

    }

    private void readUsersFromDB() {
        FirebaseDatabase.getInstance().getReference("GameManager").child("users").child(user.getPhoneNumber()).child("nickname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                name = snapshot.getValue(String.class);

                player_greeting.setText("Hello, " + name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
