package com.roman.gurdan.sudo.pro.util

import com.roman.gurdan.sudo.pro.R
import kotlin.random.Random

class Utills {

    companion object {

        fun getTitleImage(): Int {
            val random = Random(System.currentTimeMillis()).nextInt() % 9
            return when (random) {
                0 -> R.mipmap.ic_num_1
                1 -> R.mipmap.ic_num_2
                2 -> R.mipmap.ic_num_3
                3 -> R.mipmap.ic_num_4
                4 -> R.mipmap.ic_num_5
                5 -> R.mipmap.ic_num_6
                6 -> R.mipmap.ic_num_7
                7 -> R.mipmap.ic_num_8
                8 -> R.mipmap.ic_num_9
                else -> R.mipmap.ic_num_9
            }
        }

    }

}