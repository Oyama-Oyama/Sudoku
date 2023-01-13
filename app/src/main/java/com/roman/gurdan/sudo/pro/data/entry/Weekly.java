package com.roman.gurdan.sudo.pro.data.entry;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weekly")
public class Weekly {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;

    public int result;

    public long duration;

    @ColumnInfo(defaultValue = "0")
    public int week;

}
