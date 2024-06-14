package com.example.alarm.alarm.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

public class NotificationUtil {

    private static final String CHANNEL_ID = "test";
    private static final String CHANNEL_NAME = "test";

    public static void createNotification(Context context, String title, String subtitle, String action, boolean autoClose) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
//        Intent intent = new Intent(action).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setPackage(context.getPackageName());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE);
//        PendingIntent pendingIntent = null;
//        try {
//        String packageName = context.getPackageName();
//        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
//        String className = launchIntent.getComponent().getClassName();
//
//        Intent notificationIntent = new Intent(context, Class.forName(className));
//        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        notificationIntent.setAction(Intent.ACTION_MAIN);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//    } catch (Exception e){
//
//    }

        subtitle= title + "\ud83d\ude03\ud83d\ude02";
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(subtitle)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.img_info)
                .setAutoCancel(autoClose)
                .setContentIntent(pendingIntent)
                .build();
        manager.notify(1, notification);
    }

    public static boolean isChanelClosed(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = manager.getNotificationChannel(CHANNEL_ID);
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                return true;
            }
        }
        return false;
    }

    public static void openChannel(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID);
            context.startActivity(intent);
        } catch (Exception e) {
//            Log.e("")
        }
    }

}
