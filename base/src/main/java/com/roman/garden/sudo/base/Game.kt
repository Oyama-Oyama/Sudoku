package com.roman.garden.sudo.base

import com.roman.garden.sudo.base.action.MirrorManager
import com.roman.garden.sudo.base.game.Cell
import com.roman.garden.sudo.base.game.ICreator
import com.roman.garden.sudo.base.game.IGameListener
import com.roman.garden.sudo.base.game.butterfly.ButterflyCreator
import com.roman.garden.sudo.base.game.cross.CrossCreator
import com.roman.garden.sudo.base.game.flower.FlowerCreator
import com.roman.garden.sudo.base.game.square.Square4Creator
import com.roman.garden.sudo.base.game.square.Square6Creator
import com.roman.garden.sudo.base.game.square.Square8Creator
import com.roman.garden.sudo.base.game.square.Square9Creator
import com.roman.garden.sudo.base.game.triple.TripleCreator
import com.roman.garden.sudo.base.game.triple.TripleCreator2
import com.roman.garden.sudo.base.game.windmill.WindmillCreator
import com.roman.garden.sudo.base.game.windmill.WindmillCreator2
import com.roman.garden.sudo.base.util.Difficulty
import com.roman.garden.sudo.base.util.GameSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class Game(val gameSize: GameSize) : CoroutineScope by MainScope() {

    var difficulty: Difficulty = Difficulty.HARD
    private var creator: ICreator? = null
    private var mirrorManager: MirrorManager? = null
    private var isNoteOn = false
    private var listener: IGameListener? = null
    private var lastSelectCell = Pair(0, 0)

    constructor(gameSize: GameSize, diff: Difficulty) : this(gameSize) {
        this.difficulty = diff
    }

    fun createGame() {
        if (creator == null) {
            creator = createGameCreator()
        }
        if (mirrorManager == null) {
            mirrorManager = MirrorManager()
        }
        creator?.let {
            it.difficulty = this.difficulty
            lastSelectCell = setupDefaultSelectedCell()
            it.createGame()
        } ?: throw NullPointerException("game creator can't be null")
    }

    fun setListener(listener: IGameListener?) = listener.also { this.listener = it }

    fun resetGame() {
        creator?.let {
            it.resetGame()
            clearMirror()
            listener?.onGameRefresh()
        }
    }

    fun printGame() = creator?.printGame()

    fun getCell(row: Int, col: Int): Cell? = creator?.getCell(row, col)

    fun setValue(cell: Cell?, value: Int) {
        creator?.let {
            cell?.let { c ->
                recordGame(c.row, c.col)
                it.setValue(c, value, this.isNoteOn)
                listener?.onGameRefresh()
            }
        }
    }

    fun getRelatedCells(
        cell: Cell,
        highLightLineOrRow: Boolean,
        highLightGroup: Boolean,
        highLightSameNumber: Boolean
    ): MutableList<Cell>? =
        creator?.getRelatedCells(cell, highLightLineOrRow, highLightGroup, highLightSameNumber)

    fun toggleNote(cell: Cell?) {
        cell?.let {
            this.isNoteOn = !this.isNoteOn
            listener?.onGameRefresh()
            recordGame(it.row, it.col)
        }
    }

    fun isNoteOn() = this.isNoteOn

    fun hasUndo(): Boolean = mirrorManager?.hasUndo() == true

    fun undo() {
        mirrorManager?.undo()?.let {
            creator?.recoverGame(it)
            this.isNoteOn = it.isNoteOn
            listener?.onGameUnd(it.touchedRow, it.touchedCol)
        }
    }

    fun clearMirror() = mirrorManager?.clear()

    fun destroy() {
        launch {
            creator?.destroy()
            clearMirror()
            creator = null
            mirrorManager = null
            cancel()
        }
    }

    fun getData() = creator?.getGameData()

    fun getSpliceData(): Array<Array<Cell?>>? = creator?.getSpliceData()

    fun isGameOver(): Boolean = creator?.isGameOver() == true

    private fun createGameCreator(): ICreator? {
        return when (gameSize) {
            GameSize.SIZE_FOUR -> Square4Creator(gameSize)
            GameSize.SIZE_SIX -> Square6Creator(gameSize)
            GameSize.SIZE_EIGHT -> Square8Creator(gameSize)
            GameSize.SIZE_NINE -> Square9Creator(gameSize)
            GameSize.SIZE_FLOWER -> FlowerCreator(gameSize)
            GameSize.SIZE_CROSS -> CrossCreator(gameSize)
            GameSize.SIZE_BUTTERFLY -> ButterflyCreator(gameSize)
            GameSize.SIZE_WINDMILL -> WindmillCreator(gameSize)
            GameSize.SIZE_WINDMILL_2 -> WindmillCreator2(gameSize)
            GameSize.SIZE_TRIPLE -> TripleCreator(gameSize)
            GameSize.SIZE_TRIPLE_2 -> TripleCreator2(gameSize)
            else -> null
        }
    }

    fun setupDefaultSelectedCell(): Pair<Int, Int> {
        return when (gameSize) {
            GameSize.SIZE_FOUR, GameSize.SIZE_SIX, GameSize.SIZE_EIGHT, GameSize.SIZE_NINE -> Pair(
                0,
                0
            )
            GameSize.SIZE_FLOWER, GameSize.SIZE_CROSS -> Pair(10, 10)
            GameSize.SIZE_BUTTERFLY -> Pair(4, 10)
            GameSize.SIZE_WINDMILL -> Pair(6, 6)
            GameSize.SIZE_WINDMILL_2 -> Pair(6, 6)
            GameSize.SIZE_TRIPLE -> Pair(0, 0)
            GameSize.SIZE_TRIPLE_2 -> Pair(0, 0)
        }
    }

    private fun recordGame(row: Int, col: Int) {
        creator?.recordGame()?.apply {
            isNoteOn = isNoteOn()
            touchedCol = lastSelectCell.second
            touchedRow = lastSelectCell.first
            lastSelectCell = Pair(row, col)
            mirrorManager?.addMirror(this)
        }
    }

    open fun getUsedArea(): Int? = creator?.getUsedArea()

    /**
     *  返回指定区域起始 行、列坐标
     */
    open fun getArea(area: Int): Pair<Int, Int>? = creator?.getArea(area)

}