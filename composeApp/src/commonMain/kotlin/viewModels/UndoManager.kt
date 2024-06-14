package viewModels

import models.Cell
import models.Command
import models.CommandImpl
import models.Note

class UndoManager(boardSize: Int, commands: List<Command>?) {

    companion object {
        fun decodeBoard(
            size: Int,
            mission: String,
            solution: String
        ): MutableList<MutableList<Cell>> {
            val answer: List<String> = solution.chunked(1)
            val missions: List<String> = mission.chunked(1)
            var pos = -1
            return MutableList(size) { r ->
                MutableList(size) { c ->
                    pos += 1
                    val value = missions[pos].toInt()
                    val sector = c / 3 * 3 + r / 3
                    Cell(
                        row = r,
                        col = c,
                        sector = sector,
                        value = value,
                        answer = answer[pos].toInt(),
                        locked = value != 0
                    )
                }
            }
        }

        fun encodeBoard(board: List<List<Cell>>): String {
            val builder: StringBuilder = StringBuilder()
            board.map { row ->
                row.map { cell ->
                    builder.append(cell.value.toString())
                }
            }
            return builder.toString()
        }

        fun decodeNotes(data: String?): MutableList<Note> {
            return data?.let {
                val items = it.chunked(4)
                MutableList(items.size) { index ->
                    val values = items[index].chunked(1)
                    Note(
                        row = values[0].toInt(),
                        col = values[1].toInt(),
                        sector = values[2].toInt(),
                        value = values[3].toInt()
                    )
                }
            } ?: mutableListOf<Note>()
        }

        fun encodeNotes(notes: List<Note>): String {
            val builder = StringBuilder()
            notes.map {
                builder.append(it.encodeToString())
            }
            return builder.toString()
        }
    }


    private lateinit var states: List<CommandImpl>

    init {
        commands?.let { list ->
            val temp = MutableList(list.size) { index ->
                val item = list[index]
                val boards = decodeBoard(boardSize, item.board, item.solution)
                val notes = decodeNotes(item.notes)
                CommandImpl(
                    item.index,
                    boards,
                    item.solution,
                    item.selectedRow,
                    item.selectedCol,
                    item.onNote,
                    notes
                )
            }
            states = temp.sortedBy { it.index }
        } ?: run {
            states = mutableListOf()
        }

    }

    fun push(impl: CommandImpl) {
        states = states.plus(impl)
        if (states.count() > 50) {
            states = states.takeLast(50)
        }
    }

    fun pop(): CommandImpl? {
        if (states.isEmpty()) {
            return null
        }
        states = states.dropLast(1)
        return states.lastOrNull()
    }

    fun count(): Int = states.count()
    fun clear() {
        states = emptyList()
    }

    fun convertToCommand(): List<Command> {
        if (states.isEmpty()) return listOf()
        return MutableList(states.size) { index ->
            states[index].convertToCommand()
        }
    }


}

fun Note.encodeToString(): String {
    return "${this.row}${this.col}${this.sector}${this.value}"
}

fun CommandImpl.convertToCommand(): Command {
    val strBoard = UndoManager.encodeBoard(this.board)
    val strNotes = UndoManager.encodeNotes(this.notes)
    return Command(
        index = this.index,
        board = strBoard,
        solution = this.solution,
        selectedRow = this.selectedRow,
        selectedCol = this.selectedCol,
        onNote = this.onNote,
        notes = strNotes
    )
}