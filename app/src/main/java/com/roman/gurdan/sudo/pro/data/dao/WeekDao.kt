package com.roman.gurdan.sudo.pro.data.dao


import androidx.annotation.NonNull
import androidx.room.*
import com.roman.gurdan.sudo.pro.data.entry.Weekly


@Dao
interface WeekDao {

    @Query("select * from weekly")
    fun getWeeks(): List<Weekly>

    @Query("select count(*) from weekly where date = :date")
    fun getWeekly(date: String): Int

    @Update
    fun update(@NonNull item: Weekly)

    @Delete
    fun delete(@NonNull item: Weekly)

    @Insert
    fun insert(@NonNull item: Weekly)

    @Query("select count(id) from weekly where result = :result")
    fun getWeeksResult(result: Int): Int

    //近30天
    @Query("select count(id) from weekly where DATE('YYYY-MM-DD', '-15 day') <= :date and result = :result")
    fun getWeekly(date: String, result: Int): Int


}