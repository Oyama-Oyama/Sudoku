package composes.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.unit.Dp

//@Composable
//fun GridItem(content: @Composable () -> Unit) {
//    Div(attrs = {
//        style {
//            property("flex", "1 1 auto")
//            margin(4.px)
//        }
//    }) {
//        content()
//    }
//}
//
//@Composable
//fun GridRow(items: List<@Composable () -> Unit>) {
//    Div(attrs = {
//        style {
//            display(DisplayStyle.Flex)
//            flexDirection(FlexDirection.Row)
//            flexWrap(FlexWrap.Wrap)
//        }
//    }) {
//        items.forEach { item ->
//            GridItem(content = item)
//        }
//    }
//}
//
//@Composable
//fun GridLayout(itemsPerRow: Int, items: List<@Composable () -> Unit>) {
//    // 将items列表分成多个子列表，每个子列表包含itemsPerRow个元素
//    val rows = items.chunked(itemsPerRow)
//    rows.forEach { rowItems ->
//        GridRow(rowItems)
//    }
//}

@Composable
fun <T> GridLayout(
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    line: Int = 1,
    data: List<T>,
    itemSpace: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable (width: Dp, t: T) -> Unit
) {
    val chunked = data.chunked(line)
    BoxWithConstraints (modifier = Modifier.fillMaxWidth()) {
        val maxWidth by remember { mutableStateOf(maxWidth * 1.0f / line) }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment
        ) {
            chunked.forEach { group ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = itemSpace
                ) {
                    group.forEach { item ->
                        content.invoke(maxWidth, item)
                    }
                }
            }
        }
    }
}