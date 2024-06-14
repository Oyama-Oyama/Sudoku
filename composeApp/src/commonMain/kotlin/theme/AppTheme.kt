package theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CustomColors = lightColorScheme(
    primary = Color.White,
    onPrimary = Color.White,
    primaryContainer = Color.White,
    onPrimaryContainer = Color.Black,
    secondary = Color.White,
    onSecondary = Color.Gray,
    secondaryContainer = Color(0xffe6edff),
    onSecondaryContainer = Color.White,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)
//
//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
//)

enum class Themes {
    Light, Dark
}

interface BaseColors {
    //开屏背景色
    val SplashBackgroundColor: Color

    //开屏标题颜色
    val SplashTitleColor: Color

    //页面背景色
    val ScreenBackground: Color
//页面标题颜色
    val ScreenTitle:Color

    //底部导航栏背景色
    val BottomBarBackground: Color

    //底部导航栏 激活状态颜色
    val BottomBarActive: Color

    //底部导航栏 非激活状态颜色
    val BottomBarInactive: Color

    //每日挑战卡片背景色
    val DailyCardBackground: Color

    //每日挑战卡片 活跃色
    val DailyCardActive: Color

    //每日挑战卡片 通用色
    val DailyCardNormal: Color

    //每日挑战卡片 按钮背景色
    val DailyCardButtonBackground: Color

    // 未完成游戏 按钮背景色
    val continueGameButtonBackground: Color

    // 未完成游戏 按钮 文字颜色
    val continueGameButtonTitle: Color

    // 未完成游戏 按钮 副文本颜色
    val continueGameButtonSubtitle: Color

    // 新游戏 按钮 背景色
    val NewGameButtonBackground: Color

    // 新游戏 按钮 文字颜色
    val NewGameButtonTitle: Color

    // 新游戏 难度选择弹框 背景颜色
    val difficultyDialogBackground: Color

    // 新游戏 难度选择弹框  每条目背景颜色
    val difficultyDialogItemBackground: Color

    // 新游戏 难度选择弹框  每条目 标题颜色
    val difficultyDialogItemTitle: Color

    // 新游戏 难度选择弹框  每条目 副标题颜色
    val difficultyDialogItemSubtitle: Color

    //游戏信息
    val gameInfo: Color

    // 游戏页 icon 颜色
    val gamePlayIcons: Color

    // 游戏页 icon 颜色
    val gamePlayIconsActive: Color

    //loading 颜色
    val progressBarColor: Color
    val progressBarBackgroundColor: Color

    val dialogTitle: Color
    val dialogSubtitle: Color
    val dialogTipAreaBackground: Color
    val dialogActiveButtonBackground: Color
    val dialogActiveButtonTitle: Color
    val dialogInactiveButtonTitle: Color

}

class LightColors : BaseColors {
    override val SplashBackgroundColor: Color = Color(0xff2196f3)
    override val SplashTitleColor: Color = Color.White
    override val ScreenBackground: Color = Color(0xfff5f5f5)
    override val ScreenTitle: Color = Color.Black
    override val BottomBarBackground: Color = Color.White
    override val BottomBarActive: Color = Color(0xff2196f3)
    override val BottomBarInactive: Color = Color.LightGray
    override val DailyCardBackground: Color = Color.White
    override val DailyCardActive: Color = Color(0xff2196f3)
    override val DailyCardNormal: Color = Color.Black
    override val DailyCardButtonBackground: Color = Color.White.copy(alpha = 0.8f)
    override val continueGameButtonBackground: Color = Color(0xff2196f3)
    override val continueGameButtonTitle: Color = Color.White
    override val continueGameButtonSubtitle: Color = Color.White.copy(alpha = 0.75f)
    override val NewGameButtonBackground: Color = Color.White
    override val NewGameButtonTitle: Color = Color(0xff2196f3)
    override val difficultyDialogBackground: Color = Color(0xfff5f5f5)
    override val difficultyDialogItemBackground: Color = Color.White
    override val difficultyDialogItemTitle: Color = Color.Black
    override val difficultyDialogItemSubtitle: Color = Color(0xffdbdce0)
    override val gameInfo: Color = Color(0xffdbdce0)
    override val gamePlayIcons: Color = Color.Black
    override val gamePlayIconsActive: Color = Color(0xff2196f3)
    override val progressBarColor: Color = Color(0xff2196f3)
    override val progressBarBackgroundColor: Color = Color.White.copy(alpha = 0.75f)
    override val dialogTitle: Color = Color.Black
    override val dialogSubtitle: Color = Color.Black.copy(alpha = 0.8f)
    override val dialogTipAreaBackground: Color = Color(0xffdbdce0)
    override val dialogActiveButtonBackground: Color = Color(0xff2196f3)
    override val dialogActiveButtonTitle: Color = Color.White
    override val dialogInactiveButtonTitle: Color = Color(0xff2196f3)
}

class DarkColors : BaseColors {
    override val SplashBackgroundColor: Color = Color(0xff2196f3)
    override val SplashTitleColor: Color = Color.White
    override val ScreenBackground: Color = Color(0xfff5f5f5)
    override val ScreenTitle: Color = Color.Black
    override val BottomBarBackground: Color = Color.White
    override val BottomBarActive: Color = Color(0xff2196f3)
    override val BottomBarInactive: Color = Color.LightGray
    override val DailyCardBackground: Color = Color.White
    override val DailyCardActive: Color = Color(0xff2196f3)
    override val DailyCardNormal: Color = Color.Black
    override val DailyCardButtonBackground: Color = Color.Gray
    override val continueGameButtonBackground: Color = Color(0xff2196f3)
    override val continueGameButtonTitle: Color = Color.White
    override val continueGameButtonSubtitle: Color = Color.White.copy(alpha = 0.75f)
    override val NewGameButtonBackground: Color = Color.White
    override val NewGameButtonTitle: Color = Color(0xff2196f3)
    override val difficultyDialogBackground: Color = Color(0xf5f5f5)
    override val difficultyDialogItemBackground: Color = Color.White
    override val difficultyDialogItemTitle: Color = Color.Black
    override val difficultyDialogItemSubtitle: Color = Color(0xdbdce0)
    override val gameInfo: Color = Color(0xffdbdce0)
    override val gamePlayIcons: Color = Color.Black
    override val gamePlayIconsActive: Color = Color(0xff2196f3)
    override val progressBarColor: Color = Color(0xff2196f3)
    override val progressBarBackgroundColor: Color = Color.White.copy(alpha = 0.75f)
    override val dialogTitle: Color = Color.Black
    override val dialogSubtitle: Color = Color.Black.copy(alpha = 0.8f)
    override val dialogTipAreaBackground: Color = Color(0xffdbdce0)
    override val dialogActiveButtonBackground: Color = Color(0xff2196f3)
    override val dialogActiveButtonTitle: Color = Color.White
    override val dialogInactiveButtonTitle: Color = Color(0xff2196f3)
}

interface SudokuBoardColors {
    val thickLineColor: Color
    val thinLineColor: Color
    val textColor: Color
    val notesColor: Color
    val highlightColor: Color
    val errorColor: Color
    val selectedColor: Color
}

class LightSudokuBoard : SudokuBoardColors {
    override val errorColor: Color = Color.Red
    override val thickLineColor: Color = Color.Black
    override val thinLineColor: Color = Color.Gray
    override val textColor: Color = Color.Black
    override val notesColor: Color = Color.Gray
    override val highlightColor: Color = Color.Green
    override val selectedColor: Color = Color.Yellow
}

class DarkSudokuBoard : SudokuBoardColors {
    override val errorColor: Color = Color.Red
    override val thickLineColor: Color = Color.Black
    override val thinLineColor: Color = Color.Gray
    override val textColor: Color = Color.Black
    override val notesColor: Color = Color.Gray
    override val highlightColor: Color = Color.Blue.copy(alpha = 0.5f)
    override val selectedColor: Color = Color.Yellow
}

var appTheme: Themes = Themes.Light

val ColorScheme.ThemeColors: BaseColors
    get() = when (appTheme) {
        Themes.Light -> LightColors()
        Themes.Dark -> DarkColors()
    }

val ColorScheme.BoardColors: SudokuBoardColors
    get() = when (appTheme) {
        Themes.Light -> LightSudokuBoard()
        Themes.Dark -> DarkSudokuBoard()
    }

@Composable
fun ComposeSudokuTheme(
    theme: Themes,
    content: @Composable () -> Unit
) {

    appTheme = theme
    MaterialTheme(colorScheme = CustomColors, content = content)
}