package com.roman.gurdan.sudo.game.factory;

import com.roman.gurdan.sudo.game.Difficulty;
import com.roman.gurdan.sudo.game.GameSize;

public class Game6Creator extends IGameCreator {

    public Game6Creator(Difficulty difficulty) {
        super(difficulty);
    }

    @Override
    protected int calGroup(int row, int col) {
        if (row >= 0 && row < 2 && col >= 0 && col < 3) {
            return 0;
        } else if (row >= 0 && row < 2 && col >= 3 && col < 6) {
            return 1;
        } else if (row >= 2 && row < 4 && col >= 0 && col < 3) {
            return 2;
        } else if (row >= 2 && row < 4 && col >= 3 && col < 6) {
            return 3;
        } else if (row >= 4 && row < 6 && col >= 0 && col < 3) {
            return 4;
        } else if (row >= 4 && row < 6 && col >= 3 && col < 6) {
            return 5;
        }
        return 0;
    }

    @Override
    public GameSize getGameSize() {
        return GameSize.SIZE_SIX;
    }

}
