package com.roman.gurdan.sudo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.roman.gurdan.sudo.game.Game;
import com.roman.gurdan.sudo.game.GameSize;
import com.roman.gurdan.sudo.game.cell.Cell;

import java.util.List;

public class BoardView extends View {

    private static final int DEFAULT_MAX_CELL_NUMBER_IN_LINE = 9;

    public BoardView(Context context) {
        this(context, null);
    }

    public BoardView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private int width, height;
    private Game game;
    private Paint paint, numberPaint, bgPaint;
    private float cellS, startX, startY;

    private boolean highLightLineOrRow = true;// 高亮相同行、列
    private boolean highLightGroup = true; // 高亮相同组
    private boolean highLightSameNumber = true; // 高亮相同数字

    private Cell curCell = null;

    private ColorUtil colorUtil = null;

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        numberPaint = new Paint();
        numberPaint.setAntiAlias(true);
        numberPaint.setStyle(Paint.Style.STROKE);
        numberPaint.setTextAlign(Paint.Align.CENTER);

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setStyle(Paint.Style.FILL);
    }

    public void setupGame(Game game) {
        this.setupGame(game, new ColorUtil());
    }

    public Cell getCurCell() {
        return curCell;
    }

    public void setupGame(Game game, ColorUtil colorUtil) {
        this.game = game;
        this.colorUtil = colorUtil;
        if (game != null) {
            cellS = width < height ? width / DEFAULT_MAX_CELL_NUMBER_IN_LINE : height / DEFAULT_MAX_CELL_NUMBER_IN_LINE;
            startX = (width - game.getGameSize() * cellS) / 2;
            startY = (height - game.getGameSize() * cellS) / 2;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (game == null) return;

        drawCellBg(canvas);
        drawCellNumber(canvas);
        drawInnerLine(canvas);
        drawOuterLine(canvas);
    }

    private void drawCellBg(Canvas canvas) {
        List<Cell> cells = game.getRelativeCells(curCell, highLightLineOrRow, highLightGroup, highLightSameNumber);
        if (cells == null) return;
        if (cells.size() <= 0) return;
        for (Cell cell : cells) {
            if (curCell != null && curCell.row == cell.row && curCell.col == cell.col) {
                bgPaint.setColor(colorUtil.COLOR_TOUCHED_CELL_BG);
            } else {
                if (curCell != null && curCell.value != 0 && curCell.value == cell.value) {
                    bgPaint.setColor(colorUtil.COLOR_SAME_VALUE_CELL_BG);
                } else {
                    bgPaint.setColor(colorUtil.COLOR_RELATIVE_CELL_BG);
                }
            }
            canvas.drawRect(cellS * cell.col + startX + cellS * 0.02f,
                    cellS * cell.row + startY + cellS * 0.02f,
                    cellS * cell.col + startX + cellS - cellS * 0.02f,
                    cellS * cell.row + startY + cellS - cellS * 0.02f,
                    bgPaint);
        }
    }

    private void drawCellNumber(Canvas canvas) {
        Cell[][] data = game.getGameData();
        for (int i = 0; i < data.length; i++) {
            Cell[] line = data[i];
            for (int j = 0; j < line.length; j++) {
                Cell cell = line[j];
                if (game.isOpenNote() && cell.optional.size() > 0) {
                    numberPaint.setTextSize(cellS / 4);
                    for (Integer number : cell.optional) {
                        numberPaint.setColor(colorUtil.COLOR_ENTER_VALUE);
                        canvas.drawText(String.valueOf(number),
                                j * cellS + startX + (number % 3) * cellS / 3,
                                i * cellS + startY + (number / 3) * cellS / 3 + cellS / 3 / 2,
                                numberPaint);
                    }
                } else {
                    numberPaint.setTextSize(cellS / 2);
                    if (cell.value != 0) {
                        if (cell.preSet) {
                            numberPaint.setColor(colorUtil.COLOR_PRESET_VALUE);
                        } else {
                            if (!cell.valid) {
                                numberPaint.setColor(colorUtil.COLOR_ERROR_VALUE);
                            } else {
                                numberPaint.setColor(colorUtil.COLOR_ENTER_VALUE);
                            }
                        }
                        canvas.drawText(String.valueOf(cell.value), j * cellS + startX + cellS / 2, i * cellS + startY + cellS / 2 + cellS / 5, numberPaint);
                    }
                }
            }
        }
    }

    private void drawOuterLine(Canvas canvas) {
        paint.setColor(colorUtil.COLOR_OUTER_LINE);
        paint.setStrokeWidth(cellS * 0.05f);
        //内部九宫格分割线
        int rowStep;
        int colStep;
        if (game.getGameSize() == GameSize.SIZE_NINE.getValue()) {
            rowStep = 3;
            colStep = 3;
        } else {
            rowStep = 2;
            colStep = game.getGameSize() / 2;
        }
        for (int i = 1; i < game.getGameSize(); i++) {
            if (i % rowStep == 0) {
                canvas.drawLine(startX, startY + i * cellS, cellS * game.getGameSize() + startX, startY + i * cellS, paint);
            }
            if (i % colStep == 0) {
                canvas.drawLine(startX + i * cellS, startY, startX + i * cellS, startY + cellS * game.getGameSize(), paint);
            }
        }
        //外边框
        canvas.drawRect(startX, startY, startX + cellS * game.getGameSize(), startY + cellS * game.getGameSize(), paint);
    }

    private void drawInnerLine(Canvas canvas) {
        //子九宫格分割线
        paint.setColor(colorUtil.COLOR_INNER_LINE);
        paint.setStrokeWidth(cellS * 0.01f);
        int rowStep;
        int colStep;
        if (game.getGameSize() == GameSize.SIZE_NINE.getValue()) {
            rowStep = 3;
            colStep = 3;
        } else {
            rowStep = 2;
            colStep = game.getGameSize() / 2;
        }
        for (int i = 1; i < game.getGameSize(); i++) {
            if (i % rowStep != 0) {
                canvas.drawLine(startX, startY + i * cellS, cellS * game.getGameSize() + startX, startY + i * cellS, paint);
            }
            if (i % colStep != 0) {
                canvas.drawLine(startX + i * cellS, startY, startX + i * cellS, startY + cellS * game.getGameSize(), paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            updateTouchCell(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            updateTouchCell(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL ||
                event.getAction() == MotionEvent.ACTION_UP) {
            updateTouchCell(event.getX(), event.getY());
        }
        return true;
    }

    private void updateTouchCell(float x, float y) {
        int row = (int) ((y - startY) / cellS);
        int col = (int) ((x - startX) / cellS);
        if (curCell != null && curCell.row == row && curCell.col == col) {

        } else {
            try {
                curCell = game.getCell(row, col);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                invalidate();
            }
        }
    }


}
