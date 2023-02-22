package com.roman.gurdan.sudo.pro.base

import android.widget.ImageView
import com.roman.garden.sudo.base.Game
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.data.db.GameData
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseSpliceGameActivity : BaseGameActivity() {

    override fun bindViews() {
        durationTxt = findViewById(R.id.duration)
        gameBoard = findViewById(R.id.gameBoard)
        gameMenu = findViewById(R.id.gameMenu)
        boardView = findViewById(R.id.boardViewReal)

        gameBoard.listener = gameBoardListener
        gameMenu.listener = gameMenuListener

        findViewById<ImageView>(R.id.setting).setOnClickListener { openSettingPage() }
        findViewById<ImageView>(R.id.back).setOnClickListener { giveUp() }
    }

    override fun onGameCreated(game: Game) {
        boardView.post {
            boardView.setupGame(game)
        }
    }

    override fun addRecord() {
        try {
            val status = isGameWin()
            com.roman.gurdan.sudo.pro.data.entry.Game().apply {
                result = if (status) 1 else 0
                duration = timerUtil.getDuration()
                gameType = game?.let { it.gameSize.tag } ?: 0
                date = SimpleDateFormat("yyyy-MM-dd").format(Date())
                difficulty = game?.let { it.difficulty.value } ?: 0
                GameData.instance.gameDao().insert(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}