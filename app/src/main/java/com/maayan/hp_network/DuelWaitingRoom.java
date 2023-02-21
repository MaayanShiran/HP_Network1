package com.maayan.hp_network;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class DuelWaitingRoom extends AppCompatActivity implements CallBack_OnItemClickListener {

    private TextView join;
    private String playerCreatedRoomID;
    private boolean createdNewRoom = false;
    private TextView new_duel_BTN;
    private TextView textLable;
    private RecyclerView list_duels;
    private ImageView block;
    private Button BTN_back;
    private int index;
    private pl.droidsonroids.gif.GifImageView gif;
    DuelListAdapter duelListAdapter;
    CallBack_OnItemClickListener listener;
    GameManager gameManager;
    private FirebaseUser currentUser;
    private String match_id;
    private boolean updateu2;
    private CallBack_DuelsProtocol callBack_duelsProtocol;

    public void setCallBack_duelsProtocol(CallBack_DuelsProtocol callBack_duelsProtocol) {
        this.callBack_duelsProtocol = callBack_duelsProtocol;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duel_waiting_room);
        //Set direction on all devices from LEFT to RIGHT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        gameManager = GameManager.set();

        findViews();
        initViews();

        readFromDB();


        new_duel_BTN.setOnClickListener(new View.OnClickListener() {


            String playerCreatedRoomID = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();


            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("GameManager").child("users").child(playerCreatedRoomID).child("duelID").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(String.class) != null) {
                            createdNewRoom = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if (createdNewRoom == false) {//every user can create only one room

                    //start waiting for opponent
                    createdNewRoom = true;

                    //gif.setFreezesAnimation(false);
                    block.setVisibility(View.INVISIBLE);
                    textLable.setText("Waiting for opponent...");


                    FirebaseDatabase.getInstance().getReference("GameManager").child("Pressed").setValue(playerCreatedRoomID);

                    //add a new duel:
                    match_id = String.valueOf(System.currentTimeMillis());
                    startListenToNewDuel(match_id);
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser us = mAuth.getCurrentUser();
                    User user = new User(playerCreatedRoomID);
                    user.setDuelID(match_id);
                    //read his house:

                    FirebaseDatabase.getInstance().getReference("GameManager").child("users").child(playerCreatedRoomID).child("house").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("user1").child("house").setValue(snapshot.getValue(String.class));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    gameManager.getDuels().put(match_id, new Duel(match_id).setUser1(user));
                    FirebaseDatabase.getInstance().getReference("GameManager").child("users").child(playerCreatedRoomID).child("duelID").setValue(match_id);

                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").setValue(gameManager.getDuels());


                }
            }


        });


    }

    private void subscribeForDeletions() {//put it next to where intent starts
        //read match_id
        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                match_id = snapshot.child("duelID").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (match_id != null) {
            FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("match_id").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue(String.class).equals("-1")) {
                        //delete duel:
                        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void startListenToNewDuel(String match_id1) {

        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String okToEnterValue = snapshot.child("okToEnter").getValue(String.class);


                if (okToEnterValue != null && okToEnterValue.equals("true")) {
                    //update user was here and it only got here if u were the one who clicked
                    //user who created the room enters:
                    createdNewRoom = false;
                    textLable.setText("Start exploring duelling");
                    block.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(DuelWaitingRoom.this, DuelActivity.class);
                    startActivity(intent);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void updateUser2(String match_id1) {


        FirebaseAuth.getInstance().getCurrentUser().reload();
        String currNum = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        FirebaseDatabase.getInstance().getReference("GameManager").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (currNum != snapshot.child("duels").child(match_id1).child("user1").child("number").getValue(String.class)) {

                    Duel newDuel = new Duel(match_id1);
                    newDuel.setUser1(snapshot.child("duels").child(match_id1).child("user1").getValue(User.class));
                    User user2 = new User(currNum);
                    user2.setDuelID(match_id1);


                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id1).child("user2").setValue(user2);
                    newDuel.setUser2(user2);
                    gameManager.getDuels().put(match_id1, newDuel);
                    FirebaseDatabase.getInstance().getReference("GameManager").child("users").child(currNum).child("duelID").setValue(match_id1);
                    String house = snapshot.child("users").child(currNum).child("house").getValue(String.class);
                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id1).child("user2").child("house").setValue(house);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").setValue(gameManager.getDuels());


        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").setValue(gameManager.getDuels());
    }

    private void readFromDB() {

        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String, Duel> duels = (HashMap<String, Duel>) snapshot.getValue();

                for (HashMap.Entry duel : duels.entrySet()) {
                    duelListAdapter.addItem((String) duel.getKey(), new Duel((String) duel.getKey()));
                }


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                }

                duelListAdapter.notifyDataSetChanged();

                gameManager.setDuels(duels);
                duelListAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initViews() {
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                block.setVisibility(View.VISIBLE);
                finish();
            }
        });

        updateu2 = false;

        duelListAdapter = new DuelListAdapter(this, gameManager.getDuels(), this::onItemClick);
        list_duels.setLayoutManager(new LinearLayoutManager(this));
        list_duels.setAdapter(duelListAdapter);
        list_duels.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (callBack_duelsProtocol != null) {
                    callBack_duelsProtocol.duelsDetails(gameManager.getDuels().get(index));
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (callBack_duelsProtocol != null) {

                    callBack_duelsProtocol.duelsDetails(gameManager.getDuels().get(rv.findChildViewUnder(e.getX(), e.getY())));
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    private void findViews() {
        BTN_back = findViewById(R.id.BTN_back);
        block = findViewById(R.id.BLOCK);
        gif = findViewById(R.id.loading);

        gif.clearAnimation();
        list_duels = findViewById(R.id.list_duels);
        join = findViewById(R.id.join_LBL);
        new_duel_BTN = findViewById(R.id.new_duel_BTN);
        textLable = findViewById(R.id.text_LBL);
    }

    @Override
    public void onItemClick(int position, String duelID) {
        //if a user which not created the room is pressing - both will go to the next intent

        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(duelID).child("user1").child("number").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.getValue(String.class).equals((String) FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) && FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null) {
                    index = position;//add a new room with this duel id
                    //here the opponent will announce that user can enter:
                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(duelID).child("okToEnter").setValue("true");
                    //opponent enters the room
                    //new:
                    updateUser2(duelID);

                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").setValue(gameManager.getDuels()).addOnCompleteListener(new OnCompleteListener<Void>() {


                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

                    readFromDB();

                    Intent intent = new Intent(DuelWaitingRoom.this, DuelActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}