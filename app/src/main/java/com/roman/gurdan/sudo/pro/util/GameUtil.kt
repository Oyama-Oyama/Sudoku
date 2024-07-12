package com.roman.gurdan.sudo.pro.util


import com.roman.gurdan.sudo.pro.game.util.Difficulty
import com.roman.gurdan.sudo.pro.game.util.GameSize
import com.roman.gurdan.sudo.pro.R

class GameTask {

    lateinit var gameSize: GameSize
    lateinit var difficulty: Difficulty
    var titleId: Int = 0
    var detailId: Int = 0
    var star: Int = 0
    var tag:Int = -1
    var type:Int = 0
}

class GameUtil {

    companion object {



        fun getAllGames(): MutableList<GameTask> {
            val impls: MutableList<GameTask> = mutableListOf()
            impls.add(
                GameTask().apply {
                    this.gameSize = GameSize.SIZE_FOUR
                    this.difficulty = Difficulty.RANDOM
                    this.titleId = R.string.easy
                    this.detailId = R.string.detail1
                    this.star = 1
                    this.tag = -1
                }
            )
            impls.add(
                GameTask().apply {
                    this.gameSize = GameSize.SIZE_SIX
                    this.difficulty = Difficulty.RANDOM
                    this.titleId = R.string.medium
                    this.detailId = R.string.detail2
                    this.star = 2
                    this.tag = -1
                }
            )
            impls.add(
                GameTask().apply {
                    this.gameSize = GameSize.SIZE_EIGHT
                    this.difficulty = Difficulty.RANDOM
                    this.titleId = R.string.hard
                    this.detailId = R.string.detail3
                    this.star = 3
                    this.tag = -1
                }
            )
            impls.add(
                GameTask().apply {
                    this.gameSize = GameSize.SIZE_NINE
                    this.difficulty = Difficulty.RANDOM
                    this.titleId = R.string.expert
                    this.detailId = R.string.detail4
                    this.star = 4
                    this.tag = 1
                }
            )
            impls.add(
                GameTask().apply {
                    this.gameSize = GameSize.SIZE_FLOWER
                    this.difficulty = Difficulty.RANDOM
                    this.type = 1
                }
            )
            impls.add(
                GameTask().apply {
                    this.gameSize = GameSize.SIZE_FLOWER
                    this.difficulty = Difficulty.RANDOM
                    this.titleId = R.string.flower
                    this.detailId = R.string.detail5
                    this.star = 5
                    this.tag = 2
                }
            )
            impls.add(
                GameTask().apply {
                    this.gameSize = GameSize.SIZE_WINDMILL
                    this.difficulty = Difficulty.RANDOM
                    this.titleId = R.string.windmill
                    this.detailId = R.string.detail6
                    this.star = 5
                    this.tag = 2
                }
            )
            impls.add(
                GameTask().apply {
                    this.gameSize = GameSize.SIZE_BUTTERFLY
                    this.difficulty = Difficulty.RANDOM
                    this.titleId = R.string.butterfly
                    this.detailId = R.string.detail7
                    this.star = 5
                    this.tag = 2
                }
            )
            impls.add(
                GameTask().apply {
                    this.gameSize = GameSize.SIZE_STAIR
                    this.difficulty = Difficulty.RANDOM
                    this.titleId = R.string.stair
                    this.detailId = R.string.detail8
                    this.star = 5
                    this.tag = 2
                }
            )
            impls.add(
                GameTask().apply {
                    this.gameSize = GameSize.SIZE_CROSS
                    this.difficulty = Difficulty.RANDOM
                    this.titleId = R.string.cross
                    this.detailId = R.string.detail9
                    this.star = 5
                    this.tag = 2
                }
            )
            impls.add(
                GameTask().apply {
                    this.gameSize = GameSize.SIZE_TRIPLE
                    this.difficulty = Difficulty.RANDOM
                    this.titleId = R.string.triple
                    this.detailId = R.string.detail10
                    this.star = 5
                    this.tag = 2
                }
            )
            return impls
        }
    }


}
