package com.roman.gurdan.sudo.pro.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.roman.gurdan.sudo.pro.game.Game
import com.roman.gurdan.sudo.pro.game.util.Difficulty
import com.roman.gurdan.sudo.pro.game.util.GameSize
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.base.BaseGameActivity
import com.roman.gurdan.sudo.pro.data.db.GameData
import com.roman.gurdan.sudo.pro.data.entry.Weekly
import com.roman.gurdan.sudo.pro.util.DateUtil

class ChallengeGameActivity : BaseGameActivity() {

    private var challengeStep: Int = 0

    override fun getLayoutId(): Int = R.layout.activity_game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resetRewardStarCount(8)
        resetHeartCount(6)
        resetReviveStarCount(3)
    }

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
                date = DateUtil.getDate()
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
                gameSize = GameSize.SIZE_FOUR
            }
            1 -> {
                gameSize = GameSize.SIZE_SIX
            }
            2 -> {
                gameSize = GameSize.SIZE_EIGHT
            }
            3 -> {
                gameSize = GameSize.SIZE_NINE
            }
        }
        difficulty = Difficulty.randDifficulty(gameSize.tag)
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