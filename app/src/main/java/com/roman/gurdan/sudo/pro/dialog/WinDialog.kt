package com.roman.gurdan.sudo.pro.dialog

import android.content.Context
import android.widget.TextView
import com.roman.gurdan.sudo.pro.R

class WinDialog(context: Context, layoutId: Int = -1) : TAlertDialog(context, layoutId) {


    fun setStarCount(num: Int) {
        findViewById<TextView>(R.id.starCount)?.let {
            it.text = "x $num"
        }
    }

}