package com.roman.garden.sudo.base.util

enum class GameSize constructor(val tag:Int, val value:Int) {

    SIZE_FOUR(4, 4),
    SIZE_SIX(6, 6),
    SIZE_EIGHT(8, 8),
    SIZE_NINE(9, 9),
    SIZE_FLOWER(1, 21),
    SIZE_WINDMILL(2, 21);

    companion object {
        fun getGameSize(tag: Int): GameSize {
            return when (tag) {
                SIZE_FOUR.tag -> SIZE_FOUR
                SIZE_SIX.tag -> SIZE_SIX
                SIZE_EIGHT.tag -> SIZE_EIGHT
                SIZE_NINE.tag -> SIZE_NINE
                SIZE_FLOWER.tag -> SIZE_FLOWER
                SIZE_WINDMILL.tag -> SIZE_WINDMILL
                else -> SIZE_FOUR
            }
        }
    }


}