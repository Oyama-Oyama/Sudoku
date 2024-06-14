package com.roman.gurdan.sudo.pro.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import com.example.alarm.alarm.notification.NotificationMan
import com.example.alarm.alarm.notification.NotificationPermissionUtil
import com.roman.garden.core.Easy
import com.roman.gurdan.sudo.pro.BuildConfig
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.base.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class SplashActivity : BaseActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        NotificationPermissionUtil.enablePermission(this)
//        NotificationMan.push(this, "11", "22222", "222", 0, true, "com.roman.gurdan.sudoku.pro");
////        NotificationMan.push(this, "1", "111", "222", 3, true);
//        NotificationPermissionUtil.enablePermission(this)
        findViewById<TextView>(R.id.subtitle).text = BuildConfig.VERSION_NAME
        object : CountDownTimer(3000, 3000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                Intent(this@SplashActivity, MainActivity::class.java).apply {
                    this@SplashActivity.startActivity(this)
                }
                this@SplashActivity.finish()
            }

        }.start()

        try {
            if (intent.hasExtra("from") && intent.getStringExtra("from") == "notification") {
                Easy.instance.logEvent("openByNotification", null)
            }
        } catch (_:Exception){}
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }


}