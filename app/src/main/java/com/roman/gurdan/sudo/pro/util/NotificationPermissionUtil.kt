package com.roman.gurdan.sudo.pro.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class NotificationPermissionUtil {

    companion object {

        fun isNotifyEnabled(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                return ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                return NotificationManagerCompat.from(context).areNotificationsEnabled()
            }
        }

        fun requestNotificationPermission(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    val editor = activity.getSharedPreferences("permission_notification", Context.MODE_PRIVATE)
                    val hasRequested = editor.getBoolean("has_requested", false);
                    if (hasRequested && !ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    ) {
                        enableNotification(activity)
                    } else {
                        editor.edit().putBoolean("has_requested", true).apply()
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                            100
                        )
                    }
                }
            } else {
                val enabled = NotificationManagerCompat.from(activity).areNotificationsEnabled()
                if (!enabled) {
                    enableNotification(activity)
                }
            }
        }

        fun enableNotification(context: Context) {
            try {
                Intent().let {
                    it.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    it.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName())
                    it.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid)
                    it.putExtra("app_package", context.getPackageName())
                    it.putExtra("app_uid", context.getApplicationInfo().uid)
                    context.startActivity(it)
                }
            } catch (_: Exception) {
                Intent().let {
                    it.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    it.data = Uri.fromParts("package", context.packageName, null)
                    context.startActivity(it)
                }
            }
        }

    }
}