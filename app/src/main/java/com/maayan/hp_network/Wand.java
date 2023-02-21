package com.maayan.hp_network;

public class Wand {

    private boolean have = false;
    private int imageRes = 0;

    public Wand() {

    }

    public boolean isHave() {
        return have;
    }

    public Wand setHave(boolean have) {
        this.have = have;
        return this;
    }

    public int getImageRes() {
        return imageRes;
    }

    public Wand setImageRes(int imageRes) {
        this.imageRes = imageRes;
        return this;
    }
}
