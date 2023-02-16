package com.roman.garden.sudo.base.game

import com.roman.garden.sudo.base.action.IMirror
import com.roman.garden.sudo.base.util.Difficulty
import com.roman.garden.sudo.base.util.GameSize

internal abstract class ICreator constructor(var gameSize: GameSize) {

    var difficulty: Difficulty = Difficulty.HARD

    abstract fun createGame()

    abstract fun printGame()

    abstract fun destroy()

    abstract fun resetGame()

    abstract fun getCell(row: Int, col: Int): Cell?

    abstract fun isGameOver(): Boolean

    abstract fun getData(): Array<Array<Cell>>

    abstract fun setValue(cell: Cell, value: Int, isNote: Boolean = false)

    abstract fun isValid(cell: Cell): Boolean

    abstract fun getRelatedCells(cell: Cell, containSelf: Boolean = false): List<Cell>

    abstract fun getRelatedCells(
        cell: Cell,
        highLightLineOrRow: Boolean,
        highLightGroup: Boolean,
        highLightSameNumber: Boolean
    ): MutableList<Cell>

    abstract fun recordGame(): IMirror?

    abstract fun recoverGame(iMirror: IMirror?)

}