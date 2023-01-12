package com.roman.gurdan.sudo.game.factory;

import com.roman.gurdan.sudo.game.Difficulty;
import com.roman.gurdan.sudo.game.GameSize;

public class Game8Creator extends IGameCreator {


    public Game8Creator(Difficulty difficulty) {
        super(difficulty);
    }

    @Override
    public GameSize getGameSize() {
        return GameSize.SIZE_EIGHT;
    }

    @Override
    protected int calGroup(int row, int col) {
        if (row >= 0 && row < 2 && col >= 0 && col < 4) {
            return 0;
        } else if (row >= 0 && row < 2 && col >= 4 && col < 8) {
            return 1;
        } else if (row >= 2 && row < 4 && col >= 0 && col < 4) {
            return 2;
        } else if (row >= 2 && row < 4 && col >= 4 && col < 8) {
            return 3;
        } else if (row >= 4 && row < 6 && col >= 0 && col < 4) {
            return 4;
        } else if (row >= 4 && row < 6 && col >= 4 && col < 8) {
            return 5;
        } else if (row >= 6 && row < 8 && col >= 0 && col < 4) {
            return 6;
        } else if (row >= 6 && row < 8 && col >= 4 && col < 8) {
            return 7;
        }
        return 0;
    }


}
