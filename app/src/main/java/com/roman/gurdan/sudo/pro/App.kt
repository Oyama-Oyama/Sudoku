package com.roman.gurdan.sudo.pro

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.roman.garden.core.Easy
import com.roman.gurdan.sudo.pro.util.ClockInUtil
import com.roman.gurdan.sudo.pro.util.DateUtil

open class App : Application() {

    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Easy.instance.init(this, true)
        DateUtil.resetWeekTag()
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                Easy.instance.showAppOpenAd()
            }
        })
        ClockInUtil.loopClockInTask(this)
    }

}