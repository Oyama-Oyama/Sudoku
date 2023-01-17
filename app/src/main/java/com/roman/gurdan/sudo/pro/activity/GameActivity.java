package com.roman.gurdan.sudo.pro.activity;

import android.view.View;
import android.widget.TextView;

import com.roman.gurdan.sudo.game.Game;
import com.roman.gurdan.sudo.pro.R;
import com.roman.gurdan.sudo.pro.base.BaseGameActivity;
import com.roman.gurdan.sudo.pro.view.IGameMenuListener;

public class GameActivity extends BaseGameActivity {

    private TextView durationTxt;

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
