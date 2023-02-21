package com.maayan.hp_network;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

public class DuelActivity extends AppCompatActivity {

    private TextView red_player;
    private TextView blue_player;
    private int redChoseNum = 0;
    private int blueChoseNum = 0;
    private boolean first_generated = true;
    private boolean resetChose = true;
    private boolean generatedNewBlue = false;
    private boolean generatedNewRed = false;
    private ProgressBar progressBar;
    private FirebaseUser currentUser;
    private ShapeableImageView red_chosen_card;
    private ShapeableImageView blue_chosen_card;
    private ShapeableImageView[] red_player_cards;
    private ShapeableImageView[] blue_player_cards;
    private int blue4cards[][] = new int[4][4];
    private int red4cards[][] = new int[4][4];
    private User user1;
    private User user2;
    public static String match_id = "";
    private boolean playerSubmitted = false;
    private boolean opponentSubmitted = false;
    GameManager gameManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duel);
        MyUtiles.hideSystemUI(this);
        //Set direction on all devices from LEFT to RIGHT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        gameManager = GameManager.set();

        findViews();
        initialization();
        readFromDB();

    }

    private void startGame() {


        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("showCards").getValue(Boolean.class)) {

                    //if 2 players has made their chose - check which card is stronger:
                    //check if both from the same type:
                    if (snapshot.child("spell1").child("type").getValue(String.class).equals(snapshot.child("spell2").child("type").getValue(String.class))) {
                        //check both spell's level:
                        if (snapshot.child("spell1").child("level").getValue(Integer.class) == snapshot.child("spell2").child("level").getValue(Integer.class)) {
                            //both in the same level - check by strength:
                            if (snapshot.child("spell1").child("strength").getValue(Integer.class) == snapshot.child("spell1").child("strength").getValue(Integer.class)) {
                                //both also have the same strength - tie

                            } else if (snapshot.child("spell1").child("strength").getValue(Integer.class) > snapshot.child("spell1").child("strength").getValue(Integer.class)) {
                                //spell1 (of user1 - red) is stronger:
                                FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("red_score").setValue(snapshot.child("red_score").getValue(Integer.class) + snapshot.child("spell1").child("strength").getValue(Integer.class));
                                FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("blue_score").setValue(snapshot.child("blue_score").getValue(Integer.class) - snapshot.child("spell1").child("strength").getValue(Integer.class));
                                Spell empty = new Spell();
                                empty.setType(0);
                                FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("spell1").setValue(empty);
                                FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("spell2").setValue(empty);
                            } else {
                                //spell2 (of user2 - blue) is stronger:
                                FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("blue_score").setValue(snapshot.child("blue_score").getValue(Integer.class) + snapshot.child("spell2").child("strength").getValue(Integer.class));
                                FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("red_score").setValue(snapshot.child("red_score").getValue(Integer.class) - snapshot.child("spell2").child("strength").getValue(Integer.class));
                                Spell empty = new Spell();
                                empty.setType(0);
                                FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("spell1").setValue(empty);
                                FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("spell2").setValue(empty);
                            }
                        } else if (snapshot.child("spell1").child("level").getValue(Integer.class) > snapshot.child("spell2").child("level").getValue(Integer.class)) {
                            //spell1 (of user1 - red) is stronger:
                            FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("red_score").setValue(snapshot.child("red_score").getValue(Integer.class) + snapshot.child("spell1").child("level").getValue(Integer.class));
                            FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("blue_score").setValue(snapshot.child("blue_score").getValue(Integer.class) - snapshot.child("spell1").child("level").getValue(Integer.class));
                            Spell empty = new Spell();
                            empty.setType(0);
                            FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("spell1").setValue(empty);
                            FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("spell2").setValue(empty);
                        } else {
                            //spell2 (of user2 - blue) is stronger:
                            FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("blue_score").setValue(snapshot.child("blue_score").getValue(Integer.class) + snapshot.child("spell2").child("level").getValue(Integer.class));
                            FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("red_score").setValue(snapshot.child("red_score").getValue(Integer.class) - snapshot.child("spell2").child("level").getValue(Integer.class));
                            Spell empty = new Spell();
                            empty.setType(0);
                            FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("spell1").setValue(empty);
                            FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("spell2").setValue(empty);
                        }
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        progressBar.setProgress(snapshot.child("red_score").getValue(Integer.class), true);
                    }

                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("blueChoseNum").setValue(-1);
                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("redChoseNum").setValue(-1);

                    red_chosen_card.setImageResource(R.drawable.card_back);
                    blue_chosen_card.setImageResource(R.drawable.card_back);

                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("red_chose_card").setValue(false);
                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("blue_chose_card").setValue(false);
                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("showCards").setValue(false);

                    playerSubmitted = false;
                    opponentSubmitted = false;
                    Spell empty = new Spell();
                    empty.setType(0);
                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("spell1").setValue(empty);
                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("spell2").setValue(empty);


                }


                if (snapshot.child("red_score").getValue(Integer.class) <= 0 || snapshot.child("red_score").getValue(Integer.class) >= 100) {
                    //end of game
                    final int[] currentUserScore = new int[1];
                    String winnersNumber;
                    final String currentHouse;
                    final int[] currentHouseScore = new int[1];
                    if (snapshot.child("red_score").getValue(Integer.class) >= 100) {
                        //if red won the duel - add the user 10 points
                        winnersNumber = snapshot.child("user1").child("number").getValue(String.class);
                        currentHouse = snapshot.child("user1").child("house").getValue(String.class);

                    } else {
                        winnersNumber = snapshot.child("user2").child("number").getValue(String.class);
                        currentHouse = snapshot.child("user2").child("house").getValue(String.class);
                    }

                    gameManager.addScoreToHouse(currentHouse, 10);

                    FirebaseDatabase.getInstance().getReference("GameManager").child("users").child(winnersNumber).child("score").setValue(currentUserScore[0] + 10);
                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("match_id").setValue("-1");
                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).removeValue();
                    finish();

                }

                if (currentUser.getPhoneNumber().equals(snapshot.child("user1").child("number").getValue(String.class))) {
                    //first time 4 cards generation:
                    if (first_generated) {
                        fourCardsGeneration("user1", snapshot);
                        first_generated = false;
                    }
                    //if red player pressed on a card:
                    if (snapshot.child("redChoseNum").getValue(Integer.class) != -1 && playerSubmitted == false) {
                        //show red player his selected card:
                        Spell spell1 = new Spell();
                        spell1.setImgRes(red4cards[snapshot.child("redChoseNum").getValue(Integer.class)][0]);
                        spell1.setType(red4cards[snapshot.child("redChoseNum").getValue(Integer.class)][1]);
                        spell1.setLevel(red4cards[snapshot.child("redChoseNum").getValue(Integer.class)][2]);
                        spell1.setStrength(red4cards[snapshot.child("redChoseNum").getValue(Integer.class)][3]);

                        red_chosen_card.setImageResource(spell1.getImgRes());
                        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("red_chose_card").setValue(true);

                        playerSubmitted = true;

                        generateNewCard("user1", snapshot);


                        //write the selection to the firebase
                        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("spell1").setValue(spell1);
                        //write to DB that red player has made a choice:
                        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("red_chose_card").setValue(true);

                    }
                    //if both players has made the choice:
                    if (snapshot.child("red_chose_card").getValue(Boolean.class) == true && snapshot.child("blue_chose_card").getValue(Boolean.class) == true) {
                        //may expose the selected blue card:
                        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("showCards").setValue(true);
                        blue_chosen_card.setImageResource(snapshot.child("spell2").child("imgRes").getValue(Integer.class));
                    }


                }//end of user1's section

                if (currentUser.getPhoneNumber().equals(snapshot.child("user2").child("number").getValue(String.class))) {
                    //first time 4 cards generation:
                    if (first_generated) {
                        fourCardsGeneration("user2", snapshot);
                        first_generated = false;
                    }
                    //if blue player pressed on a card:
                    if (snapshot.child("blueChoseNum").getValue(Integer.class) != -1 && opponentSubmitted == false) {
                        //show blue player his selected card:
                        Spell spell2 = new Spell();
                        spell2.setImgRes(blue4cards[snapshot.child("blueChoseNum").getValue(Integer.class)][0]);
                        spell2.setType(blue4cards[snapshot.child("blueChoseNum").getValue(Integer.class)][1]);
                        spell2.setLevel(blue4cards[snapshot.child("blueChoseNum").getValue(Integer.class)][2]);
                        spell2.setStrength(blue4cards[snapshot.child("blueChoseNum").getValue(Integer.class)][3]);

                        blue_chosen_card.setImageResource(spell2.getImgRes());
                        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("blue_chose_card").setValue(true);

                        opponentSubmitted = true;
                        //if(generatedNewBlue == false){
                        generateNewCard("user2", snapshot);
                        //  generatedNewBlue = true;
                        // }
                        //write the selection to the firebase
                        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("spell2").setValue(spell2);
                        //write to DB that blue player has made a choice:

                        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("blue_chose_card").setValue(true);

                    }
                    //if both players has made the choice:
                    if (snapshot.child("red_chose_card").getValue(Boolean.class) == true && snapshot.child("blue_chose_card").getValue(Boolean.class) == true) {

                        //may expose the selected red card:
                        FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("showCards").setValue(true);
                        red_chosen_card.setImageResource(snapshot.child("spell1").child("imgRes").getValue(Integer.class));
                    }


                }//end of user2's section


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void generateNewCard(String user, DataSnapshot snapshot) {
        //generate new card:
        //generate defensive or offensive:
        Random rand = new Random();
        int type = rand.nextInt(1);

        if (type == 0) {
            int randomDef = rand.nextInt(4);
            if (user.equals("user1")) {

                red_player_cards[snapshot.child("redChoseNum").getValue(Integer.class)].setImageResource(snapshot.child("defensiveSpellsU1").child(String.valueOf(randomDef)).getValue(Spell.class).getImgRes());
                red4cards[snapshot.child("redChoseNum").getValue(Integer.class)][0] = snapshot.child("defensiveSpellsU1").child(String.valueOf(randomDef)).getValue(Spell.class).getImgRes();
                red4cards[snapshot.child("redChoseNum").getValue(Integer.class)][1] = 1;
                red4cards[snapshot.child("redChoseNum").getValue(Integer.class)][2] = snapshot.child("defensiveSpellsU1").child(String.valueOf(randomDef)).getValue(Spell.class).getLevel();
                red4cards[snapshot.child("redChoseNum").getValue(Integer.class)][3] = snapshot.child("defensiveSpellsU1").child(String.valueOf(randomDef)).getValue(Spell.class).getStrength();
            } else {

                blue_player_cards[snapshot.child("blueChoseNum").getValue(Integer.class)].setImageResource(snapshot.child("defensiveSpellsU2").child(String.valueOf(randomDef)).getValue(Spell.class).getImgRes());
                blue4cards[snapshot.child("blueChoseNum").getValue(Integer.class)][0] = snapshot.child("defensiveSpellsU2").child(String.valueOf(randomDef)).getValue(Spell.class).getImgRes();
                blue4cards[snapshot.child("blueChoseNum").getValue(Integer.class)][1] = 1;
                blue4cards[snapshot.child("blueChoseNum").getValue(Integer.class)][2] = snapshot.child("defensiveSpellsU2").child(String.valueOf(randomDef)).getValue(Spell.class).getLevel();
                blue4cards[snapshot.child("blueChoseNum").getValue(Integer.class)][3] = snapshot.child("defensiveSpellsU2").child(String.valueOf(randomDef)).getValue(Spell.class).getStrength();
            }

        } else {
            int randomOff = rand.nextInt(4);
            if (user.equals("user1")) {
                red_player_cards[snapshot.child("redChoseNum").getValue(Integer.class)].setImageResource(snapshot.child("offensiveSpellsU1").child(String.valueOf(randomOff)).getValue(Spell.class).getImgRes());
                red4cards[snapshot.child("redChoseNum").getValue(Integer.class)][0] = snapshot.child("offensiveSpellsU1").child(String.valueOf(randomOff)).getValue(Spell.class).getImgRes();
                red4cards[snapshot.child("redChoseNum").getValue(Integer.class)][1] = 0;
                red4cards[snapshot.child("redChoseNum").getValue(Integer.class)][2] = snapshot.child("offensiveSpellsU1").child(String.valueOf(randomOff)).getValue(Spell.class).getLevel();
                red4cards[snapshot.child("redChoseNum").getValue(Integer.class)][3] = snapshot.child("offensiveSpellsU1").child(String.valueOf(randomOff)).getValue(Spell.class).getStrength();
            } else {
                blue_player_cards[snapshot.child("blueChoseNum").getValue(Integer.class)].setImageResource(snapshot.child("offensiveSpellsU2").child(String.valueOf(randomOff)).getValue(Spell.class).getImgRes());
                blue4cards[snapshot.child("blueChoseNum").getValue(Integer.class)][0] = snapshot.child("offensiveSpellsU2").child(String.valueOf(randomOff)).getValue(Spell.class).getImgRes();
                blue4cards[snapshot.child("blueChoseNum").getValue(Integer.class)][1] = 0;
                blue4cards[snapshot.child("blueChoseNum").getValue(Integer.class)][2] = snapshot.child("offensiveSpellsU2").child(String.valueOf(randomOff)).getValue(Spell.class).getLevel();
                blue4cards[snapshot.child("blueChoseNum").getValue(Integer.class)][3] = snapshot.child("offensiveSpellsU2").child(String.valueOf(randomOff)).getValue(Spell.class).getStrength();
            }

        }
    }

    private void fourCardsGeneration(String user, DataSnapshot snapshot) {

        Random rand = new Random();
        int numOffensive = rand.nextInt(4);

        if (user.equals("user1")) {//red player
            for (int i = 0; i < numOffensive; i++) {

                red_player_cards[i].setImageResource(snapshot.child("offensiveSpellsU1").child(String.valueOf(i)).getValue(Spell.class).getImgRes());
                red4cards[i][0] = snapshot.child("offensiveSpellsU1").child(String.valueOf(i)).getValue(Spell.class).getImgRes();
                red4cards[i][1] = 0;//0 - offensive
                red4cards[i][2] = snapshot.child("offensiveSpellsU1").child(String.valueOf(i)).getValue(Spell.class).getLevel();
                red4cards[i][3] = snapshot.child("offensiveSpellsU1").child(String.valueOf(i)).getValue(Spell.class).getStrength();
            }
            for (int i = numOffensive; i < 4; i++) {

                red_player_cards[i].setImageResource(snapshot.child("defensiveSpellsU1").child(String.valueOf(i)).getValue(Spell.class).getImgRes());
                //card numbers:
                red4cards[i][0] = snapshot.child("defensiveSpellsU1").child(String.valueOf(i)).getValue(Spell.class).getImgRes();
                red4cards[i][1] = 1;
                red4cards[i][2] = snapshot.child("defensiveSpellsU1").child(String.valueOf(i)).getValue(Spell.class).getLevel();
                red4cards[i][3] = snapshot.child("defensiveSpellsU1").child(String.valueOf(i)).getValue(Spell.class).getStrength();

            }

        } else {
            for (int i = 0; i < numOffensive; i++) {

                blue_player_cards[i].setImageResource(snapshot.child("offensiveSpellsU2").child(String.valueOf(i)).getValue(Spell.class).getImgRes());
                blue4cards[i][0] = snapshot.child("offensiveSpellsU2").child(String.valueOf(i)).getValue(Spell.class).getImgRes();
                blue4cards[i][1] = 0;//0 - offensive
                blue4cards[i][2] = snapshot.child("offensiveSpellsU2").child(String.valueOf(i)).getValue(Spell.class).getLevel();
                blue4cards[i][3] = snapshot.child("offensiveSpellsU2").child(String.valueOf(i)).getValue(Spell.class).getStrength();
            }
            for (int i = numOffensive; i < 4 && first_generated; i++) {

                blue_player_cards[i].setImageResource(snapshot.child("defensiveSpellsU2").child(String.valueOf(i)).getValue(Spell.class).getImgRes());
                //card numbers:
                blue4cards[i][0] = snapshot.child("defensiveSpellsU2").child(String.valueOf(i)).getValue(Spell.class).getImgRes();
                blue4cards[i][1] = 1;
                blue4cards[i][2] = snapshot.child("defensiveSpellsU2").child(String.valueOf(i)).getValue(Spell.class).getLevel();
                blue4cards[i][3] = snapshot.child("defensiveSpellsU2").child(String.valueOf(i)).getValue(Spell.class).getStrength();

            }

        }
    }

    private void findViews() {

        red_player_cards = new ShapeableImageView[]{findViewById(R.id.Red_Card1), findViewById(R.id.Red_Card2), findViewById(R.id.Red_Card3), findViewById(R.id.Red_Card4)};
        blue_player_cards = new ShapeableImageView[]{findViewById(R.id.Blue_Card1), findViewById(R.id.Blue_Card2), findViewById(R.id.Blue_Card3), findViewById(R.id.Blue_Card4)};

        red_chosen_card = findViewById(R.id.Red_Card_chosen);
        blue_chosen_card = findViewById(R.id.Blue_Card_chosen);

        progressBar = findViewById(R.id.Blue_Red_Bar);

        red_player = findViewById(R.id.Red_player_chose);
        blue_player = findViewById(R.id.Blue_player_chose);
    }

    private void initialization() {


        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        for (int i = 0; i < 4; i++) {
            int finalI = i;
            red_player_cards[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redChoseNum = finalI;
                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("redChoseNum").setValue(redChoseNum);
                }
            });
            int finalI1 = i;
            blue_player_cards[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    blueChoseNum = finalI1;
                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("blueChoseNum").setValue(blueChoseNum);
                }
            });
        }


    }

    private void readFromDB() {
        FirebaseDatabase.getInstance().getReference("GameManager").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                match_id = snapshot.child("users").child(currentUser.getPhoneNumber()).child("duelID").getValue(String.class);
                if (resetChose == true) {
                    resetChose = false;
                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("blueChoseNum").setValue(-1);
                    FirebaseDatabase.getInstance().getReference("GameManager").child("duels").child(match_id).child("redChoseNum").setValue(-1);
                }

                user1 = snapshot.child("duels").child(match_id).child("user1").getValue(User.class);
                user2 = snapshot.child("duels").child(match_id).child("user2").getValue(User.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                startGame();
            }
        });

        Task<DataSnapshot> task1 = FirebaseDatabase.getInstance().getReference("GameManager").get();
        Tasks.whenAllComplete(task1).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                startGame();
            }
        });
    }
}
