package com.maayan.hp_network;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Highscores extends AppCompatActivity {

    private TextView rev_score;
    private TextView huf_score;
    private TextView sly_score;
    private TextView gry_score;
    private Button BTN_back;
    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        //Set direction on all devices from LEFT to RIGHT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        gameManager = GameManager.set();
        findViews();
        initialization();


        gameManager.addScoreToHouse("Slytherin", 0);
        gameManager.addScoreToHouse("Slytherin", 0);
        gameManager.addScoreToHouse("Slytherin", 0);
        gameManager.addScoreToHouse("Slytherin", 0);

        //Listen to FB changes:
        FirebaseDatabase.getInstance().getReference("GameManager").child("Slytherin").child("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sly_score.setText("" + snapshot.getValue(Integer.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("GameManager").child("Hufflepuff").child("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                huf_score.setText("" + snapshot.getValue(Integer.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("GameManager").child("Ravenclaw").child("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rev_score.setText("" + snapshot.getValue(Integer.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference("GameManager").child("Gryffindor").child("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gry_score.setText("" + snapshot.getValue(Integer.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initialization() {

        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void findViews() {
        rev_score = findViewById(R.id.rev_LBL_score);
        huf_score = findViewById(R.id.huf_LBL_score);
        sly_score = findViewById(R.id.sly_LBL_score);
        gry_score = findViewById(R.id.gry_LBL_score);
        BTN_back = findViewById(R.id.BTN_back);
    }


}
