package com.roman.garden.sudo.base.game.square

import com.roman.garden.sudo.base.util.GameSize

internal class Square9Creator constructor(gameSize: GameSize): SquareGameCreator(gameSize) {
    override fun calGroup(row: Int, col: Int): Int {
        if (row in 0..2 && col >= 0 && col < 3) {
            return 0
        } else if (row in 0..2 && col >= 3 && col < 6) {
            return 1
        } else if (row in 0..2 && col >= 6 && col < 9) {
            return 2
        } else if (row in 3..5 && col >= 0 && col < 3) {
            return 3
        } else if (row in 3..5 && col >= 3 && col < 6) {
            return 4
        } else if (row in 3..5 && col >= 6 && col < 9) {
            return 5
        } else if (row in 6..8 && col >= 0 && col < 3) {
            return 6
        } else if (row in 6..8 && col >= 3 && col < 6) {
            return 7
        } else if (row in 6..8 && col >= 6 && col < 9) {
            return 8
        }
        return 0
    }
}