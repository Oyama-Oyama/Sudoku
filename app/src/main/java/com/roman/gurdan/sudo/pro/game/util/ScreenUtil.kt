package com.roman.gurdan.sudo.pro.game.util

import android.content.Context

class ScreenUtil {

    companion object {
        fun getScreenWidth(context: Context): Int = context.resources.displayMetrics.widthPixels
    }

}