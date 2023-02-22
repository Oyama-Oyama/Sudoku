package com.roman.garden.sudo.base.game.square

import com.roman.garden.sudo.base.action.IMirror
import com.roman.garden.sudo.base.game.Cell
import com.roman.garden.sudo.base.game.ICreator
import com.roman.garden.sudo.base.util.GameSize
import com.roman.garden.sudo.base.util.LogUtil
import org.json.JSONArray
import java.util.*
import kotlin.system.measureTimeMillis

internal abstract class SquareGameCreator constructor(gameSize: GameSize) : ICreator(gameSize) {

    private var data: Array<Array<Cell>>
    private var solvedCells: Stack<Cell> = Stack()


    init {
        data = Array(gameSize.row) { row ->
            Array(gameSize.col) { col ->
                val group = calGroup(row, col)
                Cell(row, col, group, 0)
            }
        }
    }

    override fun getCellCount(): Int {
        return gameSize.row * gameSize.col
    }

    override fun createGame() {
        val measureTime = measureTimeMillis {
            this.buildGame()
            solvedCells.clear()
            var count = getEmptyCellCount()
            while (count > 0) {
                val row = random.nextInt(gameSize.row)
                val col = random.nextInt(gameSize.col)
                if (data[row][col].value != 0) {
                    data[row][col].value = 0
                    data[row][col].valid = false
                    data[row][col].preSet = false
                    count--
                }
            }
            data.forEach { it ->
                it.forEach { item ->
                    item.optional.clear()
                    val status = item.value != 0
                    item.preSet = status
                    item.valid = status
                }
            }
        }
        LogUtil.d("createGame ${gameSize.tag}, diff:${difficulty.value} used: $measureTime")
    }

    override fun printGame() {

    }

    override fun destroy() {
        data = emptyArray()
    }

    override fun resetGame() {
        data.forEach { it ->
            it.forEach { item ->
                if (!item.preSet) {
                    item.value = 0
                    item.valid = false
                    item.optional.clear()
                }
            }
        }
    }

    override fun getCell(row: Int, col: Int): Cell? = data[row][col]

    override fun getGameData(): Array<Array<Cell>>? = data

    override fun setValue(cell: Cell, value: Int, isNote: Boolean) {
        if (cell.preSet) return
        if (isNote) {
            if (value == 0) return
            if (cell.optional.contains(value)) {
                cell.optional.remove(value)
            } else {
                cell.optional.add(value)
            }
            return
        } else {
            val preValue = cell.value
            cell.value = value
            if (preValue != 0 || cell.value == 0) {
                checkRelatedCellsValid(cell)
            } else {
                checkCellValid(cell)
            }
        }
    }

    override fun isValid(cell: Cell): Boolean = cell.value != 0 && cell.valid

    override fun isGameOver(): Boolean {
        data.forEach { it ->
            it.forEach { item ->
                if (!item.preSet && (item.value == 0 || !item.valid)) {
                    return false
                }
            }
        }
        return true
    }

    override fun getRelatedCells(
        cell: Cell,
        highLightLineOrRow: Boolean,
        highLightGroup: Boolean,
        highLightSameNumber: Boolean
    ): MutableList<Cell> {
        val list = mutableListOf<Cell>()
        var status = false
        data.forEach { it ->
            it.forEach { item ->
                status = false
                if (highLightLineOrRow && (item.row == cell.row || item.col == cell.col)) {
                    status = true
                }
                if (highLightGroup && item.group == cell.group) {
                    status = true
                }
                if (highLightSameNumber && item.value == cell.value && cell.value != 0) {
                    status = true
                }
                if (status) list.add(item)
            }
        }
        return list
    }

    private fun getRelatedCells(cell: Cell, containSelf: Boolean): List<Cell> {
        val list = mutableListOf<Cell>()
        data.forEach { it ->
            it.forEach { item ->
                if (cell.row == item.row || cell.col == item.col || cell.group == item.group) {
                    if (containSelf) {
                        list.add(item)
                    } else {
                        if (cell.row == item.row && cell.col == item.col && cell.group == item.group) {

                        } else {
                            list.add(item)
                        }
                    }
                }
            }
        }
        return list
    }

    override fun recordGame(): IMirror? {
        encodeGame()?.let {
            return SquareMirror(it)
        }
        return null
    }

    override fun recoverGame(iMirror: IMirror?) = decodeGame(iMirror)

    protected abstract fun calGroup(row: Int, col: Int): Int

    private fun buildGame(): Boolean {
        solvedCells.clear()
        do {
            getNextEmptyCell()?.let { cell ->
                cell.optional.clear()
                val leftValues = getLeftValues(cell)
                if (leftValues.isEmpty()) {
                    try {
                        if (solvedCells.size > 0) {
                            peekCell()
                        } else {
                            return false
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        return false
                    }
                } else {
                    cell.optional.addAll(leftValues)
                    val index = random.nextInt(cell.optional.size)
                    cell.value = cell.optional.removeAt(index)
                    solvedCells.push(cell)
                }
            } ?: return true
        } while (true)
    }

    private fun getNextEmptyCell(): Cell? {
        data.forEach { it ->
            it.forEach { cell ->
                if (cell.value == 0) return cell
            }
        }
        return null
    }

    private fun getLeftValues(cell: Cell): MutableList<Int> {
        val allValues = MutableList(gameSize.row) { index -> index + 1 }
        data.forEach { it ->
            it.forEach { item ->
                if (item.value != 0 &&
                    (item.row == cell.row ||
                            item.col == cell.col ||
                            item.group == cell.group)
                ) {
                    allValues.remove(item.value)
                }
            }
        }
        return allValues
    }

    private fun getUsedValues(cell: Cell): List<Int> {
        val values = mutableListOf<Int>()
        data.forEach { it ->
            it.forEach { item ->
                if (item.value != 0 &&
                    (item.row == cell.row ||
                            item.col == cell.col ||
                            item.group == cell.group)
                ) {
                    values.add(item.value)
                }
            }
        }
        return values
    }

    private fun peekCell() {
        do {
            solvedCells.pop().let {
                if (it.optional.size <= 0) {
                    it.value = 0
                } else {
                    val index = random.nextInt(it.optional.size)
                    it.value = it.optional.removeAt(index)
                    solvedCells.push(it)
                    return
                }
            }
        } while (true)
    }


    private fun checkCellValid(cell: Cell) {
        getRelatedCells(cell, false).forEach { item ->
            if (item.value == cell.value) {
                cell.valid = false
                return
            }
        }
        cell.valid = true
    }

    private fun checkRelatedCellsValid(cell: Cell) {
        getRelatedCells(cell, true).forEach { item ->
            if (!item.preSet)
                checkCellValid(item)
        }
    }

    private fun encodeGame(): String? {
        try {
            val array = JSONArray()
            data.forEach { it ->
                val rows = JSONArray()
                it.forEach { item ->
                    item.toJson().let { json ->
                        rows.put(json)
                    }
                }
                array.put(rows)
            }
            return array.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun decodeGame(iMirror: IMirror?) {
        try {
            iMirror?.let {
                if (!iMirror.valid()) throw IllegalArgumentException("decode game error: invalid mirror")
                val array = JSONArray(it.data)
                if (array.length() != gameSize.row)
                    throw IllegalArgumentException("decode game error: invalid mirror")
                for (i in 0 until array.length()) {
                    val rows = array.getJSONArray(i)
                    for (j in 0 until rows.length()) {
                        data[i][j].fromJson(rows.getJSONObject(j))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}