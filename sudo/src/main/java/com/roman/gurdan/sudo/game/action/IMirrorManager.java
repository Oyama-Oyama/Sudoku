package com.roman.gurdan.sudo.game.action;

public interface IMirrorManager {

    void addMirror(AMirror action);

    boolean canUndo();

    AMirror undo();

    void clear();

}
