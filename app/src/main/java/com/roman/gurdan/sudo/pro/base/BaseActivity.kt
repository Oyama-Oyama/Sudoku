package com.roman.gurdan.sudo.pro.base

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {


    protected open fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    protected open fun toast(@StringRes msgId: Int) = Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show()


}