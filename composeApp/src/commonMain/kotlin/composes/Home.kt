package composes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import calendar.DateUtil
import calendar.Day
import composesudoku.composeapp.generated.resources.Lv
import composesudoku.composeapp.generated.resources.LvProgress
import composesudoku.composeapp.generated.resources.Res
import composesudoku.composeapp.generated.resources.app_title
import composesudoku.composeapp.generated.resources.continue_game
import composesudoku.composeapp.generated.resources.daily_challenge
import composesudoku.composeapp.generated.resources.daily_start
import composesudoku.composeapp.generated.resources.easy
import composesudoku.composeapp.generated.resources.expert
import composesudoku.composeapp.generated.resources.hard
import composesudoku.composeapp.generated.resources.ic_star
import composesudoku.composeapp.generated.resources.medium
import composesudoku.composeapp.generated.resources.new_game
import composesudoku.composeapp.generated.resources.unknown
import convertMillisToTimeFormat
import data.Cache
import models.DIFFICULTY_EASY
import models.DIFFICULTY_EXPERT
import models.DIFFICULTY_HARD
import models.DIFFICULTY_MEDIUM
import models.Difficulty
import models.Sudoku
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import theme.ThemeColors
import viewModels.GameViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Home(
    unfinishedGame: Sudoku? = null,
    onStartDailyChallenge: (day: Day) -> Unit,
    onStartGame: (data: Sudoku?) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(1.0f)) {
            Text(
                modifier = Modifier.height(72.dp).fillMaxWidth(1.0f)
                    .padding(start = 16.dp, top = 24.dp),
                text = stringResource(Res.string.app_title),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.ThemeColors.DailyCardNormal,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.weight(1.0f).fillMaxWidth(1.0f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                DailyCard(onStartDailyChallenge)
            }
            Column(
                modifier = Modifier.weight(1.0f).fillMaxWidth(1.0f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (unfinishedGame != null) {
                    ElevatedButton(
                        onClick = { onStartGame(unfinishedGame) },
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
                                text = stringResource(Res.string.continue_game),
                                color = MaterialTheme.colorScheme.ThemeColors.continueGameButtonTitle,
                                fontSize = 16.sp,
                                lineHeight = 16.sp
                            )
                            Text(
                                text = "${getDifficultyTitle(unfinishedGame.type)} ${
                                    convertMillisToTimeFormat(
                                        unfinishedGame.duration
                                    )
                                }",
                                color = MaterialTheme.colorScheme.ThemeColors.continueGameButtonTitle.copy(
                                    alpha = 0.9f
                                ),
                                fontSize = 10.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                ElevatedButton(
                    onClick = { onStartGame(null) },
                    enabled = true,
                    modifier = Modifier.fillMaxWidth(0.65f).height(42.dp),
                    shape = RoundedCornerShape(3.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.ThemeColors.NewGameButtonBackground
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 0.3.dp
                    )
                ) {
                    Text(
                        textAlign = TextAlign.Justify,
                        text = stringResource(Res.string.new_game),
                        color = MaterialTheme.colorScheme.ThemeColors.NewGameButtonTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DailyCard(onStartDailyChallenge: (day: Day) -> Unit) {
    val curDay: Day by remember { mutableStateOf(DateUtil.today()) }
    ElevatedCard(
        modifier = Modifier.width(156.dp).aspectRatio(0.8f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ), shape = RoundedCornerShape(3.dp), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.ThemeColors.DailyCardBackground
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(1.0f).padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = stringResource(Res.string.daily_challenge),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.ThemeColors.DailyCardNormal
            )
            Image(
                imageResource(DrawableResource("drawable-xxhdpi/ic_trophy.png")),
                contentDescription = "trophy",
                modifier = Modifier.size(42.dp),
                contentScale = ContentScale.FillBounds,
            )

//            var date: String by remember { mutableStateOf("4-15") }
//            LaunchedEffect(Unit) {
//                date = "${getCurMonth()}:${getCurDayOfMonth()}"
//            }
            Text(
                text = "${curDay.month}-${curDay.dayOfMonth}",
                color = MaterialTheme.colorScheme.ThemeColors.DailyCardActive,
                fontSize = 16.sp
            )
            Button(
                onClick = { onStartDailyChallenge(curDay) },
                modifier = Modifier.width(96.dp).height(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.ThemeColors.DailyCardButtonBackground
                )
            ) {
                Text(
                    text = stringResource(Res.string.daily_start),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.ThemeColors.DailyCardActive
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun difficultyDialog(gameViewModel: GameViewModel, onDismiss: (difficulty: Difficulty?) -> Unit) {
    val difficulties by gameViewModel.difficultiesFlow.collectAsState()
    ModalBottomSheet(
        onDismissRequest = { onDismiss(null) },
        containerColor = MaterialTheme.colorScheme.ThemeColors.difficultyDialogBackground,
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier.fillMaxSize(1.0f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Top)
        ) {
            difficulties.forEach { difficulty ->
                Card(
                    modifier = Modifier.fillMaxWidth(0.85f).height(48.dp),
                    shape = RoundedCornerShape(3.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemBackground
                    ),
                    onClick = {
                        onDismiss(difficulty)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(1.0f).padding(start = 12.dp, end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = getDifficultyTitle(difficulty.type),
                            color = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = stringResource(Res.string.Lv, difficulty.level),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemSubtitle,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.SansSerif,
                            fontStyle = FontStyle.Italic
                        )
                        Spacer(modifier = Modifier.weight(1.0f))
                        Column(
                            modifier = Modifier.width(56.dp).fillMaxHeight(0.75f),
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.wrapContentSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    imageVector = vectorResource(Res.drawable.ic_star),
                                    contentDescription = null,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = getMaxProgress(difficulty),
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle.copy(
                                        alpha = 0.85f
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            LinearProgressIndicator(
                                progress = { getCurrentProgress(difficulty) },
                                modifier = Modifier.fillMaxWidth(1.0f).height(2.dp),
                                color = MaterialTheme.colorScheme.ThemeColors.progressBarBackgroundColor,
                                trackColor = MaterialTheme.colorScheme.ThemeColors.progressBarColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun getDifficultyTitle(type: Int): String {
    return when (type) {
        DIFFICULTY_EASY -> stringResource(Res.string.easy)
        DIFFICULTY_MEDIUM -> stringResource(Res.string.medium)
        DIFFICULTY_HARD -> stringResource(Res.string.hard)
        DIFFICULTY_EXPERT -> stringResource(Res.string.expert)
        else -> stringResource(Res.string.unknown)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun getMaxProgress(gameMode: Difficulty): String {
    val max = 10
    var min = gameMode.finishCount - (gameMode.level - 1) * 10
    if (min < 0) min = 0
    return stringResource(Res.string.LvProgress, min, max)
}

fun getCurrentProgress(gameMode: Difficulty): Float {
    val max = 10
    var min = gameMode.finishCount - (gameMode.level - 1) * 10
    if (min < 0) min = 0
    return if (min == 0) 0.0f else min * 1.0f / max
}