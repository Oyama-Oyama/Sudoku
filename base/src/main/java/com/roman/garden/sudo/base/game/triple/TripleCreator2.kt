package com.roman.garden.sudo.base.game.triple

import com.roman.garden.sudo.base.game.Cell
import com.roman.garden.sudo.base.game.ISpliceCreator
import com.roman.garden.sudo.base.util.GameSize

/**
 *  三角形

 *                    1 1 1  1 1 1  2 2 2
 *                    1 1 1  2 2 2  2 2 2
 *                    1 1 1  2 2 2  2 2 2
 *      4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
 *      4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
 *      4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
 *      4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
 *      4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
 *      4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
 *      4 4 4  4 4 4  4 4 4         5 5 5  5 5 5  5 5 5
 *      4 4 4  4 4 4  4 4 4         5 5 5  5 5 5  5 5 5
 *      4 4 4  4 4 4  4 4 4         5 5 5  5 5 5  5 5 5
 *
 */

internal class TripleCreator2(gameSize: GameSize) : ISpliceCreator(gameSize) {

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
        buildGame(AREA_FIFTH)
        buildGame(AREA_SECOND)
        buildGame(AREA_THIRD)
    }

    override fun getUsedArea(): Int  = 3

    override fun getArea(area: Int): Pair<Int, Int> {
        return when (area) {
            AREA_FIRST -> Pair(0, 6)
            AREA_SECOND -> Pair(3, 0)
            AREA_THIRD -> Pair(3, 12)
            else -> throw IllegalArgumentException("invalid area:$area")
        }
    }

    override fun getCellAreas(row: Int, col: Int): Array<Int> {
        if (row in 3..8 && col in 6..8) return arrayOf(AREA_FIRST, AREA_SECOND)
        if (row in 3..8 && col in 12..14) return arrayOf(AREA_FIRST, AREA_THIRD)
        return arrayOf(getCellArea(row, col))
    }

    override fun getCellArea(row: Int, col: Int): Int {
        if (row in 9..11 && col in 9..11) return -1
        if (row in 0..8 && col in 6..14) return AREA_FIRST
        else if (row in 3..11 && col in 0..8) return AREA_SECOND
        else if (row in 3..11 && col in 12..20) return AREA_THIRD
        return -1
    }

}