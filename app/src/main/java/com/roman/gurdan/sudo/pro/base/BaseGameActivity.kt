package com.roman.gurdan.sudo.pro.base

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.roman.garden.core.Easy
import com.roman.garden.core.listener.IAdListener
import com.roman.gurdan.sudo.pro.game.Game
import com.roman.gurdan.sudo.pro.game.util.Difficulty
import com.roman.gurdan.sudo.pro.game.util.GameSize
import com.roman.gurdan.sudo.pro.game.view.IBoardView
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.activity.SettingActivity
import com.roman.gurdan.sudo.pro.dialog.*
import com.roman.gurdan.sudo.pro.model.GameViewModel
import com.roman.gurdan.sudo.pro.util.LocalStorage
import com.roman.gurdan.sudo.pro.util.DateUtil
import com.roman.gurdan.sudo.pro.util.TimerUtil
import com.roman.gurdan.sudo.pro.view.GameBoard
import com.roman.gurdan.sudo.pro.view.GameMenu
import com.roman.gurdan.sudo.pro.view.IGameMenuListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

import java.util.concurrent.TimeUnit
import kotlin.random.Random

abstract class BaseGameActivity : BaseActivity() {

    protected val dispsables = CompositeDisposable()
    protected var timerUtil = TimerUtil()
    protected lateinit var gameSize: GameSize
    protected lateinit var difficulty: Difficulty
    protected lateinit var boardView: IBoardView
    protected lateinit var gameBoard: GameBoard
    protected lateinit var gameMenu: GameMenu
    protected lateinit var durationTxt: TextView
    protected lateinit var difficultyTxt: TextView
    protected lateinit var errorCount: TextView

    private var MAX_ERROR_COUNT = 3
    private var currentErrorCount = 5
    private var reviveStarCount = 2
    private var rewardStarBase = 5

    protected var game: Game? = null

    protected abstract fun getLayoutId(): Int

    protected abstract fun bindViews()

    protected abstract fun onGameCreated(game: Game)

    protected abstract fun addRecord()

    protected fun isGameWin(): Boolean = game?.isGameOver() ?: false

    override fun onBackPressed() {
        // super.onBackPressed()
        this.giveUp()
    }

    protected fun giveUp() {
        game?.let { g ->
            if (g.isGameOver()) {
                finish()
                return
            }
            try {
                Bundle().apply {
                    putString("size", game?.gameSize?.tag.toString())
                    putString("difficulty", game?.difficulty?.value.toString())
                    Easy.instance.logEvent("giveUpGame", this)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            TAlertDialog(this).apply {
                this.setTitleId(R.string.giveUp)
                this.setContentId(R.string.giveUpContent)
                this.setActiveId(R.string.no, object : IEvent {
                    override fun onEvent(dialog: BaseDialog) {
                        dialog.dismiss()
                    }
                })
                this.setInActiveId(R.string.yes, object : IEvent {
                    override fun onEvent(dialog: BaseDialog) {
                        dialog.dismiss()
                        this@BaseGameActivity.finish()
                    }
                }, color = -1, duration = 3000)
                this.show()
            }
        } ?: finish()
    }

    protected fun openSettingPage() {
        Intent(this, SettingActivity::class.java).apply {
            startActivityIfNeeded(this, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            //      Easy.instance.showInterstitial()
        }
    }

    protected fun resetGame() {
        TAlertDialog(this).apply {
            this.setTitleId(R.string.restart)
            this.setContentId(R.string.restartContent)
            this.setActiveId(R.string.yesAd, object : IEvent {
                override fun onEvent(dialog: BaseDialog) {
                    when (Easy.instance.hasRewarded()) {
                        true -> {
                            Easy.instance.setRewardedListener(object : IAdListener {
                                override fun onClosed(rewarded: Boolean) {
                                    super.onClosed(rewarded)
                                    if (rewarded) {
                                        realResetGame()
                                        dialog.dismiss()
                                    }
                                }
                            })
                            Easy.instance.showRewarded()
                        }
                        else -> toast(R.string.noAd)
                    }
                }
            })
            this.setInActiveId(R.string.no, object : IEvent {
                override fun onEvent(dialog: BaseDialog) {
                    dialog.dismiss()
                }
            })
            this.show()
        }
    }

    private fun realResetGame() {
        game?.let {
            it.resetGame()
            timerUtil.reset()
        }
        try {
            Bundle().apply {
                putString("size", game?.gameSize?.tag.toString())
                putString("difficulty", game?.difficulty?.value.toString())
                Easy.instance.logEvent("resetGame", this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun alertTip(@StringRes msgId: Int) {
        TAlertDialog(this).apply {
            this.setTitleId(R.string.tip, color = Color.YELLOW)
            this.setContentId(msgId)
            this.setActiveId(R.string.yes, null, color = Color.GREEN, duration = 3000)
            this.hideInActive()
            this.show()
        }
    }

    protected open fun onGameOver() {
        timerUtil.cancel()
        try {
            Bundle().apply {
                putString("size", game?.gameSize?.tag.toString())
                putString("difficulty", game?.difficulty?.value.toString())
                Easy.instance.logEvent("win", this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Easy.instance.getSignInAccount(this.applicationContext)?.let { account ->
            var score = gameSize.col * gameSize.row * difficulty.value * 0.2;
            if (score >= 160){
                score /= 2
            }
            Easy.instance.submitScore(this.applicationContext, account, "", score.toLong())
        }

        WinDialog(this, R.layout.dialog_win).apply {
            this.setTitleId(R.string.win)
            this.setContentId(R.string.winContent)
            val starReward = rewardStarBase + Random.nextInt(0, 5)
            this.setStarCount(starReward)
            LocalStorage.addStar(starReward)
            this.setActiveId(R.string.yes, object : IEvent {
                override fun onEvent(dialog: BaseDialog) {
                    dialog.dismiss()
                    this@BaseGameActivity.finish()
                }
            })
            this.hideInActive()
            this.show()
        }
    }

    protected fun onError(throwable: Throwable) {
        TAlertDialog(this).apply {
            this.setTitleId(R.string.error, color = Color.RED)
            this.setContent(String.format(getString(R.string.createError), throwable.message))
            this.setCancelable(false)
            this.setCanceledOnTouchOutside(false)
            this.setActiveId(R.string.yes, object : IEvent {
                override fun onEvent(dialog: BaseDialog) {
                    this@BaseGameActivity.createGame()
                    dialog.dismiss()
                }
            }, color = Color.GREEN)
            this.setInActiveId(R.string.no, object : IEvent {
                override fun onEvent(dialog: BaseDialog) {
                    dialog.dismiss()
                    this@BaseGameActivity.finish()
                }
            }, color = -1, duration = 3000)
            this.show()
        }
    }

    protected fun onRevive() {
        ReviveDialog(this, R.layout.dialog_revive).apply {
            this.setTitleId(R.string.revive, color = Color.RED)
            this.setContentId(R.string.reviveContent)
            this.setCancelable(false)
            this.setCanceledOnTouchOutside(false)
            this.setActiveId(R.string.reviveAd, object : IEvent {
                override fun onEvent(dialog: BaseDialog) {
                    when (Easy.instance.hasRewarded()) {
                        true -> {
                            Easy.instance.setRewardedListener(object : IAdListener {
                                override fun onClosed(rewarded: Boolean) {
                                    super.onClosed(rewarded)
                                    if (rewarded) {
                                        updateHeart(0)
                                        dialog.dismiss()
                                    }
                                }
                            })
                            Easy.instance.showRewarded()
                        }
                        else -> toast(R.string.noAd)
                    }
                }
            })
            this.setInActive(String.format(getString(R.string.reviveStar), reviveStarCount),
                object : IEvent {
                    override fun onEvent(dialog: BaseDialog) {
                        when (LocalStorage.hasStar(reviveStarCount)) {
                            true -> {
                                LocalStorage.addStar(-1 * reviveStarCount)
                                updateHeart(DEFAULT_HEART_COUNT)
                                dialog.dismiss()
                            }
                            else -> toast(R.string.noEnoughStar)
                        }
                    }
                })
            this.setQuit(R.string.giveUp, object : IEvent {
                override fun onEvent(dialog: BaseDialog) {
                    try {
                        Bundle().apply {
                            putString("size", game?.gameSize?.tag.toString())
                            putString("difficulty", game?.difficulty?.value.toString())
                            Easy.instance.logEvent("giveUpGame", this)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    dialog.dismiss()
                    this@BaseGameActivity.finish()
                }
            }, color = -1, duration = 3000)
            this.show()
        }
    }

    protected open fun getIntentMessage(intent: Intent?) {
        if (intent == null) {
            gameSize = GameSize.SIZE_NINE
            difficulty = Difficulty.randDifficulty(gameSize.tag)
        } else {
            val value = intent.getIntExtra("gameSize", 0)
            gameSize = GameSize.getGameSize(value)
            val diff = intent.getIntExtra("gameDiff", 0)
            difficulty =
                if (diff == 0) Difficulty.randDifficulty(gameSize.tag) else Difficulty.getDifficulty(
                    diff
                )
        }

        when (gameSize) {
            GameSize.SIZE_FOUR -> {
                resetReviveStarCount(1)
                resetHeartCount(3)
                resetRewardStarCount(3)
            }
            GameSize.SIZE_SIX -> {
                resetReviveStarCount(2)
                resetHeartCount(4)
                resetRewardStarCount(4)
            }
            GameSize.SIZE_EIGHT -> {
                resetReviveStarCount(3)
                resetHeartCount(4)
                resetRewardStarCount(5)
            }
            GameSize.SIZE_NINE -> {
                resetReviveStarCount(3)
                resetHeartCount(5)
                resetRewardStarCount(6)
            }
            else -> {
                resetReviveStarCount(5)
                resetHeartCount(6)
                resetRewardStarCount(8)
            }
        }
    }

    private fun setupTimer() {
        dispsables.add(Flowable.interval(0, 1, TimeUnit.SECONDS).map {
            return@map DateUtil.millSecondToDate(timerUtil.getDuration())
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { s ->
            durationTxt?.let { it.text = s }
        })
    }

    protected fun resizeGameBoard() = gameBoard.updateGameSize(gameSize.col)

    protected fun onMenu(key: Int) {
        try {
            when (key) {
                0 -> resetGame()
                1 -> {
                    game?.let {
                        if (it.hasUndo()) {
                            it.undo()
                            Easy.instance.logEvent("undo", null)
                        } else {
                            alertTip(R.string.invalidUndo)
                        }
                    }
                }
                2 -> {
                    game?.let {
                        it.toggleNote(boardView.selectedCell)
                        gameMenu.updateNoteStatus(it.isNoteOn())
                        Easy.instance.logEvent("notes", null)
                    }
                }
                3 -> game?.setValue(boardView.selectedCell, 0)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun onNumber(num: Int) {
        try {
            game?.let { g ->
                boardView.selectedCell?.let { cell ->
                    g.setValue(cell, num)
                    when (g.isGameOver()) {
                        true -> {
                            this@BaseGameActivity.onGameOver()
//                            LoadingDialog(this@BaseGameActivity).let { dialog ->
//                                dispsables.add(Flowable.timer(3, TimeUnit.SECONDS)
//                                    .subscribeOn(AndroidSchedulers.mainThread())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .doOnSubscribe {
//                                        timerUtil.cancel()
//                                        dialog.show()
//                                    }
//                                    .subscribe {
//                                        dialog.dismiss()
//                                        Easy.instance.showInterstitial()
//                                        this@BaseGameActivity.onGameOver()
//                                    })
//                            }
                        }
                        false -> {
                            when (cell.valid) {
                                true -> updateHeart(1)
                                else -> updateHeart(-1)
                            }
                        }
                    }
                } ?: alertTip(R.string.invalidCell)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun createGame() {
//        val dialog = ProgressDialog(this)
//        dialog.setCancelable(false)
//        dialog.setCanceledOnTouchOutside(false)
//        dialog.setMessage(getString(R.string.createNewGame))
        LoadingDialog(this@BaseGameActivity).let { dialog ->
            dispsables.add(Observable.zip(
                Observable.just(gameSize), Observable.just(difficulty)
            ) { t1, t2 ->
                return@zip Game(t1, t2).apply {
                    this.createGame()
                }
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                if (!dialog.isShowing) dialog.show()
            }.doOnComplete {
                if (dialog.isShowing) dialog.cancel()
            }.doOnError { e ->
                onError(e)
            }.subscribe { tmp ->
                game = tmp
                game?.let {
                    onGameCreated(it)
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        bindViews()
        errorCount = findViewById(R.id.errorCount)

        val gameViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)).get(GameViewModel::class.java)

        getIntentMessage(intent)
        window.decorView.post {
            MainScope().launch {
                createGame()
                resizeGameBoard()
                setupTimer()
                timerUtil.reset()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        Easy.instance.showBanner()
        timerUtil.resume()
        boardView.post {
            boardView.highLightLineOrRow =
                LocalStorage.decode(LocalStorage.SETTING_HIGH_LIGHT_SAME_ROW_COLUMN, true)
            boardView.highLightGroup =
                LocalStorage.decode(LocalStorage.SETTING_HIGH_LIGHT_SAME_GROUP, true)
            boardView.highLightErrorNumber =
                LocalStorage.decode(LocalStorage.SETTING_HIGH_LIGHT_ERROR_VALUE, true)
            boardView.highLightSameNumber =
                LocalStorage.decode(LocalStorage.SETTING_HIGH_LIGHT_SAME_VALUE, true)
        }
    }

    override fun onPause() {
        super.onPause()
        Easy.instance.closeBanner()
        timerUtil.pause()
    }

    override fun onDestroy() {
        if (!dispsables.isDisposed) dispsables.dispose()
        addRecord()
        game?.destroy()
        super.onDestroy()
    }

    protected fun resetReviveStarCount(count: Int) {
        reviveStarCount = count
    }

    protected fun resetRewardStarCount(count: Int) {
        rewardStarBase = count
    }

    protected fun resetErrorCount(count: Int) {
        currentErrorCount = count
        errorCount?.text = getString(R.string.error) + ":$currentErrorCount"
    }

    private fun updateError(num: Int) {
        currentErrorCount += num
        if (currentErrorCount <= 0) {
            currentErrorCount = 0
            onRevive()
        } else if (currentErrorCount > DEFAULT_HEART_COUNT) {
            currentHeartCount = DEFAULT_HEART_COUNT
        }
        errorCount?.text = "$currentHeartCount"
    }

    protected val gameMenuListener = object : IGameMenuListener<Int> {
        override fun onMenuItem(t: Int) {
            onMenu(t)
        }
    }

    protected val gameBoardListener = object : IGameMenuListener<Int> {
        override fun onMenuItem(t: Int) {
            onNumber(t)
        }
    }

}