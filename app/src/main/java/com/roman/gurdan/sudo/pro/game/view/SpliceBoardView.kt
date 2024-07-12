package com.roman.gurdan.sudo.pro.game.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.roman.gurdan.sudo.pro.game.util.ScreenUtil
import com.roman.gurdan.sudo.pro.game.util.Util
import kotlin.math.abs

class SpliceBoardView(context: Context?, attrs: AttributeSet?) :
    IBoardView(context, attrs) {

    private var tStartX: Float = 0.0f
    private var tStartY: Float = 0.0f
    private val tStartPoint = Point()

    override fun initBoardSize() {
        startX = 24.0f
        startY = 24.0f
        this.game?.let {
//            it.setupDefaultSelectedCell().let { p ->
//                this.selectedCell = it.getCell(p.first, p.second)
//            }
            this.requestLayout()
            this.post {

            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        this.game?.let {
            mwidth = ScreenUtil.getScreenWidth(context)
            cellS = mwidth * 1.0f / (DEFAULT_MAX_CELL_NUMBER_IN_LINE + 1)
            mwidth = (cellS * it.gameSize.col + startX * 2.0f).toInt()
            mheight = (cellS * it.gameSize.row + startY * 2.0f).toInt()
        }
        setMeasuredDimension(mwidth, mheight)
    }

    var lineAlpha = 0
    var curAniRow = 0
    var curAniNumberRow = 0

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        this.game?.let { g ->
            canvas?.let { c ->
                if (isFirstIn) {
                    if (lineAlpha < 255) {
                        drawDivideLine(canvas, alpha = lineAlpha)
                        lineAlpha += 5
                        handler.postDelayed({
                            postInvalidate()
                        }, 5)
                    } else {
                        paint.alpha = 255
                        drawDivideLine(canvas)

                        val maxRow = g.gameSize.row + 5
                        if (curAniRow <= maxRow) {
                            drawAnimCellBg(canvas, curAniRow)
                            curAniRow += 1
                            handler.postDelayed({ postInvalidate() }, 50)
                        } else {
                            if (curAniNumberRow <= g.gameSize.row) {
                                drawAnimNumber(canvas, curAniNumberRow)
                                curAniNumberRow += 1
                                handler.postDelayed({ postInvalidate() }, 50)
                            } else {
                                drawAnimNumber(canvas, g.gameSize.row)
                                isFirstIn = false
                                boardViewListener?.onReady()
                            }
                        }
                    }
                } else {
                    drawCellBg(c)
                    drawCellNumber(c)
                    drawDivideLine(c)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
//                requestDisallowInterceptTouchEvent(true)
                tStartX = event.rawX
                tStartY = event.rawY
                tStartPoint.x = tStartX.toInt()
                tStartPoint.y = tStartY.toInt()
            }

            MotionEvent.ACTION_MOVE -> {
                val offsetX = event.rawX - tStartX
                val offsetY = event.rawY - tStartY
                this.scrollBy(-1 * offsetX.toInt(), -1 * offsetY.toInt())
                tStartX = event.rawX
                tStartY = event.rawY

            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val point = Point(event.rawX.toInt(), event.rawY.toInt())
                Util.distance(tStartPoint, point).let {
                    if (abs(it) <= 2.0f) {
                        findTouchedCell(event.x + scrollX, event.y + scrollY)
                    }
                }
            }

            else -> return false
        }
        return true//super.onTouchEvent(event)
    }

    private fun findTouchedCell(x: Float, y: Float) {
        val row = ((y - startY) / cellS).toInt()
        val col = ((x - startX) / cellS).toInt()
        if (selectedCell != null && selectedCell!!.row == row && selectedCell!!.col == col) {

        } else {
            try {
                val cell = game?.getCell(row, col)
                cell?.let { it -> selectedCell = it }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                postInvalidate()
            }
        }
    }

    private fun drawAnimCellBg(canvas: Canvas, curRow: Int) {
        game?.let { g ->
            g.getSpliceData()?.let { cells ->
                cells.forEach { rows ->
                    rows.forEach { rowItem ->
                        rowItem?.let { cell ->
                            if (cell.row > curRow) {

                            } else {
                                val offset = curRow - cell.row
                                when (offset) {
                                    0 -> animCellPaint.alpha = 255
                                    1 -> animCellPaint.alpha = 200
                                    2 -> animCellPaint.alpha = 125
                                    3 -> animCellPaint.alpha = 75
                                    4 -> animCellPaint.alpha = 25
                                    else -> animCellPaint.alpha = 0
                                }
                                canvas.drawRect(
                                    cellS * cell.col + startX + cellS * 0.02f,
                                    cellS * cell.row + startY + cellS * 0.02f,
                                    cellS * cell.col + startX + cellS - cellS * 0.02f,
                                    cellS * cell.row + startY + cellS - cellS * 0.02f,
                                    animCellPaint
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun drawAnimNumber(canvas: Canvas, curRow: Int) {
        game?.let { g ->
            g.getSpliceData()?.let { cells ->
                cells.forEach { rows ->
                    rows.forEach { rowItem ->
                        rowItem?.let { cell ->
                            if (cell.row < curRow) {
                                numberPaint.textSize = cellS / 2
                                if (cell.value != 0) {
                                    numberPaint.color = colorUtil.COLOR_PRESET_VALUE
                                    canvas.drawText(
                                        cell.value.toString(),
                                        cell.col * cellS + startX + cellS / 2,
                                        cell.row * cellS + startY + cellS / 2 + cellS / 5,
                                        numberPaint
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected open fun drawCellBg(canvas: Canvas) {
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
                        if (curCell.value != 0 && curCell.value == cell.value) {
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

    protected open fun drawCellNumber(canvas: Canvas) {
        this.game?.let { g ->
            g.getSpliceData()?.let { data ->
                data.forEachIndexed { row, value ->
                    value.forEachIndexed { col, item ->
                        item?.let { cell ->
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
    }

    private fun drawDivideLine(canvas: Canvas, alpha: Int = 255) {
        this.game?.let { g ->
            g.getUsedArea()?.let { count ->
                for (area in 1..count) {
                    g.getArea(area)?.let { pair ->
                        drawLattice(pair, canvas, alpha)
                    }
                }
            }
        }
    }

    protected fun drawLattice(pair: Pair<Int, Int>, canvas: Canvas, alpha: Int = 255) {
        for (i in 0..9) {
            if (i % 3 != 0) {
                paint.color = colorUtil.COLOR_INNER_LINE
                paint.alpha = alpha
                paint.strokeWidth = cellS * 0.01f
                canvas.drawLine(
                    pair.second * cellS + startX,
                    (pair.first + i) * cellS + startY,
                    (pair.second + 9) * cellS + startX,
                    (pair.first + i) * cellS + startY,
                    paint
                )
                canvas.drawLine(
                    (pair.second + i) * cellS + startX,
                    pair.first * cellS + startY,
                    (pair.second + i) * cellS + startX,
                    (pair.first + 9) * cellS + startY,
                    paint
                )
            } else {
                paint.color = colorUtil.COLOR_OUTER_LINE
                paint.strokeWidth = cellS * 0.05f
                paint.alpha = alpha
                canvas.drawLine(
                    pair.second * cellS + startX,
                    (pair.first + i) * cellS + startY,
                    (pair.second + 9) * cellS + startX,
                    (pair.first + i) * cellS + startY,
                    paint
                )
                canvas.drawLine(
                    (pair.second + i) * cellS + startX,
                    pair.first * cellS + startY,
                    (pair.second + i) * cellS + startX,
                    (pair.first + 9) * cellS + startY,
                    paint
                )
            }
        }
    }

    private fun getNoteX(col: Int, value: Int): Float {
        var index = value % 3 - 1
        if (index < 0) index = 2
        return startX + col * cellS + (cellS / 3) * index + cellS / 6
    }

    private fun getNoteY(row: Int, value: Int): Float {
        val tmp = value / 3.0f
        val index = if (tmp <= 1) 0 else (if (tmp <= 2) 1 else 2)
        return startY + row * cellS + (cellS / 3) * index + cellS / 4
    }

}