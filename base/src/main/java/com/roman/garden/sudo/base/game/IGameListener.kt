package com.roman.garden.sudo.base.game

interface IGameListener {


    fun onGameRefresh()

    fun onGameUnd(row: Int, col: Int)

}