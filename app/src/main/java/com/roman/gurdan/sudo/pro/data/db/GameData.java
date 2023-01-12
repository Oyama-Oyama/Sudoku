package com.roman.gurdan.sudo.pro.data.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.roman.gurdan.sudo.pro.data.dao.GameDao;
import com.roman.gurdan.sudo.pro.data.dao.WeekDao;
import com.roman.gurdan.sudo.pro.data.entry.Game;
import com.roman.gurdan.sudo.pro.data.entry.Weekly;

@Database(entities = {Game.class, Weekly.class}, version = 1, exportSchema = false)
public abstract class GameData extends RoomDatabase {

    private static GameData _instance = null;

    public GameData() {
    }

    public static GameData of(@NonNull Context context) {
        if (_instance == null) {
            synchronized (GameData.class) {
                if (_instance == null)
                    _instance = Room.databaseBuilder(context, GameData.class, "Sudoku")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .enableMultiInstanceInvalidation()
                            .build();
            }
        }
        return _instance;
    }

    public abstract GameDao gameDao();
    public abstract WeekDao weekDao();

}
