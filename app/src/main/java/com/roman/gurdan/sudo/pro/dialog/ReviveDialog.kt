package com.roman.gurdan.sudo.pro.dialog

import android.content.Context
import android.os.CountDownTimer
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.roman.gurdan.sudo.pro.R

class ReviveDialog(context: Context, layoutId: Int = -1) : TAlertDialog(context, layoutId) {


    fun setQuit(
        @StringRes str: Int,
        listener: IEvent? = null,
        @ColorInt color: Int = -1,
        duration: Long = 0
    ) {
        findViewById<TextView>(R.id.quit)?.let {
            it.setText(str)
            it.setOnClickListener {
                listener?.onEvent(this@ReviveDialog) ?: dismiss()
            }
            if (color != -1) it.setTextColor(color)
            when (duration > 0) {
                true -> {
                    it.isClickable = false
                    object : CountDownTimer(duration, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            it.text = "${context.getString(str)}(${millisUntilFinished / 1000})"
                        }

                        override fun onFinish() {
                            it.setText(str)
                            it.isClickable = true
                        }
                    }.start()
                }
                else -> it.isClickable = true
            }
        }
    }

}