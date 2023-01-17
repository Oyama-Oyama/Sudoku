package com.roman.gurdan.sudo.pro.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.roman.gurdan.sudo.pro.R;
import com.roman.gurdan.sudo.pro.base.BaseActivity;
import com.roman.gurdan.sudo.pro.util.CacheId;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setupSwitchStatus();
    }

    private void setupSwitchStatus() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SwitchMaterial one = findViewById(R.id.switchOne);
        boolean status1 = CacheId.getCache().decodeBool(CacheId.SETTING_HIGH_LIGHT_SAME_ROW_COLUMN, true);
        one.setChecked(status1);
        one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CacheId.getCache().encode(CacheId.SETTING_HIGH_LIGHT_SAME_ROW_COLUMN, b);
            }
        });


        SwitchMaterial two = findViewById(R.id.switchTwo);
        boolean status2 = CacheId.getCache().decodeBool(CacheId.SETTING_HIGH_LIGHT_SAME_GROUP, true);
        two.setChecked(status2);
        two.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CacheId.getCache().encode(CacheId.SETTING_HIGH_LIGHT_SAME_GROUP, b);
            }
        });

        SwitchMaterial three = findViewById(R.id.switchThree);
        boolean status3 = CacheId.getCache().decodeBool(CacheId.SETTING_HIGH_LIGHT_SAME_VALUE, true);
        three.setChecked(status3);
        three.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CacheId.getCache().encode(CacheId.SETTING_HIGH_LIGHT_SAME_VALUE, b);
            }
        });

        SwitchMaterial four = findViewById(R.id.switchFour);
        boolean status4 = CacheId.getCache().decodeBool(CacheId.SETTING_HIGH_LIGHT_ERROR_VALUE, true);
        four.setChecked(status4);
        four.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CacheId.getCache().encode(CacheId.SETTING_HIGH_LIGHT_ERROR_VALUE, b);
            }
        });
    }


}
