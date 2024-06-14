package com.roman.gurdan.sudo.pro.game.game.windmill

import com.roman.gurdan.sudo.pro.game.game.Cell
import com.roman.gurdan.sudo.pro.game.game.ISpliceCreator
import com.roman.gurdan.sudo.pro.game.util.GameSize

/**
 *  风车数独
 *                              1 1 1  1 1 1  1 1 1
 *                              1 1 1  1 1 1  1 1 1
 *                              1 1 1  1 1 1  1 1 1
 *                              1 1 1  1 1 1  1 1 1
 *                              1 1 1  1 1 1  1 1 1
 *                              1 1 1  1 1 1  1 1 1
 *                4 4 4  4 4 4  4 4 4  1 1 1  5 5 5  5 5 5  5 5 5
 *                4 4 4  4 4 4  4 4 4  1 1 1  5 5 5  5 5 5  5 5 5
 *                4 4 4  4 4 4  4 4 4  1 1 1  5 5 5  5 5 5  5 5 5
 *                4 4 4  4 4 4  4 4 4         5 5 5  5 5 5  5 5 5
 *                4 4 4  4 4 4  4 4 4         5 5 5  5 5 5  5 5 5
 *                4 4 4  4 4 4  4 4 4         5 5 5   5 5 5  5 5 5
 *                4 4 4  4 4 4  3 3 3  3 3 3  3 3 3  5 5 5  5 5 5
 *                4 4 4  4 4 4  3 3 3  3 3 3  3 3 3  5 5 5  5 5 5
 *                4 4 4  4 4 4  3 3 3  3 3 3  3 3 3  5 5 5  5 5 5
 *                              3 3 3  3 3 3  3 3 3
 *                              3 3 3  3 3 3  3 3 3
 *                              3 3 3  3 3 3  3 3 3
 *                              3 3 3  3 3 3  3 3 3
 *                              3 3 3  3 3 3  3 3 3
 *                              3 3 3  3 3 3  3 3 3
 *
 */
internal class WindmillCreator(gameSize: GameSize) : ISpliceCreator(gameSize) {

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

    override fun getCellArea(row: Int, col: Int): Int {
        if (row in 9..11 && col in 9..11) return -1
        if (row in 0..8 && col in 6..14) return AREA_FIRST
        else if (row in 6..14 && col in 0..8) return AREA_SECOND
        else if (row in 6..14 && col in 12..20) return AREA_THIRD
        else if (row in 12..20 && col in 6..14) return AREA_FOURTH
        return -1
    }

    override fun getArea(area: Int): Pair<Int, Int> {
        return when (area) {
            AREA_FIRST -> Pair(0, 6)
            AREA_SECOND -> Pair(6, 0)
            AREA_THIRD -> Pair(6, 12)
            AREA_FOURTH -> Pair(12, 6)
            else -> throw IllegalArgumentException("invalid area:$area")
        }
    }

    override fun getCellAreas(row: Int, col: Int): Array<Int> {
        if (row in 6..8 && col in 6..8) return arrayOf(AREA_SECOND, AREA_FIRST)
        if (row in 6..8 && col in 12..14) return arrayOf(AREA_FIRST, AREA_THIRD)
        if (row in 12..14 && col in 6..8) return arrayOf(AREA_SECOND, AREA_FOURTH)
        if (row in 12..14 && col in 12..14) return arrayOf(AREA_THIRD, AREA_FOURTH)
        return arrayOf(getCellArea(row, col))
    }

    override fun realCreateGame() {
        this.buildGame(AREA_FIRST)
        this.buildGame(AREA_SECOND)
        this.buildGame(AREA_THIRD)
        this.buildGame(AREA_FOURTH)
    }

    override fun getUsedArea(): Int  = 4

}