package com.roman.gurdan.sudo.pro.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.roman.gurdan.sudo.pro.R

class DataView(context: Context, attrs: AttributeSet?) :
    CardView(context, attrs) {

    private lateinit var title: TextView
    private lateinit var msg: TextView
    private var titleRes: Int = 0
    private var msgRes: Int = 0

    init {
        context.obtainStyledAttributes(attrs, R.styleable.DataView).apply {
            titleRes = this.getResourceId(R.styleable.DataView_vTitle, R.string.nan)
            msgRes = this.getColor(
                R.styleable.DataView_vMsgColor,
                context.resources.getColor(R.color.teal_200)
            )
            this.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        try {
            View.inflate(context, R.layout.view_data_item, this)
            title = findViewById(R.id.title)
            msg = findViewById(R.id.msg)

            title.setText(titleRes)
            msg.setTextColor(msgRes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setMsg(data: String) {
        try {
            this.msg.text = data
        } catch (e: Exception) {
        }
    }


}