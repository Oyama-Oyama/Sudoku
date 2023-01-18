package com.roman.gurdan.sudo.pro.activity;

import android.view.View;
import android.widget.TextView;

import com.roman.gurdan.sudo.game.Game;
import com.roman.gurdan.sudo.pro.App;
import com.roman.gurdan.sudo.pro.R;
import com.roman.gurdan.sudo.pro.base.BaseGameActivity;
import com.roman.gurdan.sudo.pro.data.db.GameData;
import com.roman.gurdan.sudo.pro.view.IGameMenuListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameActivity extends BaseGameActivity {


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
    protected void onGameCreated(Game game) {
        boardView.post(new Runnable() {
            @Override
            public void run() {
                boardView.setupGame(game);
            }
        });
    }

    @Override
    protected void addRecord() {
        try {
            boolean status = isGameWin();
            long duration = timerUtil.getDuration();
            com.roman.gurdan.sudo.pro.data.entry.Game game = new com.roman.gurdan.sudo.pro.data.entry.Game();
            game.result = status ? 1 : 0;
            game.duration = duration;
            game.gameType = boardView.getGameSize().getValue();
            game.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            game.difficulty = boardView.getGameDifficulty()._difficulty;
            GameData.of(App.instance)
                    .gameDao()
                    .insert(game);
        } catch (Exception e) {
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