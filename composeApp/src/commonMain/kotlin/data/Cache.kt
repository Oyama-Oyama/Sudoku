package data

import com.ctrip.flight.mmkv.defaultMMKV
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.DIFFICULTY_EASY
import models.DIFFICULTY_EXPERT
import models.DIFFICULTY_HARD
import models.DIFFICULTY_MEDIUM
import models.Difficulty


class Cache private constructor() {

//    val KEY_DIFFICULTY = "key_difficulty"
//
//    private lateinit var difficulties: List<Difficulty>
//
    companion object {
        val instance: Cache by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Cache() }
    }
//
//    fun getDifficulties(): List<Difficulty> {
//        val str = defaultMMKV().takeString(KEY_DIFFICULTY)
//        when (str.isEmpty()) {
//            true -> {
//                difficulties = listOf(
//                    Difficulty(DIFFICULTY_EASY, 1, 0, 0, 0),
//                    Difficulty(DIFFICULTY_MEDIUM, 1, 0, 0, 0),
//                    Difficulty(DIFFICULTY_HARD, 1, 0, 0, 0),
//                    Difficulty(DIFFICULTY_EXPERT, 1, 0, 0, 0)
//                )
//                val encode = Json.encodeToString(difficulties)
//                defaultMMKV().set(KEY_DIFFICULTY, encode)
//            }
//
//            false -> {
//                difficulties = Json.decodeFromString<List<Difficulty>>(str)
//            }
//        }
//        difficulties.sortedBy {
//            it.type
//        }
//        return difficulties
//    }
//
//    fun randDifficulty(): Difficulty {
//        val list = getDifficulties();
//        l
//    }


}