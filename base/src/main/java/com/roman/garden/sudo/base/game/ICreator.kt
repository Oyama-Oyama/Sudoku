package com.roman.garden.sudo.base.game

import com.roman.garden.sudo.base.action.IMirror
import com.roman.garden.sudo.base.util.Difficulty
import com.roman.garden.sudo.base.util.GameSize
import java.util.*
import kotlin.math.roundToInt

internal abstract class ICreator constructor(var gameSize: GameSize) {

    var difficulty: Difficulty = Difficulty.HARD
    val random: Random = Random()

    abstract fun createGame()

    abstract fun printGame()

    abstract fun destroy()

    abstract fun resetGame()

    abstract fun getCell(row: Int, col: Int): Cell?

    abstract fun isGameOver(): Boolean

    open fun getGameData(): Array<Array<Cell>>? {
        return null
    }

    open fun getSpliceData(): Array<Array<Cell?>>? {
        return null
    }

    abstract fun setValue(cell: Cell, value: Int, isNote: Boolean = false)

    abstract fun isValid(cell: Cell): Boolean

    abstract fun getRelatedCells(
        cell: Cell,
        highLightLineOrRow: Boolean,
        highLightGroup: Boolean,
        highLightSameNumber: Boolean
    ): MutableList<Cell>

    abstract fun recordGame(): IMirror?

    abstract fun recoverGame(iMirror: IMirror?)

    abstract fun getCellCount(): Int

    fun getEmptyCellCount(): Int {
        return when (difficulty) {
            Difficulty.EASY -> (getCellCount() * getRate(
                0.35f,
                0.2f
            )).roundToInt()
            Difficulty.MEDIUM -> (getCellCount() * getRate(
                0.55f,
                0.3f
            )).roundToInt()
            Difficulty.HARD -> (getCellCount() * getRate(
                0.95f,
                0.5f
            )).roundToInt()
            else -> (getCellCount() / 2.0f).roundToInt()
        }
    }

    private fun getRate(max: Float, min: Float): Float = min + random.nextFloat() * (max - min);


}