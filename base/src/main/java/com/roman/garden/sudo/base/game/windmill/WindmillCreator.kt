package com.roman.garden.sudo.base.game.windmill

import com.roman.garden.sudo.base.game.Cell
import com.roman.garden.sudo.base.game.ISpliceCreator
import com.roman.garden.sudo.base.util.GameSize

/**
 *  风车数独
 *                              1 1 1  1 1 1  1 1 1
 *                              1 1 1  1 1 1  1 1 1
 *                              1 1 1  1 1 1  1 1 1
 *                              1 1 1  1 1 1  1 1 1
 *                              1 1 1  1 1 1  1 1 1
 *                              1 1 1  1 1 1  1 1 1
 *                4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
 *                4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
 *                4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
 *                4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
 *                4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
 *                4 4 4  4 4 4  2 2 2  2 2 2  2 2 2  5 5 5  5 5 5
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

    private val AREA_FIRST = 1
    private val AREA_SECOND = 2
    private val AREA_THIRD = 3
    private val AREA_FOURTH = 4
    private val AREA_FIFTH = 5

    init {
        data = Array(gameSize.value) { row ->
            Array(gameSize.value) { col ->
                val area = getCellArea(row, col)
                if (area > 0) {
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
        if (row in 0..8 && col in 6..14) return AREA_FIRST
        else if (row in 6..14 && col in 6..14) return AREA_SECOND
        else if (row in 12..20 && col in 6..14) return AREA_THIRD
        else if (row in 6..14 && col in 0..8) return AREA_FOURTH
        else if (row in 6..14 && col in 12..20) return AREA_FIFTH
        return -1
    }

    override fun getArea(area: Int): Pair<Int, Int> {
        return when (area) {
            AREA_FIRST -> Pair(0, 6)
            AREA_SECOND -> Pair(6, 6)
            AREA_THIRD -> Pair(12, 6)
            AREA_FOURTH -> Pair(6, 0)
            AREA_FIFTH -> Pair(6, 12)
            else -> throw IllegalArgumentException("invalid area:$area")
        }
    }

    override fun getCellAreas(row: Int, col: Int): Array<Int> {
        if (row in 6..8 && col in 6..8) return arrayOf(AREA_SECOND, AREA_FIRST, AREA_FOURTH)
        if (row in 6..8 && col in 12..14) return arrayOf(AREA_SECOND, AREA_FIRST, AREA_FIFTH)
        if (row in 6..8 && col in 9..11) return arrayOf(AREA_SECOND, AREA_FIRST)
        if (row in 9..11 && col in 6..8) return arrayOf(AREA_SECOND, AREA_FOURTH)
        if (row in 9..11 && col in 12..14) return arrayOf(AREA_SECOND, AREA_FIFTH)
        if (row in 12..14 && col in 6..8) return arrayOf(AREA_SECOND, AREA_THIRD, AREA_FOURTH)
        if (row in 12..14 && col in 9..11) return arrayOf(AREA_SECOND, AREA_THIRD)
        if (row in 12..14 && col in 12..14) return arrayOf(AREA_SECOND, AREA_THIRD, AREA_FIFTH)
        return arrayOf(getCellArea(row, col))
    }

    override fun realCreateGame() {
        this.buildGame(AREA_FIRST)
        this.buildGame(AREA_SECOND)
        this.buildGame(AREA_THIRD)
        this.buildGame(AREA_FOURTH)
        this.buildGame(AREA_FIFTH)
    }


}