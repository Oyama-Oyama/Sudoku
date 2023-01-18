package com.roman.gurdan.sudo.pro.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.roman.gurdan.sudo.game.Difficulty;
import com.roman.gurdan.sudo.game.Game;
import com.roman.gurdan.sudo.game.GameSize;
import com.roman.gurdan.sudo.pro.App;
import com.roman.gurdan.sudo.pro.R;
import com.roman.gurdan.sudo.pro.base.BaseGameActivity;
import com.roman.gurdan.sudo.pro.data.db.GameData;
import com.roman.gurdan.sudo.pro.data.entry.Weekly;
import com.roman.gurdan.sudo.pro.util.DateUtil;
import com.roman.gurdan.sudo.pro.view.IGameMenuListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChallengeGameActivity extends BaseGameActivity {

    private int challengeStep = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game;
    }

    @Override
    protected void bindViews() {
        durationTxt = findViewById(R.id.duration);
        gameBoard = findViewById(R.id.gameBoard);
        gameMenu = findViewById(R.id.gameMenu);
        boardView = findViewById(R.id.boardView);

        gameBoard.setListener(gameBoardListener);
        gameMenu.setListener(gameMenuListener);

        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingPage();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giveUp();
            }
        });
    }

    @Override
    protected TextView getDurationTxt() {
        return durationTxt;
    }

    @Override
    protected void getIntentMessage(Intent intent) {
        //super.getIntentMessage(intent);
        if (challengeStep == 0) {
            gameSize = GameSize.SIZE_FOUR;
        } else if (challengeStep == 1) {
            gameSize = GameSize.SIZE_SIX;
        } else if (challengeStep == 2) {
            gameSize = GameSize.SIZE_EIGHT;
        } else if (challengeStep == 3) {
            gameSize = GameSize.SIZE_NINE;
        }
        difficulty = Difficulty.randDifficulty();
    }

    @Override
    protected void onGameCreated(Game game) {
        boardView.post(new Runnable() {
            @Override
            public void run() {
                boardView.setupGame(game);
            }
        });
    }

    @Override
    protected void onGameOver() {
        if (challengeStep < 3) {
            boardView.destroy();
            challengeStep++;
            getIntentMessage(null);
            this.createGame();
            this.resizeGameBoard();
        } else {
            super.onGameOver();
        }
    }

    @Override
    protected void addRecord() {
        try {
            Weekly weekly = new Weekly();
            weekly.result = challengeStep >= 4 ? 1 : 0;
            weekly.duration = timerUtil.getDuration();
            weekly.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            weekly.week = DateUtil.getWeeklyTag();
            GameData.of(App.instance).weekDao().insert(weekly);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private IGameMenuListener<Integer> gameMenuListener = new IGameMenuListener<Integer>() {
        @Override
        public void onMenuItem(Integer integer) {
            onMenu(integer);
        }
    };

    private IGameMenuListener<Integer> gameBoardListener = new IGameMenuListener<Integer>() {
        @Override
        public void onMenuItem(Integer integer) {
            onNumber(integer);
        }
    };


}
