package composes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composesudoku.composeapp.generated.resources.Res
import composesudoku.composeapp.generated.resources.autoDelete
import composesudoku.composeapp.generated.resources.autoDeleteSubtitle
import composesudoku.composeapp.generated.resources.autoFinish
import composesudoku.composeapp.generated.resources.errorLimit
import composesudoku.composeapp.generated.resources.errorLimitSubtitle
import composesudoku.composeapp.generated.resources.gameMessage
import composesudoku.composeapp.generated.resources.gameMessageSubtitle
import composesudoku.composeapp.generated.resources.guide
import composesudoku.composeapp.generated.resources.highLightCross
import composesudoku.composeapp.generated.resources.highLightCrossSubtitle
import composesudoku.composeapp.generated.resources.highLightSameNumber
import composesudoku.composeapp.generated.resources.highLightSameNumberSubtitle
import composesudoku.composeapp.generated.resources.highLightSector
import composesudoku.composeapp.generated.resources.highLightSectorSubtitle
import composesudoku.composeapp.generated.resources.ic_auto
import composesudoku.composeapp.generated.resources.ic_auto_remove_notes
import composesudoku.composeapp.generated.resources.ic_back
import composesudoku.composeapp.generated.resources.ic_dark_theme
import composesudoku.composeapp.generated.resources.ic_error
import composesudoku.composeapp.generated.resources.ic_guide
import composesudoku.composeapp.generated.resources.ic_high_light_area
import composesudoku.composeapp.generated.resources.ic_high_light_same_numbers
import composesudoku.composeapp.generated.resources.ic_hint
import composesudoku.composeapp.generated.resources.ic_left_number
import composesudoku.composeapp.generated.resources.ic_notice
import composesudoku.composeapp.generated.resources.ic_number_first
import composesudoku.composeapp.generated.resources.ic_question_infomation
import composesudoku.composeapp.generated.resources.ic_right
import composesudoku.composeapp.generated.resources.ic_score
import composesudoku.composeapp.generated.resources.ic_sector
import composesudoku.composeapp.generated.resources.ic_shock
import composesudoku.composeapp.generated.resources.ic_sound
import composesudoku.composeapp.generated.resources.ic_statistics
import composesudoku.composeapp.generated.resources.ic_timer
import composesudoku.composeapp.generated.resources.leftNumber
import composesudoku.composeapp.generated.resources.notification
import composesudoku.composeapp.generated.resources.numberFirst
import composesudoku.composeapp.generated.resources.numberFirstSubtitle
import composesudoku.composeapp.generated.resources.settings
import composesudoku.composeapp.generated.resources.shock
import composesudoku.composeapp.generated.resources.showScore
import composesudoku.composeapp.generated.resources.sound
import composesudoku.composeapp.generated.resources.statistics
import composesudoku.composeapp.generated.resources.themeMode
import composesudoku.composeapp.generated.resources.timer
import composesudoku.composeapp.generated.resources.tip
import composesudoku.composeapp.generated.resources.tipSubtitle
import models.GameSettingManager
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import theme.ThemeColors
import viewModels.GameViewModel

enum class SettingKey {
    statistics,//t统计
    guide,//玩法引导
    theme,//主题模式
    sound,//
    shock,
    notification,
    timer,
    error_limit,
    number_first,
    light_row_col,
    light_sector,
    light_same_number,
    auto_del_note,
    auto_finish,
    question_information,
    score,
    left_numbers,
    hint
}

enum class UIType {
    guide,
    switch,
}

data class SettingItem(
    val key: SettingKey,
    val image: @Composable () -> ImageVector,
    val title: @Composable () -> String,
    val subtitle: (@Composable () -> String)? = null,
    val uiType: UIType
)

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Settings(gameViewModel: GameViewModel, onQuit: () -> Unit) {
    val list: List<List<SettingItem>> = remember {
        mutableListOf<List<SettingItem>>(
            mutableListOf(
                SettingItem(
                    key = SettingKey.statistics,
                    image = { vectorResource(Res.drawable.ic_statistics) },
                    title = { stringResource(Res.string.statistics) },
                    uiType = UIType.guide
                ),
                SettingItem(
                    key = SettingKey.guide,
                    image = { vectorResource(Res.drawable.ic_guide) },
                    title = { stringResource(Res.string.guide) },
                    uiType = UIType.guide
                )
            ),
            mutableListOf(
                SettingItem(
                    key = SettingKey.statistics,
                    image = { vectorResource(Res.drawable.ic_dark_theme) },
                    title = { stringResource(Res.string.themeMode) },
                    uiType = UIType.guide
                ),
                SettingItem(
                    key = SettingKey.sound,
                    image = { vectorResource(Res.drawable.ic_sound) },
                    title = { stringResource(Res.string.sound) },
                    uiType = UIType.switch
                ),
                SettingItem(
                    key = SettingKey.shock,
                    image = { vectorResource(Res.drawable.ic_shock) },
                    title = { stringResource(Res.string.shock) },
                    uiType = UIType.switch
                ),
                SettingItem(
                    key = SettingKey.notification,
                    image = { vectorResource(Res.drawable.ic_notice) },
                    title = { stringResource(Res.string.notification) },
                    uiType = UIType.switch
                )
            ),
            mutableListOf(
                SettingItem(
                    key = SettingKey.timer,
                    image = { vectorResource(Res.drawable.ic_timer) },
                    title = { stringResource(Res.string.timer) },
                    uiType = UIType.switch
                ),
                SettingItem(
                    key = SettingKey.error_limit,
                    image = { vectorResource(Res.drawable.ic_error) },
                    title = { stringResource(Res.string.errorLimit) },
                    subtitle = { stringResource(Res.string.errorLimitSubtitle) },
                    uiType = UIType.switch
                ),
                SettingItem(
                    key = SettingKey.number_first,
                    image = { vectorResource(Res.drawable.ic_number_first) },
                    title = { stringResource(Res.string.numberFirst) },
                    subtitle = { stringResource(Res.string.numberFirstSubtitle) },
                    uiType = UIType.switch
                ),
                SettingItem(
                    key = SettingKey.light_row_col,
                    image = { vectorResource(Res.drawable.ic_high_light_area) },
                    title = { stringResource(Res.string.highLightCross) },
                    subtitle = { stringResource(Res.string.highLightCrossSubtitle) },
                    uiType = UIType.switch
                ),
                SettingItem(
                    key = SettingKey.light_sector,
                    image = { vectorResource(Res.drawable.ic_sector) },
                    title = { stringResource(Res.string.highLightSector) },
                    subtitle = { stringResource(Res.string.highLightSectorSubtitle) },
                    uiType = UIType.switch
                ),
                SettingItem(
                    key = SettingKey.light_same_number,
                    image = { vectorResource(Res.drawable.ic_high_light_same_numbers) },
                    title = { stringResource(Res.string.highLightSameNumber) },
                    subtitle = { stringResource(Res.string.highLightSameNumberSubtitle) },
                    uiType = UIType.switch
                ),
                SettingItem(
                    key = SettingKey.auto_del_note,
                    image = { vectorResource(Res.drawable.ic_auto_remove_notes) },
                    title = { stringResource(Res.string.autoDelete) },
                    subtitle = { stringResource(Res.string.autoDeleteSubtitle) },
                    uiType = UIType.switch
                ),
                SettingItem(
                    key = SettingKey.auto_finish,
                    image = { vectorResource(Res.drawable.ic_auto) },
                    title = { stringResource(Res.string.autoFinish) },
                    uiType = UIType.switch
                ),
                SettingItem(
                    key = SettingKey.question_information,
                    image = { vectorResource(Res.drawable.ic_question_infomation) },
                    title = { stringResource(Res.string.gameMessage) },
                    subtitle = { stringResource(Res.string.gameMessageSubtitle) },
                    uiType = UIType.switch
                ),
                SettingItem(
                    key = SettingKey.score,
                    image = { vectorResource(Res.drawable.ic_score) },
                    title = { stringResource(Res.string.showScore) },
                    uiType = UIType.switch
                ),
                SettingItem(
                    key = SettingKey.left_numbers,
                    image = { vectorResource(Res.drawable.ic_left_number) },
                    title = { stringResource(Res.string.leftNumber) },
                    uiType = UIType.switch
                ),
                SettingItem(
                    key = SettingKey.hint,
                    image = { vectorResource(Res.drawable.ic_hint) },
                    title = { stringResource(Res.string.tip) },
                    subtitle = { stringResource(Res.string.tipSubtitle) },
                    uiType = UIType.switch
                ),
            )
        )
    }
    val gameSettingState by gameViewModel.gameSettingFlow.collectAsState()

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
                text = stringResource(Res.string.settings),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.ThemeColors.ScreenTitle
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            items(list) { its ->
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.ThemeColors.difficultyDialogBackground),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        its.forEach { it ->
                            Row(
                                modifier = Modifier.fillMaxWidth().height(56.dp)
                                    .clickable(enabled = true, onClick = {
                                        updateState(gameSettingState, it.key)
                                        gameViewModel.refreshGameSetting()
                                    }),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(16.dp))
                                Icon(
                                    imageVector = it.image(),
                                    contentDescription = "",
                                    modifier = Modifier.size(20.dp)
                                )
                                Column(
                                    modifier = Modifier.fillMaxHeight().weight(1.0f)
                                        .padding(start = 8.dp, end = 8.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = it.title(),
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle
                                    )
                                    it.subtitle?.let { sub ->
                                        Text(
                                            text = sub(),
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.ThemeColors.difficultyDialogItemTitle.copy(
                                                alpha = 0.8f
                                            ),
                                            textAlign = TextAlign.Start,
                                            lineHeight = 12.sp,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                when (it.uiType) {
                                    UIType.guide -> {
                                        Icon(
                                            imageVector = vectorResource(Res.drawable.ic_right),
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(21.dp))
                                    }

                                    UIType.switch -> {
                                        switchButton(active = getState(gameSettingState, it.key))
                                        Spacer(modifier = Modifier.width(16.dp))
                                    }
                                }

                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
//
//
//    Scaffold(modifier = Modifier.fillMaxSize(),
//        containerColor = MaterialTheme.colorScheme.ThemeColors.ScreenBackground,
//        contentWindowInsets = WindowInsets(0, 0, 0, 0),
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = stringResource(Res.string.settings),
//                        fontSize = 16.sp,
//                        color = MaterialTheme.colorScheme.ThemeColors.ScreenTitle
//                    )
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.ThemeColors.ScreenBackground,
//                    scrolledContainerColor = MaterialTheme.colorScheme.ThemeColors.ScreenBackground
//                ),
//                navigationIcon = {
//                    IconButton(onClick = onQuit) {
//                        Icon(
//                            modifier = Modifier.size(24.dp),
//                            imageVector = vectorResource(Res.drawable.ic_back),
//                            contentDescription = "exit game",
//                            tint = MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
//                        )
//                    }
//                },
//                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(canScroll = { false })
//            )
//        }) { paddingValues ->
//
//    }
}

@Composable
fun getState(gameSettingState: GameSettingManager, key: SettingKey): Boolean {
    return when (key) {
        SettingKey.sound -> gameSettingState.gameSetting.sound
        SettingKey.shock -> gameSettingState.gameSetting.shock
        SettingKey.notification -> gameSettingState.gameSetting.notice
        SettingKey.timer -> gameSettingState.gameSetting.showTimer
        SettingKey.error_limit -> gameSettingState.gameSetting.errorLimit
        SettingKey.number_first -> gameSettingState.gameSetting.numberFirst
        SettingKey.light_row_col -> gameSettingState.gameSetting.highLightSameRowOrColumn
        SettingKey.light_sector -> gameSettingState.gameSetting.highLightSameSector
        SettingKey.light_same_number -> gameSettingState.gameSetting.highLightSameNumber
        SettingKey.auto_del_note -> gameSettingState.gameSetting.autoDeleteNote
        SettingKey.auto_finish -> gameSettingState.gameSetting.autoFinish
        SettingKey.question_information -> gameSettingState.gameSetting.questionInformation
        SettingKey.score -> gameSettingState.gameSetting.showScore
        SettingKey.left_numbers -> gameSettingState.gameSetting.showLeftNumber
        SettingKey.hint -> gameSettingState.gameSetting.smartHint
        else -> false
    }
}

fun updateState(gameSettingState: GameSettingManager, key: SettingKey) {
    when (key) {
        SettingKey.sound -> gameSettingState.updateSound(!gameSettingState.gameSetting.sound)
        SettingKey.shock -> gameSettingState.updateShock(!gameSettingState.gameSetting.shock)
        SettingKey.notification -> gameSettingState.updateNotice(!gameSettingState.gameSetting.notice)
        SettingKey.timer -> gameSettingState.updateShowTimer(!gameSettingState.gameSetting.showTimer)
        SettingKey.error_limit -> gameSettingState.updateErrorLimit(!gameSettingState.gameSetting.errorLimit)
        SettingKey.number_first -> gameSettingState.updateNumberFirst(!gameSettingState.gameSetting.numberFirst)
        SettingKey.light_row_col -> gameSettingState.updateHighLightSameRowOrColumn(!gameSettingState.gameSetting.highLightSameRowOrColumn)
        SettingKey.light_sector -> gameSettingState.updateHighLightSameSector(!gameSettingState.gameSetting.highLightSameSector)
        SettingKey.light_same_number -> gameSettingState.updateHighLightSameNumber(!gameSettingState.gameSetting.highLightSameNumber)
        SettingKey.auto_del_note -> gameSettingState.updateAutoDeleteNote(!gameSettingState.gameSetting.autoDeleteNote)
        SettingKey.auto_finish -> gameSettingState.updateAutoFinish(!gameSettingState.gameSetting.autoFinish)
        SettingKey.question_information -> gameSettingState.updateQuestionInformation(!gameSettingState.gameSetting.questionInformation)
        SettingKey.score -> gameSettingState.updateShowScore(!gameSettingState.gameSetting.showScore)
        SettingKey.left_numbers -> gameSettingState.updateShowLeftNumber(!gameSettingState.gameSetting.showLeftNumber)
        SettingKey.hint -> gameSettingState.updateSmartHint(!gameSettingState.gameSetting.smartHint)
        else -> {}
    }
}

@Composable
fun switchButton(active: Boolean) {
    Box(
        modifier = Modifier.width(42.dp).height(30.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(0.7f).height(15.dp).background(
                    color = if (active) MaterialTheme.colorScheme.ThemeColors.BottomBarActive.copy(
                        alpha = 0.65f
                    ) else MaterialTheme.colorScheme.ThemeColors.BottomBarInactive.copy(alpha = 0.65f),
                    shape = RoundedCornerShape(8.dp)
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = if (active) Alignment.End else Alignment.Start
        ) {
            Card(
                modifier = Modifier.size(20.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (active) MaterialTheme.colorScheme.ThemeColors.BottomBarActive else MaterialTheme.colorScheme.ThemeColors.BottomBarInactive
                )
            ) {}
        }
    }
}