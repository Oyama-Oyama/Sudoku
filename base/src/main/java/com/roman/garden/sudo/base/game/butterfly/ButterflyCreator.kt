package com.roman.garden.sudo.base.game.butterfly

import com.roman.garden.sudo.base.game.Cell
import com.roman.garden.sudo.base.game.ISpliceCreator
import com.roman.garden.sudo.base.util.GameSize

/**
 *  蝴蝶数独
 *           
 *                1 1 1  1 1 1  2 2 2  2 2 2  2 2 2  3 3 3  3 3 3
 *                1 1 1  1 1 1  2 2 2  2 2 2  2 2 2  3 3 3  3 3 3
 *                1 1 1  1 1 1  2 2 2  2 2 2  2 2 2  3 3 3  3 3 3
 *                1 1 1  1 1 1  2 2 2  2 2 2  2 2 2  3 3 3  3 3 3
 *                1 1 1  1 1 1  2 2 2  2 2 2  2 2 2  3 3 3  3 3 3
 *                1 1 1  1 1 1  2 2 2  2 2 2  2 2 2  3 3 3  3 3 3
 *                1 1 1  1 1 1  2 2 2  2 2 2  2 2 2  3 3 3  3 3 3
 *                1 1 1  1 1 1  2 2 2  2 2 2  2 2 2  3 3 3  3 3 3
 *                1 1 1  1 1 1  2 2 2  2 2 2  2 2 2  3 3 3  3 3 3
 *
 */
internal class ButterflyCreator(gameSize: GameSize) : ISpliceCreator(gameSize) {

    init {
        data = Array(gameSize.row) { row ->
            Array(gameSize.col) { col ->
                if (getCellArea(row, col) > 0) {
                    countCells++
                    val group = calGroup(row, col)
                    Cell(row, col, group, 0)
                } else {
                    null
                }
            }
        }
    }

    override fun getArea(area: Int): Pair<Int, Int> {
        return when (area) {
            AREA_FIRST -> Pair(0, 0)
            AREA_SECOND -> Pair(0, 6)
            AREA_THIRD -> Pair(0, 12)
            else -> throw IllegalArgumentException("invalid area:$area")
        }
    }

    override fun getUsedArea(): Int  = 3

    override fun getCellAreas(row: Int, col: Int): Array<Int> {
        if (row in 0..8 && col in 6..8) return arrayOf(AREA_SECOND, AREA_FIRST)
        if (row in 0..8 && col in 12..114) return arrayOf(AREA_SECOND, AREA_THIRD)
        return arrayOf(getCellArea(row, col))
    }

    override fun realCreateGame() {
        buildGame(AREA_FIRST)
        buildGame(AREA_SECOND)
        buildGame(AREA_THIRD)
    }

    override fun getCellArea(row: Int, col: Int): Int {
        if (row in 0..8 && col in 0..8) return AREA_FIRST
        else if (row in 0..8 && col in 6..14) return AREA_SECOND
        else if (row in 0..8 && col in 12..20) return AREA_THIRD
        return -1
    }
}