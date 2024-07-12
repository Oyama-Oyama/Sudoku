package com.roman.gurdan.sudo.pro.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.switchmaterial.SwitchMaterial
import com.roman.garden.base.BaseImpl
import com.roman.garden.core.Easy
import com.roman.gurdan.sudo.pro.BuildConfig
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.base.BaseFragment
import com.roman.gurdan.sudo.pro.util.LocalStorage

class StatFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.activity_setting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwitchStatus(view)
        view.findViewById<FrameLayout>(R.id.nativeContainer)?.let {
            Easy.instance.showNative(it)
        }

    }

    private fun setupSwitchStatus(view: View) {
        view.findViewById<ViewGroup>(R.id.topBar).visibility = View.GONE
        val one = view.findViewById<SwitchMaterial>(R.id.switchOne)
        val status1 = LocalStorage.decode(LocalStorage.SETTING_HIGH_LIGHT_SAME_ROW_COLUMN, true)
        one.isChecked = status1
        one.setOnCheckedChangeListener { _, b ->
            LocalStorage.encode(LocalStorage.SETTING_HIGH_LIGHT_SAME_ROW_COLUMN, b)
        }

        val two = view.findViewById<SwitchMaterial>(R.id.switchTwo)
        val status2 = LocalStorage.decode(LocalStorage.SETTING_HIGH_LIGHT_SAME_GROUP, true)
        two.isChecked = status2
        two.setOnCheckedChangeListener { _, b ->
            LocalStorage.encode(LocalStorage.SETTING_HIGH_LIGHT_SAME_GROUP, b)
        }

        val three = view.findViewById<SwitchMaterial>(R.id.switchThree)
        val status3 = LocalStorage.decode(LocalStorage.SETTING_HIGH_LIGHT_SAME_VALUE, true)
        three.isChecked = status3
        three.setOnCheckedChangeListener { _, b ->
            LocalStorage.encode(LocalStorage.SETTING_HIGH_LIGHT_SAME_VALUE, b)
        }

        val four = view.findViewById<SwitchMaterial>(R.id.switchFour)
        val status4 = LocalStorage.decode(LocalStorage.SETTING_HIGH_LIGHT_ERROR_VALUE, true)
        four.isChecked = status4
        four.setOnCheckedChangeListener { _, b ->
            LocalStorage.encode(LocalStorage.SETTING_HIGH_LIGHT_ERROR_VALUE, b)
        }

        view.findViewById<TextView>(R.id.rate).setOnClickListener { v ->
            BaseImpl.rate(v.context, v.context.packageName)
        }
        view.findViewById<TextView>(R.id.about).setOnClickListener { v ->
            Toast.makeText(v.context, "${getString(R.string.app_name_pro)}-${BuildConfig.VERSION_NAME}", Toast.LENGTH_LONG).show();
        }

    }

}