//package game
//
//class Cell {
//
//    var id: Int = -1
//    var value: Int = -1
//    var answer: Int = -1
//    var rowIndex: Int = -1
//    var colIndex: Int = -1
//    lateinit var sector: CellGroup
//    lateinit var row: CellGroup
//    lateinit var col: CellGroup
//
//    var isPreSet = false
//    val valid: Boolean
//        get() {
//            return if (isPreSet) true else value == answer
//        }
//
//    var notes = mutableListOf<Int>()
//
//    fun initCell(
//        id: Int,
//        value: Int,
//        answer: Int,
//        rowIndex: Int,
//        colIndex: Int,
//        sector: CellGroup,
//        row: CellGroup,
//        col: CellGroup
//    ) {
//        this.id = id
//        this.value = value
//        this.answer = answer
//        this.rowIndex = rowIndex
//        this.colIndex = colIndex
//        this.sector = sector
//        this.row = row
//        this.col = col
//        this.isPreSet = value == answer
//
//        sector.addCell(this)
//        row.addCell(this)
//        col.addCell(this)
//    }
//
//    fun updateValue(v: Int) {
//        this.value = v
//    }
//
//    fun addNote(value: Int) {
//        if (notes.contains(value)) {
//            notes.remove(value)
//        } else {
//            notes.add(value)
//        }
//    }
//
//    fun fillNote() {
//        for (i in 1..9) {
//            addNote(i)
//        }
//    }
//
//    //重写 equal 需要注意判别，否则可能打造sateflow无法触发 state 变化
////    override fun equals(other: Any?): Boolean {
////        return (other as? Cell)?.let { ot ->
////            this.id == ot.id && this.rowIndex == ot.rowIndex && this.colIndex == ot.colIndex
////        } ?: false
////    }
//
//    fun isSame(other: Cell?): Boolean {
//        return other?.let { ot ->
//            this.id == ot.id && this.rowIndex == ot.rowIndex && this.colIndex == ot.colIndex
//        } ?: false
//    }
//
//    fun isSameNumber(other: Cell?): Boolean {
//        return other?.let { it.value != 0 && it.value == this.value } ?: false
//    }
//
//    fun isAssociatedCells(
//        other: Cell?,
//        lineAndCol: Boolean = true,
//        sectors: Boolean = true
//    ): Boolean {
//        return other?.let {
//            lineAndCol && col.has(it) || lineAndCol && row.has(it) || sectors && sector.has(it)
//        } ?: false
//    }
//
//}