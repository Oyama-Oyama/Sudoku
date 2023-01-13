package com.roman.gurdan.sudo.pro.data.entry;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "game")
public class Game {

    @PrimaryKey(autoGenerate = true)
    public int id;

    //难度
    public int difficulty;

    //类型
    public int gameType;

    public String date;

    @ColumnInfo(defaultValue = "0")
    public int result;

    public long duration;


}
