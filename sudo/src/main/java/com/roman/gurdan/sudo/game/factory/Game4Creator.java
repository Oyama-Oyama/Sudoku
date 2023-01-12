package com.roman.gurdan.sudo.game.factory;

import com.roman.gurdan.sudo.game.Difficulty;
import com.roman.gurdan.sudo.game.GameSize;

public class Game4Creator extends IGameCreator {

    public Game4Creator(Difficulty difficulty) {
        super(difficulty);
    }

    @Override
    public GameSize getGameSize() {
        return GameSize.SIZE_FOUR;
    }

    @Override
    protected int calGroup(int row, int col) {
        if (row >= 0 && row < 2 && col >= 0 && col < 2) {
            return 0;
        } else if (row >= 0 && row < 2 && col >= 2 && col < 4) {
            return 1;
        } else if (row >= 2 && row < 4 && col >= 0 && col < 2) {
            return 2;
        } else if (row >= 2 && row < 4 && col >= 2 && col < 4) {
            return 3;
        }
        return 0;
    }


}
