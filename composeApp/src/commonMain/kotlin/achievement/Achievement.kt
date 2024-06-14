package achievement

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.ctrip.flight.mmkv.defaultMMKV
import composesudoku.composeapp.generated.resources.Res
import composesudoku.composeapp.generated.resources.advanced
import composesudoku.composeapp.generated.resources.advanced1
import composesudoku.composeapp.generated.resources.advanced2
import composesudoku.composeapp.generated.resources.advanced3
import composesudoku.composeapp.generated.resources.advanced4
import composesudoku.composeapp.generated.resources.advanced5
import composesudoku.composeapp.generated.resources.bachelor
import composesudoku.composeapp.generated.resources.bachelor1
import composesudoku.composeapp.generated.resources.bachelor2
import composesudoku.composeapp.generated.resources.bachelor3
import composesudoku.composeapp.generated.resources.bachelor4
import composesudoku.composeapp.generated.resources.bachelor5
import composesudoku.composeapp.generated.resources.beginner
import composesudoku.composeapp.generated.resources.beginner1
import composesudoku.composeapp.generated.resources.beginner2
import composesudoku.composeapp.generated.resources.beginner3
import composesudoku.composeapp.generated.resources.beginner4
import composesudoku.composeapp.generated.resources.beginner5
import composesudoku.composeapp.generated.resources.ic_16_sudoku_solved
import composesudoku.composeapp.generated.resources.ic_16_sudoku_solved1
import composesudoku.composeapp.generated.resources.ic_16_sudoku_solved2
import composesudoku.composeapp.generated.resources.ic_16_sudoku_solved3
import composesudoku.composeapp.generated.resources.ic_16_sudoku_solved4
import composesudoku.composeapp.generated.resources.ic_easy_sudoku_solved
import composesudoku.composeapp.generated.resources.ic_easy_sudoku_solved0
import composesudoku.composeapp.generated.resources.ic_easy_sudoku_solved1
import composesudoku.composeapp.generated.resources.ic_easy_sudoku_solved2
import composesudoku.composeapp.generated.resources.ic_easy_sudoku_solved3
import composesudoku.composeapp.generated.resources.ic_easy_sudoku_solved4
import composesudoku.composeapp.generated.resources.ic_expert_sudoku_solved
import composesudoku.composeapp.generated.resources.ic_expert_sudoku_solved0
import composesudoku.composeapp.generated.resources.ic_expert_sudoku_solved1
import composesudoku.composeapp.generated.resources.ic_expert_sudoku_solved2
import composesudoku.composeapp.generated.resources.ic_expert_sudoku_solved3
import composesudoku.composeapp.generated.resources.ic_expert_sudoku_solved4
import composesudoku.composeapp.generated.resources.ic_first_box_solved
import composesudoku.composeapp.generated.resources.ic_first_column_solved
import composesudoku.composeapp.generated.resources.ic_first_number_solved
import composesudoku.composeapp.generated.resources.ic_first_row_solved
import composesudoku.composeapp.generated.resources.ic_first_sudoku_solved
import composesudoku.composeapp.generated.resources.ic_hard_sudoku_solved
import composesudoku.composeapp.generated.resources.ic_hard_sudoku_solved0
import composesudoku.composeapp.generated.resources.ic_hard_sudoku_solved1
import composesudoku.composeapp.generated.resources.ic_hard_sudoku_solved2
import composesudoku.composeapp.generated.resources.ic_hard_sudoku_solved3
import composesudoku.composeapp.generated.resources.ic_hard_sudoku_solved4
import composesudoku.composeapp.generated.resources.ic_hints_used_count
import composesudoku.composeapp.generated.resources.ic_hints_used_count1
import composesudoku.composeapp.generated.resources.ic_hints_used_count2
import composesudoku.composeapp.generated.resources.ic_hints_used_count3
import composesudoku.composeapp.generated.resources.ic_hints_used_count4
import composesudoku.composeapp.generated.resources.ic_medium_sudoku_solved
import composesudoku.composeapp.generated.resources.ic_medium_sudoku_solved0
import composesudoku.composeapp.generated.resources.ic_medium_sudoku_solved1
import composesudoku.composeapp.generated.resources.ic_medium_sudoku_solved2
import composesudoku.composeapp.generated.resources.ic_medium_sudoku_solved3
import composesudoku.composeapp.generated.resources.ic_medium_sudoku_solved4
import composesudoku.composeapp.generated.resources.ic_play_dc_for_days
import composesudoku.composeapp.generated.resources.ic_play_dc_for_days0
import composesudoku.composeapp.generated.resources.ic_play_dc_for_days1
import composesudoku.composeapp.generated.resources.ic_play_dc_for_days2
import composesudoku.composeapp.generated.resources.ic_play_dc_for_days3
import composesudoku.composeapp.generated.resources.ic_play_dc_for_days4
import composesudoku.composeapp.generated.resources.ic_play_sudoku_for_days
import composesudoku.composeapp.generated.resources.ic_play_sudoku_for_days0
import composesudoku.composeapp.generated.resources.ic_play_sudoku_for_days1
import composesudoku.composeapp.generated.resources.ic_play_sudoku_for_days2
import composesudoku.composeapp.generated.resources.ic_play_sudoku_for_days3
import composesudoku.composeapp.generated.resources.ic_play_sudoku_for_days4
import composesudoku.composeapp.generated.resources.ic_use_pencil_in_rounds
import composesudoku.composeapp.generated.resources.ic_use_pencil_in_rounds1
import composesudoku.composeapp.generated.resources.ic_use_pencil_in_rounds2
import composesudoku.composeapp.generated.resources.ic_use_pencil_in_rounds3
import composesudoku.composeapp.generated.resources.ic_use_pencil_in_rounds4
import composesudoku.composeapp.generated.resources.ingenuity
import composesudoku.composeapp.generated.resources.ingenuity1
import composesudoku.composeapp.generated.resources.ingenuity2
import composesudoku.composeapp.generated.resources.ingenuity3
import composesudoku.composeapp.generated.resources.ingenuity4
import composesudoku.composeapp.generated.resources.ingenuity5
import composesudoku.composeapp.generated.resources.intermediate
import composesudoku.composeapp.generated.resources.intermediate1
import composesudoku.composeapp.generated.resources.intermediate2
import composesudoku.composeapp.generated.resources.intermediate3
import composesudoku.composeapp.generated.resources.intermediate4
import composesudoku.composeapp.generated.resources.intermediate5
import composesudoku.composeapp.generated.resources.king
import composesudoku.composeapp.generated.resources.king1
import composesudoku.composeapp.generated.resources.king2
import composesudoku.composeapp.generated.resources.king3
import composesudoku.composeapp.generated.resources.king4
import composesudoku.composeapp.generated.resources.king5
import composesudoku.composeapp.generated.resources.mania
import composesudoku.composeapp.generated.resources.mania1
import composesudoku.composeapp.generated.resources.mania2
import composesudoku.composeapp.generated.resources.mania3
import composesudoku.composeapp.generated.resources.mania4
import composesudoku.composeapp.generated.resources.mania5
import composesudoku.composeapp.generated.resources.newbie
import composesudoku.composeapp.generated.resources.newbie1
import composesudoku.composeapp.generated.resources.newbie2
import composesudoku.composeapp.generated.resources.newbie3
import composesudoku.composeapp.generated.resources.newbie4
import composesudoku.composeapp.generated.resources.newbie5
import composesudoku.composeapp.generated.resources.persevere
import composesudoku.composeapp.generated.resources.persevere1
import composesudoku.composeapp.generated.resources.persevere2
import composesudoku.composeapp.generated.resources.persevere3
import composesudoku.composeapp.generated.resources.persevere4
import composesudoku.composeapp.generated.resources.persevere5
import composesudoku.composeapp.generated.resources.persevere6
import composesudoku.composeapp.generated.resources.pioneer
import composesudoku.composeapp.generated.resources.pioneer1
import composesudoku.composeapp.generated.resources.pioneer2
import composesudoku.composeapp.generated.resources.pioneer3
import composesudoku.composeapp.generated.resources.pioneer4
import composesudoku.composeapp.generated.resources.pioneer5
import composesudoku.composeapp.generated.resources.pioneer6
import composesudoku.composeapp.generated.resources.rookie
import composesudoku.composeapp.generated.resources.rookie1
import composesudoku.composeapp.generated.resources.rookie2
import composesudoku.composeapp.generated.resources.rookie3
import composesudoku.composeapp.generated.resources.rookie4
import composesudoku.composeapp.generated.resources.rookie5
import composesudoku.composeapp.generated.resources.unknown
import getCurDate
import io.ktor.utils.io.core.String
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

const val KEY_ACHIEVEMENT = "_key_achievement_"

const val ACHIEVEMENT_GROUP_NEWBIE = 1 //数独新手
const val ACHIEVEMENT_GROUP_ROOKIE = 2 //数独菜鸟
const val ACHIEVEMENT_GROUP_BEGINNER = 3 //数独进阶
const val ACHIEVEMENT_GROUP_INTERMEDIATE = 4 //数独中阶
const val ACHIEVEMENT_GROUP_ADVANCED = 5 // 数独高阶
const val ACHIEVEMENT_GROUP_BACHELOR = 6 // 数独学士
const val ACHIEVEMENT_GROUP_PERSEVERE = 7 // 持之以恒
const val ACHIEVEMENT_GROUP_PIONEER = 8 // 挑战先锋
const val ACHIEVEMENT_GROUP_MANIA = 9 //铅笔狂魔
const val ACHIEVEMENT_GROUP_INGENUITY = 10 //巧夺天工
const val ACHIEVEMENT_GROUP_KING = 11 //数独之王


const val KEY_FINISHED_CELL_COUNT = "_key_finished_cell_count" //填对空格数
const val KEY_FINISHED_ROW_COUNT = "_key_finished_row_count" //填对行数
const val KEY_FINISHED_COL_COUNT = "_key_finished_col_count" //填对列数
const val KEY_FINISHED_SECTOR_COUNT = "_key_finished_sector_count" //填宫格列数
const val KEY_FINISHED_GAME_EASY = "_key_finished_game_easy" //完成easy
const val KEY_FINISHED_GAME_MEDIUM = "_key_finished_game_medium" //完成medium
const val KEY_FINISHED_GAME_HARD = "_key_finished_game_hard" //完成hard
const val KEY_FINISHED_GAME_EXPERT = "_key_finished_game_expert" //完成expert
const val KEY_FINISHED_GAME_DAILY_CHALLENGE =
    "_key_finished_game_daily_challenge" //完成daily challenge
const val KEY_FINISHED_GAME_PLAY_DAYS = "_key_finished_game_play_days" //完成play days
const val KEY_FINISHED_GAME_CHALLENGE_DAYS = "_key_finished_game_challenge_days" //完成challenge days
const val KEY_FINISHED_GAME_PENCIL_COUNT = "_key_finished_game_pencil_count" //完成pencil count
const val KEY_FINISHED_GAME_HINT_COUNT = "_key_finished_game_hint_count" //完成hint count
const val KEY_FINISHED_GAME_KING_COUNT = "_key_finished_game_king_count" //完成hint count

const val KEY_LAST_PLAY_DATE = "key_last_play_date"
const val KEY_LAST_CHALLENGE_DATE = "key_last_challenge_date"

@Serializable
data class AchievementGroup(
    val id: Int,
    var achievement: List<AchievementItem>
)

@Serializable
data class AchievementItem(
    val id: Int,
    val group: Int,
//    var progress: Int = 0,
    val max: Int = 0
)

fun AchievementItem.getProgress(): Int {
    val progress = when (group) {
        ACHIEVEMENT_GROUP_NEWBIE -> {
            return when (id) {
                1 -> defaultMMKV().takeInt(KEY_FINISHED_CELL_COUNT, 0)
                2 -> defaultMMKV().takeInt(KEY_FINISHED_ROW_COUNT, 0)
                3 -> defaultMMKV().takeInt(KEY_FINISHED_COL_COUNT, 0)
                4 -> defaultMMKV().takeInt(KEY_FINISHED_SECTOR_COUNT, 0)
                5 -> defaultMMKV().takeInt(KEY_FINISHED_GAME_EASY, 0) +
                        defaultMMKV().takeInt(KEY_FINISHED_GAME_MEDIUM, 0) +
                        defaultMMKV().takeInt(KEY_FINISHED_GAME_HARD, 0) +
                        defaultMMKV().takeInt(KEY_FINISHED_GAME_EXPERT, 0)

                else -> 0
            }
        }

        ACHIEVEMENT_GROUP_ROOKIE -> {
            return when (id) {
                1 -> defaultMMKV().takeInt(KEY_FINISHED_GAME_EASY, 0)
                2 -> defaultMMKV().takeInt(KEY_FINISHED_GAME_MEDIUM, 0)
                3 -> defaultMMKV().takeInt(KEY_FINISHED_GAME_HARD, 0)
                4 -> defaultMMKV().takeInt(KEY_FINISHED_GAME_EXPERT, 0)
                5 -> defaultMMKV().takeInt(KEY_FINISHED_GAME_DAILY_CHALLENGE, 0)
                else -> 0
            }
        }

        ACHIEVEMENT_GROUP_BEGINNER -> defaultMMKV().takeInt(KEY_FINISHED_GAME_EASY, 0)
        ACHIEVEMENT_GROUP_INTERMEDIATE -> defaultMMKV().takeInt(KEY_FINISHED_GAME_MEDIUM, 0)
        ACHIEVEMENT_GROUP_ADVANCED -> defaultMMKV().takeInt(KEY_FINISHED_GAME_HARD, 0)
        ACHIEVEMENT_GROUP_BACHELOR -> defaultMMKV().takeInt(KEY_FINISHED_GAME_EXPERT, 0)
        ACHIEVEMENT_GROUP_PERSEVERE -> defaultMMKV().takeInt(KEY_FINISHED_GAME_PLAY_DAYS, 0)
        ACHIEVEMENT_GROUP_PIONEER -> defaultMMKV().takeInt(KEY_FINISHED_GAME_CHALLENGE_DAYS, 0)
        ACHIEVEMENT_GROUP_MANIA -> defaultMMKV().takeInt(KEY_FINISHED_GAME_PENCIL_COUNT, 0)
        ACHIEVEMENT_GROUP_INGENUITY -> defaultMMKV().takeInt(KEY_FINISHED_GAME_HINT_COUNT, 0)
        ACHIEVEMENT_GROUP_KING -> defaultMMKV().takeInt(KEY_FINISHED_GAME_KING_COUNT, 0)
        else -> 0
    }

    if (progress >= max) {
        return max
    }
    return progress
}

fun AchievementItem.hadFinished(): Boolean = getProgress() >= max

class Achievement private constructor() {

    private var achievementGroupState = MutableStateFlow<List<AchievementGroup>?>(null)
    val achievementGroupFlow = achievementGroupState.asStateFlow()
    private lateinit var achievementGroup: List<AchievementGroup>

    companion object {
        val Instance: Achievement by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Achievement() }
    }

    init {
        val pre = defaultMMKV().takeString(KEY_ACHIEVEMENT, "")
        if (pre.isNotBlank()) {
            achievementGroup = Json.decodeFromString<List<AchievementGroup>>(pre)
            achievementGroupState.value = achievementGroup
        } else {
            initAchievementGroup()
        }
    }

    fun addProgress(key: String, progress: Int = 1) {
        val pre = defaultMMKV().takeInt(key, 1) + progress
        defaultMMKV().set(key, pre)
        if (key == KEY_FINISHED_GAME_EASY ||
            key == KEY_FINISHED_GAME_MEDIUM ||
            key == KEY_FINISHED_GAME_HARD ||
            key == KEY_FINISHED_GAME_EXPERT
        ) {
            val lastPlayDate = defaultMMKV().takeString(KEY_LAST_PLAY_DATE, "")
            val curDate = getCurDate()
            if (curDate == lastPlayDate) {
                defaultMMKV().set(KEY_LAST_PLAY_DATE, curDate)
                val seed = defaultMMKV().takeInt(KEY_FINISHED_GAME_PLAY_DAYS, 0) + 1
                defaultMMKV().set(KEY_FINISHED_GAME_PLAY_DAYS, seed)
            }
        } else if (key == KEY_FINISHED_GAME_DAILY_CHALLENGE) {
            val lastChallengeDate = defaultMMKV().takeString(KEY_LAST_CHALLENGE_DATE, "")
            val curDate = getCurDate()
            if (curDate == lastChallengeDate) {
                defaultMMKV().set(KEY_LAST_PLAY_DATE, curDate)
                val seed = defaultMMKV().takeInt(KEY_FINISHED_GAME_CHALLENGE_DAYS, 0) + 1
                defaultMMKV().set(KEY_FINISHED_GAME_CHALLENGE_DAYS, seed)
            }
        }
    }

    private fun groupNoRef(): List<AchievementGroup> =
        achievementGroup.map {
            it.copy(id = it.id, achievement = it.achievement.map { item ->
                item.copy(
                    id = item.id,
                    group = item.group,
//                    progress = item.progress,
                    max = item.max
                )
            })
        }

    @OptIn(ExperimentalResourceApi::class)
    private fun initAchievementGroup() {
        CoroutineScope(Dispatchers.Default).launch {
            val bytes: ByteArray = Res.readBytes("files/Achievement.json")
            val str = String(bytes)
            achievementGroup = Json.decodeFromString<List<AchievementGroup>>(str)
            println("Achievement::${achievementGroup}")
            achievementGroupState.value = achievementGroup
        }
    }

    @Composable
    @OptIn(ExperimentalResourceApi::class)
    fun getTitle(id: Int): String {
        return when (id) {
            ACHIEVEMENT_GROUP_NEWBIE -> stringResource(Res.string.newbie)
            ACHIEVEMENT_GROUP_ROOKIE -> stringResource(Res.string.rookie)
            ACHIEVEMENT_GROUP_BEGINNER -> stringResource(Res.string.beginner)
            ACHIEVEMENT_GROUP_INTERMEDIATE -> stringResource(Res.string.intermediate)
            ACHIEVEMENT_GROUP_ADVANCED -> stringResource(Res.string.advanced)
            ACHIEVEMENT_GROUP_BACHELOR -> stringResource(Res.string.bachelor)
            ACHIEVEMENT_GROUP_PERSEVERE -> stringResource(Res.string.persevere)
            ACHIEVEMENT_GROUP_PIONEER -> stringResource(Res.string.pioneer)
            ACHIEVEMENT_GROUP_MANIA -> stringResource(Res.string.mania)
            ACHIEVEMENT_GROUP_INGENUITY -> stringResource(Res.string.ingenuity)
            ACHIEVEMENT_GROUP_KING -> stringResource(Res.string.king)
            else -> stringResource(Res.string.unknown)
        }
    }

    @Composable
    @OptIn(ExperimentalResourceApi::class)
    fun getTitle(id: String): String {
        return when (id) {
            "${ACHIEVEMENT_GROUP_NEWBIE}_1" -> stringResource(Res.string.newbie1)
            "${ACHIEVEMENT_GROUP_NEWBIE}_2" -> stringResource(Res.string.newbie2)
            "${ACHIEVEMENT_GROUP_NEWBIE}_3" -> stringResource(Res.string.newbie3)
            "${ACHIEVEMENT_GROUP_NEWBIE}_4" -> stringResource(Res.string.newbie4)
            "${ACHIEVEMENT_GROUP_NEWBIE}_5" -> stringResource(Res.string.newbie5)
            "${ACHIEVEMENT_GROUP_ROOKIE}_1" -> stringResource(Res.string.rookie1)
            "${ACHIEVEMENT_GROUP_ROOKIE}_2" -> stringResource(Res.string.rookie2)
            "${ACHIEVEMENT_GROUP_ROOKIE}_3" -> stringResource(Res.string.rookie3)
            "${ACHIEVEMENT_GROUP_ROOKIE}_4" -> stringResource(Res.string.rookie4)
            "${ACHIEVEMENT_GROUP_ROOKIE}_5" -> stringResource(Res.string.rookie5)
            "${ACHIEVEMENT_GROUP_BEGINNER}_1" -> stringResource(Res.string.beginner1)
            "${ACHIEVEMENT_GROUP_BEGINNER}_2" -> stringResource(Res.string.beginner2)
            "${ACHIEVEMENT_GROUP_BEGINNER}_3" -> stringResource(Res.string.beginner3)
            "${ACHIEVEMENT_GROUP_BEGINNER}_4" -> stringResource(Res.string.beginner4)
            "${ACHIEVEMENT_GROUP_BEGINNER}_5" -> stringResource(Res.string.beginner5)
            "${ACHIEVEMENT_GROUP_INTERMEDIATE}_1" -> stringResource(Res.string.intermediate1)
            "${ACHIEVEMENT_GROUP_INTERMEDIATE}_2" -> stringResource(Res.string.intermediate2)
            "${ACHIEVEMENT_GROUP_INTERMEDIATE}_3" -> stringResource(Res.string.intermediate3)
            "${ACHIEVEMENT_GROUP_INTERMEDIATE}_4" -> stringResource(Res.string.intermediate4)
            "${ACHIEVEMENT_GROUP_INTERMEDIATE}_5" -> stringResource(Res.string.intermediate5)
            "${ACHIEVEMENT_GROUP_ADVANCED}_1" -> stringResource(Res.string.advanced1)
            "${ACHIEVEMENT_GROUP_ADVANCED}_2" -> stringResource(Res.string.advanced2)
            "${ACHIEVEMENT_GROUP_ADVANCED}_3" -> stringResource(Res.string.advanced3)
            "${ACHIEVEMENT_GROUP_ADVANCED}_4" -> stringResource(Res.string.advanced4)
            "${ACHIEVEMENT_GROUP_ADVANCED}_5" -> stringResource(Res.string.advanced5)
            "${ACHIEVEMENT_GROUP_BACHELOR}_1" -> stringResource(Res.string.bachelor1)
            "${ACHIEVEMENT_GROUP_BACHELOR}_2" -> stringResource(Res.string.bachelor2)
            "${ACHIEVEMENT_GROUP_BACHELOR}_3" -> stringResource(Res.string.bachelor3)
            "${ACHIEVEMENT_GROUP_BACHELOR}_4" -> stringResource(Res.string.bachelor4)
            "${ACHIEVEMENT_GROUP_BACHELOR}_5" -> stringResource(Res.string.bachelor5)
            "${ACHIEVEMENT_GROUP_PERSEVERE}_1" -> stringResource(Res.string.persevere1)
            "${ACHIEVEMENT_GROUP_PERSEVERE}_2" -> stringResource(Res.string.persevere2)
            "${ACHIEVEMENT_GROUP_PERSEVERE}_3" -> stringResource(Res.string.persevere3)
            "${ACHIEVEMENT_GROUP_PERSEVERE}_4" -> stringResource(Res.string.persevere4)
            "${ACHIEVEMENT_GROUP_PERSEVERE}_5" -> stringResource(Res.string.persevere5)
            "${ACHIEVEMENT_GROUP_PERSEVERE}_6" -> stringResource(Res.string.persevere6)
            "${ACHIEVEMENT_GROUP_PIONEER}_1" -> stringResource(Res.string.pioneer1)
            "${ACHIEVEMENT_GROUP_PIONEER}_2" -> stringResource(Res.string.pioneer2)
            "${ACHIEVEMENT_GROUP_PIONEER}_3" -> stringResource(Res.string.pioneer3)
            "${ACHIEVEMENT_GROUP_PIONEER}_4" -> stringResource(Res.string.pioneer4)
            "${ACHIEVEMENT_GROUP_PIONEER}_5" -> stringResource(Res.string.pioneer5)
            "${ACHIEVEMENT_GROUP_PIONEER}_6" -> stringResource(Res.string.pioneer6)
            "${ACHIEVEMENT_GROUP_MANIA}_1" -> stringResource(Res.string.mania1)
            "${ACHIEVEMENT_GROUP_MANIA}_2" -> stringResource(Res.string.mania2)
            "${ACHIEVEMENT_GROUP_MANIA}_3" -> stringResource(Res.string.mania3)
            "${ACHIEVEMENT_GROUP_MANIA}_4" -> stringResource(Res.string.mania4)
            "${ACHIEVEMENT_GROUP_MANIA}_5" -> stringResource(Res.string.mania5)
            "${ACHIEVEMENT_GROUP_INGENUITY}_1" -> stringResource(Res.string.ingenuity1)
            "${ACHIEVEMENT_GROUP_INGENUITY}_2" -> stringResource(Res.string.ingenuity2)
            "${ACHIEVEMENT_GROUP_INGENUITY}_3" -> stringResource(Res.string.ingenuity3)
            "${ACHIEVEMENT_GROUP_INGENUITY}_4" -> stringResource(Res.string.ingenuity4)
            "${ACHIEVEMENT_GROUP_INGENUITY}_5" -> stringResource(Res.string.ingenuity5)
            "${ACHIEVEMENT_GROUP_KING}_1" -> stringResource(Res.string.king1)
            "${ACHIEVEMENT_GROUP_KING}_2" -> stringResource(Res.string.king2)
            "${ACHIEVEMENT_GROUP_KING}_3" -> stringResource(Res.string.king3)
            "${ACHIEVEMENT_GROUP_KING}_4" -> stringResource(Res.string.king4)
            "${ACHIEVEMENT_GROUP_KING}_5" -> stringResource(Res.string.king5)
            else -> stringResource(Res.string.unknown)
        }
    }

    @Composable
    @OptIn(ExperimentalResourceApi::class)
    fun getIcon(id: String): ImageVector {
        return when (id) {
            "${ACHIEVEMENT_GROUP_NEWBIE}_1" -> vectorResource(Res.drawable.ic_first_number_solved)
            "${ACHIEVEMENT_GROUP_NEWBIE}_2" -> vectorResource(Res.drawable.ic_first_row_solved)
            "${ACHIEVEMENT_GROUP_NEWBIE}_3" -> vectorResource(Res.drawable.ic_first_column_solved)
            "${ACHIEVEMENT_GROUP_NEWBIE}_4" -> vectorResource(Res.drawable.ic_first_box_solved)
            "${ACHIEVEMENT_GROUP_NEWBIE}_5" -> vectorResource(Res.drawable.ic_first_sudoku_solved)
            "${ACHIEVEMENT_GROUP_ROOKIE}_1" -> vectorResource(Res.drawable.ic_easy_sudoku_solved)
            "${ACHIEVEMENT_GROUP_ROOKIE}_2" -> vectorResource(Res.drawable.ic_medium_sudoku_solved)
            "${ACHIEVEMENT_GROUP_ROOKIE}_3" -> vectorResource(Res.drawable.ic_hard_sudoku_solved)
            "${ACHIEVEMENT_GROUP_ROOKIE}_4" -> vectorResource(Res.drawable.ic_expert_sudoku_solved)
            "${ACHIEVEMENT_GROUP_ROOKIE}_5" -> vectorResource(Res.drawable.ic_easy_sudoku_solved0)
            "${ACHIEVEMENT_GROUP_BEGINNER}_1" -> vectorResource(Res.drawable.ic_easy_sudoku_solved0)
            "${ACHIEVEMENT_GROUP_BEGINNER}_2" -> vectorResource(Res.drawable.ic_easy_sudoku_solved1)
            "${ACHIEVEMENT_GROUP_BEGINNER}_3" -> vectorResource(Res.drawable.ic_easy_sudoku_solved2)
            "${ACHIEVEMENT_GROUP_BEGINNER}_4" -> vectorResource(Res.drawable.ic_easy_sudoku_solved3)
            "${ACHIEVEMENT_GROUP_BEGINNER}_5" -> vectorResource(Res.drawable.ic_easy_sudoku_solved4)
            "${ACHIEVEMENT_GROUP_INTERMEDIATE}_1" -> vectorResource(Res.drawable.ic_medium_sudoku_solved0)
            "${ACHIEVEMENT_GROUP_INTERMEDIATE}_2" -> vectorResource(Res.drawable.ic_medium_sudoku_solved1)
            "${ACHIEVEMENT_GROUP_INTERMEDIATE}_3" -> vectorResource(Res.drawable.ic_medium_sudoku_solved2)
            "${ACHIEVEMENT_GROUP_INTERMEDIATE}_4" -> vectorResource(Res.drawable.ic_medium_sudoku_solved3)
            "${ACHIEVEMENT_GROUP_INTERMEDIATE}_5" -> vectorResource(Res.drawable.ic_medium_sudoku_solved4)
            "${ACHIEVEMENT_GROUP_ADVANCED}_1" -> vectorResource(Res.drawable.ic_hard_sudoku_solved0)
            "${ACHIEVEMENT_GROUP_ADVANCED}_2" -> vectorResource(Res.drawable.ic_hard_sudoku_solved1)
            "${ACHIEVEMENT_GROUP_ADVANCED}_3" -> vectorResource(Res.drawable.ic_hard_sudoku_solved2)
            "${ACHIEVEMENT_GROUP_ADVANCED}_4" -> vectorResource(Res.drawable.ic_hard_sudoku_solved3)
            "${ACHIEVEMENT_GROUP_ADVANCED}_5" -> vectorResource(Res.drawable.ic_hard_sudoku_solved4)
            "${ACHIEVEMENT_GROUP_BACHELOR}_1" -> vectorResource(Res.drawable.ic_expert_sudoku_solved0)
            "${ACHIEVEMENT_GROUP_BACHELOR}_2" -> vectorResource(Res.drawable.ic_expert_sudoku_solved1)
            "${ACHIEVEMENT_GROUP_BACHELOR}_3" -> vectorResource(Res.drawable.ic_expert_sudoku_solved2)
            "${ACHIEVEMENT_GROUP_BACHELOR}_4" -> vectorResource(Res.drawable.ic_expert_sudoku_solved3)
            "${ACHIEVEMENT_GROUP_BACHELOR}_5" -> vectorResource(Res.drawable.ic_expert_sudoku_solved4)
            "${ACHIEVEMENT_GROUP_PERSEVERE}_1" -> vectorResource(Res.drawable.ic_play_sudoku_for_days)
            "${ACHIEVEMENT_GROUP_PERSEVERE}_2" -> vectorResource(Res.drawable.ic_play_sudoku_for_days0)
            "${ACHIEVEMENT_GROUP_PERSEVERE}_3" -> vectorResource(Res.drawable.ic_play_sudoku_for_days1)
            "${ACHIEVEMENT_GROUP_PERSEVERE}_4" -> vectorResource(Res.drawable.ic_play_sudoku_for_days2)
            "${ACHIEVEMENT_GROUP_PERSEVERE}_5" -> vectorResource(Res.drawable.ic_play_sudoku_for_days3)
            "${ACHIEVEMENT_GROUP_PERSEVERE}_6" -> vectorResource(Res.drawable.ic_play_sudoku_for_days4)
            "${ACHIEVEMENT_GROUP_PIONEER}_1" -> vectorResource(Res.drawable.ic_play_dc_for_days)
            "${ACHIEVEMENT_GROUP_PIONEER}_2" -> vectorResource(Res.drawable.ic_play_dc_for_days0)
            "${ACHIEVEMENT_GROUP_PIONEER}_3" -> vectorResource(Res.drawable.ic_play_dc_for_days1)
            "${ACHIEVEMENT_GROUP_PIONEER}_4" -> vectorResource(Res.drawable.ic_play_dc_for_days2)
            "${ACHIEVEMENT_GROUP_PIONEER}_5" -> vectorResource(Res.drawable.ic_play_dc_for_days3)
            "${ACHIEVEMENT_GROUP_PIONEER}_6" -> vectorResource(Res.drawable.ic_play_dc_for_days4)
            "${ACHIEVEMENT_GROUP_MANIA}_1" -> vectorResource(Res.drawable.ic_use_pencil_in_rounds)
            "${ACHIEVEMENT_GROUP_MANIA}_2" -> vectorResource(Res.drawable.ic_use_pencil_in_rounds1)
            "${ACHIEVEMENT_GROUP_MANIA}_3" -> vectorResource(Res.drawable.ic_use_pencil_in_rounds2)
            "${ACHIEVEMENT_GROUP_MANIA}_4" -> vectorResource(Res.drawable.ic_use_pencil_in_rounds3)
            "${ACHIEVEMENT_GROUP_MANIA}_5" -> vectorResource(Res.drawable.ic_use_pencil_in_rounds4)
            "${ACHIEVEMENT_GROUP_INGENUITY}_1" -> vectorResource(Res.drawable.ic_hints_used_count)
            "${ACHIEVEMENT_GROUP_INGENUITY}_2" -> vectorResource(Res.drawable.ic_hints_used_count1)
            "${ACHIEVEMENT_GROUP_INGENUITY}_3" -> vectorResource(Res.drawable.ic_hints_used_count2)
            "${ACHIEVEMENT_GROUP_INGENUITY}_4" -> vectorResource(Res.drawable.ic_hints_used_count3)
            "${ACHIEVEMENT_GROUP_INGENUITY}_5" -> vectorResource(Res.drawable.ic_hints_used_count4)
            "${ACHIEVEMENT_GROUP_KING}_1" -> vectorResource(Res.drawable.ic_16_sudoku_solved)
            "${ACHIEVEMENT_GROUP_KING}_2" -> vectorResource(Res.drawable.ic_16_sudoku_solved1)
            "${ACHIEVEMENT_GROUP_KING}_3" -> vectorResource(Res.drawable.ic_16_sudoku_solved2)
            "${ACHIEVEMENT_GROUP_KING}_4" -> vectorResource(Res.drawable.ic_16_sudoku_solved3)
            "${ACHIEVEMENT_GROUP_KING}_5" -> vectorResource(Res.drawable.ic_16_sudoku_solved4)
            else -> vectorResource(Res.drawable.ic_16_sudoku_solved4)
        }
    }


}