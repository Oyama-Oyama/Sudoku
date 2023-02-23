package com.roman.gurdan.sudo.pro.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.roman.garden.base.log.Logger
import com.roman.garden.core.Easy
import com.roman.garden.core.listener.IAdListener
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.activity.ChallengeGameActivity
import com.roman.gurdan.sudo.pro.base.BaseFragment
import com.roman.gurdan.sudo.pro.data.db.GameData
import com.roman.gurdan.sudo.pro.util.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChallengeFragment : BaseFragment() {

    private lateinit var subtitle: TextView
    private var weekDates = arrayOfNulls<TextView>(7)
    private var dates = arrayOfNulls<String>(7)
    private lateinit var start: MaterialButton
    private lateinit var adTag: ImageView
    private lateinit var selectedDate: String

    override fun getLayoutId(): Int = R.layout.fragment_challenge

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adTag = view.findViewById(R.id.adTag)
        subtitle = view.findViewById(R.id.subtitle)
        weekDates[0] = view.findViewById(R.id.sunDate)
        weekDates[1] = view.findViewById(R.id.monDate)
        weekDates[2] = view.findViewById(R.id.tuesDate)
        weekDates[3] = view.findViewById(R.id.wedDate)
        weekDates[4] = view.findViewById(R.id.thurDate)
        weekDates[5] = view.findViewById(R.id.friDate)
        weekDates[6] = view.findViewById(R.id.satDate)
        start = view.findViewById(R.id.start)
        setupStatus()
        start.setOnClickListener {
            when (adTag.visibility) {
                View.VISIBLE -> {
                    if (Easy.instance.hasRewarded()) {
                        Easy.instance.setRewardedListener(object : IAdListener {
                            override fun onClosed(rewarded: Boolean) {
                                super.onClosed(rewarded)
                                if (rewarded) {
                                    Intent(
                                        context,
                                        ChallengeGameActivity::class.java
                                    ).apply { context?.startActivity(this) }
                                }
                            }
                        })
                        Easy.instance.showRewarded()
                    } else {
                        Toast.makeText(this.context, R.string.noAd, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> Toast.makeText(this.context, R.string.noAd, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupStatus() {
        selectedDate = DateUtil.getDate()
        DateUtil.getWeek().forEachIndexed { i, it ->
            dates[i] = it.first
            weekDates[i]?.text = it.second
            weekDates[i]?.tag = i
            weekDates[i]?.isSelected = it.first == selectedDate
            weekDates[i]?.setOnClickListener { view ->
                try {
                    val tag = view.tag.toString().toInt()
                    dates[tag]?.let {
                        selectedDate = it
                        view.isSelected = true
                        MainScope().launch {
                            flowOf(it).map { d ->
                                Logger.e("flow:${Thread.currentThread().name}")
                                val count = GameData.instance.weekDao().getWeekly(d)
                                return@map count > 0
                            }
                                .flowOn(Dispatchers.IO)
                                .onStart {
                                    weekDates.forEach { t ->
                                        t?.isSelected = false
                                    }
                                }
                                .onCompletion {
                                    view.isSelected = true
                                }
                                .catch { e -> e.printStackTrace() }
                                .collect { aBoolean ->
                                    if (start != null) {
                                        start.setText(if (aBoolean) R.string.restartChange else R.string.startChallenge);
                                        start.tag = aBoolean;
                                    }
                                    if (adTag != null) adTag.visibility =
                                        if (aBoolean) View.VISIBLE else View.INVISIBLE;
                                }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        MainScope().launch {
            Logger.e("dates:${dates[0]}")
            flowOf(dates).map {
                var count = 0
                it.forEach { d ->
                    val tmp = d?.let { dd ->
                        GameData.instance.weekDao().getWeekly(dd)
                    } ?: 0
                    if (tmp <= 0) count++
                }
                return@map count
            }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { count ->
                    if (subtitle != null) subtitle.text =
                        String.format(getString(R.string.leftChange), count)
                }
        }

    }

}