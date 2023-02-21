package com.maayan.hp_network;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DuelListAdapter extends RecyclerView.Adapter<DuelListAdapter.ListViewHolder> {


    private Context context;
    private AdapterView.OnItemClickListener listener;
    private HashMap<String, Duel> duels_list;
    private View view;
    static ListViewHolder myViewHolder;
    public static int clickedPosition = -1;
    private CallBack_OnItemClickListener onItemClickListener;

    public DuelListAdapter(Context context, HashMap<String, Duel> duels_list, CallBack_OnItemClickListener listener) {
        this.context = context;
        this.duels_list = duels_list;
        onItemClickListener = listener;
    }


    @NonNull
    @Override
    public DuelListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recycle_view, parent, false);

        myViewHolder = new ListViewHolder(view, onItemClickListener);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DuelListAdapter.ListViewHolder holder, int position) {
        Duel duels = duels_list.get(position);

        for (Duel duel : duels_list.values()) {

            holder.name.setText("Room #" + position);


        }


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser us = mAuth.getCurrentUser();
        FirebaseDatabase.getInstance().getReference("GameManager").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String duelID = (String) snapshot.child("users").child(us.getPhoneNumber()).child("duelID").getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return duels_list == null ? 0 : duels_list.size();
    }

    public void addItem(String key, Duel duel) {
        duels_list.put(key, duel);
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        private TextView name;
        private RelativeLayout back;
        public int clickedPosition = -1;
        CallBack_OnItemClickListener onItemClickListener;


        public ListViewHolder(View itemView, CallBack_OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnTouchListener(this);
            name = itemView.findViewById(R.id.duel_IMG_name);
            back = itemView.findViewById(R.id.back_IMG_background);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            clickedPosition = getAdapterPosition();
            GameManager gameManager = GameManager.set();
            List<String> keys = new ArrayList<>(duels_list.keySet());
            Collections.sort(keys);

            onItemClickListener.onItemClick(getAdapterPosition(), keys.get(clickedPosition));

            return false;
        }

    }

    public interface CallBack_OnItemClickListener {

        void onItemClick(int adapterPosition, String duelID);
    }
}


