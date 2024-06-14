package viewModels

import achievement.Achievement
import achievement.KEY_FINISHED_CELL_COUNT
import achievement.KEY_FINISHED_COL_COUNT
import achievement.KEY_FINISHED_GAME_DAILY_CHALLENGE
import achievement.KEY_FINISHED_GAME_EASY
import achievement.KEY_FINISHED_GAME_EXPERT
import achievement.KEY_FINISHED_GAME_HARD
import achievement.KEY_FINISHED_GAME_HINT_COUNT
import achievement.KEY_FINISHED_GAME_MEDIUM
import achievement.KEY_FINISHED_GAME_PENCIL_COUNT
import achievement.KEY_FINISHED_ROW_COUNT
import achievement.KEY_FINISHED_SECTOR_COUNT
import androidx.lifecycle.ViewModel
import calendar.Day
import com.ctrip.flight.mmkv.defaultMMKV
import composes.DailyUtil
import composes.isValid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Cell
import models.CommandImpl
import models.DIFFICULTY_EASY
import models.DIFFICULTY_EXPERT
import models.DIFFICULTY_HARD
import models.DIFFICULTY_MEDIUM
import models.Difficulty
import models.GameItem
import models.GameSetting
import models.GameSettingManager
import models.InputNumber
import models.Note
import models.Sudoku
import net.requestGameItem
import kotlin.random.Random

const val KEY_LAST_GAME = "key_last_game"
const val KEY_DIFFICULTY = "key_difficulty"

const val MAX_ERROR_COUNT = 3

data class PlayingGameConfig(
    val day: Day? = null,
    val isContinue: Boolean = false,
    val difficulty: Difficulty? = null
)

enum class GameState(i: Int) {
    PREPARING(0),
    READY(1),
    PLAYING(2),
    RESUME(3),
    PAUSED(4),
    FAILED(5),
    WIN(6)
}

class GameViewModel : ViewModel() {

    fun init() {
        val lastGame = defaultMMKV().takeString(KEY_LAST_GAME)
        if (lastGame.isNotBlank()) {
            val sudoku = Json.decodeFromString<Sudoku>(lastGame)
            unfinishedGame.value = sudoku
        }
        initDifficulties()
    }

    private var difficulties = MutableStateFlow<List<Difficulty>>(listOf())
    val difficultiesFlow = difficulties.asStateFlow()

    private fun initDifficulties(autoSave: Boolean = true) {
        val str = defaultMMKV().takeString(KEY_DIFFICULTY)

        val list = when (str.isEmpty()) {
            true -> {
                listOf(
                    Difficulty(DIFFICULTY_EASY, 1, 0, 0, 0),
                    Difficulty(DIFFICULTY_MEDIUM, 1, 0, 0, 0),
                    Difficulty(DIFFICULTY_HARD, 1, 0, 0, 0),
                    Difficulty(DIFFICULTY_EXPERT, 1, 0, 0, 0)
                )
            }

            false -> {
                Json.decodeFromString<List<Difficulty>>(str)
            }
        }
        list.sortedBy {
            it.type
        }

        difficulties.value = list
        if (autoSave) {
            saveDifficulties()
        }
    }

    fun updateDifficulty(difficulty: Difficulty) {
        val pre = difficulties.value
        val list = MutableList(pre.size) { index ->
            val item = pre.get(index)
            if (item.type == difficulty.type) {
                difficulty
            } else {
                item
            }
        }
        difficulties.value = list
        saveDifficulties()
    }

//    fun reloadDifficulty() {
//        initDifficulties(autoSave = false)
//    }

    private fun saveDifficulties() {
        val encode = Json.encodeToString(difficulties.value)
        defaultMMKV().set(KEY_DIFFICULTY, encode)
    }

    private fun randDifficulty(): Difficulty {
        val index = Random(1000).nextInt() % difficulties.value.size
        return difficulties.value.get(index)
    }


    private var unfinishedGame = MutableStateFlow<Sudoku?>(null)
    val unfinishedGameFlow = unfinishedGame.asStateFlow()

    fun setupUnfinishedGame(data: Sudoku?) {
        unfinishedGame.value = data
    }

    private var gameSetting = MutableStateFlow<GameSettingManager>(GameSettingManager())
    val gameSettingFlow = gameSetting.asStateFlow()

    fun refreshGameSetting() {
        gameSetting.value = getGameSettingManagerNoRef(gameSetting.value.gameSetting)
    }

    private fun getGameSettingManagerNoRef(gameSetting: GameSetting): GameSettingManager =
        GameSettingManager(gameSetting)


    private var playingGameConfig = MutableStateFlow<PlayingGameConfig?>(null)
    val playingGameConfigFlow = playingGameConfig.asStateFlow()
    fun updatePlayingGameConfig(config: PlayingGameConfig?, resetGame: Boolean = false) {
        playingGameConfig.value = config
        if (resetGame) {
            setupGame(playingGameConfig.value)
        }
    }


    //------------------------ 9*9 游戏相关 ---------------------------------
    fun setupGame(playingGameConfig: PlayingGameConfig?) {
        if (playingGameConfig == null) return
        if (playingGameConfig.day != null) {
            val sudoku: Sudoku? = DailyUtil.getDailyGame(playingGameConfig.day)
            sudoku?.let {
                setupSudoku(it)
            } ?: run {
                // 获取随机新游戏
                val difficulty = randDifficulty()
                CoroutineScope(Dispatchers.Default).launch {
                    createNewGame(difficulty)?.let {
                        setupSudoku(it)
//                        difficulty.startCount += 1
//                        updateDifficulty(difficulty)
                        DailyUtil.setupDailyGame(playingGameConfig.day, it, 0.5f)
                    }
                }
            }
            return
        }
        if (playingGameConfig.isContinue) {
            unfinishedGame.value?.let {
                setupSudoku(it)
            }//这里需要注意 空值情况
            unfinishedGame.value = null
            return
        }
        if (playingGameConfig.difficulty != null) {
            // 获取随机新游戏
            CoroutineScope(Dispatchers.Default).launch {
                createNewGame(playingGameConfig.difficulty)?.let {
                    setupSudoku(it)
                    playingGameConfig.difficulty.startCount += 1
                    updateDifficulty(playingGameConfig.difficulty)
                }
            }
        }

    }

    private suspend fun createNewGame(difficulty: Difficulty): Sudoku? {
        val result: Result<GameItem> = requestGameItem(difficulty.type)
        if (result.isSuccess && result.getOrNull() != null) {
            val gameItem = result.getOrNull()!!
            val game = Sudoku().apply {
                this.type = difficulty.type
                this.mission = gameItem.mission
                this.board = gameItem.mission
                this.solution = gameItem.solution
            }
            return game
        }
        return null
    }

    //游戏状态
    private var gameState = MutableStateFlow(GameState.PREPARING)
    val gameStateFlow = gameState.asStateFlow()

    fun setGameState(state: GameState) {
        gameState.value = state
    }

    //游戏对象
    private var sudokuGame = MutableStateFlow<Sudoku>(Sudoku())
    val sudokuGameFlow = sudokuGame.asStateFlow()

    //棋盘大小
    private var boardSize: Int = 9

    // 可输入数字及余量
    private val inputNumbers = MutableStateFlow<MutableList<InputNumber>>(mutableListOf())
    val inputNumbersFlow = inputNumbers.asStateFlow()

    //棋盘
    private var board = MutableStateFlow<List<List<Cell>>>(mutableListOf())
    val boardFlow = board.asStateFlow()

    //笔记开关
    private var noteState = MutableStateFlow(false)
    val noteStateFlow = noteState.asStateFlow()

    //笔记
    private var notes = MutableStateFlow<List<Note>>(listOf())
    val notesFlow = notes.asStateFlow()

    // 错误计数
    private var errorCount = MutableStateFlow<Int>(0)
    val errorCountFlow = errorCount.asStateFlow()

    //当前选中单元格
    private var currentCell = MutableStateFlow<Cell?>(null)
    val currentCellFlow = currentCell.asStateFlow()

    //操作记录
    private lateinit var undoManager: UndoManager

    private fun setupSudoku(sudoku: Sudoku) {
        sudokuGame.value = sudoku
        boardSize = sudoku.size

        if (sudoku.board.isNullOrBlank()) {
            board.value = UndoManager.decodeBoard(sudoku.size, sudoku.board!!, sudoku.solution!!)
        } else {
            board.value = UndoManager.decodeBoard(sudoku.size, sudoku.mission!!, sudoku.solution!!)
        }
        errorCount.value = sudoku.error
        noteState.value = sudoku.onNote
        notes.value = UndoManager.decodeNotes(sudoku.notes)
        undoManager = UndoManager(boardSize, sudoku.stack)
        if (undoManager.count() <= 0) {
            pushCommand()
        }
        _time.value = sudoku.duration
        setupLeftInputNumbers()
        setGameState(GameState.READY)
    }

    private fun setupLeftInputNumbers() {
        val tmplInputNumbers = getInputNumbersNoRef()
        board.value.map { row ->
            row.map { cell ->
                if (cell.value != 0) {
                    val item = tmplInputNumbers.find { it.value == cell.value }
                    item?.let { it.leftCount -= 1 }
                }
            }
        }
        inputNumbers.value = tmplInputNumbers
    }

    fun startGame() {
        setGameState(GameState.PLAYING)
        startOrResumeTimer()
    }

    fun pauseGame() {
        pauseTimer()
        setGameState(GameState.PAUSED)
    }

    fun saveGame(isWin: Boolean = false) {
        pauseTimer()
        sudokuGame.value.board = UndoManager.encodeBoard(board.value)
        sudokuGame.value.duration = _time.value
        sudokuGame.value.error = errorCount.value
        sudokuGame.value.onNote = noteState.value
        sudokuGame.value.notes = UndoManager.encodeNotes(notes.value)
        sudokuGame.value.stack = undoManager.convertToCommand()

        val encoded = Json.encodeToString(sudokuGame.value)

        playingGameConfig.value?.let { config ->
            config.day?.let { day ->
                DailyUtil.setupDailyGame(day, encoded, if (isWin) 1.0f else 0.5f)
                Achievement.Instance.addProgress(key = KEY_FINISHED_GAME_DAILY_CHALLENGE)
            } ?: with(config) {
                if (isWin) {
                    defaultMMKV().set(KEY_LAST_GAME, "")
                    setupUnfinishedGame(null)
                    this.difficulty?.let {
                        it.finishCount += 1
                        val duration = sudokuGame.value.duration
                        if (it.bestTime == 0L) {
                            it.bestTime = duration
                        } else {
                            if (duration < it.bestTime) {
                                it.bestTime = duration
                            }
                        }
                        if (errorCount.value == 0) {
                            it.perfect += 1
                        }
                        if (it.lastGameState) {
                            it.winCount += 1
                            if (it.winCount > it.bestWinStreak) {
                                it.bestWinStreak = it.winCount
                            }
                        } else {
                            it.lastGameState = true
                            it.winCount = 1
                        }
                        updateDifficulty(it)
                        when (it.type) {
                            DIFFICULTY_EASY -> Achievement.Instance.addProgress(key = KEY_FINISHED_GAME_EASY)
                            DIFFICULTY_MEDIUM -> Achievement.Instance.addProgress(key = KEY_FINISHED_GAME_MEDIUM)
                            DIFFICULTY_HARD -> Achievement.Instance.addProgress(key = KEY_FINISHED_GAME_HARD)
                            DIFFICULTY_EXPERT -> Achievement.Instance.addProgress(key = KEY_FINISHED_GAME_EXPERT)
                        }
                    }
                } else {
                    defaultMMKV().set(KEY_LAST_GAME, encoded)
                    setupUnfinishedGame(sudokuGame.value)
                }
            }
        }
    }

    fun setGameFailure() {
        playingGameConfig.value?.let { config ->
            config.difficulty?.let { diff ->
                diff.lastGameState = false
                if (diff.winCount > diff.bestWinStreak) {
                    diff.bestWinStreak = diff.winCount
                }
                diff.winCount = 0
                diff.lastGameState = false
                updateDifficulty(diff)
            }
        }
    }

    fun resetGame() {
        resetTimer()
        setGameFailure()
        errorCount.value = 0
        noteState.value = false
        notes.value = emptyList()
        undoManager.clear()
        board.value = UndoManager.decodeBoard(
            boardSize,
            sudokuGame.value.mission!!,
            sudokuGame.value.solution!!
        )
        if (undoManager.count() <= 0) {
            pushCommand()
        }
        startOrResumeTimer()
        startGame()
    }

    fun resetErrorCount() {
        errorCount.value = 0
        startGame()
    }

    private fun updateAchievement(cell: Cell) {
        if (cell.isValid()) {
            Achievement.Instance.addProgress(key = KEY_FINISHED_CELL_COUNT)

            var status = true
            for (item in board.value[cell.row]) {
                if (!item.isValid()) {
                    status = false
                    break
                }
            }
            if (status) {
                Achievement.Instance.addProgress(key = KEY_FINISHED_ROW_COUNT)
            }

            status = true
            for (r in 0..<boardSize) {
                if (!board.value[r][cell.row].isValid()) {
                    status = false
                    break
                }
            }
            if (status) {
                Achievement.Instance.addProgress(key = KEY_FINISHED_COL_COUNT)
            }

            status = true
            board.value.forEach { row ->
                for (item in row)
                    if (item.sector == cell.sector && !item.isValid()) {
                        status = false
                        break
                    }
            }
            if (status) {
                Achievement.Instance.addProgress(key = KEY_FINISHED_SECTOR_COUNT)
            }
        }
    }

    fun setValue(value: Int) {
        currentCell.value?.let { cell ->
            if (cell.locked) return@let
            if (noteState.value) {
                setNote(value)
                return@let
            }
            val tmplBoard = getBoardNoRef()
            tmplBoard[cell.row][cell.col].value = value
            board.value = tmplBoard
            if (gameSetting.value.gameSetting.autoDeleteNote) {
                //删除相关笔记
                notes.value = notes.value.filterNot {
                    it.value == value && (it.row == cell.row || it.col == cell.col || it.sector == cell.sector)
                }
            }
            pushCommand()
            setupLeftInputNumbers()
            updateAchievement(cell)
            if (value != 0) {
                val isValid = tmplBoard[cell.row][cell.col].isValid()
                if (!isValid) {
                    errorCount.value += 1
                }
                if (errorCount.value >= MAX_ERROR_COUNT) {
                    pauseTimer()
                    setGameState(GameState.FAILED)
                } else {
                    val state = checkGameFinished()
                    if (state) {
                        saveGame(isWin = true)
                    }
                }
            }
        }
    }

    private fun checkGameFinished(): Boolean {
        board.value.map { row ->
            row.map { cell ->
                if (!cell.isValid()) {
                    return false
                }
            }
        }
        setGameState(GameState.WIN)
        return true
    }

    fun setupCurrentCell(cell: Cell?) {
        currentCell.value = cell
    }

    fun toggleNoteState() {
        noteState.value = !noteState.value
        pushCommand()
        if (noteState.value) {
            Achievement.Instance.addProgress(KEY_FINISHED_GAME_PENCIL_COUNT)
        }
    }

    private fun setNote(number: Int) {
        currentCell.value?.let { cell ->
            notes.value =
                notes.value.find { it.row == cell.row && it.col == cell.col && it.value == number }
                    ?.let {
                        removeNote(number, cell.row, cell.col, cell.sector)
                    } ?: addNote(number, cell.row, cell.col, cell.sector)
        }
    }

    private fun addNote(note: Int, row: Int, col: Int, sector: Int): List<Note> {
        return notes.value.plus(Note(row = row, col = col, value = note, sector = sector))
    }

    private fun removeNote(note: Int, row: Int, col: Int, sector: Int): List<Note> =
        notes.value.minus(Note(row = row, col = col, value = note, sector = sector))

    fun undo() {
        popCommand()
    }

    fun hint() {
        currentCell.value?.let {
            if (!noteState.value) {
                setValue(it.answer)
                Achievement.Instance.addProgress(KEY_FINISHED_GAME_HINT_COUNT)
            }
        }
    }

    private fun pushCommand() {
        val item = CommandImpl(
            index = undoManager.count(),
            board = getBoardNoRef(),
            solution = sudokuGame.value.solution!!,
            selectedRow = currentCell.value?.row ?: -1,
            selectedCol = currentCell.value?.col ?: -1,
            onNote = noteState.value,
            notes = getNotesNoRef()
        )
        undoManager.push(item)
    }

    private fun popCommand() {
        undoManager.pop()?.let { impl ->
            board.value = impl.board
            notes.value = impl.notes
            noteState.value = impl.onNote
            try {
                val cell = board.value[impl.selectedRow][impl.selectedCol]
                setupCurrentCell(cell)
            } catch (e: Exception) {
                setupCurrentCell(null)
            }
            setupLeftInputNumbers()
        }
    }

    private fun getBoardNoRef(): List<List<Cell>> =
        board.value.map { items -> items.map { item -> item.copy() } }

    private fun getNotesNoRef(): List<Note> =
        notes.value.map { items -> items.copy() }


    private fun getInputNumbersNoRef(): MutableList<InputNumber> =
        MutableList(boardSize) { i -> InputNumber(i + 1, boardSize) }


//------------------------ 9*9 游戏相关 ---------------------------------


//----------------------- 计时器 ---------------------------------

    private var _time = MutableStateFlow(0L)
    val timeFlow = _time.asStateFlow()
    private var _isRunning = MutableStateFlow(false)
    val isRunningFlow = _isRunning.asStateFlow()

    private var timerJob: Job? = null
    private var pauseElapsedTime = 0L

    fun startOrResumeTimer() {
        if (_isRunning.value)
            return
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                _time.value += 1000
                delay(INTERVAL)
            }
        }

        _isRunning.value = true
    }

    fun pauseTimer() {
        pauseElapsedTime = _time.value
        timerJob?.cancel()
        _isRunning.value = false
    }

    fun resetTimer() {
        timerJob?.cancel()
        _time.value = 0L
        pauseElapsedTime = 0L
        _isRunning.value = false
    }

    companion object {
        private const val INTERVAL = 1000L
    }

//----------------------- 计时器 ---------------------------------


}