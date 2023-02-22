package com.roman.garden.sudo.base.util

import android.content.Context

class ScreenUtil {

    companion object {
        fun getScreenWidth(context: Context): Int = context.resources.displayMetrics.widthPixels
    }

}