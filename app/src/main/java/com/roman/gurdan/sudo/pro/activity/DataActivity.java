package com.roman.gurdan.sudo.pro.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.tabs.TabLayout;
import com.roman.gurdan.sudo.pro.R;
import com.roman.gurdan.sudo.pro.base.BaseActivity;
import com.roman.gurdan.sudo.util.LogUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class DataActivity extends BaseActivity {

    private NavController navController;
    private TabLayout pagerStrip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        pagerStrip = findViewById(R.id.pagerStrip);
        navController = Navigation.findNavController(this, R.id.viewPager);

        pagerStrip.addTab(pagerStrip.newTab().setText(R.string.titleWeekly), true);
        pagerStrip.addTab(pagerStrip.newTab().setText(R.string.title4x4));
        pagerStrip.addTab(pagerStrip.newTab().setText(R.string.title6x6));
        pagerStrip.addTab(pagerStrip.newTab().setText(R.string.title8x8));
        pagerStrip.addTab(pagerStrip.newTab().setText(R.string.title9x9));
        pagerStrip.addOnTabSelectedListener(listener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pagerStrip != null)
            pagerStrip.removeOnTabSelectedListener(listener);
    }


    private TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Observable.just(tab.getPosition())
                    .debounce(200, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Throwable {
                            LogUtil.e("data page select error:" + throwable.getMessage());
                        }
                    })
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Throwable {
                            NavOptions navOptions = new NavOptions.Builder()
                                    .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim) //进入动画
                                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)    //退出动画
                                    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)    //弹出进入动画
                                    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)  //弹出退出动画
                                    .build();
                            try {
                                if (navController != null) {
                                    if (integer == 0) {
                                        navController.navigate(R.id.weekly, null, navOptions);
                                    } else if (integer == 1) {
                                        navController.navigate(R.id.title4x4, null, navOptions);
                                    } else if (integer == 2) {
                                        navController.navigate(R.id.title6x6, null, navOptions);
                                    } else if (integer == 3) {
                                        navController.navigate(R.id.title8x8, null, navOptions);
                                    } else if (integer == 4) {
                                        navController.navigate(R.id.title9x9, null, navOptions);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

}
