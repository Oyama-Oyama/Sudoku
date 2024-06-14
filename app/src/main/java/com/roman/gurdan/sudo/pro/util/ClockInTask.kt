package com.roman.gurdan.sudo.pro.util

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.roman.gurdan.sudo.pro.game.util.LogUtil
import java.util.*

class ClockInTask(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {


    override fun doWork(): Result {
        try {
            LogUtil.e("loopClockInTask start")
            when (ClockInUtil.hasClockInYet()) {
                true -> {
                    LogUtil.e("loopClockInTask already")
                    Result.success()
                }
                false -> {
                    val hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    LogUtil.e("loopClockInTask run hour:" + hours)
                    return when (hours <= 7) {
                        true -> Result.retry()
                        false -> {
                            ClockInUtil.notifyClockIn(applicationContext)
                            Result.success()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            LogUtil.e("loopClockInTask err:" + e.message)
        }
        return Result.success()
    }


}