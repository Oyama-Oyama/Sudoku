package com.roman.gurdan.sudo.pro.game.game

interface IGameListener {


    fun onGameRefresh()

    fun onGameUnd(row: Int, col: Int)

}