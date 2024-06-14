package com.roman.gurdan.sudo.pro.util

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.roman.gurdan.sudo.pro.game.util.LogUtil
import java.text.SimpleDateFormat
import java.util.*

import java.util.concurrent.TimeUnit
import kotlin.random.Random


class ClockInUtil {

    companion object {

        fun clockIn(onSuccess: (count: Int) -> Unit, onFail: () -> Unit) {
            val duration = System.currentTimeMillis()
            val last = LocalStorage.decode("lastClockInTimeInMillis", 0L)
            when (duration > last && !isSameDay(duration, last)) {
                true -> {
                    LocalStorage.encode("lastClockInTimeInMillis", duration)
                    var clockInCount = LocalStorage.decode("clockInCount", 0)

                    if (clockInCount == 7) {
                        clockInCount = 0
                    }
                    var count = 2 * clockInCount + Random.nextInt(0, 5)
                    LocalStorage.encode("clockInCount", clockInCount + 1)
                    onSuccess(count)
                }
                false -> onFail()
            }
        }

        fun isSameDay(first: Long, second: Long): Boolean {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            return dateFormat.format(first) == dateFormat.format(second)
        }

        fun hasClockInYet(): Boolean {
            val last = LocalStorage.decode("lastClockInTimeInMillis", 0L)
            val c = Calendar.getInstance()
            c.timeInMillis = last
            return DateUtil.getDate() == DateUtil.getDate(c)
        }

        fun notifyClockIn(context: Context) {
            LogUtil.e("loopClockInTask apply notification")
            NotificationUtil.sendNotification(context)
        }

        fun loopClockInTask(context: Context) {
            LogUtil.e("loopClockInTask")
            WorkManager.getInstance(context).apply {
                this.cancelAllWorkByTag("ClockInTask")
                PeriodicWorkRequest
                    .Builder(ClockInTask::class.java, 15, TimeUnit.MINUTES)
                    .addTag("ClockInTask")
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.HOURS)
                    .build().let { work ->
                        this.enqueue(work)
                    }
            }
        }


    }


}