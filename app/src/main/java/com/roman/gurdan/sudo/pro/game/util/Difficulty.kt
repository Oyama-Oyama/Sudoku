package com.roman.gurdan.sudo.pro.game.util

import com.roman.gurdan.sudo.pro.util.LocalStorage
import java.util.Random

enum class Difficulty constructor(val value: Int) {

    RANDOM(0),
    EASY(1),
    MEDIUM(2),
    HARD(3),
    EXPERT(4);

    companion object {
        const val base_easy: Float = 0.32f
        const val base_medium: Float = 0.6f
        const val base_HARD: Float = 0.84f

        fun getDifficulty(diff: Int, tag: Int? = 0): Difficulty {
            return when (diff) {
                EASY.value -> EASY
                MEDIUM.value -> MEDIUM
                HARD.value -> HARD
                EXPERT.value -> EXPERT
                RANDOM.value -> randDifficulty(tag)
                else -> MEDIUM
            }
        }

        fun randDifficulty(tag: Int? = 0): Difficulty {
            val rand = Random().nextFloat()
            val keyEasy = "${tag}_easy"
            val keyMedium = "${tag}_medium"
            val keyHard = "${tag}_hard"
            val preEasy = LocalStorage.decode(keyEasy, base_easy)
            val preMedium = LocalStorage.decode(keyMedium, base_medium)
            val preHard = LocalStorage.decode(keyHard, base_HARD)
            if (rand <= preEasy) return EASY
            if (rand <= preMedium) return MEDIUM
            if (rand <= preHard) return HARD
            return EXPERT
        }

        fun resetDifficulty(diff: Difficulty, tag: Int) {
            val keyEasy = "${tag}_easy"
            val keyMedium = "${tag}_medium"
            val keyHard = "${tag}_hard"
            var preEasy = LocalStorage.decode(keyEasy, base_easy)
            var preMedium = LocalStorage.decode(keyMedium, base_medium)
            var preHard = LocalStorage.decode(keyHard, base_HARD)
            when (diff) {
                EASY -> {
                    preEasy = preEasy * 1.9f / 2
                    preMedium = preMedium * 1.9f / 2
                    preHard = preHard * 1.9f / 2
                }

                MEDIUM -> {
                    preEasy = preEasy * 1.7f / 2
                    preMedium = preMedium * 1.7f / 2
                    preHard = preHard * 1.7f / 2
                }

                HARD -> {
                    preEasy = preEasy * 1.6f / 2
                    preMedium = preMedium * 1.6f / 2
                    preHard = preHard * 1.6f / 2
                }

                else -> {
                    preEasy = preEasy * 1.5f / 2
                    preMedium = preMedium * 1.5f / 2
                    preHard = preHard * 1.5f / 2
                }
            }
            LocalStorage.encode(keyEasy, preEasy)
            LocalStorage.encode(keyMedium, preMedium)
            LocalStorage.encode(keyHard, preHard)
        }

    }


}