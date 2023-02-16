package com.roman.gurdan.sudo.pro.util

import com.roman.garden.base.log.Logger
import java.util.*

class DateUtil {

    companion object {

        fun getDate(c: Calendar): String =
            "${c.get(Calendar.YEAR)}-${(c.get(Calendar.MONTH) + 1)}-${c.get(Calendar.DAY_OF_MONTH)}"

        fun getDate(): String = getDate(Calendar.getInstance())

        //前或后 count  天，  YYYY-MM-dd
        fun getDate(count: Int): String {
            val c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, count);
            return getDate(c);
        }

        fun getWeek(): Array<Pair<String, String>> {
            val c = Calendar.getInstance()
            val day = c.get(Calendar.DAY_OF_WEEK) - 1
            c.add(Calendar.DAY_OF_MONTH, -1 * day)
            return Array<Pair<String, String>>(7) { i ->
                val dayT = c.get(Calendar.DAY_OF_MONTH)
                val date = getDate(c)
                c.add(Calendar.DAY_OF_MONTH, 1)
                return@Array Pair(date, if (dayT > 9) dayT.toString() else "0$dayT")
            }
        }

        fun getWeeklyTag(): Int {
            val preRecord = Cache.getCache().decodeLong(Cache.WEEKLY_START_POINT, -1)
            val current = GregorianCalendar()
            val previous = GregorianCalendar()
            previous.timeInMillis = preRecord
            return ((current.timeInMillis - previous.timeInMillis) / (1000.0f * 2600 * 24) / 7.0f + 1).toInt()
        }

        fun resetWeekTag() {
            val preRecord = Cache.getCache().decodeLong(Cache.WEEKLY_START_POINT, -1)
            if (preRecord <= 0) {
                val c = Calendar.getInstance()
                val day = c.get(Calendar.DAY_OF_WEEK) - 1
                c.add(Calendar.DAY_OF_MONTH, -1 * day)
                Cache.getCache().encode(Cache.WEEKLY_START_POINT, c.timeInMillis)
            }
        }

        fun millSecondToDate(millSecond: Long): String {
            val seconds = millSecond / 1000
            val second = seconds % 60
            val minutes = seconds / 60 % 60
            val hours = seconds / 60 / 60
            return (if (hours > 9) "$hours" else "0$hours") + ":" +
                    (if (minutes > 9) "$minutes" else "0$minutes") + ":" +
                    (if (second > 9) "$second" else "0$second")
        }

    }

}