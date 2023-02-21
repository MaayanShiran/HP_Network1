package com.maayan.hp_network;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Random;

public class Duel {

    private User user1;
    private User user2;
    private String username_blue;
    private String username_red;
    private String match_id;
    private String okToEnter;
    private int blue_score;
    private int red_score;
    private int blueChoseNum;
    private int redChoseNum;
    private HashMap<String, Spell> defensiveSpellsU1 = new HashMap<>();
    private HashMap<String, Spell> offensiveSpellsU1 = new HashMap<>();
    private HashMap<String, Spell> defensiveSpellsU2 = new HashMap<>();
    private HashMap<String, Spell> offensiveSpellsU2 = new HashMap<>();


    private int color;
    private boolean showCards = false;
    private boolean red_chose_card = false;
    private boolean blue_chose_card = false;
    private Spell spell1;
    private Spell spell2;

    public Duel() {

    }

    public Duel(String match_id) {
        this.match_id = match_id;
        this.blue_score = 50;
        this.red_score = 50;
        this.spell1 = new Spell();
        this.spell2 = new Spell();
        this.user1 = new User();//?
        this.user2 = new User();//?
        Random rnd = new Random();
        this.color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        this.okToEnter = "false";
        this.blueChoseNum = 0;
        this.redChoseNum = 0;

        //4 random spells
        for (int i = 0; i < 4; i++) {
            this.defensiveSpellsU1.put(String.valueOf(i), Spells.getdefensiveSpells()[rnd.nextInt(6)]);
            this.offensiveSpellsU1.put(String.valueOf(i), Spells.getdefensiveSpells()[rnd.nextInt(6)]);
            this.defensiveSpellsU2.put(String.valueOf(i), Spells.getdefensiveSpells()[rnd.nextInt(6)]);
            this.offensiveSpellsU2.put(String.valueOf(i), Spells.getdefensiveSpells()[rnd.nextInt(6)]);
        }


    }

    public User getUser1() {
        return user1;
    }

    public Duel setUser1(User user1) {
        this.user1 = user1;
        return this;
    }

    public HashMap<String, Spell> getDefensiveSpellsU1() {
        return defensiveSpellsU1;
    }

    public void setDefensiveSpellsU1(HashMap<String, Spell> defensiveSpellsU1) {
        this.defensiveSpellsU1 = defensiveSpellsU1;
    }

    public HashMap<String, Spell> getOffensiveSpellsU1() {
        return offensiveSpellsU1;
    }

    public void setOffensiveSpellsU1(HashMap<String, Spell> offensiveSpellsU1) {
        this.offensiveSpellsU1 = offensiveSpellsU1;
    }

    public HashMap<String, Spell> getDefensiveSpellsU2() {
        return defensiveSpellsU2;
    }

    public void setDefensiveSpellsU2(HashMap<String, Spell> defensiveSpellsU2) {
        this.defensiveSpellsU2 = defensiveSpellsU2;
    }

    public HashMap<String, Spell> getOffensiveSpellsU2() {
        return offensiveSpellsU2;
    }

    public void setOffensiveSpellsU2(HashMap<String, Spell> offensiveSpellsU2) {
        this.offensiveSpellsU2 = offensiveSpellsU2;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public int getColor() {
        return color;
    }

    public String getOkToEnter() {
        return okToEnter;
    }

    public void setOkToEnter(String okToEnter) {
        this.okToEnter = okToEnter;
    }


    public String getUsername_blue() {
        return username_blue;
    }

    public Duel setUsername_blue(String username_blue) {
        this.username_blue = username_blue;
        return this;
    }

    public String getUsername_red() {
        return username_red;
    }

    public Duel setUsername_red(String username_red) {
        this.username_red = username_red;
        return this;
    }

    public String getMatch_id() {
        return match_id;
    }

    public Duel setMatch_id(String match_id) {
        this.match_id = match_id;
        return this;
    }

    public int getBlue_score() {
        return blue_score;
    }

    public Duel setBlue_score(int blue_score) {
        this.blue_score = blue_score;
        return this;
    }

    public int getRed_score() {
        return red_score;
    }

    public Duel setRed_score(int red_score) {
        this.red_score = red_score;
        return this;
    }

    public boolean isShowCards() {
        return showCards;
    }

    public Duel setShowCards(boolean showCards) {
        this.showCards = showCards;
        return this;
    }

    public boolean isRed_chose_card() {
        return red_chose_card;
    }

    public Duel setRed_chose_card(boolean red_chose_card) {
        this.red_chose_card = red_chose_card;
        return this;
    }

    public boolean isBlue_chose_card() {
        return blue_chose_card;
    }

    public Duel setBlue_chose_card(boolean blue_chose_card) {
        this.blue_chose_card = blue_chose_card;
        return this;
    }

    public Spell getSpell1() {
        return spell1;
    }

    public Duel setSpell1(Spell spell1) {
        this.spell1 = spell1;
        return this;
    }

    public Spell getSpell2() {
        return spell2;
    }

    public Duel setSpell2(Spell spell2) {
        this.spell2 = spell2;
        return this;
    }
}
