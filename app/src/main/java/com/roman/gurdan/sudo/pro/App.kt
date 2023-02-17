package com.roman.gurdan.sudo.pro

import android.app.Application
import com.roman.garden.core.Easy
import com.roman.gurdan.sudo.pro.util.DateUtil

open class App : Application() {


    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Easy.instance.init(this, false)
        DateUtil.resetWeekTag()

    }

}