package com.roman.gurdan.sudo.pro.base

import com.roman.gurdan.sudo.pro.game.Game

abstract class BaseSpliceGameActivity : BaseGameActivity() {

    override fun onGameCreated(game: Game) {
        boardView.post {
            boardView.setupGame(game)
        }
    }

//    override fun addRecord() {
//        try {
//            val status = isGameWin()
//            com.roman.gurdan.sudo.pro.data.entry.Game().apply {
//                result = if (status) 1 else 0
//                duration = timerUtil.getDuration()
//                gameType = game?.let { it.gameSize.tag } ?: 0
//                date = DateUtil.getDate()
//                difficulty = game?.let { it.difficulty.value } ?: 0
//                GameData.instance.gameDao().insert(this)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

}