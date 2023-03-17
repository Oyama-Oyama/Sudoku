package com.roman.garden.sudo.base.util

import com.roman.garden.base.BaseImpl
import java.util.*


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

        fun randDifficulty(tag: Int): Difficulty {
            val rand = Random().nextFloat()
            val mkv = BaseImpl.getDefaultCacheInstance()
            val keyEasy = "${tag}_easy"
            val keyMedium = "${tag}_medium"
            var preEasy = mkv.decodeFloat(keyEasy, base_easy)
            var preMedium = mkv.decodeFloat(keyMedium, base_medium)
            if (rand <= preEasy) return EASY
            if (rand <= preMedium) return MEDIUM
            return HARD
        }

        fun resetDifficulty(diff: Difficulty, tag: Int) {
            val mkv = BaseImpl.getDefaultCacheInstance()
            val keyEasy = "${tag}_easy"
            val keyMedium = "${tag}_medium"
            var preEasy = mkv.decodeFloat(keyEasy, base_easy)
            var preMedium = mkv.decodeFloat(keyMedium, base_medium)
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
            mkv.encode(keyEasy, preEasy)
            mkv.encode(keyMedium, preMedium)
        }

    }


}