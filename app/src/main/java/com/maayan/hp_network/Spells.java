package com.maayan.hp_network;

public class Spells {

    static Spell[] defensiveSpells;
    static Spell[] offensiveSpells;


    public static Spell[] getdefensiveSpells() {
        defensiveSpells = new Spell[]{
                new Spell()
                        .setName("Expelliarmus")
                        .setType(1)
                        .setStrength(5)
                        .setLevel(2)
                        .setImgRes(R.drawable.expelliarmus_card),
                new Spell()
                        .setName("Expecto Patronum")
                        .setType(1)
                        .setStrength(2)
                        .setLevel(5)
                        .setImgRes(R.drawable.expecto_patronum_card),
                new Spell()
                        .setName("Impedimenta")
                        .setType(1)
                        .setStrength(5)
                        .setLevel(4)
                        .setImgRes(R.drawable.impedimenta_card),
                new Spell()
                        .setName("Langlock")
                        .setType(1)
                        .setStrength(4)
                        .setLevel(6)
                        .setImgRes(R.drawable.langlock_card),
                new Spell()
                        .setName("Protego")
                        .setType(1)
                        .setStrength(1)
                        .setLevel(5)
                        .setImgRes(R.drawable.protego_card),
                new Spell()
                        .setName("Revulsion Jinx")
                        .setType(1)
                        .setStrength(2)
                        .setLevel(4)
                        .setImgRes(R.drawable.revulsion_jinx_card)
        };

        return defensiveSpells;
    }


    public static Spell[] getOffensiveSpells() {

        offensiveSpells = new Spell[]{
                new Spell().setName("Stupefy")
                        .setType(0)
                        .setStrength(4)
                        .setLevel(5)
                        .setImgRes(R.drawable.stupefy_card1),
                new Spell()
                        .setName("Reducto")
                        .setType(0)
                        .setStrength(1)
                        .setLevel(4)
                        .setImgRes(R.drawable.reducto_card),
                new Spell()
                        .setName("Relashio")
                        .setType(0)
                        .setStrength(4)
                        .setLevel(4)
                        .setImgRes(R.drawable.relashio_card1),
                new Spell()
                        .setName("Rictusempra")
                        .setType(0)
                        .setStrength(1)
                        .setLevel(2)
                        .setImgRes(R.drawable.rictusempra_card),
                new Spell()
                        .setName("Confringo")
                        .setType(0)
                        .setStrength(4)
                        .setLevel(3)
                        .setImgRes(R.drawable.confringo_card),
                new Spell()
                        .setName("Bombarda")
                        .setType(0)
                        .setStrength(1)
                        .setLevel(5)
                        .setImgRes(R.drawable.bombarda_card)
        };


        return offensiveSpells;
    }

}
