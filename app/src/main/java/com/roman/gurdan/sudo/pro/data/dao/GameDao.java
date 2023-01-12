package com.roman.gurdan.sudo.pro.data.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.roman.gurdan.sudo.pro.data.entry.Game;

import java.util.List;

@Dao
public interface GameDao {

    @Query("select * from game")
    List<Game> getGames();

    @Update
    void update(@NonNull Game game);

    @Delete
    void delete(@NonNull Game game);

    @Insert
    void insert(@NonNull Game game);

    @Query("select count(id) from game")
    int getCount();

    @Query("select * from game where difficulty = :difficulty")
    List<Game> getGamesByDifficulty(int difficulty);

    @Query("select * from game where gameType = :type")
    List<Game> getGamesByType(int type);

    @Query("select count(id) from game where result = :result")
    int getGamesResultCount(int result);

    @Query("select * from game group by date order by date DESC")
    List<Game> getGamesByDate();

}
