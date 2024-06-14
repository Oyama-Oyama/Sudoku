package com.example.alarm.alarm.notification;

import android.content.Context;

import androidx.work.BackoffPolicy;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class NotificationMan {

    /**
     * @param tag        任务标志，可根据此标志关闭任务
     * @param title      通知栏标题
     * @param subtitle   通知栏副标题
     * @param delay      延迟时间，单位分钟
     * @param autoCancel 是否自动关闭
     */
    public static void push(Context context, String tag, String title, String subtitle, long delay, boolean autoCancel, String action) {
        Data data = new Data.Builder()
                .putString("title", title)
                .putString("subtitle", subtitle)
                .putString("action", action)
                .putBoolean("autoClose", autoCancel)
                .build();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationTask.class)
                .setInputData(data)
                .setInitialDelay(delay, TimeUnit.MINUTES)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.MINUTES)//任务重试间隔
                .addTag(tag)
                .build();
        WorkManager.getInstance(context).enqueue(oneTimeWorkRequest);
    }

    /**
     * 关闭对应tag的任务，无法确保一定会关闭
     *
     * @param context
     * @param tag
     */
    public static void cancelTask(Context context, String tag) {
        WorkManager.getInstance(context).cancelAllWorkByTag(tag);
    }

    /**
     * 关闭所有任务
     *
     * @param context
     */
    public static void cancelAll(Context context) {
        WorkManager.getInstance(context).cancelAllWork();
    }


}
