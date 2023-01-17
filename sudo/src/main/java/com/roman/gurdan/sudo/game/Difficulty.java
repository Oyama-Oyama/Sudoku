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

    private static double[] randomArea = {
            0.08,
            0.18,
            1.0
    };

    public static Difficulty randDifficulty() {
        int _diff = 0;
        float rand = new Random().nextFloat();
        for (int i = 0; i < randomArea.length; i++) {
            if (rand < randomArea[i]) {
                _diff = i + 1;
                break;
            }
        }
        return getDifficulty(_diff);
    }

    public static Difficulty getDifficulty(int diff) {
        if (diff == EASY._difficulty) return EASY;
        else if (diff == MEDIUM._difficulty) return MEDIUM;
        else if (diff == HARD._difficulty) return HARD;
        return EASY;
    }

}
