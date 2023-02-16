package com.roman.gurdan.sudo.pro.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.roman.gurdan.sudo.pro.App
import com.roman.gurdan.sudo.pro.data.dao.GameDao
import com.roman.gurdan.sudo.pro.data.dao.WeekDao
import com.roman.gurdan.sudo.pro.data.entry.Game
import com.roman.gurdan.sudo.pro.data.entry.Weekly

@Database(entities = [Game::class, Weekly::class], version = 1, exportSchema = false)
abstract class GameData : RoomDatabase() {

    abstract fun gameDao(): GameDao

    abstract fun weekDao(): WeekDao

    companion object {
        val instance: GameData by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(App.instance, GameData::class.java, "Sudoku")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .enableMultiInstanceInvalidation()
                .build();
        }
    }


}