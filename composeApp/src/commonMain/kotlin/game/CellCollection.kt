//package game
//
//class CellCollection constructor(private val mission:String, private val solution:String) {
//
//    private var mSectors: Array<CellGroup>
//    private var mRows: Array<CellGroup>
//    private var mCols: Array<CellGroup>
//    private var cells: Array<Array<Cell>> =
//        Array(SUDOKU_SIZE) { Array(SUDOKU_SIZE) { Cell() } }
//
//    companion object {
//        val SUDOKU_SIZE = 9
//    }
//
//    init {
//        val missions: List<String> = mission.chunked(1)
//        val answer: List<String> = solution.chunked(1)
//        var pos = 0
//
//        mRows = Array(SUDOKU_SIZE) { CellGroup() }
//        mCols = Array(SUDOKU_SIZE) { CellGroup() }
//        mSectors = Array(SUDOKU_SIZE) { CellGroup() }
//        for (r in 0..<SUDOKU_SIZE) {
//            for (c in 0..<SUDOKU_SIZE) {
//                val cell = cells[r][c]
//                cell.initCell(
//                    pos,
//                    missions[pos].toInt(),
//                    answer[pos].toInt(),
//                    r,
//                    c,
//                    mSectors[c / 3 * 3 + r / 3],
//                    mRows[c],
//                    mCols[r]
//                )
//                pos++
//            }
//        }
//    }
//
//    fun getCells():Array<Array<Cell>> = cells
//
//    fun getCell(rowIndex: Int, colIndex: Int): Cell = cells[rowIndex][colIndex]
//
//    fun getAssociatedCells(
//        cell: Cell,
//        lineAndCol: Boolean = true,
//        sector: Boolean = true
//    ): List<Cell> {
//        val data = mutableListOf<Cell>()
//        if (lineAndCol) {
//            data.addAll(cell.row.cells)
//            data.addAll(cell.col.cells)
//        }
//        if (sector) {
//            data.addAll(cell.sector.cells)
//        }
//        return data
//    }
//
//    fun setValue(cell: Cell, value: Int) {
//        if (cell.isPreSet) return
//        cell.updateValue(value)
//    }
//
//    fun addNote(cell: Cell, value: Int) {
//        if (cell.isPreSet) return
//        cell.addNote(value)
//    }
//
//    fun fillNote() {
//        for (r in 0..<SUDOKU_SIZE) {
//            for (c in 0..<SUDOKU_SIZE) {
//                if (cells[r][c].isPreSet) continue
//                cells[r][c].fillNote()
//            }
//        }
//    }
//
//    fun isRowValid(cell: Cell) = cell.row.valid
//
//    fun isColValid(cell: Cell) = cell.col.valid
//
//    fun isSectorValid(cell: Cell) = cell.sector.valid
//
//
//}