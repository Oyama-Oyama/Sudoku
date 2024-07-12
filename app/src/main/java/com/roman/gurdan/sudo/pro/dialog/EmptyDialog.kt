package com.roman.gurdan.sudo.pro.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class EmptyDialog(context: Context) : BaseDialog(context) {

    fun setLayout(layoutRes: Int): EmptyDialog {
        setContentView(layoutRes)
        return this
    }

    fun cancelable(able: Boolean): EmptyDialog {
        this.setCancelable(able)
        this.setCanceledOnTouchOutside(able)
        return this
    }

    fun bindClickEvent(itemId: Int, onClick: (view: View, dialog: Dialog) -> Unit): EmptyDialog {
        this.findViewById<ViewGroup>(itemId)?.setOnClickListener { v ->
            onClick.invoke(v, this@EmptyDialog)
        }
        return this
    }

    fun bindTextView(
        itemId: Int,
        strRes: Int = -1,
        onClick: ((view: View, dialog: Dialog) -> Unit)? = null
    ): EmptyDialog {
        this.findViewById<TextView>(itemId)?.apply {
            if (strRes != -1) this.text = context.getString(strRes)
            onClick?.let {
                this.setOnClickListener { v ->
                    it.invoke(v, this@EmptyDialog)
                }
            }
        }
        return this
    }

    fun bindTextView(
        itemId: Int,
        strRes: String? = null,
        onClick: ((view: View, dialog: Dialog) -> Unit)? = null
    ): EmptyDialog {
        this.findViewById<TextView>(itemId)?.apply {
            if (!strRes.isNullOrEmpty()) {
                this.text = strRes
            }
            onClick?.let {
                this.setOnClickListener { v ->
                    it.invoke(v, this@EmptyDialog)
                }
            }
        }
        return this
    }

    fun bindButtonText(itemId: Int, strRes: Int): EmptyDialog {
        this.findViewById<Button>(itemId)?.text = context.getString(strRes)
        return this
    }

    fun bindImage(itemId: Int, imgRes: Int): EmptyDialog {
        this.findViewById<ImageView>(itemId)?.setImageResource(imgRes)
        return this
    }

    fun listenDismiss(listener: DialogInterface.OnDismissListener? = null): EmptyDialog {
        this.setOnDismissListener(listener)
        return this
    }

    override fun bindView() {

    }


}