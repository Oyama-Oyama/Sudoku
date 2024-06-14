package com.roman.gurdan.sudo.pro.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.roman.gurdan.sudo.pro.game.util.Difficulty
import com.roman.gurdan.sudo.pro.game.util.GameSize

class GameViewModel(application: Application) : AndroidViewModel(application) {

    val gameDifficulty: MutableLiveData<Difficulty> = MutableLiveData<Difficulty>()
    val gameSize: MutableLiveData<GameSize> = MutableLiveData<GameSize>()


    fun updateGameDifficulty(diff: Int) {
        val difficulty =
            if (diff == 0) Difficulty.randDifficulty(gameSize.value?.tag) else Difficulty.getDifficulty(
                diff
            )
        gameDifficulty.postValue(difficulty)
    }

    fun updateGameSize(size: Int) {
        gameSize.postValue(GameSize.getGameSize(size))
    }


}