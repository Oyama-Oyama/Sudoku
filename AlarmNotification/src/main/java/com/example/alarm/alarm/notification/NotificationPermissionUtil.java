package com.example.alarm.alarm.notification;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class NotificationPermissionUtil {

    public static boolean isPermissionEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED;
        } else {
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        }
    }

    public static void enablePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_DENIED
            ) {
                SharedPreferences editor = activity.getSharedPreferences("permission_notification", Context.MODE_PRIVATE);
                boolean hasRequested = editor.getBoolean("has_Requested_Notification", false);
                if (hasRequested && !ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.POST_NOTIFICATIONS
                )
                ) {
                    openPermissionSetting(activity);
                } else {
                    editor.edit().putBoolean("has_Requested_Notification", true).commit();
                    ActivityCompat.requestPermissions(
                            activity,
                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            100
                    );
                }
            }
        } else {
            boolean enabled = NotificationManagerCompat.from(activity).areNotificationsEnabled();
            if (!enabled) {
                openPermissionSetting(activity);
            }
        }
    }

    private static void openPermissionSetting(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            // intent.putExtra(Settings.EXTRA_CHANNEL_ID, "channel_id");
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
            context.startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
            context.startActivity(intent);
        }
    }

}
