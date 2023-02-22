package com.roman.garden.sudo.base.game

import com.roman.garden.sudo.base.action.IMirror
import com.roman.garden.sudo.base.game.square.SquareMirror
import com.roman.garden.sudo.base.util.GameSize
import com.roman.garden.sudo.base.util.LogUtil
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.system.measureTimeMillis

internal abstract class ISpliceCreator(gameSize: GameSize) : ICreator(gameSize) {

    private var solvedCells: Stack<Cell> = Stack()
    protected lateinit var data: Array<Array<Cell?>>
    private var lastSelectedArea = -1
    protected var countCells = 0

    protected val AREA_FIRST = 1
    protected val AREA_SECOND = 2
    protected val AREA_THIRD = 3
    protected val AREA_FOURTH = 4
    protected val AREA_FIFTH = 5

    override fun createGame() {
        val measureTime = measureTimeMillis {
            realCreateGame()
            solvedCells.clear()
            var count = getEmptyCellCount()
            while (count > 0) {
                val row = random.nextInt(gameSize.row)
                val col = random.nextInt(gameSize.col)
                data[row][col]?.let { it ->
                    it.value = 0
                    it.valid = false
                    it.preSet = false
                    count--
                }
            }
            data.forEach { it ->
                it.forEach { c ->
                    c?.let { item ->
                        item.optional.clear()
                        val status = item.value != 0
                        item.preSet = status
                        item.valid = status
                    }
                }
            }
        }
        LogUtil.e("splice game:${gameSize.tag}  created cost:$measureTime ")
        printGame()
    }

    override fun destroy() {
        data = emptyArray()
    }

    override fun resetGame() {
        data.forEach { it ->
            it.forEach { cell ->
                cell?.let { item ->
                    if (!item.preSet) {
                        item.value = 0
                        item.valid = false
                        item.optional.clear()
                    }
                }
            }
        }
    }

    override fun isGameOver(): Boolean {
        data.forEach { it ->
            it.forEach { cell ->
                cell?.let { item ->
                    if (!item.preSet && (item.value == 0 || !item.valid)) {
                        return false
                    }
                }
            }
        }
        return true
    }

    override fun getSpliceData(): Array<Array<Cell?>>? = data

    override fun getCell(row: Int, col: Int): Cell? = data[row][col]

    override fun getCellCount(): Int = countCells

    override fun recordGame(): IMirror? {
        encodeGame()?.let {
            return SquareMirror(it)
        }
        return null
    }

    override fun recoverGame(iMirror: IMirror?) = decodeGame(iMirror)

    override fun isValid(cell: Cell): Boolean = cell.value != 0 && cell.valid

    override fun getRelatedCells(
        cell: Cell,
        highLightLineOrRow: Boolean,
        highLightGroup: Boolean,
        highLightSameNumber: Boolean
    ): MutableList<Cell> {
        val list = mutableListOf<Cell>()
        getCellAreas(cell.row, cell.col).let { it ->
            if (it.size > 1) {
                if (it.contains(lastSelectedArea)) {

                } else {
                    lastSelectedArea = it[0]
                }
            } else {
                if (it[0] == -1) {
                    return list
                }
                lastSelectedArea = it[0]
            }
        }
        var status = false
        val pair = getArea(lastSelectedArea)
        for (i in pair.first..(pair.first + 8)) {
            for (j in pair.second..(pair.second + 8)) {
                status = false
                data[i][j]?.let { item ->
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
        }
        return list
    }

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

    override fun printGame() {
        for (i in 0 until gameSize.row) {
            for (j in 0 until gameSize.col) {
                data[i][j]?.let {
                    print("${it.value} ")
                } ?: print("  ")
            }
            println()
        }
    }

    protected fun calGroup(row: Int, col: Int): Int {
        return col / 3 + row / 3 * 6
    }

    /**
     *  area: 指定生成区域
     */
    protected fun buildGame(area: Int): Boolean {
        solvedCells.clear()
        do {
            getNextEmptyCell(area)?.let { cell ->
                cell.optional.clear()
                val leftValues = getLeftValues(area, cell)
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

    private fun getNextEmptyCell(area: Int): Cell? {
        val pair = getArea(area)
        if (area == 3) {
            for (i in (pair.first + 8) downTo pair.first) {
                for (j in pair.second..(pair.second + 8)) {
                    val cell = data[i][j]
                    val value = cell?.let { it.value } ?: -1
                    if (value == 0)
                        return cell
                }
            }
        } else {
            for (i in pair.first..(pair.first + 8)) {
                for (j in pair.second..(pair.second + 8)) {
                    val cell = data[i][j]
                    val value = cell?.let { it.value } ?: -1
                    if (value == 0)
                        return cell
                }
            }
        }
        return null
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

    private fun getLeftValues(area: Int, cell: Cell): MutableList<Int> {
        val allValues = MutableList(9) { index -> index + 1 }
        val pair = getArea(area)
        for (i in pair.first..(pair.first + 8)) {
            for (j in pair.second..(pair.second + 8)) {
                data[i][j]?.let { item ->
                    if (item.value != 0 &&
                        (item.row == cell.row ||
                                item.col == cell.col ||
                                item.group == cell.group)
                    ) {
                        allValues.remove(item.value)
                    }
                }
            }
        }
        return allValues
    }

    protected fun encodeGame(): String? {
        try {
            val array = JSONArray()
            data.forEach { it ->
                val rows = JSONArray()
                it.forEach { item ->
                    item?.toJson().let { json ->
                        rows.put(json)
                    } ?: rows.apply { rows.put(JSONObject()) }
                }
                array.put(rows)
            }
            return array.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    protected fun decodeGame(iMirror: IMirror?) {
        try {
            iMirror?.let {
                if (!iMirror.valid()) throw IllegalArgumentException("decode game error: invalid mirror")
                val array = JSONArray(it.data)
                if (array.length() != gameSize.row)
                    throw IllegalArgumentException("decode game error: invalid mirror")
                for (i in 0 until array.length()) {
                    val rows = array.getJSONArray(i)
                    for (j in 0 until rows.length()) {
                        data[i][j]?.let { cell ->
                            cell.fromJson(rows.getJSONObject(j))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *  此单元格是否为交界处单元格？是 需要多重判断
     */
    private fun checkCellValid(cell: Cell) {
        getCellAreas(cell.row, cell.col).forEach { area ->
            getRelatedCells(cell, false, area).forEach { item ->
                if (item.value == cell.value) {
                    cell.valid = false
                    return
                }
            }
        }
        cell.valid = true
    }

    private fun checkRelatedCellsValid(cell: Cell) {
        getCellAreas(cell.row, cell.col).forEach { area ->
            getRelatedCells(cell, true, area).forEach { item ->
                if (!item.preSet)
                    checkCellValid(item)
            }
        }
    }

    private fun getRelatedCells(cell: Cell, containSelf: Boolean, area: Int): List<Cell> {
        val list = mutableListOf<Cell>()
        val pair = getArea(area)
        for (i in pair.first..(pair.first + 8)) {
            for (j in pair.second..(pair.second + 8)) {
                data[i][j]?.let { item ->
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
        }
        return list
    }

    protected abstract fun realCreateGame()

    /**
     *  返回包含 传入 行、列 坐标的区域
     */
    protected abstract fun getCellAreas(row: Int, col: Int): Array<Int>

    /**
     *  根据行、列坐标获得cell所在区域
     */
    protected abstract fun getCellArea(row: Int, col: Int): Int

}