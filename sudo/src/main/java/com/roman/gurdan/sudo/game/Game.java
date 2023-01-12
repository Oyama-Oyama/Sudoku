package com.roman.gurdan.sudo.game;

import com.roman.gurdan.sudo.game.action.AMirror;
import com.roman.gurdan.sudo.game.action.MirrorManager;
import com.roman.gurdan.sudo.game.cell.Cell;
import com.roman.gurdan.sudo.game.factory.IGameCreator;

import java.util.List;

public class Game {

    private IGameCreator creator;
    private MirrorManager actionManager;

    private boolean openNote = false;

    private IGameChangeListener listener = null;

    public Game() {
    }

    public void setListener(IGameChangeListener listener) {
        this.listener = listener;
    }

    public void setupCreator(IGameCreator creator) {
        this.creator = creator;
    }

    public void setupActionManager(MirrorManager actionManager) {
        this.actionManager = actionManager;
    }

    public void initGame() throws Exception {
        if (this.creator == null)
            throw new NullPointerException("Game creator can't be null");
        if (this.actionManager == null)
            throw new NullPointerException("Game Action Manager can't be null");
        this.creator.createGame();
    }

    public int getGameSize() {
        if (this.creator == null)
            throw new NullPointerException("Game creator can't be null");
        return this.creator.getGameSize().getValue();
    }

    public Cell[][] getGameData() {
        if (this.creator == null)
            throw new NullPointerException("Game creator can't be null");
        return this.creator.getData();
    }

    public Cell getCell(int row, int col) {
        if (this.creator == null)
            throw new NullPointerException("Game creator can't be null");
        return this.creator.getCell(row, col);
    }

    public List<Cell> getRelativeCells(Cell cell, boolean highLightLineOrRow, boolean highLightGroup, boolean highLightSameNumber) {
        if (this.creator == null)
            throw new NullPointerException("Game creator can't be null");
        return this.creator.getRelativeCells(cell, highLightLineOrRow, highLightGroup, highLightSameNumber);
    }

    public void toggleNote() {
        openNote = !openNote;
        this.recordMirrorImage();
        if (listener != null) listener.onGameChanged();
    }

    public boolean isOpenNote() {
        return openNote;
    }

    public void setCellValue(int row, int col, int value) {
        if (this.creator == null)
            throw new NullPointerException("Game creator can't be null");
        this.creator.setValue(row, col, value, openNote);
        this.recordMirrorImage();
        if (listener != null) listener.onGameChanged();
    }

    public void setCellValue(Cell cell, int value) {
        if (this.creator == null)
            throw new NullPointerException("Game creator can't be null");
        this.creator.setValue(cell, value, openNote);
        this.recordMirrorImage();
        if (listener != null) listener.onGameChanged();
    }

    private void recordMirrorImage() {
        if (this.actionManager == null)
            throw new NullPointerException("Game Action Manager can't be null");
        AMirror mirror = this.creator.recordMirrorImage();
        mirror.openNote = this.openNote;
        this.actionManager.addMirror(mirror);
    }

    public boolean hasUndo() {
        if (this.actionManager == null)
            throw new NullPointerException("Game Action Manager can't be null");
        return this.actionManager.canUndo();
    }

    public void Undo() {
        if (this.actionManager == null)
            throw new NullPointerException("Game Action Manager can't be null");
        AMirror action = this.actionManager.undo();
        this.creator.recoverMirrorImage(action);
        this.openNote = action.openNote;
        if (listener != null) listener.onGameChanged();
    }

    public void clearMirrorImage() {
        if (this.actionManager == null)
            throw new NullPointerException("Game Action Manager can't be null");
        this.actionManager.clear();
    }

    public void printGame() {
        this.creator.printGame();
    }

    public void destroy() {
        if (this.creator != null) {
            this.creator.destroy();
        }
        this.actionManager = null;
    }


}
