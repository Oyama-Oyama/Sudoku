package com.roman.gurdan.sudo.game.factory;

import com.roman.gurdan.sudo.game.Difficulty;
import com.roman.gurdan.sudo.game.GameSize;

public class Game9Creator extends IGameCreator {

    public Game9Creator(Difficulty difficulty) {
        super(difficulty);
    }

    @Override
    public GameSize getGameSize() {
        return GameSize.SIZE_NINE;
    }

    @Override
    protected int calGroup(int row, int col) {
        if (row >= 0 && row < 3 && col >= 0 && col < 3) {
            return 0;
        } else if (row >= 0 && row < 3 && col >= 3 && col < 6) {
            return 1;
        } else if (row >= 0 && row < 3 && col >= 6 && col < 9) {
            return 2;
        } else if (row >= 3 && row < 6 && col >= 0 && col < 3) {
            return 3;
        } else if (row >= 3 && row < 6 && col >= 3 && col < 6) {
            return 4;
        } else if (row >= 3 && row < 6 && col >= 6 && col < 9) {
            return 5;
        } else if (row >= 6 && row < 9 && col >= 0 && col < 3) {
            return 6;
        } else if (row >= 6 && row < 9 && col >= 3 && col < 6) {
            return 7;
        } else if (row >= 6 && row < 9 && col >= 6 && col < 9) {
            return 8;
        }
        return 0;
    }


}
