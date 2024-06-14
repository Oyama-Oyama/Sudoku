//package game
//
//import models.GameData
//
//
//class Game(private var data: GameData) {
//
//    private lateinit var cellCollection: CellCollection
//    private var commands: CommandStack
//
//    var size:Int = 9
//
//    init {
//        data.board?.let {
//            cellCollection = CellCollection(it, data.solution!!)
//        } ?: run {
//            cellCollection = CellCollection(data.mission!!, data.solution!!)
//        }
//        commands = CommandStack(data.stack)
//    }
//
//    fun getCells(): Array<Array<Cell>> = cellCollection.getCells()
//
//    fun getCell(rowIndex: Int, colIndex: Int): Cell = cellCollection.getCell(rowIndex, colIndex)
//
//    fun getAssociatedCells(
//        cell: Cell,
//        lineAndCol: Boolean = true,
//        sector: Boolean = true
//    ): List<Cell> = cellCollection.getAssociatedCells(cell, lineAndCol, sector)
//
//    fun setValue(cell: Cell, value: Int) = cellCollection.setValue(cell, value)
//
//    fun addNote(cell: Cell, value: Int) = cellCollection.addNote(cell, value)
//
//    fun fillNote() = cellCollection.fillNote()
//
//    fun isRowValid(cell: Cell) = cellCollection.isRowValid(cell)
//
//    fun isColValid(cell: Cell) = cellCollection.isColValid(cell)
//
//    fun isSectorValid(cell: Cell) = cellCollection.isSectorValid(cell)
//
//    fun encode(): String {
//        return ""
//    }
//
//    fun play() {
//
//    }
//
//    fun pause() {
//
//    }
//
//    fun finish() {
//
//    }
//
//
//}