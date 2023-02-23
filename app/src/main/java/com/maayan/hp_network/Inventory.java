package com.maayan.hp_network;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Inventory extends AppCompatActivity {

    private RelativeLayout background;
    private ImageView house_q;
    private TextView name;
    private TextView score;
    private String house;
    private String username;
    private int userScore;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_room);
        //Set direction on all devices from LEFT to RIGHT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);


        findViews();
        inizialization();

        String userNum = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        FirebaseDatabase.getInstance().getReference("GameManager").child("users").child(userNum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                house = snapshot.child("house").getValue(String.class);
                username = snapshot.child("nickname").getValue(String.class);
                userScore = snapshot.child("score").getValue(Integer.class);
                name.setText("Username: " + username);
                score.setText("Score: " + userScore);
                if (house.equals("Slytherin")) {
                    background.setBackgroundResource(R.drawable.slytherin_common2);
                    house_q.setBackgroundResource(R.drawable.sly_q);
                }
                if (house.equals("Hufflepuff")) {
                    background.setBackgroundResource(R.drawable.hufflepuff_common1);
                    house_q.setBackgroundResource(R.drawable.huf_q);
                }
                if (house.equals("Ravenclaw")) {
                    background.setBackgroundResource(R.drawable.ravenclaw_common1);
                    house_q.setBackgroundResource(R.drawable.rav_q);
                }
                if (house.equals("Gryffindor")) {
                    background.setBackgroundResource(R.drawable.gryffindor_common);
                    house_q.setBackgroundResource(R.drawable.gry_q);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void inizialization() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void findViews() {
        backButton = findViewById(R.id.BTN_back);
        background = findViewById(R.id.lay_Background);
        house_q = findViewById(R.id.house_q);
        name = findViewById(R.id.player_LBL_name);
        score = findViewById(R.id.player_LBL_score);
    }
}
