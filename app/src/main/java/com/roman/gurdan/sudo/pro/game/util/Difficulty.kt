package com.roman.gurdan.sudo.pro.game.util

import com.roman.gurdan.sudo.pro.util.LocalStorage
import java.util.Random

enum class Difficulty constructor(val value: Int) {

    RANDOM(0),
    EASY(1),
    MEDIUM(2),
    HARD(3);

    companion object {
        const val base_easy: Float = 0.32f
        const val base_medium: Float = 0.64f

        fun getDifficulty(diff: Int): Difficulty {
            return when (diff) {
                EASY.value -> EASY
                MEDIUM.value -> MEDIUM
                HARD.value -> HARD
                else -> MEDIUM
            }
        }

        fun randDifficulty(tag: Int? = 0): Difficulty {
            val rand = Random().nextFloat()
            val keyEasy = "${tag}_easy"
            val keyMedium = "${tag}_medium"
            val preEasy = LocalStorage.decode(keyEasy, base_easy)
            val preMedium = LocalStorage.decode(keyMedium, base_medium)
            if (rand <= preEasy) return EASY
            if (rand <= preMedium) return MEDIUM
            return HARD
        }

        fun resetDifficulty(diff: Difficulty, tag: Int) {
            val keyEasy = "${tag}_easy"
            val keyMedium = "${tag}_medium"
            var preEasy = LocalStorage.decode(keyEasy, base_easy)
            var preMedium = LocalStorage.decode(keyMedium, base_medium)
            when (diff) {
                EASY -> {
                    preEasy = preEasy * 1.5f / 2
                    preMedium = preMedium * 1.5f / 2
                }

                MEDIUM -> {
                    preEasy = preEasy * 1.4f / 2
                    preMedium = preMedium * 1.4f / 2
                }

                else -> {
                    preEasy = preEasy * 1.2f / 2
                    preMedium = preMedium * 1.2f / 2
                }
            }
            LocalStorage.encode(keyEasy, preEasy)
            LocalStorage.encode(keyMedium, preMedium)
        }

    }


}