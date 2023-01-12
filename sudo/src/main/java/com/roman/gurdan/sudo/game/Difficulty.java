package com.roman.gurdan.sudo.game;

import java.util.Random;

public enum Difficulty {

    RANDOM(0),
    EASY(1),
    MEDIUM(2),
    HARD(3);


    public int _difficulty;

    Difficulty(int i) {
        this._difficulty = i;
    }

    public static Difficulty randDifficulty() {
        int _diff = new Random().nextInt(Difficulty.HARD._difficulty) + 1;
        return Difficulty.EASY;
    }



}
