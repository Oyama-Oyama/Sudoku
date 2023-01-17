package com.roman.gurdan.sudo.pro;

import android.app.Application;

import com.roman.gurdan.sudo.pro.util.DateUtil;
import com.tencent.mmkv.MMKV;

public class App extends Application {

    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        MMKV.initialize(this);
        DateUtil.resetWeekTag();

    }



}
