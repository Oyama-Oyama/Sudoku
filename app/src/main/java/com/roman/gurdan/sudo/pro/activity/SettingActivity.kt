package com.roman.gurdan.sudo.pro.activity

import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.roman.garden.core.Easy
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.base.BaseActivity
import com.roman.gurdan.sudo.pro.util.Cache

class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setupSwitchStatus()
        Easy.instance.showInterstitial()
    }

    private fun setupSwitchStatus() {
        findViewById<ImageView>(R.id.back).setOnClickListener { finish() }
        val one = findViewById<SwitchMaterial>(R.id.switchOne)
        val status1 = Cache.getCache().decodeBool(Cache.SETTING_HIGH_LIGHT_SAME_ROW_COLUMN, true)
        one.isChecked = status1
        one.setOnCheckedChangeListener { _, b ->
            Cache.getCache().encode(Cache.SETTING_HIGH_LIGHT_SAME_ROW_COLUMN, b)
        }

        val two = findViewById<SwitchMaterial>(R.id.switchTwo)
        val status2 = Cache.getCache().decodeBool(Cache.SETTING_HIGH_LIGHT_SAME_GROUP, true)
        two.isChecked = status2
        two.setOnCheckedChangeListener { _, b ->
            Cache.getCache().encode(Cache.SETTING_HIGH_LIGHT_SAME_GROUP, b)
        }

        val three = findViewById<SwitchMaterial>(R.id.switchThree)
        val status3 = Cache.getCache().decodeBool(Cache.SETTING_HIGH_LIGHT_SAME_VALUE, true)
        three.isChecked = status3
        three.setOnCheckedChangeListener { _, b ->
            Cache.getCache().encode(Cache.SETTING_HIGH_LIGHT_SAME_VALUE, b);
        }

        val four = findViewById<SwitchMaterial>(R.id.switchFour)
        val status4 = Cache.getCache().decodeBool(Cache.SETTING_HIGH_LIGHT_ERROR_VALUE, true)
        four.isChecked = status4
        four.setOnCheckedChangeListener { _, b ->
            Cache.getCache().encode(Cache.SETTING_HIGH_LIGHT_ERROR_VALUE, b)
        }

    }

}