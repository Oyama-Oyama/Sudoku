package com.example.alarm.alarm.notification;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationTask extends Worker {


    public NotificationTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        NotificationUtil.createNotification(getApplicationContext(),
                data.getString("title"),
                data.getString("subtitle"),
                data.getString("action"),
                data.getBoolean("autoClose", true));
        return Result.success();
    }


}
