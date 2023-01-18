package com.roman.gurdan.sudo.pro;

import android.app.Application;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.roman.garden.core.Easy;
import com.roman.gurdan.sudo.pro.util.DateUtil;
import com.tencent.mmkv.MMKV;

public class App extends Application implements LifecycleObserver {

    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        MMKV.initialize(this);
        DateUtil.resetWeekTag();
        Easy.Companion.init(this);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Easy.Companion.showAppOpenAd();
    }

}
