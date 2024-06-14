import calendar.Day
import java.util.Calendar

actual fun getCurDate(): String{
    return "${getCurYear()}_${getCurMonth()}_${getCurDayOfMonth()}"
}

actual fun getCurYear(): Int {
    return Calendar.getInstance().get(Calendar.YEAR)
}

actual fun getCurMonth(): Int {
    return Calendar.getInstance().get(Calendar.MONTH) + 1
}

actual fun getCurDayOfMonth(): Int {
    return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
}

actual fun plus(cur: Day, number: Int, type: Int): Day {
    val calendar: Calendar = Calendar.getInstance()
    calendar.set(cur.year, cur.month, cur.dayOfMonth)
    if (type == 1) {
        calendar.add(number, Calendar.DAY_OF_YEAR)
    } else if (type == 7) {
        calendar.add(number, Calendar.WEEK_OF_YEAR)
    } else {
        calendar.add(number, Calendar.MONTH)
    }
    return Day(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH) + 1,
        calendar.get(Calendar.DAY_OF_WEEK)
    )
}

actual fun daysUntil(cur: Day, target: Day): Int {
    val calendar: Calendar = Calendar.getInstance()
    calendar.set(cur.year, cur.month, cur.dayOfMonth)
    val tCalendar: Calendar = Calendar.getInstance()
    tCalendar.set(target.year, target.month, target.dayOfMonth)

    return ((tCalendar.time.time - calendar.time.time) / (1000 * 60 * 60 * 24)).toInt()
}