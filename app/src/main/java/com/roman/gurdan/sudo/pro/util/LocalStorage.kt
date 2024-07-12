package com.roman.gurdan.sudo.pro.util

import com.roman.gurdan.sudo.pro.game.util.Difficulty
import com.roman.gurdan.sudo.pro.game.util.GameSize
import com.tencent.mmkv.MMKV
import java.util.Date


data class GameRecord(val gameSize: GameSize, val difficulty: Difficulty, val duration: Long)
data class DateGameRecord(val gameSize: GameSize, val difficulty: Difficulty, val duration: Long)



class LocalStorage {

    companion object {
        const val WEEKLY_START_POINT = "_weekly_start_point_"
        const val SETTING_HIGH_LIGHT_SAME_VALUE = "high_light_same_value"
        const val SETTING_HIGH_LIGHT_SAME_ROW_COLUMN = "high_light_same_row_column"
        const val SETTING_HIGH_LIGHT_SAME_GROUP = "high_light_same_group"
        const val SETTING_HIGH_LIGHT_ERROR_VALUE = "high_light_error_value"
        const val STAR_COUNT = "star_count"
        const val DEFAULT_STAR_COUNT = 50


        fun getImpl(): MMKV = MMKV.defaultMMKV()

        fun encode(key: String, value: Int) = getImpl().encode(key, value)

        fun decode(key: String, defaultValue: Int): Int = getImpl().decodeInt(key, defaultValue)

        fun encode(key: String, value: String) = getImpl().encode(key, value)

        fun decode(key: String, defaultValue: String): String? =
            getImpl().decodeString(key, defaultValue)

        fun encode(key: String, value: Float) = getImpl().encode(key, value)

        fun decode(key: String, defaultValue: Float): Float =
            getImpl().decodeFloat(key, defaultValue)

        fun encode(key: String, value: Boolean) = getImpl().encode(key, value)

        fun decode(key: String, defaultValue: Boolean): Boolean =
            getImpl().decodeBool(key, defaultValue)

        fun encode(key: String, value: Long) = getImpl().encode(key, value)

        fun decode(key: String, defaultValue: Long): Long = getImpl().decodeLong(key, defaultValue)

    }


}