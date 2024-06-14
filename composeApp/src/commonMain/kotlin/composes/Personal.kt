package composes

import achievement.Achievement
import achievement.getProgress
import achievement.hadFinished
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composes.layout.GridLayout
import composesudoku.composeapp.generated.resources.Res
import composesudoku.composeapp.generated.resources.achievement
import composesudoku.composeapp.generated.resources.bestRecord
import composesudoku.composeapp.generated.resources.branch_tasks
import composesudoku.composeapp.generated.resources.classic
import composesudoku.composeapp.generated.resources.dailyChallenge
import composesudoku.composeapp.generated.resources.finished
import composesudoku.composeapp.generated.resources.game_loading
import composesudoku.composeapp.generated.resources.ic_header
import composesudoku.composeapp.generated.resources.ic_setting
import composesudoku.composeapp.generated.resources.ic_statistic_best_time
import composesudoku.composeapp.generated.resources.ic_statistic_game_win
import composesudoku.composeapp.generated.resources.ic_statistic_no_mistake
import composesudoku.composeapp.generated.resources.ic_statistic_win_streak
import composesudoku.composeapp.generated.resources.login
import composesudoku.composeapp.generated.resources.perfect
import composesudoku.composeapp.generated.resources.winningStreak
import convertMillisToTimeFormat
import models.DIFFICULTY_EASY
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import theme.ThemeColors
import viewModels.GameViewModel
import viewModels.LoginViewModel

data class StatisticsItem(val id: Int, val title: @Composable () -> String)

data class ClassicItem(
    val id: Int,
    val icon: @Composable () -> ImageVector,
    val title: @Composable () -> String
)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Personal(gameViewModel: GameViewModel) {
    var settingState by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.ThemeColors.ScreenBackground),
        verticalArrangement = Arrangement.Top
    ) {
        items(3) { index ->
            when (index) {
                0 -> {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(62.dp).padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(1.0f))
                        IconButton(onClick = {
                            settingState = true
                        }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = vectorResource(Res.drawable.ic_setting),
                                contentDescription = "settings",
                                tint = MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                            )
                        }
                    }
                }

                1 -> LoginView()
                2 -> GameScoreView(gameViewModel = gameViewModel)
            }
        }
    }

    if (settingState) {
        Settings(gameViewModel) {
            settingState = false
        }
    }

}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoginView(loginInfoViewModel: LoginViewModel = LoginViewModel.Instance) {
    val loginInfoFlow = loginInfoViewModel.loginInfoFlow.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth().height(180.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = vectorResource(Res.drawable.ic_header),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        ElevatedButton(
            onClick = {}, enabled = true,
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
            Text(
                textAlign = TextAlign.Justify,
                text = stringResource(Res.string.login),
                color = MaterialTheme.colorScheme.ThemeColors.continueGameButtonTitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun GameScoreView(gameViewModel: GameViewModel) {
    var selectedId by remember { mutableStateOf(0) }
    val itemTitles by remember {
        mutableStateOf(List(4) { it ->
            when (it) {
                0 -> StatisticsItem(id = it, title = { stringResource(Res.string.classic) })
                1 -> StatisticsItem(id = it, title = { stringResource(Res.string.dailyChallenge) })
                2 -> StatisticsItem(id = it, title = { stringResource(Res.string.achievement) })
                4 -> StatisticsItem(id = it, title = { stringResource(Res.string.branch_tasks) })
                else -> StatisticsItem(id = it, title = { stringResource(Res.string.branch_tasks) })
            }
        })
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(itemTitles) { it ->
                Column(
                    modifier = Modifier.height(36.dp).width(IntrinsicSize.Max)
                        .padding(start = 8.dp, end = 8.dp)
                        .clickable(enabled = true, onClick = { selectedId = it.id }),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = it.title(),
                        color = if (it.id == selectedId) MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle else MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle.copy(
                            alpha = 0.55f
                        ),
                        fontSize = if (it.id == selectedId) 16.sp else 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().height(2.dp)
                            .background(color = if (it.id == selectedId) MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle else Color.Transparent)
                    ) {}
                }
            }
        }
        when (selectedId) {
            0 -> ItemClassic(gameViewModel)
            1 -> ItemDailyChallenge()
            2 -> ItemAchievement()
            3 -> ItemBranchTasks()
            else -> ItemBranchTasks()
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ItemClassic(gameViewModel: GameViewModel) {
    val difficulties by gameViewModel.difficultiesFlow.collectAsState()
    var selectedDifficulty by remember { mutableStateOf(DIFFICULTY_EASY) }
    val curDifficulty by remember { mutableStateOf(difficulties.find { it.type == selectedDifficulty }) }
    val classicItems by remember {
        mutableStateOf(List(4) {
            when (it) {
                0 -> ClassicItem(
                    id = it,
                    icon = { vectorResource(Res.drawable.ic_statistic_game_win) },
                    title = { stringResource(Res.string.finished) })

                1 -> ClassicItem(
                    id = it,
                    icon = { vectorResource(Res.drawable.ic_statistic_no_mistake) },
                    title = { stringResource(Res.string.perfect) })

                2 -> ClassicItem(
                    id = it,
                    icon = { vectorResource(Res.drawable.ic_statistic_win_streak) },
                    title = { stringResource(Res.string.winningStreak) })

                3 -> ClassicItem(
                    id = it,
                    icon = { vectorResource(Res.drawable.ic_statistic_best_time) },
                    title = { stringResource(Res.string.bestRecord) })

                else -> ClassicItem(
                    id = it,
                    icon = { vectorResource(Res.drawable.ic_statistic_best_time) },
                    title = { stringResource(Res.string.bestRecord) })
            }
        })
    }


    Column(
        modifier = Modifier.fillMaxSize().padding(top = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        LazyRow(modifier = Modifier.fillMaxWidth().padding(bottom = 2.dp)) {
            items(difficulties) {
                Row(
                    modifier = Modifier.fillMaxHeight().padding(start = 8.dp, end = 8.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(
                            color = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemSubtitle
                        ).clickable(enabled = true, onClick = { selectedDifficulty = it.type })
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = getDifficultyTitle(it.type),
                        color = if (it.type == selectedDifficulty) MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle else MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle.copy(
                            alpha = 0.55f
                        ),
                        fontSize = if (it.type == selectedDifficulty) 14.sp else 13.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
        classicItems.forEach {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(0.95f).height(48.dp),
                shape = RoundedCornerShape(3.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemBackground
                ),
                onClick = {

                }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(1.0f).padding(start = 12.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        imageVector = it.icon(),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = it.title(),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle
                    )
                    Spacer(modifier = Modifier.weight(1.0f))
                    Text(
                        text = when (it.id) {
                            0 -> "${curDifficulty?.finishCount}"
                            1 -> "${curDifficulty?.perfect}"
                            2 -> "${curDifficulty?.bestWinStreak}"
                            3 -> curDifficulty?.bestTime?.let { it1 ->
                                convertMillisToTimeFormat(
                                    it1
                                )
                            } ?: "00:00"

                            else -> curDifficulty?.bestTime?.let { it1 ->
                                convertMillisToTimeFormat(
                                    it1
                                )
                            } ?: "00:00"
                        },
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}


@Composable
fun ItemDailyChallenge() {

}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ItemAchievement() {
    val achievementGroup by Achievement.Instance.achievementGroupFlow.collectAsState()//MutableStateFlow<MutableList<AchievementGroup>>(Achievement.Instance.achievementGroup).collectAsState()
    println("achievementGroup::${achievementGroup?.size}")
    if (achievementGroup == null) {
        loadingView(stringResource(Res.string.game_loading))
    }
    achievementGroup?.forEach { group ->
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = Achievement.Instance.getTitle(group.id),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(3.dp))
            GridLayout(
                line = 3,
                data = group.achievement,
                itemSpace = Arrangement.Start
            ) { width, item ->
                Card(
                    modifier = Modifier.width(width).aspectRatio(0.85f).padding(width / 60),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemBackground),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Box(
                            modifier = Modifier.size(width * 0.55f)
                                .background(
                                    color = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemSubtitle,
                                    shape = CircleShape
                                )
                        ) {
                            Image(
                                imageVector = Achievement.Instance.getIcon("${item.group}_${item.id}"),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    color = MaterialTheme.colorScheme.ThemeColors.SplashTitleColor,
                                    blendMode = if (item.hadFinished()) BlendMode.DstIn else BlendMode.SrcIn
                                )
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${item.getProgress()}/${item.max}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle,
                                lineHeight = 13.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = Achievement.Instance.getTitle("${item.group}_${item.id}"),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle,
                                lineHeight = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemBranchTasks() {

}