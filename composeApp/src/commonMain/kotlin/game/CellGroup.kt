//package game
//
//class CellGroup {
//
//    val cells = Array(CellCollection.SUDOKU_SIZE) { Cell() }
//    private var pos = 0
//
//    fun addCell(cell: Cell) {
//        cells[pos] = cell
//        pos++
//    }
//
//    fun contains(value: Int): Boolean {
//        for (cell in cells) {
//            if (cell.value == value) {
//                return true
//            }
//        }
//        return false
//    }
//
//    val valid: Boolean
//        get() {
//            for (cell in cells) {
//                if (!cell.valid) return false
//            }
//            return true
//        }
//
//    fun has(cell: Cell): Boolean {
//        return cells.find { cell.id == it.id && cell.rowIndex == it.rowIndex && cell.colIndex == it.colIndex }
//            ?.let { true } ?: false
//    }
//
//}