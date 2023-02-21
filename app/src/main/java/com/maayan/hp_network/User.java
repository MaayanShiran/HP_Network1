package com.maayan.hp_network;

public class User {

    private double longitude = 0.0;
    private double latitude = 0.0;
    private String number = "";
    private String nickname = "";
    private Wand wand = new Wand();
    private Spells defensiveSpells[];
    private Spells offensiveSpells[];
    private int score = 0;
    private String house = "";
    private String[] houses = {"Slytherin", "Hufflepuff", "Ravenclaw", "Gryffindor"};
    private String duelID = "";

    public User() {

    }

    public User(String userID) {
        this.number = userID;
        //set 4 random offensive:
        for (int i = 0; i < 4; i++) {

        }
    }

    public double getLongitude() {
        return longitude;
    }

    public User setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public User setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getDuelID() {
        return duelID;
    }

    public void setDuelID(String duelID) {
        this.duelID = duelID;

    }

    public String getNumber() {
        return number;
    }

    public User setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public Wand getWand() {
        return wand;
    }

    public User setWand(Wand wand) {
        this.wand = wand;
        return this;
    }

    public int getScore() {
        return score;
    }

    public User setScore(int score) {
        this.score = score;
        return this;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;

    }

    public void setRandomHouse() {
        int rand = (int) (Math.random() * (4 - 0));
        this.house = houses[rand];
    }
}

