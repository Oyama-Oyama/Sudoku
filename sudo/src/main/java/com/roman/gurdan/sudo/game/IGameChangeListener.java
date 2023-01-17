package com.roman.gurdan.sudo.game;

public interface IGameChangeListener {

    void onGameChanged();

    void onUndoAction(int row, int col);

}
