package com.roman.gurdan.sudo.pro.util

import com.tencent.mmkv.MMKV

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


        fun addStar(num: Int): Int {
            var count = getImpl().decodeInt(STAR_COUNT, DEFAULT_STAR_COUNT)
            count += num
            if (count < 0) count = 0
            getImpl().encode(STAR_COUNT, count)
            return count
        }

        fun hasStar(num: Int): Boolean {
            return getImpl().decodeInt(STAR_COUNT, DEFAULT_STAR_COUNT) > num
        }

    }


}