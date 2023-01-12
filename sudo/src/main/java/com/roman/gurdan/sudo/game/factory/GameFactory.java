package com.roman.gurdan.sudo.game.factory;

import com.roman.gurdan.sudo.game.Difficulty;
import com.roman.gurdan.sudo.game.GameSize;

public class GameFactory {

    public static IGameCreator createGameData(GameSize size, Difficulty difficulty) throws IllegalArgumentException {
        if (size == GameSize.SIZE_FOUR) {
            return new Game4Creator(difficulty);
        } else if (size == GameSize.SIZE_SIX) {
            return new Game6Creator(difficulty);
        } else if (size == GameSize.SIZE_EIGHT) {
            return new Game8Creator(difficulty);
        } else if (size == GameSize.SIZE_NINE) {
            return new Game9Creator(difficulty);
        } else
            throw new IllegalArgumentException("Invalid game size");
    }


}
