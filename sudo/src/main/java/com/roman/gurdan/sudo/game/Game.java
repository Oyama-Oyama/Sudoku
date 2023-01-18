package com.roman.gurdan.sudo.game;

import com.roman.gurdan.sudo.game.action.AMirror;
import com.roman.gurdan.sudo.game.action.MirrorManager;
import com.roman.gurdan.sudo.game.cell.Cell;
import com.roman.gurdan.sudo.game.factory.IGameCreator;

import java.util.List;

public class Game {

    private IGameCreator creator;
    private MirrorManager actionManager;

    public boolean openNote = false;

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

    public GameSize getGameSize() {
        if (this.creator == null)
            throw new NullPointerException("Game creator can't be null");
        return this.creator.getGameSize();
    }

    public Difficulty getGameDifficulty() {
        if (this.creator == null)
            throw new NullPointerException("Game creator can't be null");
        return this.creator.getDifficulty();
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

    public void toggleNote(Cell cell) {
        this.openNote = !this.openNote;
        this.recordMirrorImage(cell.row, cell.col);
        if (listener != null) listener.onGameChanged();
    }

    public boolean isOpenNote() {
        return openNote;
    }

    public boolean setCellValue(int row, int col, int value) {
        if (this.creator == null)
            throw new NullPointerException("Game creator can't be null");
        return this.setCellValue(this.creator.getCell(row, col), value);
    }

    public boolean setCellValue(Cell cell, int value) {
        if (this.creator == null)
            throw new NullPointerException("Game creator can't be null");
        boolean status = this.creator.setValue(cell, value, openNote);
        this.recordMirrorImage(cell.row, cell.col);
        if (listener != null) listener.onGameChanged();
        return status;
    }

    private void recordMirrorImage(int row, int col) {
        if (this.actionManager == null)
            throw new NullPointerException("Game Action Manager can't be null");
        AMirror mirror = this.creator.recordMirrorImage();
        mirror.openNote = this.openNote;
        mirror.touchedRow = row;
        mirror.touchedCol = col;
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
        if (listener != null) listener.onUndoAction(action.touchedRow, action.touchedCol);
    }

    public void clearMirrorImage() {
        if (this.actionManager == null)
            throw new NullPointerException("Game Action Manager can't be null");
        this.actionManager.clear();
    }

    public void printGame() {
        this.creator.printGame();
    }

    public void resetGame() {
        if (this.creator == null)
            throw new NullPointerException("Game creator can't be null");
        this.creator.resetGame();
        if (listener != null) listener.onGameChanged();
    }

    public void solve() {

    }

    public boolean isGameOver() {
        if (this.creator == null)
            throw new NullPointerException("Game creator can't be null");
        return this.creator.isGameOver();
    }

    public void destroy() {
        if (this.creator != null) {
            this.creator.destroy();
        }
        this.creator = null;
        if (this.actionManager != null){
            this.actionManager.clear();
        }
        this.actionManager = null;
    }


}
