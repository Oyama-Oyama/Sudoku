package com.roman.gurdan.sudo.pro.data.dao

import androidx.annotation.NonNull
import androidx.room.*
import com.roman.gurdan.sudo.pro.data.entry.Game

@Dao
interface GameDao {

    @Query("select * from game")
    fun getGames(): List<Game>

    @Update
    fun update(@NonNull game: Game)

    @Delete
    fun delete(@NonNull game: Game)

    @Insert
    fun insert(@NonNull game: Game)

    @Query("select count(id) from game")
    fun getCount(): Int

    @Query("select * from game where difficulty = :difficulty")
    fun getGamesByDifficulty(difficulty: Int): List<Game>

    @Query("select * from game where gameType = :type")
    fun getGamesByType(type: Int): List<Game>

    @Query("select count(id) from game where result = :result")
    fun getGamesResultCount(result: Int): Int

    //近30天
    @Query("select * from game where DATE('yyyy-MM-dd', '-30 day') <= date group by date order by date DESC")
    fun getGamesByDate(): List<Game>

    @Query("select count(id) from game where gameType = :gameType and date = :date")
    fun getGames(gameType: Int, date: String): Int

    @Query("select count(id) from game where gameType = :gameType and date = :date and result = :result")
    fun getGames(gameType: Int, date: String, result: Int): Int

}