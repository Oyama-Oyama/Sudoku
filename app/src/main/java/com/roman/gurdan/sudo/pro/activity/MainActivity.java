package com.roman.gurdan.sudo.pro.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.roman.gurdan.sudo.Sudoku;
import com.roman.gurdan.sudo.game.Difficulty;
import com.roman.gurdan.sudo.game.Game;
import com.roman.gurdan.sudo.game.GameSize;
import com.roman.gurdan.sudo.pro.R;
import com.roman.gurdan.sudo.pro.base.BaseActivity;
import com.roman.gurdan.sudo.view.BoardView;

public class MainActivity extends BaseActivity {

//    private BoardView boardView;
//
//    private boolean startCreate = true;
//    private Handler handler = new Handler();
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            Sudoku.createGame(GameSize.SIZE_NINE, Difficulty.HARD, new Sudoku.ICreateGameListener() {
//                @Override
//                public void onError(Throwable throwable) {
//
//                }
//
//                @Override
//                public void onCreated(Game game) {
//                    game.printGame();
////                    game.destroy();
////                    game = null;
////                    if (startCreate)
////                        handler.post(runnable);
//
//                    boardView.setupGame(game);
//
//                }
//            });
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomMenu = findViewById(R.id.bottomMenu);
        NavController navController = Navigation.findNavController(this, R.id.navFragment);
        NavigationUI.setupWithNavController(bottomMenu, navController);

    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        startCreate = true;
//        handler.post(runnable);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        startCreate = false;
//        handler.removeCallbacks(runnable);
//    }


}