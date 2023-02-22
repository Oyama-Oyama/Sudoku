package com.roman.gurdan.sudo.pro.base

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.roman.garden.core.Easy
import com.roman.garden.core.listener.IAdListener
import com.roman.garden.sudo.base.Game
import com.roman.garden.sudo.base.util.Difficulty
import com.roman.garden.sudo.base.util.GameSize
import com.roman.garden.sudo.base.view.IBoardView
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.activity.SettingActivity
import com.roman.gurdan.sudo.pro.util.Cache
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

abstract class BaseGameActivity : BaseActivity() {

    protected val dispsables = CompositeDisposable()
    protected var timerUtil = TimerUtil()
    protected lateinit var gameSize: GameSize
    protected lateinit var difficulty: Difficulty
    protected lateinit var boardView: IBoardView
    protected lateinit var gameBoard: GameBoard
    protected lateinit var gameMenu: GameMenu
    protected lateinit var durationTxt: TextView

    protected var game: Game? = null

    protected abstract fun getLayoutId(): Int

    protected abstract fun bindViews()

    protected abstract fun onGameCreated(game: Game)

    protected abstract fun addRecord()

    protected fun isGameWin(): Boolean = game?.let { it.isGameOver() } ?: false

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
            AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage(R.string.giveUp)
                .setCancelable(false)
                .setNegativeButton(R.string.cancel) { d, _ ->
                    d.dismiss()
                }
                .setPositiveButton(R.string.sure) { d, _ ->
                    d.dismiss()
                    finish()
                }.show()
        } ?: finish()
    }

    protected fun openSettingPage() {
        startActivity(Intent(this, SettingActivity::class.java))
    }

    protected fun resetGame() {
        AlertDialog.Builder(this)
            .setTitle(R.string.tip)
            .setMessage(R.string.resetGame)
            .setNegativeButton(R.string.cancel) { d, _ ->
                d.dismiss()
            }

            .setPositiveButton(getString(R.string.sure) + "(ad)") { d, _ ->
                if (Easy.instance.hasRewarded()) {
                    Easy.instance.setRewardedListener(object : IAdListener {
                        override fun onClosed(rewarded: Boolean) {
                            super.onClosed(rewarded)
                            d.dismiss()
                        }

                        override fun onUserRewarded() {
                            super.onUserRewarded()
                            realResetGame()
                        }
                    })
                    Easy.instance.showRewarded()
                } else toast(R.string.noAd)
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

    protected fun alertTip(@StringRes msgId: Int) {
        AlertDialog.Builder(this)
            .setTitle(R.string.tip)
            .setMessage(msgId)
            .setCancelable(false)
            .setPositiveButton(R.string.sure) { d, _ ->
                d.dismiss()
            }.show()
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
        Easy.instance.showInterstitial()
        AlertDialog.Builder(this)
            .setTitle(R.string.winner)
            .setMessage(R.string.win)
            .setIcon(R.mipmap.img_trophy)
            .setCancelable(false)
            .setPositiveButton(R.string.sure) { d, _ ->
                d.dismiss()
                this@BaseGameActivity.finish()
            }
            .show()
    }

    protected fun onError(throwable: Throwable) {
        AlertDialog.Builder(this)
            .setTitle(R.string.error)
            .setMessage(String.format(getString(R.string.createError), throwable.message))
            .setCancelable(false)
            .setPositiveButton(R.string.sure) { d, _ ->
                createGame()
                d.dismiss()
            }
            .setNegativeButton(R.string.cancel) { d, _ ->
                d.dismiss()
                finish()
            }.show()
    }

    protected open fun getIntentMessage(intent: Intent?) {
        if (intent == null) {
            gameSize = GameSize.SIZE_FOUR
            difficulty = Difficulty.randDifficulty()
        } else {
            val value = intent.getIntExtra("gameSize", 0)
            gameSize = GameSize.getGameSize(value)
            val diff = intent.getIntExtra("gameDiff", 0)
            difficulty =
                if (diff == 0) Difficulty.randDifficulty() else Difficulty.getDifficulty(diff)
        }
    }

    private fun setupTimer() {
        dispsables.add(Flowable.interval(0, 1, TimeUnit.SECONDS)
            .map {
                return@map DateUtil.millSecondToDate(timerUtil.getDuration())
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { s ->
                durationTxt?.let { it.text = s }
            }
        )
    }

    protected fun resizeGameBoard() = gameBoard.updateGameSize(gameSize.value)

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
                    if (g.isGameOver()) onGameOver()
                } ?: alertTip(R.string.invalidCell)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun createGame() {
        val dialog = ProgressDialog(this)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setMessage(getString(R.string.createNewGame))
        dispsables.add(Observable.zip(
            Observable.just(gameSize),
            Observable.just(difficulty)
        ) { t1, t2 ->
            return@zip Game(t1, t2).apply {
                this.createGame()
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                if (!dialog.isShowing)
                    dialog.show()
            }
            .doOnComplete {
                if (dialog.isShowing)
                    dialog.cancel()
            }
            .doOnError { e ->
                onError(e)
            }
            .subscribe { tmp ->
                game = tmp
                game?.let {
                    onGameCreated(it)
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        bindViews()
        getIntentMessage(intent)
        window.decorView.post {
            MainScope().launch {
                createGame()
                resizeGameBoard()
                setupTimer()
                timerUtil.reset()
            }
        }
        Easy.instance.showBanner()
        Easy.instance.showInterstitial()
    }

    override fun onResume() {
        super.onResume()
        timerUtil.resume()
        boardView.post {
            boardView.highLightLineOrRow =
                Cache.getCache().decodeBool(Cache.SETTING_HIGH_LIGHT_SAME_ROW_COLUMN, true)
            boardView.highLightGroup =
                Cache.getCache().decodeBool(Cache.SETTING_HIGH_LIGHT_SAME_GROUP, true)
            boardView.highLightErrorNumber =
                Cache.getCache().decodeBool(Cache.SETTING_HIGH_LIGHT_ERROR_VALUE, true)
            boardView.highLightSameNumber =
                Cache.getCache().decodeBool(Cache.SETTING_HIGH_LIGHT_SAME_VALUE, true)
        }
    }

    override fun onPause() {
        super.onPause()
        timerUtil.pause()
    }

    override fun onDestroy() {
        if (!dispsables.isDisposed) dispsables.dispose()
        Easy.instance.closeBanner()
        addRecord()
        super.onDestroy()
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