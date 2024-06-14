package com.roman.gurdan.sudo.pro.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.roman.garden.core.Easy
import com.roman.garden.core.listener.IAdListener
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.activity.ChallengeGameActivity
import com.roman.gurdan.sudo.pro.base.BaseFragment
import com.roman.gurdan.sudo.pro.data.db.GameData
import com.roman.gurdan.sudo.pro.dialog.BaseDialog
import com.roman.gurdan.sudo.pro.dialog.IEvent
import com.roman.gurdan.sudo.pro.dialog.RateDialog
import com.roman.gurdan.sudo.pro.dialog.TAlertDialog
import com.roman.gurdan.sudo.pro.util.LocalStorage
import com.roman.gurdan.sudo.pro.util.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.random.Random
import java.util.*

class ChallengeFragment : BaseFragment() {

    //    private lateinit var subtitle: TextView
//    private var weekDates = arrayOfNulls<TextView>(7)
//    private var dates = arrayOfNulls<String>(7)
    private var start: MaterialButton? = null

    //    private var adTag: ImageView? = null
    private lateinit var selectedDate: String

    private lateinit var date: TextView
    private lateinit var month: TextView
    private lateinit var year: TextView
    private lateinit var calendar: Calendar
    private lateinit var right: ImageView
    private var starCount: TextView? = null

    private lateinit var today: String
    private var isRePlay: Boolean = false


    override fun getLayoutId(): Int = R.layout.fragment_challenge

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        adTag = view.findViewById(R.id.adTag)
        start = view.findViewById(R.id.start)
        date = view.findViewById(R.id.date)
        month = view.findViewById(R.id.month)
        year = view.findViewById(R.id.year)
        starCount = view.findViewById(R.id.starCount)
        // Easy.instance.showInterstitial()

        calendar = Calendar.getInstance()
        today = DateUtil.getDate(calendar)

        view.findViewById<ImageView>(R.id.rate).setOnClickListener { v ->
            RateDialog(v.context).show()
        }

        view.findViewById<ConstraintLayout>(R.id.starContent).setOnClickListener { v ->
            TAlertDialog(v.context).apply {
                this.setTitleId(R.string.moreStar)
                this.setContentId(R.string.moreStarContent)
                this.setActiveId(R.string.yesAd, object : IEvent {
                    override fun onEvent(dialog: BaseDialog) {
                        when (Easy.instance.hasRewarded()) {
                            true -> {
                                Easy.instance.setRewardedListener(object : IAdListener {
                                    override fun onClosed(rewarded: Boolean) {
                                        super.onClosed(rewarded)
                                        if (rewarded) {
                                            val count = 8 + Random.nextInt(0, 5)
                                            LocalStorage.addStar(count)
                                            starCount?.text = "x${
                                                LocalStorage.decode(
                                                    LocalStorage.STAR_COUNT, LocalStorage.DEFAULT_STAR_COUNT
                                                )
                                            }"
                                            dialog.dismiss()
                                        }
                                    }
                                })
                                Easy.instance.showRewarded()
                            }
                            else -> Toast.makeText(v.context, R.string.noAd, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })
                this.setInActiveId(R.string.no, object : IEvent {
                    override fun onEvent(dialog: BaseDialog) {
                        dialog.dismiss()
                    }
                })
                this.show()
            }
        }

        view.findViewById<ImageView>(R.id.left).setOnClickListener { _ ->
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            setupDate()
        }

        right = view.findViewById<ImageView>(R.id.right)
        right.setOnClickListener { _ ->
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            setupDate()
        }

        setupDate()
        start?.setOnClickListener { view ->
            when (isRePlay) {
                true -> {
                    TAlertDialog(view.context).let { dialog ->
                        dialog.setCancelable(true)
                        dialog.setCanceledOnTouchOutside(true)
                        dialog.setTitleId(R.string.startChallenge)
                        dialog.setContentId(R.string.rechallenge)
                        dialog.setActiveId(R.string.yesAd, object : IEvent {
                            override fun onEvent(dialog: BaseDialog) {
                                when (Easy.instance.hasRewarded()) {
                                    true -> {
                                        Easy.instance.setRewardedListener(object : IAdListener {
                                            override fun onClosed(rewarded: Boolean) {
                                                super.onClosed(rewarded)
                                                if (rewarded) {
                                                    activity?.let {
                                                        Intent(
                                                            it,
                                                            ChallengeGameActivity::class.java
                                                        ).apply {
                                                            it.startActivityIfNeeded(this, 2)
                                                        }
                                                    }
                                                    dialog.dismiss()
                                                }
                                            }
                                        })
                                        Easy.instance.showRewarded()
                                    }
                                    else -> Toast.makeText(
                                        dialog.context,
                                        R.string.noAd,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        })
                        dialog.setInActiveId(R.string.no, object : IEvent {
                            override fun onEvent(dialog: BaseDialog) {
                                dialog.dismiss()
                            }
                        })
                        dialog.show()
                    }
                }
                else -> {
                    activity?.let {
                        Intent(it, ChallengeGameActivity::class.java).apply {
                            it.startActivityIfNeeded(this, 2)
                        }
                    }
                    isRePlay = true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        starCount?.text =
            "x${LocalStorage.decode(LocalStorage.STAR_COUNT, LocalStorage.DEFAULT_STAR_COUNT)}"

        view?.findViewById<FrameLayout>(R.id.nativeContainer)?.let {
            Easy.instance.showNative(it)
        }
    }

    private fun format(value: Int): String {
        return value.let {
            if (it > 9) {
                "$it"
            } else {
                "0${it}"
            }
        }
    }

    private fun setupDate() {
        year.text = format(calendar.get(Calendar.YEAR))
        month.text = DateUtil.getMonthString(calendar)
        date.text = format(calendar.get(Calendar.DAY_OF_MONTH))
        selectedDate = DateUtil.getDate(calendar)
        if (selectedDate == today) {
            right.setColorFilter(Color.GRAY)
            right.isClickable = false
        } else {
            right.setColorFilter(Color.TRANSPARENT)
            right.isClickable = true
        }
        MainScope().launch {
            checkStatus()
        }
    }

    private suspend fun checkStatus() {
        flowOf(selectedDate).map {
            val count = GameData.instance.weekDao().getWeekly(it)
            return@map count > 0
        }.flowOn(Dispatchers.IO).catch {
            it.printStackTrace()
        }.collect { b ->
            when (b) {
                true -> {
                    isRePlay = true
                }
                else -> {
                    isRePlay = false
                }
            }
        }
    }

}