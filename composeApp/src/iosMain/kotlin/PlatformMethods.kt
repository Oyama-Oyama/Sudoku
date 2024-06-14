import calendar.Day
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitWeekOfYear
import platform.Foundation.NSCalendarUnitWeekday
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.timeIntervalSince1970

actual fun getCurDate(): String{
    return "${getCurYear()}_${getCurMonth()}_${getCurDayOfMonth()}"
}

actual fun getCurYear(): Int {
    val calendar = NSCalendar.currentCalendar
    return calendar.component(NSCalendarUnitYear, NSDate()).toInt()
}

actual fun getCurMonth(): Int {
    val calendar = NSCalendar.currentCalendar
    return calendar.component(NSCalendarUnitMonth, NSDate()).toInt()
}

actual fun getCurDayOfMonth(): Int {
    val calendar = NSCalendar.currentCalendar
    return calendar.component(NSCalendarUnitDay, NSDate()).toInt()
}

actual fun plus(cur: Day, number: Int, type: Int): Day {
    val dateComponents = NSDateComponents()
    dateComponents.year = cur.year.toLong()
    dateComponents.month = cur.month.toLong()
    dateComponents.day = cur.dayOfMonth.toLong()

    val calendar = NSCalendar.currentCalendar
    val date = calendar.dateFromComponents(dateComponents)
    val resultDate: NSDate?

    if (type == 1) {
        resultDate = calendar.dateByAddingUnit(
            unit = NSCalendarUnitDay,
            value = number.toLong(),
            toDate = date!!,
            options = 1u
        )
    } else if (type == 7) {
        resultDate = calendar.dateByAddingUnit(
            unit = NSCalendarUnitWeekOfYear,
            value = number.toLong(),
            toDate = date!!,
            options = 1u
        )
    } else {
        resultDate = calendar.dateByAddingUnit(
            unit = NSCalendarUnitMonth,
            value = number.toLong(),
            toDate = date!!,
            options = 1u
        )
    }

    return Day(
        calendar.component(NSCalendarUnitYear, resultDate!!).toInt(),
        calendar.component(NSCalendarUnitMonth, resultDate).toInt(),
        calendar.component(NSCalendarUnitDay, resultDate).toInt(),
        calendar.component(NSCalendarUnitWeekday, resultDate).toInt()
    )
}

actual fun daysUntil(cur: Day, target: Day): Int {
    val dateComponents = NSDateComponents()
    dateComponents.year = cur.year.toLong()
    dateComponents.month = cur.month.toLong()
    dateComponents.day = cur.dayOfMonth.toLong()

    val calendar = NSCalendar.currentCalendar
    val date = calendar.dateFromComponents(dateComponents)

    val tDateComponents = NSDateComponents()
    dateComponents.year = target.year.toLong()
    dateComponents.month = target.month.toLong()
    dateComponents.day = target.dayOfMonth.toLong()
    val tDate = calendar.dateFromComponents(tDateComponents)

    return ((date!!.timeIntervalSince1970 - tDate!!.timeIntervalSince1970) / (1000 * 60 * 60 * 24)).toInt()
}