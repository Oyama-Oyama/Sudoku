package com.roman.gurdan.sudo.pro.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.roman.garden.core.Easy
import com.roman.garden.sudo.base.util.Difficulty
import com.roman.garden.sudo.base.util.GameSize
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.base.BaseActivity
import com.roman.gurdan.sudo.pro.util.Cache

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottomMenu);
        val navController = Navigation.findNavController(this, R.id.navFragment);
        NavigationUI.setupWithNavController(bottomMenu, navController);

        newPlayerRequired()

    }



    private fun newPlayerRequired() {
        val status = Cache.getCache().decodeBool("isFirstGame", true)
        Cache.getCache().encode("isFirstGame", false)
        if (status) {
            AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage(R.string.newPlayer1)
                .setCancelable(false)
                .setNegativeButton(R.string.yes) { d, _ ->
                    onPlayer(true)
                    d.dismiss()
                }
                .setPositiveButton(R.string.no) { d, _ ->
                    onPlayer(false)
                    d.dismiss()
                }.show()
        }
    }

    private fun onPlayer(status: Boolean) {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(R.string.tip)
            .setMessage(if (status) R.string.newPlayer2 else R.string.newPlayer3)
            .setNegativeButton(R.string.cancel) { d, _ ->
                d.dismiss()
            }
            .setPositiveButton(R.string.start) { d, _ ->
                Intent(this@MainActivity, GameActivity::class.java).apply {
                    if (status) {
                        putExtra("gameSize", GameSize.SIZE_FOUR.tag)
                        putExtra("gameDiff", Difficulty.EASY.value)
                    } else {
                        putExtra("gameSize", GameSize.SIZE_NINE.tag)
                        putExtra("gameDiff", Difficulty.HARD.value)
                    }
                    this@MainActivity.startActivity(this)
                }
                d.dismiss()
            }
            .show()
        Bundle().apply {
            putString("value", if (status) "newbie" else "master")
            Easy.instance.logEvent("playerLevel", this)
        }
    }


}