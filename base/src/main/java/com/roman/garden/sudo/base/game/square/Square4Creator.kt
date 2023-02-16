package com.roman.garden.sudo.base.game.square

import com.roman.garden.sudo.base.util.GameSize

internal class Square4Creator constructor(gameSize: GameSize) : SquareGameCreator(gameSize) {

    override fun calGroup(row: Int, col: Int): Int {
        if (row in 0..1 && col >= 0 && col < 2) {
            return 0
        } else if (row in 0..1 && col >= 2 && col < 4) {
            return 1
        } else if (row in 2..3 && col >= 0 && col < 2) {
            return 2
        } else if (row in 2..3 && col >= 2 && col < 4) {
            return 3
        }
        return 0
    }

}