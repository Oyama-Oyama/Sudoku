package com.roman.gurdan.sudo.pro.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.roman.garden.core.Easy
import com.roman.garden.core.google.IGoogleSignListener
import com.roman.garden.core.listener.IAdListener
import com.roman.gurdan.sudo.pro.game.util.Difficulty
import com.roman.gurdan.sudo.pro.game.util.GameSize
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.base.BaseActivity
import com.roman.gurdan.sudo.pro.dialog.*
import com.roman.gurdan.sudo.pro.util.LocalStorage
import com.roman.gurdan.sudo.pro.util.ClockInUtil
import com.roman.gurdan.sudo.pro.util.NotificationPermissionUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

class MainActivity : BaseActivity() {

    override fun onBackPressed() {
        TAlertDialog(this).let { dialog ->
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setTitleId(R.string.giveUp)
            dialog.setContentId(R.string.exit)
            dialog.setInActiveId(R.string.yes, object : IEvent {
                override fun onEvent(dialog: BaseDialog) {
                    dialog.dismiss()
                    exitProcess(0)
                }
            })
            dialog.setActiveId(R.string.noExit, object : IEvent {
                override fun onEvent(dialog: BaseDialog) {
                    dialog.dismiss()
                }
            })
            dialog.show()
        }
//        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottomMenu)
        val navController = Navigation.findNavController(this, R.id.navFragment)
        NavigationUI.setupWithNavController(bottomMenu, navController)
        newPlayerRequired()
        if (!Easy.instance.isSignIn(this.applicationContext)) {
            Easy.instance.signInSilently(this, object :IGoogleSignListener{
                override fun onSignInFail(e: Exception?) {
                    Easy.instance.logEvent("googleLoginFail", null)
                }

                override fun onSignInSuccess(
                    id: String?,
                    displayName: String?,
                    email: String?,
                    avatar: Uri?
                ) {
                    Easy.instance.logEvent("googleLoginSuccess", null)
                }
            })
        }
    }

    private fun onClockIn() {
        ClockInUtil.clockIn(onSuccess = { count ->
            WinDialog(this, R.layout.dialog_win).apply {
                this.setTitleId(R.string.signIn)
                this.setContentId(R.string.signInDetail)

                this.setStarCount(count)
                LocalStorage.addStar(count)
                this.setActiveId(R.string.doubleAd, object : IEvent {
                    override fun onEvent(dialog: BaseDialog) {
                        if (Easy.instance.hasRewarded()) {
                            Easy.instance.setRewardedListener(object : IAdListener {
                                override fun onClosed(rewarded: Boolean) {
                                    super.onClosed(rewarded)
                                    LocalStorage.addStar(count)
                                    dialog.dismiss()
                                }
                            })
                            Easy.instance.showRewarded()
                        } else {
                            Toast.makeText(this@MainActivity, R.string.noAd, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })
                this.setInActiveId(R.string.yes, object : IEvent {
                    override fun onEvent(dialog: BaseDialog) {
                        dialog.dismiss()
                    }
                })
                this.show()
            }
            Easy.instance.logEvent("signIn", null)
        }) {

        }
        if (!NotificationPermissionUtil.isNotifyEnabled(applicationContext)) {
            TAlertDialog(this).apply {
                this.setTitleId(R.string.permission)
                this.setContentId(R.string.permissionRequire)
                this.setActiveId(R.string.ok, object : IEvent {
                    override fun onEvent(dialog: BaseDialog) {
                        NotificationPermissionUtil.requestNotificationPermission(this@MainActivity)
                        dialog.dismiss()
                    }
                })
                this.setInActiveId(R.string.no, object : IEvent {
                    override fun onEvent(dialog: BaseDialog) {
                        dialog.dismiss()
                    }
                })
                this.show()
            }
        }
    }

    private fun newPlayerRequired() {
        val status = LocalStorage.decode("isFirstGame", true)
        LocalStorage.encode("isFirstGame", false)
        if (status) {
            TAlertDialog(this).apply {
                this.setTitleId(R.string.guide)
                this.setContentId(R.string.newPlayer1)
                this.setCancelable(false)
                this.setCanceledOnTouchOutside(false)
                this.setActiveId(R.string.yes, object : IEvent {
                    override fun onEvent(dialog: BaseDialog) {
                        dialog.dismiss()
                        onPlayer(true)
                    }
                })
                this.setInActiveId(R.string.no, object : IEvent {
                    override fun onEvent(dialog: BaseDialog) {
                        dialog.dismiss()
                        onPlayer(false)
                    }
                })
                this.show()
            }
        } else {
            onClockIn()
        }
    }

    private fun onPlayer(status: Boolean) {
        TAlertDialog(this).apply {
            this.setTitleId(R.string.guide)
            this.setContentId(
                when (status) {
                    true -> R.string.newPlayer2
                    else -> R.string.newPlayer3
                }
            )
            this.setCancelable(false)
            this.setCanceledOnTouchOutside(false)
            this.setActiveId(R.string.yes, object : IEvent {
                override fun onEvent(dialog: BaseDialog) {
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
                }
            })
            this.setInActiveId(R.string.no, object : IEvent {
                override fun onEvent(dialog: BaseDialog) {
                    dialog.dismiss()
                    onClockIn()
                }
            })
            this.show()
        }
        Bundle().apply {
            putString("value", if (status) "newbie" else "master")
            Easy.instance.logEvent("playerLevel", this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            // Easy.instance.showInterstitial()
            LoadingDialog(this@MainActivity).let { dialog ->
                Flowable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        dialog.show()
                    }
                    .subscribe {
                        dialog.dismiss()
                        Easy.instance.showInterstitial()
                    }
            }
        }
    }


}