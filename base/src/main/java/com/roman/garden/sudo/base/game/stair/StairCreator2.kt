package com.roman.garden.sudo.base.game.stair

import com.roman.garden.sudo.base.game.Cell
import com.roman.garden.sudo.base.game.ISpliceCreator
import com.roman.garden.sudo.base.util.GameSize

/**
 *  阶梯
 *
 *      1 1 1  1 1 1  1 1 1
 *      1 1 1  1 1 1  1 1 1
 *      1 1 1  1 1 1  1 1 1
 *      1 1 1  1 1 1  1 1 1
 *      1 1 1  1 1 1  1 1 1
 *      1 1 1  1 1 1  1 1 1
 *      1 1 1  2 2 2  2 2 2  2 2 2
 *      1 1 1  2 2 2  2 2 2  2 2 2
 *      1 1 1  2 2 2  2 2 2  2 2 2
 *             2 2 2  2 2 2  2 2 2
 *             2 2 2  2 2 2  2 2 2
 *             2 2 2  2 2 2  2 2 2
 *             2 2 2  3 3 3  3 3 3  3 3 3
 *             2 2 2  3 3 3  3 3 3  3 3 3
 *             2 2 2  3 3 3  3 3 3  3 3 3
 *                    3 3 3  3 3 3  3 3 3
 *                    3 3 3  3 3 3  3 3 3
 *                    3 3 3  3 3 3  3 3 3
 *                    3 3 3  3 3 3  3 3 3
 *                    3 3 3  3 3 3  3 3 3
 *                    3 3 3  3 3 3  3 3 3
 *
 */
internal class StairCreator2(gameSize: GameSize) : ISpliceCreator(gameSize) {

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

    override fun realCreateGame() {
        buildGame(AREA_FIRST)
        buildGame(AREA_SECOND)
        buildGame(AREA_THIRD)
    }

    override fun getUsedArea(): Int  = 3

    override fun getArea(area: Int): Pair<Int, Int> {
        return when (area) {
            AREA_FIRST -> Pair(0, 0)
            AREA_SECOND -> Pair(6, 3)
            AREA_THIRD -> Pair(12, 6)
            else -> throw IllegalArgumentException("invalid area:$area")
        }
    }

    override fun getCellAreas(row: Int, col: Int): Array<Int> {
        if (row in 6..8 && col in 3..8) return arrayOf(AREA_SECOND, AREA_FIFTH)
        if (row in 12..14 && col in 6..11) return arrayOf(AREA_SECOND, AREA_THIRD)
        return arrayOf(getCellArea(row, col))
    }

    override fun getCellArea(row: Int, col: Int): Int {
        if (row in 9..20 && col in 0..2) return -1
        if (row in 15..20 && col in 0..5) return -1
        if (row in 0..8 && col in 0..8) return AREA_FIRST
        else if (row in 6..14 && col in 3..11) return AREA_SECOND
        else if (row in 12..20 && col in 6..14) return AREA_THIRD
        return -1
    }

}