package com.roman.gurdan.sudo.pro.util;

import com.tencent.mmkv.MMKV;

public class CacheId {

    public static final String WEEKLY_START_POINT = "_weekly_start_point_";

    public static final String SETTING_HIGH_LIGHT_SAME_VALUE = "high_light_same_value";
    public static final String SETTING_HIGH_LIGHT_SAME_ROW_COLUMN = "high_light_same_row_column";
    public static final String SETTING_HIGH_LIGHT_SAME_GROUP = "high_light_same_group";
    public static final String SETTING_HIGH_LIGHT_ERROR_VALUE = "high_light_error_value";


    public static MMKV getCache(){
        return MMKV.defaultMMKV();
    }

}
