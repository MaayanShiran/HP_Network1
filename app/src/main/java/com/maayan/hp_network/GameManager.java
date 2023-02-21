package com.maayan.hp_network;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class GameManager {

    private HashMap<String, User> users = new HashMap<String, User>();
    private HashMap<String, Duel> duels = new HashMap<>();
    DuelListAdapter duelListAdapter;

    public DuelListAdapter getDuelListAdapter() {
        return duelListAdapter;
    }

    public DuelListAdapter setDuelListAdapter(DuelListAdapter duelListAdapter) {
        if (this.duelListAdapter != null) {
            this.duelListAdapter = duelListAdapter;
        }
        return this.duelListAdapter;
    }


    private static GameManager manager;

    public GameManager() {

    }

    public static GameManager set() {
        if (manager == null) {
            manager = new GameManager();


        }
        return manager;
    }

    public void addScoreToHouse(String house, int scoreToAdd) {
        final int[] currentScore = new int[1];
        //read from DB:
        FirebaseDatabase.getInstance().getReference("GameManager").child(house).child("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseDatabase.getInstance().getReference("GameManager").child(house).child("score").setValue(snapshot.getValue(Integer.class) + scoreToAdd);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void setUsers(HashMap<String, User> users) {
        this.users = users;

    }

    public void setDuels(HashMap<String, Duel> duels) {
        this.duels = duels;
    }

    public HashMap<String, Duel> getDuels() {
        return duels;
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void showUsersOnMap() {

    }

}
