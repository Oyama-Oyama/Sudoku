package com.roman.garden.sudo.base.util

enum class GameSize constructor(val value: Int) {

    SIZE_FOUR(4),
    SIZE_SIX(6),
    SIZE_EIGHT(8),
    SIZE_NINE(9);


    companion object {
        fun getGameSize(s: Int): GameSize {
            return when (s) {
                SIZE_FOUR.value -> SIZE_FOUR
                SIZE_SIX.value -> SIZE_SIX
                SIZE_EIGHT.value -> SIZE_EIGHT
                SIZE_NINE.value -> SIZE_NINE
                else -> SIZE_FOUR
            }
        }
    }


}