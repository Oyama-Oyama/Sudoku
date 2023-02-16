package com.roman.garden.sudo.base

import android.os.Handler
import android.os.Looper
import com.roman.garden.sudo.base.action.MirrorManager
import com.roman.garden.sudo.base.game.Cell
import com.roman.garden.sudo.base.game.ICreator
import com.roman.garden.sudo.base.game.IGameListener
import com.roman.garden.sudo.base.game.square.Square4Creator
import com.roman.garden.sudo.base.game.square.Square6Creator
import com.roman.garden.sudo.base.game.square.Square8Creator
import com.roman.garden.sudo.base.game.square.Square9Creator
import com.roman.garden.sudo.base.util.Difficulty
import com.roman.garden.sudo.base.util.GameSize
import com.roman.garden.sudo.base.util.LogUtil
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

    fun getData() = creator?.getData()

    fun isGameOver(): Boolean = creator?.isGameOver() == true

    private fun createGameCreator(): ICreator? {
        return when (gameSize) {
            GameSize.SIZE_FOUR -> Square4Creator(gameSize)
            GameSize.SIZE_SIX -> Square6Creator(gameSize)
            GameSize.SIZE_EIGHT -> Square8Creator(gameSize)
            GameSize.SIZE_NINE -> Square9Creator(gameSize)
            else -> null
        }
    }

    private fun recordGame(row: Int, col: Int) {
        creator?.recordGame()?.apply {
            isNoteOn = isNoteOn()
            touchedCol = col
            touchedRow = row
            mirrorManager?.addMirror(this)
        }
    }

}