package com.roman.gurdan.sudo.pro.data.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.roman.gurdan.sudo.pro.data.entry.Game;
import com.roman.gurdan.sudo.pro.data.entry.Weekly;

import java.util.List;

@Dao
public interface WeekDao {

    @Query("select * from weekly")
    List<Weekly> getWeeks();

    @Query("select count(*) from weekly where date = :date")
    int getWeekly(String date);

    @Update
    void update(@NonNull Weekly item);

    @Delete
    void delete(@NonNull Weekly item);

    @Insert
    void insert(@NonNull Weekly item);

    @Query("select count(id) from weekly where result = :result")
    int getWeeksResult(int result);

    //近30天
    @Query("select count(id) from weekly where DATE('YYYY-MM-DD', '-15 day') <= :date and result = :result")
    int getWeekly(String date, int result);




}
