package viewModels

import androidx.lifecycle.ViewModel

class GameDataViewModel private constructor() : ViewModel() {

    companion object {
        val Instance: GameDataViewModel by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { GameDataViewModel() }
    }

    init {

    }


}