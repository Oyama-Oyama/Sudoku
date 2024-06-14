package composes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composesudoku.composeapp.generated.resources.Res
import composesudoku.composeapp.generated.resources.app_title
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import theme.ThemeColors

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Splash(onFinish: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(1.0f)
            .background(MaterialTheme.colorScheme.ThemeColors.SplashBackgroundColor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        Image(
            imageResource(DrawableResource("drawable-xxhdpi/launch.png")),
            contentDescription = "splash icon",
            modifier = Modifier.size(
                Dp(144.0f)
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.app_title),
            color = MaterialTheme.colorScheme.ThemeColors.SplashTitleColor,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.weight(1.0f))
        LaunchedEffect(Unit) {
            delay(1 * 1000)
            onFinish()
        }

    }
}