package models

import com.ctrip.flight.mmkv.defaultMMKV
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


const val DIFFICULTY_EASY = 5
const val DIFFICULTY_MEDIUM = 6
const val DIFFICULTY_HARD = 7
const val DIFFICULTY_EXPERT = 8

@Serializable
data class GameSetting(
    var sound: Boolean = true,
    var shock: Boolean = true,
    var notice: Boolean = true,
    var showTimer: Boolean = true,
    var errorLimit: Boolean = true,
    var numberFirst: Boolean = true,
    var highLightSameRowOrColumn: Boolean = true,
    var highLightSameSector: Boolean = true,
    var highLightSameNumber: Boolean = true,
    var autoDeleteNote: Boolean = true,
    var autoFinish: Boolean = true,
    var questionInformation: Boolean = true,
    var showScore: Boolean = true,
    var showLeftNumber: Boolean = true,
    var smartHint: Boolean = true
)

open class GameSettingManager constructor(pregameSetting: GameSetting? = null) {

    var gameSetting: GameSetting
    private val KEY_GAME_SETTINGS = "key_game_settings"

    init {
        gameSetting = if (pregameSetting == null) {
            val preSettings = defaultMMKV().takeString(KEY_GAME_SETTINGS)
            if (preSettings.isBlank()) {
                GameSetting()
            } else {
                Json.decodeFromString(preSettings)
            }
        } else {
            pregameSetting
        }
    }

    fun updateSound(state: Boolean) {
        gameSetting.sound = state
        save()
    }

    fun updateShock(state: Boolean) {
        gameSetting.shock = state
        save()
    }

    fun updateNotice(state: Boolean) {
        gameSetting.notice = state
        save()
    }

    fun updateShowTimer(state: Boolean) {
        gameSetting.showTimer = state
        save()
    }

    fun updateErrorLimit(state: Boolean) {
        gameSetting.errorLimit = state
        save()
    }

    fun updateNumberFirst(state: Boolean) {
        gameSetting.numberFirst = state
        save()
    }

    fun updateHighLightSameRowOrColumn(state: Boolean) {
        gameSetting.highLightSameRowOrColumn = state
        save()
    }

    fun updateHighLightSameSector(state: Boolean) {
        gameSetting.highLightSameSector = state
        save()
    }

    fun updateHighLightSameNumber(state: Boolean) {
        gameSetting.highLightSameNumber = state
        save()
    }


    fun updateAutoDeleteNote(state: Boolean) {
        gameSetting.autoDeleteNote = state
        save()
    }

    fun updateAutoFinish(state: Boolean) {
        gameSetting.autoFinish = state
        save()
    }

    fun updateQuestionInformation(state: Boolean) {
        gameSetting.questionInformation = state
        save()
    }

    fun updateShowScore(state: Boolean) {
        gameSetting.showScore = state
        save()
    }

    fun updateShowLeftNumber(state: Boolean) {
        gameSetting.showLeftNumber = state
        save()
    }

    fun updateSmartHint(state: Boolean) {
        gameSetting.smartHint = state
        save()
    }

    private fun save() {
        val str = Json.encodeToString(gameSetting)
        defaultMMKV().set(KEY_GAME_SETTINGS, str)
    }

}


@Serializable
data class GameItem(val mission: String, val solution: String)


//{
//    "mission":"",
//    "solutation":"",
//    "board":"",
//    "type":1,
//    "duration":0,
//    "error":0,
//    "onNote":false,
//    "notes": "rcsvrcsv",// r：行号  c：列号  v：值
//    "stack":[
//        {
//            "index":0,
//            "board":"",
//            "solution":"",
//            "onNote":false,
//            "notes": "rcsvrcsv",// r：行号  c：列号  v：值
//        }
//    ]
//}

@Serializable
data class InputNumber(val value: Int, var leftCount: Int = 9)

@Serializable
data class CommandImpl(
    val index: Int,
    val board: List<List<Cell>>,
    val solution: String,
    val selectedRow: Int,
    val selectedCol: Int,
    val onNote: Boolean,
    val notes: List<Note>
)

@Serializable
data class Command(
    val index: Int,
    val board: String,
    val solution: String,
    val selectedRow: Int,
    val selectedCol: Int,
    val onNote: Boolean,
    val notes: String? = null
)

@Serializable
data class Note(
    val row: Int,
    val col: Int,
    val sector: Int,
    val value: Int
)

@Serializable
data class Cell(
    val row: Int,
    val col: Int,
    val sector: Int,
    var value: Int = 0,
    val answer: Int,
    var error: Boolean = false,
    var locked: Boolean = false,
)

@Serializable
data class Sudoku(
    var type: Int = -1,
    var size: Int = 9,
    var mission: String? = null,
    var solution: String? = null,
    var board: String? = null,
    var duration: Long = 0,
    var error: Int = 0,
    var onNote: Boolean = false,
    var notes: String? = null,
    var stack: List<Command>? = null
)


@Serializable
data class Difficulty(
    var type: Int,
    var level: Int,
    var startCount: Int,
    var finishCount: Int,
    var experience: Int,
    var bestTime: Long = 0,
    var perfect: Int = 0,
    var bestWinStreak: Int = 0, //最佳连胜
    var winCount: Int = 0, // 当前连胜
    var lastGameState: Boolean = false//上一次游戏结果
)
