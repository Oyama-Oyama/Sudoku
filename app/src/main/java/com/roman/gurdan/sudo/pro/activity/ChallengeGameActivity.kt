package com.roman.gurdan.sudo.pro.activity

import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import com.roman.garden.sudo.base.Game
import com.roman.garden.sudo.base.util.Difficulty
import com.roman.garden.sudo.base.util.GameSize
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.base.BaseGameActivity
import com.roman.gurdan.sudo.pro.data.db.GameData
import com.roman.gurdan.sudo.pro.data.entry.Weekly
import com.roman.gurdan.sudo.pro.util.DateUtil
import com.roman.gurdan.sudo.pro.view.IGameMenuListener
import java.text.SimpleDateFormat
import java.util.*

class ChallengeGameActivity : BaseGameActivity() {

    private var challengeStep: Int = 0

    override fun getLayoutId(): Int = R.layout.activity_game

    override fun bindViews() {
        durationTxt = findViewById(R.id.duration)
        gameBoard = findViewById(R.id.gameBoard)
        gameMenu = findViewById(R.id.gameMenu)
        boardView = findViewById(R.id.boardView)

        gameBoard.listener = gameBoardListener
        gameMenu.listener = gameMenuListener

        findViewById<ImageView>(R.id.setting).setOnClickListener { openSettingPage() }
        findViewById<ImageView>(R.id.back).setOnClickListener { giveUp() }
    }

    override fun onGameCreated(game: Game) = boardView.setupGame(game)

    override fun addRecord() {
        try {
            Weekly().apply {
                result = if (challengeStep >= 4) 1 else 0
                duration = timerUtil.getDuration()
                date = SimpleDateFormat("yyyy-MM-dd").format(Date())
                week = DateUtil.getWeeklyTag()
                GameData.instance.weekDao().insert(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getIntentMessage(intent: Intent?) {
        when (challengeStep) {
            0 -> {
                gameSize = GameSize.SIZE_FOUR;
            }
            1 -> {
                gameSize = GameSize.SIZE_SIX;
            }
            2 -> {
                gameSize = GameSize.SIZE_EIGHT;
            }
            3 -> {
                gameSize = GameSize.SIZE_NINE;
            }
        }
        difficulty = Difficulty.randDifficulty()
    }

    override fun onGameOver() {
        if (challengeStep < 3) {
            game?.let {
                it.destroy()
                challengeStep++
                getIntentMessage(null)
                createGame()
                resizeGameBoard()
            }
        } else {
            super.onGameOver()
        }
    }



}