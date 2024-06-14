import calendar.Day

expect fun getCurDate(): String

expect fun getCurYear(): Int

expect fun getCurMonth(): Int

expect fun getCurDayOfMonth(): Int

expect fun plus(cur: Day, number: Int, type: Int): Day

expect fun daysUntil(cur: Day, target: Day): Int