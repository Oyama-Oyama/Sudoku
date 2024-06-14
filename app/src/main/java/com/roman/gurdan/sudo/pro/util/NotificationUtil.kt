package com.roman.gurdan.sudo.pro.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.activity.SplashActivity

class NotificationUtil {

    companion object {

        fun sendNotification(context: Context) {
            try {
                (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.let { manager ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel(
                            "Sudoku",
                            "SignIn",
                            NotificationManager.IMPORTANCE_DEFAULT
                        ).let { channel ->
                            manager.createNotificationChannel(channel)
                        }
                    }
                    val intent = Intent(context, SplashActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("from", "notification")
                    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
                    NotificationCompat.Builder(context, "sudoku")
                        .setContentTitle(context.getString(R.string.signInNotificationTitle))
                        .setContentText(context.getString(R.string.signInNotificationContent))
                        .setSmallIcon(R.mipmap.ic_4)
                        .setLargeIcon(
                            BitmapFactory.decodeResource(
                                context.resources,
                                R.mipmap.ic_launcher
                            )
                        )
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build().let {
                            manager.notify(101, it)
                        }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

}