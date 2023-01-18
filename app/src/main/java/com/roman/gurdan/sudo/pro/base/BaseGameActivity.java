package com.roman.gurdan.sudo.pro.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.roman.garden.core.Easy;
import com.roman.gurdan.sudo.game.Difficulty;
import com.roman.gurdan.sudo.game.Game;
import com.roman.gurdan.sudo.game.GameSize;
import com.roman.gurdan.sudo.game.action.MirrorManager;
import com.roman.gurdan.sudo.game.factory.GameFactory;
import com.roman.gurdan.sudo.game.factory.IGameCreator;
import com.roman.gurdan.sudo.pro.R;
import com.roman.gurdan.sudo.pro.activity.SettingActivity;
import com.roman.gurdan.sudo.pro.util.CacheId;
import com.roman.gurdan.sudo.pro.util.DateUtil;
import com.roman.gurdan.sudo.pro.util.TimerUtil;
import com.roman.gurdan.sudo.pro.view.GameBoard;
import com.roman.gurdan.sudo.pro.view.GameMenu;
import com.roman.gurdan.sudo.util.LogUtil;
import com.roman.gurdan.sudo.view.BoardView;
import com.roman.gurdan.sudo.view.IBoardEvent;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class BaseGameActivity extends BaseActivity {

    protected TimerUtil timerUtil = new TimerUtil();
    private CompositeDisposable disposables = new CompositeDisposable();
    protected GameSize gameSize;
    protected Difficulty difficulty;

    protected BoardView boardView;
    protected GameBoard gameBoard;
    protected GameMenu gameMenu;
    protected TextView durationTxt;

    protected abstract int getLayoutId();

    protected abstract void bindViews();

    protected abstract TextView getDurationTxt();

    protected abstract void onGameCreated(Game game);

    protected abstract void addRecord();

    protected boolean isGameWin(){
        return boardView == null ? false : boardView.isGameOver();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        this.giveUp();
    }

    protected void giveUp(){
        if (boardView.isGameOver()){
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.tip)
                    .setMessage(R.string.giveUp)
                    .setCancelable(false)
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    }).show();
        }
    }

    protected void openSettingPage() {
        startActivity(new Intent(this, SettingActivity.class));
    }

    protected void resetGame() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage(R.string.resetGame)
                .setCancelable(false)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (boardView != null){
                            boardView.resetGame();
                        }
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    protected void alertTip(int msgId) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage(msgId)
                .setCancelable(false)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    protected void onGameOver() {
        timerUtil.cancel();
        Easy.Companion.showInterstitial();
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setIcon(R.mipmap.img_trophy)
                .setMessage(R.string.win)
                .setCancelable(false)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                }).show();
    }

    private void onGameCreateError(Throwable throwable) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(String.format(getString(R.string.createError), throwable.getMessage()))
                .setCancelable(false)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createGame();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BaseGameActivity.this.finish();
                    }
                }).show();
    }

    protected void getIntentMessage(Intent intent) {
        if (intent == null) {
            gameSize = GameSize.SIZE_FOUR;
            difficulty = Difficulty.randDifficulty();
        } else {
            int value = intent.getIntExtra("gameSize", 0);
            gameSize = GameSize.getGameSize(value);
            int diff = intent.getIntExtra("gameDiff", 0);
            if (diff == 0)
                difficulty = Difficulty.randDifficulty();
            else
                difficulty = Difficulty.getDifficulty(diff);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bindViews();
        Intent intent = getIntent();
        getIntentMessage(intent);
        createGame();
        resizeGameBoard();
        setupTimer();
        timerUtil.reset();
        FrameLayout banner = findViewById(R.id.bottomBanner);
        Easy.Companion.showBanner(banner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerUtil.resume();
        if (boardView != null) {
            boardView.resetSettings(
                    CacheId.getCache().decodeBool(CacheId.SETTING_HIGH_LIGHT_SAME_ROW_COLUMN, true),
                    CacheId.getCache().decodeBool(CacheId.SETTING_HIGH_LIGHT_SAME_GROUP, true),
                    CacheId.getCache().decodeBool(CacheId.SETTING_HIGH_LIGHT_SAME_VALUE, true),
                    CacheId.getCache().decodeBool(CacheId.SETTING_HIGH_LIGHT_ERROR_VALUE, true)
            );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerUtil.pause();
    }

    @Override
    protected void onDestroy() {
        timerUtil.cancel();
        if (disposables != null && !disposables.isDisposed()) disposables.dispose();
        addRecord();
        super.onDestroy();
        Easy.Companion.hideBanner();
    }

    private void setupTimer() {
        disposables.add(Observable.interval(1, TimeUnit.SECONDS)
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Throwable {
//                        long duration = timerUtil.getDuration();
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//                        return dateFormat.format(duration);
                        return DateUtil.millSecondToDate(timerUtil.getDuration());
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        LogUtil.e("setup timer error:" + throwable.getMessage());
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        if (getDurationTxt() != null) getDurationTxt().setText(s);
                    }
                }));
    }

    protected void createGame() {
        disposables.add(
                Observable.zip(Observable.just(gameSize), Observable.just(difficulty),
                        new BiFunction<GameSize, Difficulty, Game>() {
                            @Override
                            public Game apply(GameSize gameSize, Difficulty difficulty) throws Throwable {
                                IGameCreator creator = GameFactory.createGameData(gameSize, difficulty);
                                Game game = new Game();
                                game.setupCreator(creator);
                                MirrorManager actionManager = new MirrorManager();
                                game.setupActionManager(actionManager);
                                return game;
                            }
                        })
                        .doOnNext(new Consumer<Game>() {
                            @Override
                            public void accept(Game game) throws Throwable {
                                game.initGame();
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                onGameCreateError(throwable);
                            }
                        })
                        .subscribe(new Consumer<Game>() {
                            @Override
                            public void accept(Game game) throws Throwable {
                                onGameCreated(game);
                            }
                        })
        );
    }

    protected void onMenu(int integer) {
        try {
            if (integer == 0) {
                resetGame();
            } else if (integer == 1) {
                if (boardView.hasUndo()) {
                    boardView.Undo();
                } else {
                    alertTip(R.string.invalidUndo);
                }
            } else if (integer == 2) {
                boardView.toggleNote();
                gameMenu.updateNoteStatus(boardView.isNoteOn());
            } else if (integer == 3) {
                boardView.setValue(0);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    protected void onNumber(int integer) {
        try {
            boolean status = boardView.setValue(integer);
            if (status) {
                boolean isGameOver = boardView.isGameOver();
                if (isGameOver) {
                    onGameOver();
                }
            }
        } catch (Exception e) {
            if (e.getMessage().equals(IBoardEvent.INVALID_VALUE)) {
                alertTip(R.string.invalidValue);
            } else if (e.getMessage().equals(IBoardEvent.INVALID_CELL)) {
                alertTip(R.string.invalidCell);
            }
            e.printStackTrace();
        }
    }

    protected void resizeGameBoard() {
        gameBoard.setGameSize(gameSize.getValue());
    }

}
