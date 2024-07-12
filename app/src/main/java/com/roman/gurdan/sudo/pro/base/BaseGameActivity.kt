package com.roman.gurdan.sudo.pro.base

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.roman.garden.core.Easy
import com.roman.garden.core.listener.IAdListener
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.activity.SettingActivity
import com.roman.gurdan.sudo.pro.dialog.EmptyDialog
import com.roman.gurdan.sudo.pro.dialog.LoadingDialog
import com.roman.gurdan.sudo.pro.dialog.TAlertDialog
import com.roman.gurdan.sudo.pro.game.Game
import com.roman.gurdan.sudo.pro.game.action.IMirror
import com.roman.gurdan.sudo.pro.game.util.Difficulty
import com.roman.gurdan.sudo.pro.game.util.GameSize
import com.roman.gurdan.sudo.pro.game.view.IBoardView
import com.roman.gurdan.sudo.pro.game.view.IBoardViewListener
import com.roman.gurdan.sudo.pro.model.GameViewModel
import com.roman.gurdan.sudo.pro.util.DateUtil
import com.roman.gurdan.sudo.pro.util.LocalStorage
import com.roman.gurdan.sudo.pro.util.TimerUtil
import com.roman.gurdan.sudo.pro.util.Utills
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

data class GameBackUp(
    val gameSize: GameSize,
    val difficulty: Difficulty,
    val duration: Long,
    val mirror: IMirror
)

fun GameBackUp.ToStr(): String {
    return "${gameSize.tag}_${difficulty.value}_${duration}_${mirror.data}"
}

data class RecoverGame(val str: String)

fun RecoverGame.ToSelf(str: String): GameBackUp? {
    try {
        val splits = str.split("_")
        val gameSize = GameSize.getGameSize(splits[0].toInt())
        val difficulty = Difficulty.getDifficulty(splits[1].toInt())
        val duration = splits[2].toLong()
        val mirror = IMirror(splits[3], false)
        return GameBackUp(gameSize, difficulty, duration, mirror)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

abstract class BaseGameActivity : BaseActivity() {

    private var isRecover = false
    private var playDate: String? = null

    protected val dispsables = CompositeDisposable()
    protected var timerUtil = TimerUtil()

    protected lateinit var boardView: IBoardView
    protected lateinit var gameBoard: GameBoard
    protected lateinit var gameMenu: GameMenu
    protected lateinit var durationTxt: TextView
    protected lateinit var difficultyTxt: TextView
    protected lateinit var errorCount: TextView

    private var MAX_ERROR_COUNT = 3
    private var currentErrorCount = 5

    protected var game: Game? = null

    protected lateinit var gameViewModel: GameViewModel

    protected abstract fun getLayoutId(): Int

    protected fun bindViews() {
        durationTxt = findViewById(R.id.duration)
        gameBoard = findViewById(R.id.gameBoard)
        gameMenu = findViewById(R.id.gameMenu)
        (findViewById(R.id.boardView) as? IBoardView)?.let {
            boardView = it
        } ?: run { boardView = findViewById(R.id.boardViewReal) }

//        boardView = findViewById(R.id.boardView)
        difficultyTxt = findViewById(R.id.difficulty)

        gameBoard.listener = gameBoardListener
        gameMenu.listener = gameMenuListener
        boardView.boardViewListener = object : IBoardViewListener {
            override fun onReady() {
                gameBoard.visibility = View.VISIBLE
                gameMenu.visibility = View.VISIBLE
            }
        }
        findViewById<ImageView>(R.id.setting).setOnClickListener { openSettingPage() }
        findViewById<ImageView>(R.id.pause).setOnClickListener { pauseGame() }
        findViewById<ImageView>(R.id.restart).setOnClickListener { resetGame() }
        findViewById<ImageView>(R.id.back).setOnClickListener { giveUp() }
    }

    protected abstract fun onGameCreated(game: Game)

    private fun splitPlayDate(): List<String>? {
        return when (playDate.isNullOrEmpty()) {
            true -> null
            false -> {
                val list = playDate!!.split(":")
                if (list != null && list.size == 3)
                    return list
                return null
            }
        }
    }

    protected fun addRecord(isGameFailure: Boolean = false) {
        if (playDate != null) {
            game?.let {
                it.copy()?.let { copy ->
                    GameBackUp(
                        it.gameSize,
                        it.difficulty,
                        timerUtil.getDuration(),
                        copy
                    ).apply {
                        LocalStorage.encode(playDate!!, this.ToStr())
                    }
                }
            }
        } else {
            game?.let {
                val baseKey = "game_${it.gameSize.tag}_${it.difficulty.value}"
                when (isGameFailure) {
                    true -> {
                        val keyFailure = "${baseKey}_failure"
                        val failedCount = LocalStorage.decode(keyFailure, 0) + 1
                        LocalStorage.encode(keyFailure, failedCount)
                    }

                    false -> {
                        when (isGameWin()) {
                            true -> {
                                val keyWin = "${baseKey}_win"
                                val keyTime = "${baseKey}_duration"
                                val winCount = LocalStorage.decode(keyWin, 0) + 1
                                LocalStorage.encode(keyWin, winCount)
                                var lastDuration = LocalStorage.decode(keyTime, 99999999999L)
                                val duration = timerUtil.getDuration()
                                if (lastDuration > duration) lastDuration = duration
                                LocalStorage.encode(keyTime, lastDuration)

                            }

                            false -> {
                                //未完成
                                it.copy()?.let { copy ->
                                    GameBackUp(
                                        it.gameSize,
                                        it.difficulty,
                                        timerUtil.getDuration(),
                                        copy
                                    ).apply {
                                        LocalStorage.encode("LastUnFinishedGame", this.ToStr())
                                        Log.e("adadadada", "sava data")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

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

            EmptyDialog(this).setLayout(R.layout.dialog_pause)
                .cancelable(false)
                .bindImage(R.id.image, Utills.getTitleImage())
                .bindTextView(R.id.title, R.string.giveUp)
                .bindTextView(R.id.message, R.string.giveUpContent)
                .bindTextView(R.id.active, R.string.no) { _, dialog ->
                    dialog.dismiss()
                }.bindTextView(R.id.inactive, R.string.yes) { _, dialog ->
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
                }.show()
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
        timerUtil.pause()
        EmptyDialog(this).setLayout(R.layout.dialog_pause)
            .cancelable(false)
            .bindImage(R.id.image, Utills.getTitleImage())
            .bindTextView(R.id.title, R.string.restart)
            .bindTextView(R.id.message, R.string.restartContent)
            .bindTextView(R.id.active, R.string.continueGame) { _, dialog ->
                timerUtil.resume()
                dialog.dismiss()
            }.bindTextView(R.id.inactive, R.string.restart) { _, dialog ->
                realResetGame()
                dialog.dismiss()
            }.show()
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

    protected fun pauseGame() {
        timerUtil.pause()
        EmptyDialog(this).setLayout(R.layout.dialog_pause)
            .cancelable(false)
            .bindImage(R.id.image, Utills.getTitleImage())
            .bindTextView(R.id.title, R.string.pause)
            .bindTextView(R.id.message, R.string.pause_info)
            .bindTextView(R.id.active, R.string.continueGame) { _, dialog ->
                timerUtil.resume()
                dialog.dismiss()
            }.bindTextView(R.id.inactive, R.string.restart) { _, dialog ->
                realResetGame()
                dialog.dismiss()
            }.show()
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

        EmptyDialog(this).setLayout(R.layout.dialog_pause)
            .cancelable(false)
            .bindImage(R.id.image, R.mipmap.ic_win_first)
            .bindTextView(R.id.title, R.string.win)
            .bindTextView(R.id.message, R.string.winContent)
            .bindTextView(R.id.active, R.string.yes) { _, dialog ->
                this@BaseGameActivity.finish()
                dialog.dismiss()
            }.bindTextView(R.id.inactive, R.string.restart) { _, dialog ->
                resetGame()
                dialog.dismiss()
            }.show()
    }

    protected fun onError(throwable: Throwable) {
        EmptyDialog(this).setLayout(R.layout.dialog_alert)
            .cancelable(false)
            .bindImage(R.id.image, Utills.getTitleImage())
            .bindTextView(R.id.title, R.string.error)
            .bindTextView(
                R.id.message,
                String.format(getString(R.string.createError), throwable.message)
            )
            .bindTextView(R.id.active, R.string.yes) { _, dialog ->
                this@BaseGameActivity.createGame(
                    gameViewModel.gameSize.value!!,
                    gameViewModel.gameDifficulty.value!!
                )
                dialog.dismiss()
            }.bindTextView(R.id.inactive, R.string.no) { _, dialog ->
                this@BaseGameActivity.finish()
                dialog.dismiss()
            }.show()
    }

    protected fun onRevive() {
        EmptyDialog(this).setLayout(R.layout.dialog_pause)
            .cancelable(false)
            .bindImage(R.id.image, Utills.getTitleImage())
            .bindTextView(R.id.title, R.string.revive)
            .bindTextView(R.id.message, R.string.reviveContent)
            .bindTextView(R.id.active, R.string.reviveAd) { _, dialog ->
                when (Easy.instance.hasRewarded()) {
                    true -> {
                        Easy.instance.setRewardedListener(object : IAdListener {
                            override fun onClosed(rewarded: Boolean) {
                                super.onClosed(rewarded)
                                if (rewarded) {
                                    resetErrorCount()
                                    dialog.dismiss()
                                }
                            }
                        })
                        Easy.instance.showRewarded()
                    }

                    else -> toast(R.string.noAd)
                }
            }.bindTextView(R.id.inactive, R.string.giveUp) { _, dialog ->
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
            }.show()
    }

    protected open fun getIntentMessage(intent: Intent?) {
        try {
            if (intent == null)
                throw NullPointerException("intent is null")
            if (intent.hasExtra("recover")) {
                isRecover = true
                val unfinishedGame = LocalStorage.decode("LastUnFinishedGame", "")
                RecoverGame(unfinishedGame!!).apply {
                    this.ToSelf(this.str)?.let {
                        gameViewModel.updateGameSize(it.gameSize.tag)
                        gameViewModel.updateGameDifficulty(it.difficulty)
                        timerUtil.reset()
                        timerUtil.plus(it.duration)
                        game = Game(it.gameSize, it.difficulty)
                        game?.let { g ->
                            g.setupGame(it.mirror)
                            boardView.post {
                                onGameCreated(g)
                            }

                            // boardView.postInvalidate()
                            setupTimer()

                        }
                    } ?: run {
                        throw IllegalStateException("")
                    }
                }

            } else if (intent.hasExtra("playDate")) {
                isRecover = true
                playDate = intent.getStringExtra("playDate")
                playDate?.let { date ->
                    val unfinishedGame = LocalStorage.decode(date, "")
                    RecoverGame(unfinishedGame!!).apply {
                        this.ToSelf(this.str)?.let {
                            gameViewModel.updateGameSize(it.gameSize.tag)
                            gameViewModel.updateGameDifficulty(it.difficulty)
                            timerUtil.reset()
                            timerUtil.plus(it.duration)
                            game = Game(it.gameSize, it.difficulty)
                            game?.let { g ->
                                g.setupGame(it.mirror)
                                boardView.post {
                                    onGameCreated(g)
                                }
                                // boardView.postInvalidate()
                                setupTimer()
                            }
                        } ?: run {
                            throw IllegalStateException("")
                        }
                    }
                }
            } else {
                isRecover = false
                val value = intent.getIntExtra("gameSize", 0)
                val diff = intent.getIntExtra("gameDiff", 0)
                gameViewModel.updateGameSize(value)
                gameViewModel.updateGameDifficulty(
                    Difficulty.getDifficulty(
                        diff = diff,
                        tag = value
                    )
                )
            }
        } catch (e: Exception) {
            isRecover = false
            gameViewModel.updateGameSize(GameSize.SIZE_NINE.tag)
            gameViewModel.updateGameDifficulty(Difficulty.randDifficulty(GameSize.SIZE_NINE.tag))
        } finally {
            gameViewModel.setError(0)
        }
    }

    private fun setupTimer() {
        dispsables.add(Flowable.interval(0, 1, TimeUnit.SECONDS).map {
            return@map DateUtil.millSecondToDate(timerUtil.getDuration())
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { s ->
            durationTxt?.let { it.text = s }
        })
    }

    protected fun resizeGameBoard(gameSize: GameSize) = gameBoard.updateGameSize(gameSize.col)

    protected fun onMenu(key: Int) {
        try {
            when (key) {
                0 -> {
                    game?.let {
                        if (it.hasUndo()) {
                            it.undo()
                            Easy.instance.logEvent("undo", null)
                        } else {
                            alertTip(R.string.invalidUndo)
                        }
                    }
                }

                1 -> game?.setValue(boardView.selectedCell, 0)

                2 -> {
                    game?.let {
                        it.toggleNote(boardView.selectedCell)
                        gameMenu.updateNoteStatus(it.isNoteOn())
                        Easy.instance.logEvent("notes", null)
                    }
                }

                3 -> game?.hint(boardView.selectedCell)

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
                        }

                        false -> {
                            when (cell.valid) {
                                true -> {}
                                else -> gameViewModel.addError(1)
                            }
                        }
                    }
                } ?: alertTip(R.string.invalidCell)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun createGame(gameSize: GameSize, difficulty: Difficulty) {
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
                    setupTimer()
                    timerUtil.reset()
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        bindViews()
        errorCount = findViewById(R.id.errorCount)

        gameViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(GameViewModel::class.java)
        gameViewModel.gameSize.observe(this) { size ->
            resizeGameBoard(size)
        }
        gameViewModel.gameDifficulty.observe(this) { diff ->
            difficultyTxt.text = when (diff) {
                Difficulty.EASY -> getString(R.string.easy)
                Difficulty.MEDIUM -> getString(R.string.medium)
                Difficulty.HARD -> getString(R.string.hard)
                Difficulty.EXPERT -> getString(R.string.expert)
                else -> getString(R.string.hard)
            }
            if (!isRecover) {
                window.decorView.post {
                    MainScope().launch {
                        createGame(gameViewModel.gameSize.value!!, diff)
                    }
                }
            }
        }
        gameViewModel.error.observe(this) { num ->
            if (num >= MAX_ERROR_COUNT) {
                onRevive()
            }
            errorCount.text = getString(R.string.error) + ":$num"
        }
        getIntentMessage(intent)
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

    override fun onStop() {
        super.onStop()
        addRecord()
    }

    override fun onDestroy() {
        if (!dispsables.isDisposed) dispsables.dispose()
        //addRecord()
        game?.destroy()
        super.onDestroy()
    }

    protected fun resetErrorCount() {
        gameViewModel.setError(0)
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