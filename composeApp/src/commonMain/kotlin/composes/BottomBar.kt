package composes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composesudoku.composeapp.generated.resources.Res
import composesudoku.composeapp.generated.resources.main_page_challenge
import composesudoku.composeapp.generated.resources.main_page_explore
import composesudoku.composeapp.generated.resources.main_page_home
import composesudoku.composeapp.generated.resources.main_page_personal
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import theme.ThemeColors

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BottomBar(selectedIndex: Int = 0, onItemSelected: (index: Int) -> Unit) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(1.0f).height(64.dp),
        containerColor = MaterialTheme.colorScheme.ThemeColors.BottomBarBackground,
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(1.0f)
        ) {
            Surface(
                modifier = Modifier.weight(1.0f),
                color = MaterialTheme.colorScheme.ThemeColors.BottomBarBackground
            ) {
                BottomBarItem(
                    0,
                    Icons.Rounded.Home,
                    stringResource(Res.string.main_page_home),
                    onItemSelected,
                    0 == selectedIndex
                )
            }
            Surface(
                modifier = Modifier.weight(1.0f),
                color = MaterialTheme.colorScheme.ThemeColors.BottomBarBackground
            ) {
                BottomBarItem(
                    1,
                    Icons.Rounded.Home,
                    stringResource(Res.string.main_page_challenge),
                    onItemSelected,
                    1 == selectedIndex
                )
            }
            Surface(
                modifier = Modifier.weight(1.0f),
                color = MaterialTheme.colorScheme.ThemeColors.BottomBarBackground
            ) {
                BottomBarItem(
                    2,
                    Icons.Rounded.Home,
                    stringResource(Res.string.main_page_explore),
                    onItemSelected,
                    2 == selectedIndex
                )
            }
            Surface(
                modifier = Modifier.weight(1.0f),
                color = MaterialTheme.colorScheme.ThemeColors.BottomBarBackground
            ) {
                BottomBarItem(
                    3,
                    Icons.Rounded.Home,
                    stringResource(Res.string.main_page_personal),
                    onItemSelected,
                    3 == selectedIndex
                )
            }
        }
    }
}

@Composable
fun BottomBarItem(
    index: Int,
    image: ImageVector,
    title: String,
    onClicked: (index: Int) -> Unit,
    selected: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxSize(1.0f).clickable(enabled = true, onClick = {
            if (!selected) {
                onClicked(index)
            }
        }),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            image,
            modifier = Modifier.size(if (selected) 32.dp else 26.dp),
            contentDescription = "",
            colorFilter = ColorFilter.tint(
                if (selected) MaterialTheme.colorScheme.ThemeColors.BottomBarActive else MaterialTheme.colorScheme.ThemeColors.BottomBarInactive,
                blendMode = BlendMode.SrcIn
            )
        )

        Text(
            text = title,
            fontSize = 12.sp,
            fontFamily = FontFamily.SansSerif,
            color = if (selected) MaterialTheme.colorScheme.ThemeColors.BottomBarActive else MaterialTheme.colorScheme.ThemeColors.BottomBarInactive
        )
    }
}

