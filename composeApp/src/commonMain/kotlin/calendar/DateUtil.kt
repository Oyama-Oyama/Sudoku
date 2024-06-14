package calendar

import composesudoku.composeapp.generated.resources.Res
import composesudoku.composeapp.generated.resources.fri
import composesudoku.composeapp.generated.resources.mon
import composesudoku.composeapp.generated.resources.sat
import composesudoku.composeapp.generated.resources.sun
import composesudoku.composeapp.generated.resources.thur
import composesudoku.composeapp.generated.resources.tues
import composesudoku.composeapp.generated.resources.unknown
import composesudoku.composeapp.generated.resources.wed
import daysUntil
import getCurDayOfMonth
import getCurMonth
import getCurYear
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import plus

data class Day(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val dayOfWeek: Int // 周一为1，周日为7
)

fun Day.copy(dayOfMonth: Int): Day {
    return Day(this.year, this.month, dayOfMonth, this.dayOfWeek)
}

fun Day.plus(number: Int, value: Int): Day {
    return plus(this, number, value)
}

fun Day.daysUntil(target: Day): Int {
    return daysUntil(this, target)
}

fun Day.str(): String {
    return "${this.year}_${this.month}_${this.dayOfMonth}"
}

data class Months(
    val year: Int,
    val month: Int
)

class DateUtil {

    companion object {

        fun getMonths(): List<Months> {
            val year = getCurYear()
            val month = getCurMonth()
            var startYear = 2023
            var startMonth = 1
            val result: MutableList<Months> = mutableListOf()
            while (startYear <= year) {
                val cur = Months(startYear, startMonth)
                result.add(cur)
                startMonth++
                if (startMonth > 12) {
                    startMonth = 1
                    startYear++
                }
                if (startYear == year && startMonth > month) {
                    break
                }
            }
            return result
        }

        @OptIn(ExperimentalResourceApi::class)
        fun dayOfWeek(h: Int): StringResource {
            return when (h) {
                0 -> Res.string.sun
                1 -> Res.string.mon
                2 -> Res.string.tues
                3 -> Res.string.wed
                4 -> Res.string.thur
                5 -> Res.string.fri
                6 -> Res.string.sat

                else -> Res.string.unknown
            }
        }

        private fun daysInMonth(year: Int, month: Int): Int {
            val daysInMonth = when (month) {
                1, 3, 5, 7, 8, 10, 12 -> 31
                4, 6, 9, 11 -> 30
                2 -> if (isLeapYear(year)) 29 else 28
                else -> throw IllegalArgumentException("Invalid month: $month")
            }
            return daysInMonth
        }

        fun calculateMonthDays(year: Int, month: Int): MutableList<Day> {
            val daysInMonth = daysInMonth(year, month)
            return MutableList(daysInMonth) { day ->
                val dayOfMonth = day + 1
                val dayOfWeek = calculateDayOfWeek(year, month, dayOfMonth)
                Day(year, month, dayOfMonth, dayOfWeek)
            }
        }

        fun getMonthDays(year: Int, month: Int): List<Day> {
            val list: MutableList<Day> = calculateMonthDays(year, month)
            val day = list.get(0)
            val offset = 7 - (7 - day.dayOfWeek)
            val offsets = MutableList(offset) {
                Day(-1, -1, -1, -1)
            }
            offsets.addAll(list)
            return offsets
        }

        private fun isLeapYear(year: Int): Boolean {
            return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
        }

        private fun calculateDayOfWeek(year: Int, month: Int, day: Int): Int {
            // 蔡勒公式（Zeller's congruence）计算星期
            var y = year % 100
            var c = year / 100
            var m = month
            var d = day
            if (m == 1 || m == 2) {
                y--
                m += 12
            }

            var w = y + y / 4 + c / 4 - 2 * c + 13 * (m + 1) / 5 + d - 1
            while (w < 0) w += 7
            return w % 7
        }

        fun isFutureDay(day: Day): Boolean {
            val year = getCurYear()
            val month = getCurMonth()
            val dayOfMonth = getCurDayOfMonth()
            if (day.year < year) return true
            if (day.month < month) return true
            if (day.month == month && day.dayOfMonth <= dayOfMonth) return true
            return false
        }

        fun getSelectedDay(months: Months? = null): Day {
            if (months == null) {
                val year = getCurYear()
                val month = getCurMonth()
                val dayOfMonth = getCurDayOfMonth()
                val dayOfWeek = calculateDayOfWeek(year, month, dayOfMonth)
                return Day(year, month, dayOfMonth, dayOfWeek)
            } else {
                val year = getCurYear()
                val month = getCurMonth()
                var dayOfMonth:Int
                var dayOfWeek:Int
                if (year == months.year && month == months.month) {
                    dayOfMonth = getCurDayOfMonth()
                    dayOfWeek = calculateDayOfWeek(year, month, dayOfMonth)
                    return Day(year, month, dayOfMonth, dayOfWeek)
                }
                dayOfMonth = daysInMonth(months.year, months.month)
                dayOfWeek = calculateDayOfWeek(months.year, months.month, dayOfMonth)
                return Day(months.year, months.month, dayOfMonth, dayOfWeek)
            }
        }

        fun today(): Day {
            val year = getCurYear()
            val month = getCurMonth()
            val dayOfMonth = getCurDayOfMonth()
            val dayOfWeek = calculateDayOfWeek(year, month, dayOfMonth)
            return Day(year, month, dayOfMonth, dayOfWeek)
        }

    }


}