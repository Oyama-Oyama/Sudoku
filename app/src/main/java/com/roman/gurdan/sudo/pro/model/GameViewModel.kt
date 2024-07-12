package com.roman.gurdan.sudo.pro.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.roman.gurdan.sudo.pro.game.util.Difficulty
import com.roman.gurdan.sudo.pro.game.util.GameSize

class GameViewModel(application: Application) : AndroidViewModel(application) {

    val gameDifficulty: MutableLiveData<Difficulty> = MutableLiveData<Difficulty>()
    val gameSize: MutableLiveData<GameSize> = MutableLiveData<GameSize>()
    val error: MutableLiveData<Int> = MutableLiveData<Int>(0)

    fun updateGameDifficulty(diff: Difficulty) = gameDifficulty.postValue(diff)

    fun updateGameSize(size: Int) = gameSize.postValue(GameSize.getGameSize(size))

    fun addError(num: Int) {
        val count = error.value!!.plus(num)
        error.postValue(count)
    }

    fun setError(num: Int) = error.postValue(num)

}