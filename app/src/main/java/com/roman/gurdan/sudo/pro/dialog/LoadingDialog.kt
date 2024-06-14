package com.roman.gurdan.sudo.pro.dialog

import android.content.Context
import android.widget.TextView
import androidx.annotation.StringRes
import com.roman.gurdan.sudo.pro.R

class LoadingDialog(context: Context) : BaseDialog(context) {

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.dialog_loading)
    }

    override fun bindView() {

    }

    fun setMessage(@StringRes str:Int){
        findViewById<TextView>(R.id.msg)?.setText(str)
    }

}