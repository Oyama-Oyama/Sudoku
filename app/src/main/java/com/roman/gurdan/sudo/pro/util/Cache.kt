package com.roman.gurdan.sudo.pro.util

import com.tencent.mmkv.MMKV

class Cache {

    companion object {
        val WEEKLY_START_POINT = "_weekly_start_point_";
        val SETTING_HIGH_LIGHT_SAME_VALUE = "high_light_same_value";
        val SETTING_HIGH_LIGHT_SAME_ROW_COLUMN = "high_light_same_row_column";
        val SETTING_HIGH_LIGHT_SAME_GROUP = "high_light_same_group";
        val SETTING_HIGH_LIGHT_ERROR_VALUE = "high_light_error_value";


        fun getCache(): MMKV = MMKV.defaultMMKV()
    }


}