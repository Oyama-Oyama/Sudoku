package com.roman.garden.sudo.base.util

import android.util.Log

class LogUtil {

    companion object {
        private val TAG: String = "SUDUKO"

        fun v(msg: String?) = Log.v(TAG, "$msg")

        fun d(msg: String?) = Log.d(TAG, "$msg")

        fun e(msg: String?) = Log.e(TAG, "$msg")

        fun w(msg: String?) = Log.w(TAG, "$msg")

    }

}