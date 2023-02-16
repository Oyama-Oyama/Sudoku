package com.roman.garden.sudo.base.util

import java.util.*


enum class Difficulty constructor(val value: Int) {

    RANDOM(0),
    EASY(1),
    MEDIUM(2),
    HARD(3);

    companion object {
        private val randomArea = listOf<Float>(0.04f, 0.12f, 1.0f)

        fun getDifficulty(diff: Int): Difficulty {
            return when (diff) {
                EASY.value -> EASY
                MEDIUM.value -> MEDIUM
                HARD.value -> HARD
                else -> MEDIUM
            }
        }

        fun randDifficulty(): Difficulty {
            val rand = Random().nextFloat()
            randomArea.forEachIndexed { index, value ->
                if (rand <= value) return getDifficulty(index + 1)
            }
            return MEDIUM
        }

    }


}