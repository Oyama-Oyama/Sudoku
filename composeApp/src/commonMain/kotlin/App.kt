import achievement.Achievement
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import composes.BottomBar
import composes.Challenge
import composes.Explore
import composes.GamePlay
import composes.Home
import composes.Personal
import composes.Settings
import composes.Splash
import composes.difficultyDialog
import org.jetbrains.compose.ui.tooling.preview.Preview
import theme.ComposeSudokuTheme
import theme.ThemeColors
import theme.Themes
import viewModels.GameViewModel
import viewModels.PlayingGameConfig

const val SCREEN_SPLASH = "screen_splash"
const val SCREEN_HOME = "screen_home"
const val SCREEN_CHALLENGE = "screen_challenge"
const val SCREEN_EXPLORE = "screen_explore"
const val SCREEN_PERSONAL = "screen_personal"
const val SCREEN_GAME_PLAY = "screen_game_play"
const val SCREEN_SETTING = "screen_setting"

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val focusRequester = remember { FocusRequester() }
    val theme by remember { mutableStateOf(Themes.Light) }
    var showBottomBar by remember { mutableStateOf(false) }
    val navController = rememberNavController()
    var currentSelectedBottomBarIndex by remember { mutableStateOf(0) }
    var difficultyDialogState by remember { mutableStateOf(false) }

    val gameViewModel = GameViewModel().apply { init() }

    val unfinishedGame by gameViewModel.unfinishedGameFlow.collectAsState()
    val playingGameConfig by gameViewModel.playingGameConfigFlow.collectAsState()

    val onDestinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            destination.route?.let {
                when (it) {
                    SCREEN_HOME -> {
                        currentSelectedBottomBarIndex = 0
                        showBottomBar = true
                    }

                    SCREEN_CHALLENGE -> {
                        currentSelectedBottomBarIndex = 1
                        showBottomBar = true
                    }

                    SCREEN_EXPLORE -> {
                        currentSelectedBottomBarIndex = 2
                        showBottomBar = true
                    }

                    SCREEN_PERSONAL -> {
                        currentSelectedBottomBarIndex = 3
                        showBottomBar = true
                    }

                    else -> showBottomBar = false
                }
            } ?: run { showBottomBar = false }
        }

    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener(onDestinationChangedListener)
    }

    DisposableEffect(Unit) {
        onDispose {
            navController.removeOnDestinationChangedListener(onDestinationChangedListener)
        }
    }

    if (difficultyDialogState) {
        difficultyDialog (gameViewModel) {
            difficultyDialogState = false
            if (it != null) {
                //gameViewModel.startPlayGame(difficulty = it)
                gameViewModel.updatePlayingGameConfig(PlayingGameConfig(difficulty = it))
                navController.navigate(SCREEN_GAME_PLAY)
//                gameViewModel.startPlayGame(difficulty = it)
            }
        }
    }

    ComposeSudokuTheme(theme = theme) {
        Scaffold(modifier = Modifier.fillMaxSize(1.0f).focusRequester(focusRequester)
            .focusProperties {
                exit = { focusRequester }
            }  // make LocalFocusManager.current.clear() to focus this node instead of the root window node
            .focusable().onPreviewKeyEvent {
                println(" back button::${it.key}")
                if (it.key != Key.Back) {
                    println("not back::${it.key}")
                    return@onPreviewKeyEvent false
                }
//                return@onPreviewKeyEvent navController.currentDestination?.route?.let { str ->
//                    println(" back::${str}")
//                    str == SCREEN_GAME_PLAY
//                } ?: run { false }
                //true
                false
            }, contentWindowInsets = WindowInsets(0, 0, 0, 0), bottomBar = {
            if (showBottomBar) {

                BottomBar(currentSelectedBottomBarIndex) { index ->
                    when (index) {
                        0 -> navController.navigate(SCREEN_HOME)
                        1 -> navController.navigate(SCREEN_CHALLENGE)
                        2 -> navController.navigate(SCREEN_EXPLORE)
                        3 -> navController.navigate(SCREEN_PERSONAL)
                    }
                }
            }
        }) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = SCREEN_SPLASH,
                modifier = Modifier.fillMaxSize(1.0f).padding(paddingValues)
                    .background(color = MaterialTheme.colorScheme.ThemeColors.ScreenBackground)
            ) {
                composable(SCREEN_SPLASH) {
                    Splash {
                        navController.navigate(SCREEN_HOME)
                    }
                }
                composable(SCREEN_HOME) {
                    Home(unfinishedGame = unfinishedGame, onStartGame = { gameData ->
                        if (gameData == null) {
                            //TODO：开始新游戏
                            difficultyDialogState = true
                        } else {
                            //TODO: continue game
//                            gameViewModel.startPlayGame(isContinue = true)
                            gameViewModel.updatePlayingGameConfig(PlayingGameConfig(isContinue = true))
                            navController.navigate(SCREEN_GAME_PLAY)
                        }
                    }, onStartDailyChallenge = { date ->
                        //TODO: 每日挑战，需要检测是否已开始
//                        gameViewModel.startPlayGame(date = date)
                        gameViewModel.updatePlayingGameConfig(PlayingGameConfig(day = date))
                        navController.navigate(SCREEN_GAME_PLAY)
                    })
                }
                composable(SCREEN_CHALLENGE) {
                    Challenge()
                }
                composable(SCREEN_EXPLORE) {
                    Explore()
                }
                composable(SCREEN_PERSONAL) {
                    Personal(gameViewModel = gameViewModel)
                }
                composable(SCREEN_GAME_PLAY) {
                    GamePlay(
                        lifecycleOwner = lifecycleOwner,
                        gameViewModel = gameViewModel,
                        navController = navController,
                        onGameWin = {

                        },
                        onQuit = {
                          navController.navigate(SCREEN_HOME)
                        },
                        playingGameConfig
                    )
                }
                composable(SCREEN_SETTING) {
                    Settings(gameViewModel) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

}

fun convertMillisToTimeFormat(millis: Long): String {
    val totalSeconds = millis / 1000
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600
    var result: String = ""
    if (hours > 0) {
        result += if (hours > 9) {
            "${hours}:"
        } else {
            "0${hours}:"
        }
    }
    result += if (minutes > 9) {
        "${minutes}:"
    } else {
        "0${minutes}:"
    }
    result += if (seconds > 9) {
        "${seconds}"
    } else {
        "0${seconds}"
    }

    return result
}