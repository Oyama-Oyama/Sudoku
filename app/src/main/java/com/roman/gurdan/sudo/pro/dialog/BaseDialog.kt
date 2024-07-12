package com.roman.gurdan.sudo.pro.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.view.Window

abstract class BaseDialog : Dialog {

    constructor(context: Context):super(context){

    }

//    protected var listener: IDialogEvent? = null
//
//    fun setListener(listener: IDialogEvent) {
//        this.listener = listener
//    }

    override fun setContentView(layoutResID: Int) {
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        super.setContentView(layoutResID)
        val params = window?.attributes
        params?.gravity = Gravity.CENTER
        params?.width = (context.resources.displayMetrics.widthPixels * 0.9).toInt()
        params?.height = LayoutParams.WRAP_CONTENT
        window?.attributes = params
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bindView()
    }

    abstract fun bindView()


}