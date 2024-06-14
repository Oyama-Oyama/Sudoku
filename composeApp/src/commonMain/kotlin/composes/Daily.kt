package composes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import calendar.CalendarAnimator
import calendar.DateUtil
import calendar.Day
import calendar.Months
import calendar.str
import com.ctrip.flight.mmkv.defaultMMKV
import composesudoku.composeapp.generated.resources.Res
import composesudoku.composeapp.generated.resources.continue_game
import composesudoku.composeapp.generated.resources.daily_challenge
import composesudoku.composeapp.generated.resources.ic_back
import composesudoku.composeapp.generated.resources.played
import composesudoku.composeapp.generated.resources.start
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Sudoku
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import theme.ThemeColors

data class SelectedDay(val day: Day, val playProgress: Float)

class DailyUtil {
    companion object {

        fun setupDailyGame(day: Day, sudoku: Sudoku, progress: Float) {
            val str = Json.encodeToString(sudoku)
            defaultMMKV().set("game_${day.str()}", str)
            defaultMMKV().set("state_${day.str()}", progress)
        }

        fun setupDailyGame(day: Day, sudoku: String, progress: Float) {
            defaultMMKV().set("game_${day.str()}", sudoku)
            defaultMMKV().set("state_${day.str()}", progress)
        }

        fun getDailyGame(day: Day): Sudoku? {
            val sudokuStr = defaultMMKV().takeString("game_${day.str()}")
            if (sudokuStr.isNotBlank()) {
                return Json.decodeFromString<Sudoku>(sudokuStr)
            }
            return null
        }

        fun getDailyGameProgress(day: Day): Float {
            val sudoku = getDailyGame(day)
            return sudoku?.let {
                if (it.board != null &&
                    it.solution != null &&
                    it.board!!.isNotBlank() &&
                    it.solution!!.isNotBlank() &&
                    it.board.equals(it.solution)
                ) 1.0f else 0.5f
            } ?: 0.0f
        }

    }
}

class CalendarViewModel private constructor() : ViewModel() {

    companion object {
        val Instance: CalendarViewModel by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { CalendarViewModel() }
    }

    private var selectedDay = MutableStateFlow<SelectedDay>(
        SelectedDay(
            day = DateUtil.getSelectedDay(),
            playProgress = 0.0f
        )
    )
    val selectedDayFlow = selectedDay.asStateFlow()

    fun updateSelectedDay(day: Day, progress: Float) {
        selectedDay.value = SelectedDay(day, progress)
    }

}

@OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class)
@Composable
fun sudokuCalendar(
    current: Day? = null,
    onQuit: () -> Unit,
    onGame: (day: Day) -> Unit,
    calendarViewModel: CalendarViewModel = CalendarViewModel.Instance
) {
    val selectedDay by calendarViewModel.selectedDayFlow.collectAsState()
//    val dailyPlayProgress = calendarViewModel.selectedDayFlow.value.playProgress
    println("playingGameConfig::selectedDay::${selectedDay.day}")
    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.ThemeColors.ScreenBackground)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onQuit) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = vectorResource(Res.drawable.ic_back),
                    contentDescription = "exit game",
                    tint = MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(Res.string.daily_challenge),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.ThemeColors.ScreenTitle
            )
        }

        Box(modifier = Modifier.fillMaxWidth().weight(1.0f)) {
            HorizontalCalendarView(
                day = current ?: DateUtil.today(),
                modifier = Modifier.fillMaxSize(),
                calendarViewModel = calendarViewModel
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth().weight(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ElevatedButton(
                onClick = { onGame(selectedDay.day) },
                enabled = true,
                shape = RoundedCornerShape(3.dp),
                modifier = Modifier.fillMaxWidth(0.65f).height(42.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.ThemeColors.continueGameButtonBackground
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 0.3.dp
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (selectedDay.playProgress == 0.0f) stringResource(Res.string.start) else if (selectedDay.playProgress == 0.5f) stringResource(
                            Res.string.continue_game
                        ) else stringResource(Res.string.played),
                        color = MaterialTheme.colorScheme.ThemeColors.continueGameButtonTitle,
                        fontSize = 16.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun HorizontalCalendarView(
    day: Day,
    modifier: Modifier = Modifier,
    pageSize: PageSize = PageSize.Fill,
    calendarViewModel: CalendarViewModel
) {
    val months by remember { mutableStateOf(DateUtil.getMonths()) }

    val pagerState: PagerState = rememberPagerState(
        initialPage = months.size,
        pageCount = { months.size }
    )
    var curMonth: Months? by remember { mutableStateOf(null) }

    val calendarAnimator: CalendarAnimator by remember {
        mutableStateOf(
            CalendarAnimator(
                day,
                months.size - 1
            )
        )
    }

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth().height(24.dp),
            text = curMonth?.let { "${it.year}-${it.month}" } ?: "",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle
        )
        Row(
            modifier = Modifier.fillMaxWidth().height(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (index in 0..<7) {
                Text(
                    modifier = Modifier.weight(1.0f),
                    text = stringResource(DateUtil.dayOfWeek(index)),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle.copy(
                        alpha = 0.4f
                    )
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().weight(1.0f),
            pageSize = pageSize
        ) {
            calendarAnimator.updatePagerState(pagerState)
            LaunchedEffect(Unit) {
                calendarAnimator.setAnimationMode(CalendarAnimator.AnimationMode.MONTH)
            }
            MonthView(
                months.get(it),
                modifier = Modifier.fillMaxSize(),
                calendarViewModel = calendarViewModel
            )
        }

    }

    LaunchedEffect(pagerState.currentPage) {
        // 当前页面下标变化时的逻辑
        curMonth = months.get(pagerState.currentPage)
        val day = DateUtil.getSelectedDay(curMonth)
        val playProgress = DailyUtil.getDailyGameProgress(day)
        calendarViewModel.updateSelectedDay(day, playProgress)
    }

}

@Composable
fun MonthView(month: Months, modifier: Modifier = Modifier, calendarViewModel: CalendarViewModel) {
    val days by remember { mutableStateOf(DateUtil.getMonthDays(month.year, month.month)) }
    val selectedDay by calendarViewModel.selectedDayFlow.collectAsState()
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items(days) {
            dayItem(
                it,
                modifier = Modifier.fillMaxSize(),
                selectedDay = selectedDay.day
            ) { day, progress ->
                calendarViewModel.updateSelectedDay(day, progress)
            }
        }
    }
}

@Composable
fun dayItem(
    day: Day,
    modifier: Modifier = Modifier,
    selectedDay: Day,
    isSelected: Boolean = isSameDay(day, selectedDay),
    onDaySelected: (day: Day, progress: Float) -> Unit
) {
    val isActive by remember { mutableStateOf(DateUtil.isFutureDay(day)) }
    val isValid by remember { mutableStateOf(day.dayOfMonth != -1) }
    val dayProgress by remember { mutableStateOf(DailyUtil.getDailyGameProgress(day)) }
    Box(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(25.dp).background(
                    color = if (isSelected) MaterialTheme.colorScheme.ThemeColors.progressBarColor else Color.Transparent,
                    shape = CircleShape
                ).clip(CircleShape)
                    .clickable(
                        enabled = if (isValid && isActive) true else false,
                        onClick = { onDaySelected(day, dayProgress) })
            ) {

            }
        }

        Text(
            modifier = modifier,
            text = if (isValid) "${day.dayOfMonth}" else "",
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            color = if (isActive) MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle else MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemSubtitle,
            fontStyle = FontStyle.Normal
        )
        if (isValid && isActive) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    progress = { dayProgress },
                    modifier = Modifier.size(25.dp),
                    trackColor = Color.Transparent,
                    color = MaterialTheme.colorScheme.ThemeColors.progressBarColor,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

fun isSameDay(day: Day, selectedDay: Day): Boolean =
    day.dayOfMonth == selectedDay.dayOfMonth && day.month == selectedDay.month && day.year == selectedDay.year
