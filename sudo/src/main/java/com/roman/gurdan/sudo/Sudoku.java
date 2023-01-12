package com.roman.gurdan.sudo;

import com.roman.gurdan.sudo.game.Difficulty;
import com.roman.gurdan.sudo.game.Game;
import com.roman.gurdan.sudo.game.GameSize;
import com.roman.gurdan.sudo.game.action.MirrorManager;
import com.roman.gurdan.sudo.game.factory.GameFactory;
import com.roman.gurdan.sudo.game.factory.IGameCreator;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Sudoku {

    public interface ICreateGameListener {
        void onError(Throwable throwable);

        void onCreated(Game game);
    }

    /**
     * 创建新游戏
     *
     * @param size 游戏长度  4*4 6*6 8*8 9*9
     * @return
     */
    public static void createGame(GameSize size, ICreateGameListener listener) {
        createGame(size, Difficulty.RANDOM, listener);
    }

    public static void createGame(GameSize size, Difficulty difficulty, ICreateGameListener listener) {
        if (difficulty == Difficulty.RANDOM)
            difficulty = Difficulty.randDifficulty();
        realCreateGame(size, difficulty, listener);
    }

    private static void realCreateGame(GameSize size, Difficulty difficulty, ICreateGameListener listener) {
        Observable.zip(Observable.just(size), Observable.just(difficulty), new BiFunction<GameSize, Difficulty, Game>() {
            @Override
            public Game apply(GameSize gameSize, Difficulty difficulty) throws Throwable {
                IGameCreator creator = GameFactory.createGameData(gameSize, difficulty);
                Game game = new Game();
                game.setupCreator(creator);
                MirrorManager actionManager = new MirrorManager();
                game.setupActionManager(actionManager);
                return game;
            }
        }).doOnNext(new Consumer<Game>() {
            @Override
            public void accept(Game game) throws Throwable {
                game.initGame();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        if (listener != null) listener.onError(throwable);
                    }
                })
                .subscribe(new Consumer<Game>() {
                    @Override
                    public void accept(Game game) throws Throwable {
                        if (listener != null) listener.onCreated(game);
                    }
                });
    }


}
