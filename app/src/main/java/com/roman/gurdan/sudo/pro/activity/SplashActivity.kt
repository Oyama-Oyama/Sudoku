package com.roman.gurdan.sudo.pro.activity

import android.content.Intent
import android.os.Bundle
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.base.BaseActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class SplashActivity : BaseActivity(), CoroutineScope by MainScope() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        launch {
            flow<Boolean> {
                emit(true)
            }.onEach {
                delay(2500)
            }.collect { _ ->
                Intent(this@SplashActivity, MainActivity::class.java).apply {
                    this@SplashActivity.startActivity(this)
                }
                this@SplashActivity.finish()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }


}