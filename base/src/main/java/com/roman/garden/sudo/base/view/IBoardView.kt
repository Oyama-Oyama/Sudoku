package com.roman.garden.sudo.base.view

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.roman.garden.sudo.base.Game
import com.roman.garden.sudo.base.game.Cell
import com.roman.garden.sudo.base.game.IGameListener
import com.roman.garden.sudo.base.util.ColorUtil
import kotlin.math.min

abstract class IBoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs), IGameListener {

    protected val DEFAULT_MAX_CELL_NUMBER_IN_LINE = 9

    protected var mwidth = 0
    protected var mheight = 0
    protected var boardSize = 0.0f
    protected var paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    protected var numberPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        textAlign = Paint.Align.CENTER
    }
    protected var bgPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    protected var cellS: Float = 0.0f
    protected var startX: Float = 0.0f
    protected var startY: Float = 0.0f
    var selectedCell: Cell? = null
    protected lateinit var colorUtil: ColorUtil


    var highLightLineOrRow = true// 高亮相同行、列
    var highLightGroup = true // 高亮相同组
    var highLightSameNumber = true // 高亮相同数字
    var highLightErrorNumber = true // 高亮错误数字


    protected var game: Game? = null

    fun setupGame(game: Game) {
        this.setupGame(game, ColorUtil())
    }

    fun setupGame(game: Game, colorUtil: ColorUtil) {
        this.game = game
        this.colorUtil = colorUtil
        this.initBoardSize()
        this.game?.setListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        this.game?.setListener(null)
    }

    override fun onGameRefresh() {
        postInvalidate()
    }

    override fun onGameUnd(row: Int, col: Int) {
        game?.getCell(row, col)?.let { it ->
            selectedCell = it
            postInvalidate()
        }
    }

    protected abstract fun initBoardSize()


}