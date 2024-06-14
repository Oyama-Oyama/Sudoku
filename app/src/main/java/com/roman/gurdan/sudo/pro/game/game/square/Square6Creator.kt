package com.roman.gurdan.sudo.pro.game.game.square

import com.roman.gurdan.sudo.pro.game.util.GameSize

internal class Square6Creator constructor(gameSize: GameSize) : SquareGameCreator(gameSize){
    override fun calGroup(row: Int, col: Int): Int {
        if (row in 0..1 && col >= 0 && col < 3) {
            return 0
        } else if (row in 0..1 && col >= 3 && col < 6) {
            return 1
        } else if (row in 2..3 && col >= 0 && col < 3) {
            return 2
        } else if (row in 2..3 && col >= 3 && col < 6) {
            return 3
        } else if (row in 4..5 && col >= 0 && col < 3) {
            return 4;
        } else if (row in 4..5 && col >= 3 && col < 6) {
            return 5
        }
        return 0
    }
}