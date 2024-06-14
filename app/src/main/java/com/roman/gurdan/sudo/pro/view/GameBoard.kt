package com.roman.gurdan.sudo.pro.view

import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.roman.gurdan.sudo.pro.R

class GameBoard(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val textViews = arrayOfNulls<TextView>(9)
    var listener: IGameMenuListener<Int>? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        textViews[0] = findViewById(R.id.num1)
        textViews[0]?.setOnClickListener(clickListener)
        textViews[1] = findViewById(R.id.num2)
        textViews[1]?.setOnClickListener(clickListener)
        textViews[2] = findViewById(R.id.num3)
        textViews[2]?.setOnClickListener(clickListener)
        textViews[3] = findViewById(R.id.num4)
        textViews[3]?.setOnClickListener(clickListener)
        textViews[4] = findViewById(R.id.num5)
        textViews[4]?.setOnClickListener(clickListener)
        textViews[5] = findViewById(R.id.num6)
        textViews[5]?.setOnClickListener(clickListener)
        textViews[6] = findViewById(R.id.num7)
        textViews[6]?.setOnClickListener(clickListener)
        textViews[7] = findViewById(R.id.num8)
        textViews[7]?.setOnClickListener(clickListener)
        textViews[8] = findViewById(R.id.num9)
        textViews[8]?.setOnClickListener(clickListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener = null
    }

    fun updateGameSize(size: Int) {
        this.post {
            textViews.forEachIndexed { index, view ->
                view?.visibility = if (index >= size) GONE else VISIBLE
            }
        }
    }

    private val clickListener = OnClickListener { view ->
        try {
            val num = (view as? TextView)?.let { it.text }
            listener?.onMenuItem(num.toString().toInt())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}