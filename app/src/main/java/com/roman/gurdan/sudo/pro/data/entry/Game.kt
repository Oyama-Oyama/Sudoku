package com.roman.gurdan.sudo.pro.data.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game")
class Game {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var difficulty: Int = 0

    var gameType: Int = 0

    var date: String = ""

    @ColumnInfo(defaultValue = "0")
    var result: Int = 0

    var duration: Long = 0L

}