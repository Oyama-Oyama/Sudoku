package com.roman.garden.sudo.base.game.flower

import com.roman.garden.sudo.base.game.Cell
import com.roman.garden.sudo.base.game.ISpliceCreator
import com.roman.garden.sudo.base.util.GameSize


/**
 *  融合四角 九宫格
 *      1 1 1  1 1 1  1 1 1         3 3 3  3 3 3  3 3 3
 *      1 1 1  1 1 1  1 1 1         3 3 3  3 3 3  3 3 3
 *      1 1 1  1 1 1  1 1 1         3 3 3  3 3 3  3 3 3
 *      1 1 1  1 1 1  1 1 1         3 3 3  3 3 3  3 3 3
 *      1 1 1  1 1 1  1 1 1         3 3 3  3 3 3  3 3 3
 *      1 1 1  1 1 1  1 1 1         3 3 3  3 3 3  3 3 3
 *      1 1 1  1 1 1  1 1 1  2 2 2  2 2 2  3 3 3  3 3 3
 *      1 1 1  1 1 1  1 1 1  2 2 2  2 2 2  3 3 3  3 3 3
 *      1 1 1  1 1 1  1 1 1  2 2 2  2 2 2  3 3 3  3 3 3
 *                    2 2 2  2 2 2  2 2 2
 *                    2 2 2  2 2 2  2 2 2
 *                    2 2 2  2 2 2  2 2 2
 *      4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
 *      4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
 *      4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
 *      4 4 4  4 4 4  4 4 4         5 5 5  5 5 5  5 5 5
 *      4 4 4  4 4 4  4 4 4         5 5 5  5 5 5  5 5 5
 *      4 4 4  4 4 4  4 4 4         5 5 5  5 5 5  5 5 5
 *      4 4 4  4 4 4  4 4 4         5 5 5  5 5 5  5 5 5
 *      4 4 4  4 4 4  4 4 4         5 5 5  5 5 5  5 5 5
 *      4 4 4  4 4 4  4 4 4         5 5 5  5 5 5  5 5 5
 *
 */
internal class FlowerCreator(gameSize: GameSize) : ISpliceCreator(gameSize) {

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

    override fun getUsedArea(): Int  = 5

    override fun getCellArea(row: Int, col: Int): Int {
        if (row in 0..8 && col in 0..8) return AREA_FIRST
        else if (row in 6..14 && col in 6..14) return AREA_SECOND
        else if (row in 0..8 && col in 12..20) return AREA_THIRD
        else if (row in 12..20 && col in 0..8) return AREA_FOURTH
        else if (row in 12..20 && col in 12..20) return AREA_FIFTH
        return -1
    }

    override fun getArea(area: Int): Pair<Int, Int> {
        return when (area) {
            AREA_FIRST -> Pair(0, 0)
            AREA_SECOND -> Pair(6, 6)
            AREA_THIRD -> Pair(0, 12)
            AREA_FOURTH -> Pair(12, 0)
            AREA_FIFTH -> Pair(12, 12)
            else -> throw IllegalArgumentException("invalid area:$area")
        }
    }

    override fun getCellAreas(row: Int, col: Int): Array<Int> {
        if (row in 6..8 && col in 6..8) return arrayOf(AREA_SECOND, AREA_FIRST)
        if (row in 6..8 && col in 12..14) return arrayOf(AREA_SECOND, AREA_THIRD)
        if (row in 12..14 && col in 6..8) return arrayOf(AREA_SECOND, AREA_FOURTH)
        if (row in 12..14 && col in 12..14) return arrayOf(AREA_SECOND, AREA_FIFTH)
        return arrayOf(getCellArea(row, col))
    }


}