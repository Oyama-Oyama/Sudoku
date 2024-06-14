package com.roman.gurdan.sudo.pro.dialog

import android.animation.Animator
import android.content.Context
import android.widget.ImageView
import com.roman.garden.base.util.GpUtil
import com.roman.gurdan.sudo.pro.R

class RateDialog(context: Context) : BaseDialog(context) {

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.dialog_rate)
    }

    override fun bindView() {

        findViewById<ImageView>(R.id.rate).setOnClickListener {
            GpUtil().openPlayStore(context, context.packageName)
            dismiss()
        }
        val close = findViewById<ImageView>(R.id.close)
        close.alpha = 0.0f
        close.animate()
            .alphaBy(1.0f)
            .setDuration(500)
            .setStartDelay(1000)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    close.isClickable = false
                }

                override fun onAnimationEnd(animation: Animator) {
                    close.isClickable = true
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            }).start()
        close.setOnClickListener {
            dismiss()
        }
    }

}