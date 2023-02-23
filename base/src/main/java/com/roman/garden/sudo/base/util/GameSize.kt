package com.roman.garden.sudo.base.util

enum class GameSize constructor(val tag: Int, val row: Int, val col: Int) {

    SIZE_FOUR(4, 4, 4),
    SIZE_SIX(6, 6, 6),
    SIZE_EIGHT(8, 8, 8),
    SIZE_NINE(9, 9, 9),
    SIZE_FLOWER(1, 21, 21),
    SIZE_CROSS(2, 21, 21),
    SIZE_BUTTERFLY(3, 9, 21),
    SIZE_WINDMILL(5, 21, 21),
    SIZE_WINDMILL_2(7, 21, 21),
    SIZE_TRIPLE(10, 15, 21),
    SIZE_TRIPLE_2(11, 12, 21),
    SIZE_STAIR(12, 18, 18),
    SIZE_STAIR_2(13, 21, 15);

    companion object {
        fun getGameSize(tag: Int): GameSize {
            return when (tag) {
                SIZE_FOUR.tag -> SIZE_FOUR
                SIZE_SIX.tag -> SIZE_SIX
                SIZE_EIGHT.tag -> SIZE_EIGHT
                SIZE_NINE.tag -> SIZE_NINE
                SIZE_FLOWER.tag -> SIZE_FLOWER
                SIZE_CROSS.tag -> SIZE_CROSS
                SIZE_BUTTERFLY.tag -> SIZE_BUTTERFLY
                SIZE_WINDMILL.tag -> SIZE_WINDMILL
                SIZE_WINDMILL_2.tag -> SIZE_WINDMILL_2
                SIZE_TRIPLE.tag -> SIZE_TRIPLE
                SIZE_TRIPLE_2.tag -> SIZE_TRIPLE_2
                SIZE_STAIR.tag -> SIZE_STAIR
                SIZE_STAIR_2.tag -> SIZE_STAIR_2
                else -> SIZE_FOUR
            }
        }
    }


}