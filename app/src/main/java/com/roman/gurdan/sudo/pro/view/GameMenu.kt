package com.roman.gurdan.sudo.pro.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.roman.gurdan.sudo.pro.R

class GameMenu(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {
    var listener: IGameMenuListener<Int>? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        findViewById<MaterialButton>(R.id.menu1).setOnClickListener {
            listener?.onMenuItem(0)
        }
        findViewById<MaterialButton>(R.id.menu2).setOnClickListener {
            listener?.onMenuItem(1)
        }
        findViewById<MaterialButton>(R.id.menu3).setOnClickListener {
            listener?.onMenuItem(2)
        }
        findViewById<MaterialButton>(R.id.menu4).setOnClickListener {
            listener?.onMenuItem(3)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener = null
    }

    fun updateNoteStatus(isNoteOn: Boolean) {
        findViewById<MaterialButton>(R.id.menu3).setBackgroundColor(
            if (isNoteOn) context.resources.getColor(R.color.teal_200) else context.resources.getColor(
                R.color.secondColor
            )
        )
    }

}