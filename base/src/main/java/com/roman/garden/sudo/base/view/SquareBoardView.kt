package com.roman.garden.sudo.base.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import com.roman.garden.sudo.base.util.GameSize
import kotlin.math.min

class SquareBoardView(context: Context?, attrs: AttributeSet?) : IBoardView(context, attrs) {


    override fun initBoardSize() {
        this.game?.let {
            it.setupDefaultSelectedCell().apply {
                this@SquareBoardView.selectedCell = it.getCell(first, second)
            }
            cellS = boardSize / DEFAULT_MAX_CELL_NUMBER_IN_LINE
            startX = (mwidth - it.gameSize.value * cellS) / 2
            startY = (mheight - it.gameSize.value * cellS) / 2
            this.postInvalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN -> updateTouchCell(e.x, e.y)
                MotionEvent.ACTION_MOVE -> updateTouchCell(e.x, e.y)
//                MotionEvent.ACTION_CANCEL -> updateTouchCell(e.x, e.y)
//                MotionEvent.ACTION_UP -> updateTouchCell(e.x, e.y)
            }
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mwidth = MeasureSpec.getSize(widthMeasureSpec)
        mheight = MeasureSpec.getSize(heightMeasureSpec)
        boardSize = min(mwidth, mheight).toFloat()
        setMeasuredDimension(mwidth, mheight)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.let { it ->
            this.game?.let { _ ->
                drawCellBg(it)
                drawCellNumber(it)
                drawInnerLine(it)
              //  drawOuterLine(it)
            }
        }
    }

    private fun drawCellBg(canvas: Canvas) {
        selectedCell?.let { curCell ->
            this.game?.getRelatedCells(
                curCell,
                highLightLineOrRow,
                highLightGroup,
                highLightSameNumber
            )?.let { data ->
                if (data.isEmpty())
                    data.add(curCell)
                data.forEach { cell ->
                    if (curCell.row == cell.row && curCell.col == cell.col) {
                        bgPaint.color = colorUtil.COLOR_TOUCHED_CELL_BG
                    } else {
                        if (curCell != null && curCell.value != 0 && curCell.value == cell.value) {
                            bgPaint.color = colorUtil.COLOR_SAME_VALUE_CELL_BG
                        } else {
                            bgPaint.color = colorUtil.COLOR_RELATIVE_CELL_BG
                        }
                    }
                    canvas.drawRect(
                        cellS * cell.col + startX + cellS * 0.02f,
                        cellS * cell.row + startY + cellS * 0.02f,
                        cellS * cell.col + startX + cellS - cellS * 0.02f,
                        cellS * cell.row + startY + cellS - cellS * 0.02f,
                        bgPaint
                    )
                }
            }
        }
    }

    private fun drawCellNumber(canvas: Canvas) {
        this.game?.let { g ->
            g.getData()?.let { data ->
                data.forEachIndexed { row, value ->
                    value.forEachIndexed { col, cell ->
                        if (g.isNoteOn() && cell.optional.size > 0) {
                            numberPaint.textSize = cellS / 4
                            numberPaint.color = colorUtil.COLOR_ENTER_VALUE
                            cell.optional.forEach { op ->
                                canvas.drawText(
                                    op.toString(),
                                    getNoteX(col, op),
                                    getNoteY(row, op),
                                    numberPaint
                                )
                            }
                        } else {
                            numberPaint.textSize = cellS / 2
                            if (cell.value != 0) {
                                when (cell.preSet) {
                                    true -> numberPaint.color = colorUtil.COLOR_PRESET_VALUE
                                    else -> {
                                        if (!cell.valid && highLightErrorNumber) {
                                            numberPaint.color = colorUtil.COLOR_ERROR_VALUE;
                                        } else {
                                            numberPaint.color = colorUtil.COLOR_ENTER_VALUE;
                                        }
                                    }
                                }
                                canvas.drawText(
                                    cell.value.toString(),
                                    col * cellS + startX + cellS / 2,
                                    row * cellS + startY + cellS / 2 + cellS / 5,
                                    numberPaint
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun drawInnerLine(canvas: Canvas) {
        this.game?.let { g ->
            paint.color = colorUtil.COLOR_INNER_LINE
            paint.strokeWidth = cellS * 0.01f
            var rowStep: Int
            var colStep: Int
            when (g.gameSize) {
                GameSize.SIZE_NINE -> {
                    rowStep = 3
                    colStep = 3
                }
                else -> {
                    rowStep = 2
                    colStep = g.gameSize.value / 2
                }
            }
            for (i in 0..g.gameSize.value) {
                paint.color = colorUtil.COLOR_INNER_LINE
                paint.strokeWidth = cellS * 0.01f
                if (i % rowStep != 0) {
                    canvas.drawLine(
                        startX,
                        startY + i * cellS,
                        cellS * g.gameSize.value + startX,
                        startY + i * cellS,
                        paint
                    )
                }
                if (i % colStep != 0) {
                    canvas.drawLine(
                        startX + i * cellS,
                        startY,
                        startX + i * cellS,
                        startY + cellS * g.gameSize.value,
                        paint
                    )
                }

                paint.color = colorUtil.COLOR_OUTER_LINE
                paint.strokeWidth = cellS * 0.05f
                if (i % rowStep == 0) {
                    canvas.drawLine(
                        startX,
                        startY + i * cellS,
                        cellS * g.gameSize.value + startX,
                        startY + i * cellS,
                        paint
                    )
                }
                if (i % colStep == 0) {
                    canvas.drawLine(
                        startX + i * cellS,
                        startY,
                        startX + i * cellS,
                        startY + cellS * g.gameSize.value,
                        paint
                    )
                }
            }
        }
    }

    private fun getNoteX(col: Int, value: Int): Float {
        return this.game?.let { g ->
            when (g.gameSize.tag) {
                GameSize.SIZE_FOUR.tag -> startX + col * cellS + (cellS / 2) * (1 - value % 2) + cellS / 4
                else -> {
                    var index = value % 3 - 1
                    if (index < 0) index = 2
                    return startX + col * cellS + (cellS / 3) * index + cellS / 6
                }
            }
        } ?: 0.0f
    }

    private fun getNoteY(row: Int, value: Int): Float {
        return this.game?.let { g ->
            when (g.gameSize.tag) {
                GameSize.SIZE_FOUR.tag -> {
                    val tmp = value / 2.0f
                    val index = if (tmp <= 1) 0 else 1
                    return startY + row * cellS + (cellS / 2) * index + cellS / 3
                }
                GameSize.SIZE_SIX.tag -> {
                    val tmp = value / 3.0f
                    val index = if (tmp <= 1) 0 else (if (tmp <= 2) 1 else 2)
                    return startY + row * cellS + (cellS / 2) * index + cellS / 3
                }
                else -> {
                    val tmp = value / 3.0f
                    val index = if (tmp <= 1) 0 else (if (tmp <= 2) 1 else 2)
                    return startY + row * cellS + (cellS / 3) * index + cellS / 4
                }
            }
        } ?: 0.0f
    }

    private fun updateTouchCell(x: Float, y: Float) {
        val row = ((y - startY) / cellS).toInt()
        val col = ((x - startX) / cellS).toInt()
        if (selectedCell != null && selectedCell!!.row == row && selectedCell!!.col == col) {

        } else {
            try {
                selectedCell = game?.getCell(row, col)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                postInvalidate()
            }
        }
    }

}