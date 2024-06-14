//package game
//
//import androidx.lifecycle.ViewModel
//import com.ctrip.flight.mmkv.defaultMMKV
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.serialization.json.Json
//import models.Difficulty
//
//import models.GameItem
//import net.requestGameItem
//import viewModels.PlayingGameConfig
//
//
//
//class GameModel : ViewModel() {
//
//
//    fun setupGame(playingGameConfig: PlayingGameConfig?) {
//        if (playingGameConfig == null) return
//        if (playingGameConfig.date != null) {
//            val hadFinished = defaultMMKV().takeBoolean("state_${playingGameConfig.date}", false)
//            if (hadFinished) {
//                //如果当天已完成， 给出提示
//            } else {
//                val preGame = defaultMMKV().takeString("game_${playingGameConfig.date}")
//                if (preGame.isEmpty()) {
//                    // 获取随机新游戏
////                    val diff = Difficulty(type = 0)
////                    createNewGame(difficulty)
//                } else {
//                    val game = Json.decodeFromString<GameData>(preGame)
//                    playingGame.value = Game(game)
//                    initNumbers()
//                }
//            }
//            return
//        }
//        if (playingGameConfig.isContinue) {
//            unfinishedGame.value?.let {
//                playingGame.value = Game(it)
//                initNumbers()
//            }
//            unfinishedGame.value = null
//            return
//        }
//        if (playingGameConfig.difficulty != null) {
//            // 获取随机新游戏
//            CoroutineScope(Dispatchers.Default).launch {
//                createNewGame(playingGameConfig.difficulty)?.let {
//                    playingGame.value = Game(it)
//                    initNumbers()
//                }
//                println("playing game::${playingGame.value}")
//            }
//        }
//    }
//
//    private suspend fun createNewGame(difficulty: Difficulty): GameData? {
//        val result: Result<GameItem> = requestGameItem(difficulty.type)
//        if (result.isSuccess && result.getOrNull() != null) {
//            val gameItem = result.getOrNull()!!
//            val game = GameData().apply {
//                this.type = difficulty.type
//                this.mission = gameItem.mission
//                this.board = gameItem.mission
//                this.solution = gameItem.solution
//            }
//            return game
//        }
//        return null
//    }
//
//
//}