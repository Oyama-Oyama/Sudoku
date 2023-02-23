package com.roman.garden.sudo.base.game.windmill

import com.roman.garden.sudo.base.game.Cell
import com.roman.garden.sudo.base.game.ISpliceCreator
import com.roman.garden.sudo.base.util.GameSize


/**
 *  风车数独
 *
 *                 1 1 1  1 1 1  1 1 1
 *                 1 1 1  1 1 1  1 1 1
 *                 1 1 1  1 1 1  1 1 1
 *                 1 1 1  1 1 1  1 1 1  5 5 5  5 5 5  5 5 5
 *                 1 1 1  1 1 1  1 1 1  5 5 5  5 5 5  5 5 5
 *                 1 1 1  1 1 1  1 1 1  5 5 5  5 5 5  5 5 5
 *                 1 1 1  1 1 1  1 1 1  5 5 5  5 5 5  5 5 5
 *                 1 1 1  1 1 1  1 1 1  5 5 5  5 5 5  5 5 5
 *                 1 1 1  1 1 1  1 1 1  5 5 5  5 5 5  5 5 5
 *          3 3 3  3 3 3  3 3 3  2 2 2  5 5 5  5 5 5  5 5 5
 *          3 3 3  3 3 3  3 3 3  2 2 2  5 5 5  5 5 5  5 5 5
 *          3 3 3  3 3 3  3 3 3  2 2 2  5 5 5  5 5 5  5 5 5
 *          3 3 3  3 3 3  3 3 3  4 4 4  4 4 4  4 4 4
 *          3 3 3  3 3 3  3 3 3  4 4 4  4 4 4  4 4 4
 *          3 3 3  3 3 3  3 3 3  4 4 4  4 4 4  4 4 4
 *          3 3 3  3 3 3  3 3 3  4 4 4  4 4 4  4 4 4
 *          3 3 3  3 3 3  3 3 3  4 4 4  4 4 4  4 4 4
 *          3 3 3  3 3 3  3 3 3  4 4 4  4 4 4  4 4 4
 *                               4 4 4  4 4 4  4 4 4
 *                               4 4 4  4 4 4  4 4 4
 *                               4 4 4  4 4 4  4 4 4
 *
 *
 */
internal class WindmillCreator2(gameSize: GameSize) : ISpliceCreator(gameSize) {

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
        this.buildGame(AREA_FIRST)
        this.buildGame(AREA_SECOND)
        this.buildGame(AREA_THIRD)
        this.buildGame(AREA_FOURTH)
        this.buildGame(AREA_FIFTH)
    }

    override fun getUsedArea(): Int = 5

    override fun getArea(area: Int): Pair<Int, Int> {
        return when (area) {
            AREA_FIRST -> Pair(0, 3)
            AREA_SECOND -> Pair(6, 6)
            AREA_THIRD -> Pair(9, 0)
            AREA_FOURTH -> Pair(12, 9)
            AREA_FIFTH -> Pair(3, 12)
            else -> throw IllegalArgumentException("invalid area:$area")
        }
    }

    override fun getCellAreas(row: Int, col: Int): Array<Int> {
        if (row in 6..8 && col in 6..11) return arrayOf(AREA_SECOND, AREA_FIRST)
        if (row in 9..14 && col in 6..8) return arrayOf(AREA_SECOND, AREA_THIRD)
        if (row in 12..14 && col in 9..14) return arrayOf(AREA_SECOND, AREA_FOURTH)
        if (row in 6..11 && col in 12..14) return arrayOf(AREA_SECOND, AREA_FIFTH)
        return arrayOf(getCellArea(row, col))
    }

    override fun getCellArea(row: Int, col: Int): Int {
        if (row in 0..8 && col in 0..2) return -1
        if (row in 0..2 && col in 12..20) return -1
        if (row in 12..20 && col in 18..20) return -1
        if (row in 18..20 && col in 0..8) return -1
        if (row in 0..8 && col in 3..11) return AREA_FIRST
        if (row in 6..14 && col in 6..14) return AREA_SECOND
        if (row in 9..17 && col in 0..8) return AREA_THIRD
        if (row in 12..20 && col in 9..17) return AREA_FOURTH
        if (row in 3..11 && col in 12..20) return AREA_FIFTH
        return -1
    }
}