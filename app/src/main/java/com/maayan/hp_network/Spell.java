package com.maayan.hp_network;

public class Spell {

    public enum TYPE {
        DEFENCE,
        ATTACK,
    }

    private TYPE type;
    private int strength;
    private int level;
    private String name;
    private int imgRes;

    public Spell() {

    }

    public int getImgRes() {
        return imgRes;
    }

    public Spell setImgRes(int imgRes) {
        this.imgRes = imgRes;
        return this;
    }

    public String getName() {
        return name;
    }

    public Spell setName(String name) {
        this.name = name;
        return this;
    }

    public TYPE getType() {
        return type;
    }

    public Spell setType(int type) {
        if (type == 0) {
            this.type = TYPE.ATTACK;
        } else {
            this.type = TYPE.DEFENCE;
        }
        return this;
    }

    public int getStrength() {
        return strength;
    }

    public Spell setStrength(int strength) {
        this.strength = strength;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public Spell setLevel(int level) {
        this.level = level;
        return this;
    }
}
