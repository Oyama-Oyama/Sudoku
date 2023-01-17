package com.roman.gurdan.sudo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.roman.gurdan.sudo.game.Game;
import com.roman.gurdan.sudo.game.IGameChangeListener;
import com.roman.gurdan.sudo.game.cell.Cell;

public abstract class IBoardEvent extends View implements IGameChangeListener {

    public static final String INVALID_VALUE = "invalid_value";
    public static final String INVALID_CELL = "invalid_cell";

    protected Game game;

    protected boolean highLightLineOrRow = true;// 高亮相同行、列
    protected boolean highLightGroup = true; // 高亮相同组
    protected boolean highLightSameNumber = true; // 高亮相同数字
    protected boolean highLightErrorNumber = true; // 高亮错误数字


    public IBoardEvent(Context context) {
        super(context);
    }

    public IBoardEvent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IBoardEvent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Deprecated
    public abstract void setupGame(Game game);

    protected abstract Cell getCurCell();
    protected abstract void resetCurCell(Cell cell);

    public void setupGame(Game game, ColorUtil colorUtil) {
        this.game = game;
        this.game.setListener(this);
    }

    public boolean setValue(int value) throws Exception {
        if (value < 0 || value > game.getGameSize())
            throw new IllegalArgumentException(INVALID_VALUE);
        if (getCurCell() == null)
            throw new NullPointerException(INVALID_CELL);
        return game.setCellValue(getCurCell(), value);
    }

    public boolean hasUndo() {
        return this.game.hasUndo();
    }

    public void Undo() {
        this.game.Undo();
    }

    public void toggleNote() {
        this.game.toggleNote(getCurCell());
    }

    public boolean isNoteOn() {
        return this.game.openNote;
    }

    public void clearMirrorImage() {
        this.game.clearMirrorImage();
    }

    public void resetGame() {
        this.game.resetGame();

    }

    public void solve() {

    }

    public boolean isGameOver() {
        return this.game.isGameOver();
    }

    public void destroy() {
        this.game.destroy();
        this.game = null;
    }


    @Override
    public void onUndoAction(int row, int col) {
        this.resetCurCell(this.game.getCell(row, col));
        this.postInvalidate();
    }

    @Override
    public void onGameChanged() {
        this.postInvalidate();
    }

    public void resetSettings(boolean highLightLineOrRow, boolean highLightGroup, boolean highLightSameNumber, boolean highLightErrorNumber){
        this.highLightLineOrRow = highLightLineOrRow;// 高亮相同行、列
        this.highLightGroup = highLightGroup; // 高亮相同组
        this.highLightSameNumber = highLightSameNumber; // 高亮相同数字
        this.highLightErrorNumber = highLightErrorNumber; // 高亮错误数字
        this.postInvalidate();
    }


}
