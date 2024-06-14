package composes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import models.Cell
import models.Note
import theme.SudokuBoardColors
import viewModels.GameState
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

@Composable
fun SudokuBoard(
    selectedCell: Cell?,
    cells: List<List<Cell>>,
    notes: List<Note>,
    boardColors: SudokuBoardColors,
    gameState: GameState = GameState.PREPARING,
    size: Int = 9,
    textSize: TextUnit = when (size) {
        6 -> 32.sp
        9 -> 26.sp
        12 -> 24.sp
        else -> 14.sp
    },
    noteTextSize: TextUnit = when (size) {
        6 -> 18.sp
        9 -> 10.sp
        12 -> 7.sp
        else -> 14.sp
    },
    enabled: Boolean = true,
    highLightSameNumber: Boolean = true,
    highSameSector: Boolean = true,
    highLineAndCol: Boolean = true,
    onCellSelected: (Cell) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)) {
        var isFirst = remember { mutableStateOf(true) }

        val maxWidth = constraints.maxWidth.toFloat()
        val cellSize by remember(size) { mutableStateOf(maxWidth / size.toFloat()) }

//        val vertThick by remember(size) { mutableStateOf(floor(sqrt(size.toFloat())).toInt()) }
//        val horThick by remember(size) { mutableStateOf(ceil(sqrt(size.toFloat())).toInt()) }
//        var fontSizePx = with(LocalDensity.current) { mainTextSize.toPx() }
//        var noteSizePx = with(LocalDensity.current) { noteTextSize.toPx() }
        val cellSizeDp = with(LocalDensity.current) { cellSize.toDp() }
        val cellPaddingDp = with(LocalDensity.current) { 0.65.dp }

        val thinLineWidth = with(LocalDensity.current) { 1.3.dp.toPx() }
        val thickLineWidth = with(LocalDensity.current) { 1.3.dp.toPx() }
        var dragPos by remember { mutableStateOf(Offset.Zero) }
        if (cells.isNotEmpty() && !(gameState == GameState.PREPARING || gameState == GameState.READY)) {
            Column(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                detectDragGestures(onDragStart = { offset ->
                    dragPos = offset
                }) { change, dragAmount ->
                    try {
                        dragPos += dragAmount
                        val row = floor((dragPos.y) / cellSize).toInt()
                        val column = floor((dragPos.x) / cellSize).toInt()
                        onCellSelected(cells[row][column])
                    } catch (e: Exception) {
                        println("err::${e.message}")
                        e.printStackTrace()
                    } finally {
                        change.consume()
                    }
                }
            }) {
                for (r in 0..<size) {
                    Row(modifier = Modifier.fillMaxWidth().height(cellSizeDp)) {
                        for (c in 0..<size) {
                            val cell = cells[r][c]
                            val isSelected = cell.isEqual(selectedCell)
                            val sameNumber = highLightSameNumber && cell.isEqualValue(selectedCell)
                            val highLight = cell.isAssociatedCells(
                                selectedCell,
                                lineAndCol = highLineAndCol,
                                sectors = highSameSector
                            )
                            val color =
                                if (sameNumber || isSelected) boardColors.selectedColor else boardColors.highlightColor.copy(
                                    alpha = if (highLight) 1.0f else 0.0f
                                )
                            Box(
                                modifier = Modifier.size(cellSizeDp).padding(cellPaddingDp)
                                    .background(color = color).clickable(enabled = true, onClick = {
                                        onCellSelected(cell)
                                    }),
                            ) {
                                gameCell(
                                    size,
                                    cell,
                                    boardColors,
                                    textSize,
                                    noteTextSize,
                                    notes.filter { it.row == cell.row && it.col == cell.col }
                                )
                            }
                        }
                    }
                }
            }
        }
        drawLines(size, cellSize, boardColors, maxWidth, thinLineWidth, thickLineWidth)

    }
}

@Composable
fun gameCell(
    size: Int,
    cell: Cell,
    boardColors: SudokuBoardColors,
    textSize: TextUnit,
    noteTextSize: TextUnit,
    notes: List<Note>
) {
    if (cell.value != 0) {
        Text(
            text = if (cell.value == 0) "" else "${cell.value}",
            color = if (cell.isValid()) boardColors.textColor else boardColors.errorColor,
            fontSize = textSize,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        if (notes.isNotEmpty()) {
            val maxCol: Int = size / 3
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                var pos = 1;
                for (row in 1..3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (col in 1..maxCol) {
                            Text(
                                text = "${pos}",
                                lineHeight = noteTextSize,
                                fontSize = noteTextSize,
                                color = boardColors.notesColor.copy(alpha = notes.find { it.value == pos }
                                    ?.let { 1.0f } ?: 0.0f)
                            )
                            pos += 1
                        }
                    }
                }
            }
        }
    }

}

fun Cell.isValid(): Boolean {
    return this.value == this.answer
}

fun Cell.isEqual(other: Cell?): Boolean {
    return other?.let { ot ->
        this.row == ot.row && this.col == ot.col
    } ?: false
}

fun Cell.isEqualValue(other: Cell?): Boolean {
    return other?.let { it.value != 0 && it.value == this.value } ?: false
}

fun Cell.isAssociatedCells(
    other: Cell?,
    lineAndCol: Boolean = true,
    sectors: Boolean = true
): Boolean {
    return other?.let {
        lineAndCol && this.row == it.row || lineAndCol && this.col == it.col || sectors && this.sector == it.sector
    } ?: false
}

@Composable
fun drawLines(
    size: Int,
    cellSize: Float,
    boardColors: SudokuBoardColors,
    maxWidth: Float,
    thinLineWidth: Float,
    thickLineWidth: Float
) {
    val vertThick by remember(size) { mutableStateOf(floor(sqrt(size.toFloat())).toInt()) }
    val horThick by remember(size) { mutableStateOf(ceil(sqrt(size.toFloat())).toInt()) }
    Canvas(modifier = Modifier.fillMaxSize()) {
        // horizontal line
        for (i in 1 until size) {
            val isThickLine = i % horThick == 0
            drawLine(
                color = if (isThickLine) boardColors.thickLineColor else boardColors.thinLineColor,
                start = Offset(cellSize * i.toFloat(), 0f),
                end = Offset(cellSize * i.toFloat(), maxWidth),
                strokeWidth = if (isThickLine) thickLineWidth else thinLineWidth
            )
        }
        // vertical line
        for (i in 1 until size) {
            val isThickLine = i % vertThick == 0
            if (maxWidth >= cellSize * i) {
                drawLine(
                    color = if (isThickLine) boardColors.thickLineColor else boardColors.thinLineColor,
                    start = Offset(0f, cellSize * i.toFloat()),
                    end = Offset(maxWidth, cellSize * i.toFloat()),
                    strokeWidth = if (isThickLine) thickLineWidth else thinLineWidth
                )
            }
        }

        //外框
        drawRoundRect(
            color = boardColors.thickLineColor,
            topLeft = Offset.Zero,
            size = Size(maxWidth, maxWidth),
            cornerRadius = CornerRadius(15f, 15f),
            style = Stroke(
                width = thickLineWidth
            )
        )

    }
}