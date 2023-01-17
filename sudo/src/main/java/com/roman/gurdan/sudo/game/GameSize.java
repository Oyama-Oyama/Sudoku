package com.roman.gurdan.sudo.game;

public enum GameSize {

    SIZE_FOUR(4),
    SIZE_SIX(6),
    SIZE_EIGHT(8),
    SIZE_NINE(9);


    int _size;

    GameSize(int size) {
        this._size = size;
    }

    public int getValue() {
        return _size;
    }

    public static GameSize getGameSize(int size) {
        if (size == SIZE_FOUR._size) return SIZE_FOUR;
        else if (size == SIZE_SIX._size) return SIZE_SIX;
        else if (size == SIZE_EIGHT._size) return SIZE_EIGHT;
        else if (size == SIZE_NINE._size) return SIZE_NINE;
        return SIZE_FOUR;
    }

}
