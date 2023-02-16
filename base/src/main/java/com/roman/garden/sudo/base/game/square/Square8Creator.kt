package com.roman.garden.sudo.base.game.square

import com.roman.garden.sudo.base.util.GameSize

internal class Square8Creator constructor(gameSize: GameSize) : SquareGameCreator(gameSize) {
    override fun calGroup(row: Int, col: Int): Int {
        if (row in 0..1 && col >= 0 && col < 4) {
            return 0
        } else if (row in 0..1 && col >= 4 && col < 8) {
            return 1
        } else if (row in 2..3 && col >= 0 && col < 4) {
            return 2
        } else if (row in 2..3 && col >= 4 && col < 8) {
            return 3
        } else if (row in 4..5 && col >= 0 && col < 4) {
            return 4
        } else if (row in 4..5 && col >= 4 && col < 8) {
            return 5
        } else if (row in 6..7 && col >= 0 && col < 4) {
            return 6
        } else if (row in 6..7 && col >= 4 && col < 8) {
            return 7
        }
        return 0
    }
}