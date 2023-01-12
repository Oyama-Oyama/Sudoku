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

    public int getValue(){
        return _size;
    }

}
