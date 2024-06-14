package composes

import SCREEN_GAME_PLAY
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import composesudoku.composeapp.generated.resources.Res
import composesudoku.composeapp.generated.resources.continue_game
import composesudoku.composeapp.generated.resources.erase
import composesudoku.composeapp.generated.resources.eraseTip
import composesudoku.composeapp.generated.resources.error_times
import composesudoku.composeapp.generated.resources.gameOver
import composesudoku.composeapp.generated.resources.gameOverTip
import composesudoku.composeapp.generated.resources.game_loading
import composesudoku.composeapp.generated.resources.hint
import composesudoku.composeapp.generated.resources.hintTip
import composesudoku.composeapp.generated.resources.ic_back
import composesudoku.composeapp.generated.resources.ic_erase
import composesudoku.composeapp.generated.resources.ic_hint
import composesudoku.composeapp.generated.resources.ic_pause
import composesudoku.composeapp.generated.resources.ic_pencil
import composesudoku.composeapp.generated.resources.ic_setting
import composesudoku.composeapp.generated.resources.ic_share
import composesudoku.composeapp.generated.resources.ic_theme
import composesudoku.composeapp.generated.resources.ic_undo
import composesudoku.composeapp.generated.resources.level
import composesudoku.composeapp.generated.resources.pause
import composesudoku.composeapp.generated.resources.pencil
import composesudoku.composeapp.generated.resources.pencilTip
import composesudoku.composeapp.generated.resources.resetGame
import composesudoku.composeapp.generated.resources.restart
import composesudoku.composeapp.generated.resources.secondChance
import composesudoku.composeapp.generated.resources.time
import composesudoku.composeapp.generated.resources.undo
import composesudoku.composeapp.generated.resources.undoTip
import convertMillisToTimeFormat
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import theme.BoardColors
import theme.ThemeColors
import viewModels.GameState
import viewModels.GameViewModel
import viewModels.MAX_ERROR_COUNT
import viewModels.PlayingGameConfig
import kotlin.random.Random


data class GameToolItem(
    val index: Int,
    val image: @Composable () -> ImageVector,
    val title: @Composable () -> String
)

data class GameTip(
    val image: @Composable () -> ImageVector,
    val title: @Composable () -> String,
    val subtitle: @Composable () -> String
)


@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun GamePlay(
    lifecycleOwner: LifecycleOwner,
    gameViewModel: GameViewModel,
    navController: NavController,
    onGameWin: () -> Unit,
    onQuit: () -> Unit,
    playingGameConfig: PlayingGameConfig?
) {

    val gameTools = remember {
        listOf(
            GameToolItem(
                0,
                image = { vectorResource(Res.drawable.ic_undo) },
                title = { stringResource(Res.string.undo) }
            ),
            GameToolItem(
                1,
                image = { vectorResource(Res.drawable.ic_erase) },
                title = { stringResource(Res.string.erase) }
            ),
            GameToolItem(
                2,
                image = { vectorResource(Res.drawable.ic_pencil) },
                title = { stringResource(Res.string.pencil) }
            ),
            GameToolItem(
                3,
                image = { vectorResource(Res.drawable.ic_hint) },
                title = { stringResource(Res.string.hint) }
            )
        )
    }
    val sudoku by gameViewModel.sudokuGameFlow.collectAsState()
    val gameBoard by gameViewModel.boardFlow.collectAsState()
    val inputNumbers by gameViewModel.inputNumbersFlow.collectAsState()
    val gameSetting by gameViewModel.gameSettingFlow.collectAsState()
    val selectedCell by gameViewModel.currentCellFlow.collectAsState()
    val noteState by gameViewModel.noteStateFlow.collectAsState()
    val notes by gameViewModel.notesFlow.collectAsState()
    val errorCount by gameViewModel.errorCountFlow.collectAsState()
    val time by gameViewModel.timeFlow.collectAsState()
    var settingState by remember { mutableStateOf(false) }
    val gameState by gameViewModel.gameStateFlow.collectAsState()
    var hasPaused by remember { mutableStateOf(false) }
    var showDailyCalendar by remember { mutableStateOf(false) }
    var gameWinState by remember { mutableStateOf(false) }


    val observer = remember {
        LifecycleEventObserver { source, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                println("life resume")
                if (hasPaused) {
                    if (gameState == GameState.PAUSED) {
                        gameViewModel.startGame()
                    }
                }
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                println("life pause")
                if (gameState == GameState.PLAYING) {
                    gameViewModel.pauseGame()
                    hasPaused = true
                }
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                println("life destroy")
                gameViewModel.saveGame()
            }
        }
    }
    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(observer)
    }

    DisposableEffect(Unit) {
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            gameViewModel.setGameState(GameState.PREPARING)
        }
    }
    when (gameState) {
        GameState.PREPARING -> {
            loadingView(stringResource(Res.string.game_loading))
        }

        GameState.READY -> {
            randomTipDialog() {
                gameViewModel.startGame()
            }
        }

        GameState.RESUME -> {

        }

        GameState.PAUSED -> {
            gamePauseDialog(time, sudoku.type, onRestart = {
                gameViewModel.resetGame()
            }, onContinue = {
                gameViewModel.startGame()
            })
        }

        GameState.WIN -> {
            gameWinState = true
            onGameWin()
        }

        GameState.FAILED -> {
            onGameFailure(onSecondChance = {
                //看广告
                gameViewModel.resetErrorCount()
            }, onReset = {
                gameViewModel.resetGame()
            })
        }

        else -> {}
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(1.0f),
        contentWindowInsets = WindowInsets(0, 0, 0, 0), topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.ThemeColors.ScreenBackground
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        gameViewModel.saveGame()
                        playingGameConfig?.let {
                            it.day?.let { day ->
                                showDailyCalendar = true
                            } ?: onQuit()
                        }
                    }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = vectorResource(Res.drawable.ic_back),
                            contentDescription = "exit game",
                            tint = MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = vectorResource(Res.drawable.ic_share),
                            contentDescription = "share game",
                            tint = MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                        )
                    }
                    IconButton(onClick = { gameViewModel.pauseGame() }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = vectorResource(Res.drawable.ic_pause),
                            contentDescription = "pause game",
                            tint = MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = vectorResource(Res.drawable.ic_theme),
                            contentDescription = "game theme",
                            tint = MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                        )
                    }
                    IconButton(onClick = {
                        settingState = true
//                        navController.navigate(SCREEN_SETTING)
                    }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = vectorResource(Res.drawable.ic_setting),
                            contentDescription = "settings",
                            tint = MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                        )
                    }
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(canScroll = { false })
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize(1.0f).padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(0.95f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(Res.string.error_times, errorCount, MAX_ERROR_COUNT),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.ThemeColors.gameInfo
                )
                Text(
                    text = getDifficultyTitle(sudoku.type),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.ThemeColors.gameInfo
                )
                Text(
                    text = convertMillisToTimeFormat(time),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.ThemeColors.gameInfo
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(0.95f).aspectRatio(1.0f).background(Color.Blue)
            ) {
                SudokuBoard(
                    selectedCell = selectedCell,
                    cells = gameBoard,
                    gameState = gameState,
                    size = 9,
                    notes = notes,
                    boardColors = MaterialTheme.colorScheme.BoardColors,
                    highLightSameNumber = gameSetting.gameSetting.highLightSameNumber,
                    highSameSector = gameSetting.gameSetting.highLightSameSector,
                    highLineAndCol = gameSetting.gameSetting.highLightSameRowOrColumn
                ) { cell ->
                    gameViewModel.setupCurrentCell(cell)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (item in gameTools) {
                    Column(
                        modifier = Modifier.weight(1.0f).aspectRatio(1.0f)
                            .clickable(enabled = true, onClick = {
                                when (item.index) {
                                    0 -> {
                                        gameViewModel.undo()
                                    }

                                    1 -> {
                                        gameViewModel.setValue(0)
                                    }

                                    2 -> {
                                        gameViewModel.toggleNoteState()
                                    }

                                    3 -> {
                                        gameViewModel.hint()
                                    }
                                }
                            }),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = item.image(),
                            contentDescription = item.title(),
                            tint = when (item.index) {
                                0 -> MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                                1 -> MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                                2 -> if (noteState) MaterialTheme.colorScheme.ThemeColors.gamePlayIconsActive else MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                                3 -> MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                                else -> MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                            }
                        )
                        Text(
                            text = item.title(),
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Normal,
                            color = when (item.index) {
                                0 -> MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                                1 -> MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                                2 -> if (noteState) MaterialTheme.colorScheme.ThemeColors.gamePlayIconsActive else MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                                3 -> MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                                else -> MaterialTheme.colorScheme.ThemeColors.gamePlayIcons
                            }
                        )
                    }
                }
            }
            if (gameState == GameState.PLAYING) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    inputNumbers.forEach { gameNumber ->
                        Spacer(modifier = Modifier.fillMaxHeight().weight(0.15f))
                        ElevatedCard(
                            modifier = Modifier.fillMaxHeight().weight(1.0f),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 3.dp,
                                pressedElevation = 0.5.dp
                            ),
                            shape = RoundedCornerShape(3.dp),
                            onClick = {
                                gameViewModel.setValue(gameNumber.value)
                            }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${gameNumber.value}",
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.BoardColors.textColor
                                )
                                if (gameSetting.gameSetting.showLeftNumber && !noteState) {
                                    Text(
                                        text = "${gameNumber.leftCount}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.BoardColors.notesColor
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.fillMaxHeight().weight(0.15f))
                    }
                }
            }

        }
    }

    if (settingState) {
        Settings(gameViewModel) {
            settingState = false
        }
    }

    if (showDailyCalendar) {
        sudokuCalendar(
            current = playingGameConfig?.day,
            onQuit = {
            onQuit()
        }, onGame = { day ->
            showDailyCalendar = false
            gameViewModel.updatePlayingGameConfig(PlayingGameConfig(day = day), true)
//            navController.navigate(SCREEN_GAME_PLAY)
//            gameViewModel.setupGame(playingGameConfig)
        })
    }

    if (gameWinState){
        gameWinDialog()
    }

    LaunchedEffect(Unit) {
        gameViewModel.setupGame(playingGameConfig)
    }

}

@Composable
fun gameWinDialog() {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.ThemeColors.ScreenBackground)
    ) {

    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun onGameFailure(onSecondChance: () -> Unit, onReset: () -> Unit) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().height(264.dp).padding(start = 12.dp, end = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.ThemeColors.ScreenBackground)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    text = stringResource(Res.string.gameOver),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.ThemeColors.dialogTitle
                )

                Text(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    text = stringResource(Res.string.gameOverTip, MAX_ERROR_COUNT),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.ThemeColors.dialogSubtitle
                )

                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Button(
                        onClick = onSecondChance,
                        modifier = Modifier.fillMaxWidth().height(42.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.ThemeColors.dialogActiveButtonBackground),
                        shape = RoundedCornerShape(3.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.secondChance),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.ThemeColors.dialogActiveButtonTitle
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Button(
                        onClick = onReset,
                        modifier = Modifier.height(42.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text = stringResource(Res.string.resetGame),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.ThemeColors.dialogInactiveButtonTitle
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun gamePauseDialog(duration: Long, type: Int, onRestart: () -> Unit, onContinue: () -> Unit) {
    val gameTip = remember {
        when (Random(1000).nextInt() % 4) {
            0 -> GameTip(
                image = { vectorResource(Res.drawable.ic_undo) },
                title = { stringResource(Res.string.undo) },
                subtitle = { stringResource(Res.string.undoTip) })

            1 -> GameTip(
                image = { vectorResource(Res.drawable.ic_erase) },
                title = { stringResource(Res.string.erase) },
                subtitle = { stringResource(Res.string.eraseTip) })

            2 -> GameTip(
                image = { vectorResource(Res.drawable.ic_pencil) },
                title = { stringResource(Res.string.pencil) },
                subtitle = { stringResource(Res.string.pencilTip) })

            3 -> GameTip(
                image = { vectorResource(Res.drawable.ic_hint) },
                title = { stringResource(Res.string.hint) },
                subtitle = { stringResource(Res.string.hintTip) })

            else -> GameTip(
                image = { vectorResource(Res.drawable.ic_undo) },
                title = { stringResource(Res.string.undo) },
                subtitle = { stringResource(Res.string.undo) })
        }
    }

    Dialog(
        onDismissRequest = {}, properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().height(450.dp).padding(start = 12.dp, end = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.ThemeColors.ScreenBackground)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    text = stringResource(Res.string.pause),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.ThemeColors.dialogTitle
                )
                //Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight().weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier,
                            text = stringResource(Res.string.time),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.ThemeColors.dialogSubtitle
                        )
                        Text(
                            text = convertMillisToTimeFormat(duration),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.ThemeColors.dialogTitle
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxHeight().weight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.level),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.ThemeColors.dialogSubtitle
                        )
                        Text(
                            text = getDifficultyTitle(type),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.ThemeColors.dialogTitle
                        )
                    }
                }

                // Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().height(196.dp)
                        .padding(start = 16.dp, end = 16.dp, top = 14.dp)
                        .clip(shape = RoundedCornerShape(6.dp))
                        .background(color = MaterialTheme.colorScheme.ThemeColors.dialogTipAreaBackground),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {

                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape = CircleShape,
                        color = Color.White
                    ) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = gameTip.image(),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.ThemeColors.dialogTitle.copy(alpha = 0.9f)
                        )
                    }
                    //   Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = gameTip.title(),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.ThemeColors.dialogTitle
                    )
                    //  Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        text = gameTip.subtitle(),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.ThemeColors.dialogTitle
                    )
                }
                //   Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Button(
                        onClick = onContinue,
                        modifier = Modifier.fillMaxWidth().height(42.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.ThemeColors.dialogActiveButtonBackground),
                        shape = RoundedCornerShape(3.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.continue_game),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.ThemeColors.dialogActiveButtonTitle
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Button(
                        onClick = onRestart,
                        modifier = Modifier.height(42.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text = stringResource(Res.string.restart),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.ThemeColors.dialogInactiveButtonTitle
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun randomTipDialog(onClosed: () -> Unit) {
//    LaunchedEffect(Unit) {
//        delay(1000)
//        onClosed()
//    }
    Dialog(
        onDismissRequest = {}, properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = onClosed) {
                    Text(text = "Close")
                }
            }
        }
    }
}

@Composable
fun loadingView(msg: String) {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier.fillMaxWidth(0.4f).aspectRatio(1.0f),
            shape = RoundedCornerShape(3.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.ThemeColors.ScreenBackground)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(1.0f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxWidth(0.35f).aspectRatio(1.0f),
                    color = MaterialTheme.colorScheme.ThemeColors.progressBarBackgroundColor,
                    trackColor = MaterialTheme.colorScheme.ThemeColors.progressBarColor
                )
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.ThemeColors.gamePlayIcons,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal
                )
            }
        }
    }
}




