package com.roman.gurdan.sudo.pro.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.roman.garden.base.util.GpUtil
import com.roman.garden.sudo.base.util.Difficulty
import com.roman.garden.sudo.base.util.GameSize
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.activity.GameActivity
import com.roman.gurdan.sudo.pro.base.BaseFragment

class HomeFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.findViewById<CardView>(R.id.beginner)
//            .setOnClickListener { startGame(GameSize.SIZE_FOUR, Difficulty.RANDOM) }
        view.findViewById<CardView>(R.id.easy)
            .setOnClickListener { startGame(GameSize.SIZE_FOUR, Difficulty.RANDOM) }
        view.findViewById<CardView>(R.id.medium)
            .setOnClickListener { startGame(GameSize.SIZE_SIX, Difficulty.RANDOM) }
        view.findViewById<CardView>(R.id.hard)
            .setOnClickListener { startGame(GameSize.SIZE_EIGHT, Difficulty.RANDOM) }
        view.findViewById<CardView>(R.id.expert)
            .setOnClickListener { startGame(GameSize.SIZE_NINE, Difficulty.RANDOM) }
        view.findViewById<MaterialButton>(R.id.rateUs).setOnClickListener {
            GpUtil().openPlayStore(view.context, view.context.packageName)
        }
    }

    private fun startGame(size: GameSize, difficulty: Difficulty) {
        Intent(context, GameActivity::class.java).apply {
            putExtra("gameSize", size.value);
            putExtra("gameDiff", difficulty.value);
            context?.startActivity(this)
        }
    }

}