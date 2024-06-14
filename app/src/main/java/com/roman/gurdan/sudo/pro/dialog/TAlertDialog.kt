package com.roman.gurdan.sudo.pro.dialog

import android.content.Context
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.roman.gurdan.sudo.pro.R

open class TAlertDialog : BaseDialog {

    constructor(context: Context, @LayoutRes layoutId:Int = -1) : super(context) {
        when(layoutId == -1){
            true -> setContentView(R.layout.dialog_tool)
            else -> setContentView(layoutId)
        }
    }

    override fun bindView() {

    }

    fun setTitle(str: String, @ColorInt color: Int = -1) {
        findViewById<TextView>(R.id.title)?.let {
            it.text = str
            if (color != -1) it.setTextColor(color)
        }
    }

    fun setTitleId(@StringRes str: Int, @ColorInt color: Int = -1) {
        findViewById<TextView>(R.id.title)?.let {
            it.setText(str)
            if (color != -1) it.setTextColor(color)
        }
    }

    fun setContent(str: String, @ColorInt color: Int = -1) {
        findViewById<TextView>(R.id.content)?.let {
            it.text = str
            if (color != -1) it.setTextColor(color)
        }
    }

    fun setContentId(@StringRes str: Int, @ColorInt color: Int = -1) {
        findViewById<TextView>(R.id.content)?.let {
            it.setText(str)
            if (color != -1) it.setTextColor(color)
        }
    }

    fun setActive(
        str: String,
        listener: IEvent? = null,
        @ColorInt color: Int = -1,
        duration: Long = 0
    ) {
        findViewById<TextView>(R.id.active)?.let {
            it.text = str
            it.setOnClickListener {
                listener?.onEvent(this@TAlertDialog) ?: dismiss()
            }
            if (color != -1) it.setTextColor(color)
            when (duration > 0) {
                true -> {
                    it.isClickable = false
                    object : CountDownTimer(duration, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            it.text = "${str}(${millisUntilFinished / 1000})"
                        }

                        override fun onFinish() {
                            it.text = str
                            it.isClickable = true
                        }
                    }.start()
                }
                else -> it.isClickable = true
            }
        }
    }

    fun setActiveId(
        @StringRes str: Int,
        listener: IEvent? = null,
        @ColorInt color: Int = -1,
        duration: Long = 0
    ) {
        findViewById<TextView>(R.id.active)?.let {
            it.setText(str)
            it.setOnClickListener {
                listener?.onEvent(this@TAlertDialog) ?: dismiss()
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

    fun setInActive(
        str: String,
        listener: IEvent? = null,
        @ColorInt color: Int = -1,
        duration: Long = 0
    ) {
        findViewById<TextView>(R.id.inactive)?.let {
            it.text = str
            it.setOnClickListener {
                listener?.onEvent(this@TAlertDialog) ?: dismiss()
            }
            if (color != -1) it.setTextColor(color)
            when (duration > 0) {
                true -> {
                    it.isClickable = false
                    object : CountDownTimer(duration, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            it.text = "${str}(${millisUntilFinished / 1000})"
                        }

                        override fun onFinish() {
                            it.text = str
                            it.isClickable = true
                        }
                    }.start()
                }
                else -> it.isClickable = true
            }
        }
    }

    fun setInActiveId(
        @StringRes str: Int,
        listener: IEvent? = null,
        @ColorInt color: Int = -1,
        duration: Long = 0
    ) {
        findViewById<TextView>(R.id.inactive)?.let {
            it.setText(str)
            it.setOnClickListener {
                listener?.onEvent(this@TAlertDialog) ?: dismiss()
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

    fun hideInActive() {
        findViewById<TextView>(R.id.inactive)?.let {
            it.visibility = View.GONE
        }
        findViewById<TextView>(R.id.divider1)?.let {
            it.visibility = View.GONE
        }
    }

}