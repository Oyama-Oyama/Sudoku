package com.roman.gurdan.sudo.pro.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import com.AAChartCore.AAChartCoreLib.AAChartCreator.AAChartModel
import com.AAChartCore.AAChartCoreLib.AAChartCreator.AAChartView
import com.AAChartCore.AAChartCoreLib.AAChartCreator.AASeriesElement
import com.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartType
import com.AAChartCore.AAChartCoreLib.AAOptionsModel.AADataLabels
import com.AAChartCore.AAChartCoreLib.AAOptionsModel.AAPie
import com.roman.garden.sudo.base.util.GameSize
import com.roman.gurdan.sudo.pro.BuildConfig
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.data.db.GameData
import com.roman.gurdan.sudo.pro.util.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*


class ChartView(context: Context, attrs: AttributeSet?) :
    CardView(context, attrs) {

    private val TEST_MODE = BuildConfig.DEBUG
    private var random = Random()
    private var chartType: Int = 0
    private lateinit var chartView: AAChartView

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ChartView).apply {
            chartType = this.getInt(R.styleable.ChartView_chartType, 0);
            this.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        try {
            View.inflate(context, R.layout.view_data_chart, this);
            chartView = findViewById(R.id.chartView);
        } catch (e: Exception) {
            e.printStackTrace();
        }
        MainScope().launch {
            flowOf(chartType)
                .map {
                    when (it) {
                        1 -> buildTotalWinRate()
                        2 -> buildRecent15DayCount()
                        3 -> buildType15DayWinRate()
                        else -> null
                    }
                }
                .flowOn(Dispatchers.IO)
                .catch { e->
                    e.printStackTrace()
                }
                .collect { model ->
                    model?.let {
                        if (chartView != null)
                            chartView.aa_drawChartWithChartModel(it)
                    }
                }
        }
    }

    private fun buildTotalWinRate(): AAChartModel {
        val win: Float = if (TEST_MODE) random.nextFloat() else GameData.instance.gameDao()
            .getGamesResultCount(1)
            .toFloat()
        val fail: Float =
            if (TEST_MODE) (1 - win) else GameData.instance.gameDao().getGamesResultCount(0)
                .toFloat()
        return AAChartModel()
            .chartType(AAChartType.Pie)
            .title(context.getString(R.string.winRate))
            .backgroundColor("#c4c6c9")
            .tooltipEnabled(false)
            .dataLabelsEnabled(true)
            .series(
                arrayOf<AAPie>(
                    AAPie().innerSize("20%")
                        .size(180)
                        .allowPointSelect(true)
                        .dataLabels(
                            AADataLabels().enabled(true)
                                .useHTML(true)
                                .distance(5)
                                .format("<b>{point.name}</b>: <br> {point.percentage:.1f} %")
                        )
                        .data(
                            arrayOf(
                                arrayOf(context.getString(R.string.winn), win),
                                arrayOf(context.getString(R.string.fail), fail)
                            )
                        )
                )
            )
    }

    private fun buildRecent15DayCount(): AAChartModel {
        val categories = arrayOfNulls<String>(15)
        val four = arrayOfNulls<Any>(15)
        val six = arrayOfNulls<Any>(15)
        val eight = arrayOfNulls<Any>(15)
        val nine = arrayOfNulls<Any>(15)
        for (i in 14 downTo 0) {
            val targetDate = DateUtil.getDate(-1 * i)
            categories[14 - i] = targetDate
            four[14 - i] = if (TEST_MODE) random.nextInt(100) else GameData.instance.gameDao()
                .getGames(GameSize.SIZE_FOUR.value, targetDate)
            six[14 - i] = if (TEST_MODE) random.nextInt(100) else GameData.instance.gameDao()
                .getGames(GameSize.SIZE_SIX.value, targetDate)
            eight[14 - i] = if (TEST_MODE) random.nextInt(100) else GameData.instance.gameDao()
                .getGames(GameSize.SIZE_EIGHT.value, targetDate)
            nine[14 - i] = if (TEST_MODE) random.nextInt(100) else GameData.instance.gameDao()
                .getGames(GameSize.SIZE_NINE.value, targetDate)
        }
        return AAChartModel()
            .chartType(AAChartType.Line)
            .title(getContext().getString(R.string.recent15day))
            .backgroundColor("#c4c6c9")
            .categories(categories)
            .dataLabelsEnabled(false)
            .series(
                arrayOf(
                    AASeriesElement().name(context.getString(R.string.easy)).data(four),
                    AASeriesElement().name(context.getString(R.string.medium)).data(six),
                    AASeriesElement().name(context.getString(R.string.hard)).data(eight),
                    AASeriesElement().name(context.getString(R.string.expert)).data(nine)
                )
            )
    }

    private fun getRate(has: Int, count: Int): Float {
        if (TEST_MODE) return random.nextFloat() * 100
        if (count <= 0) return 0.0f
        return has * 1.0f / count * 100.0f
    }

    private fun buildType15DayWinRate(): AAChartModel {
        val categories = arrayOfNulls<String>(15)
        val four = arrayOfNulls<Any>(15)
        val six = arrayOfNulls<Any>(15)
        val eight = arrayOfNulls<Any>(15)
        val nine = arrayOfNulls<Any>(15)
        for (i in 14 downTo 0) {
            val targetDate = DateUtil.getDate(-1 * i)
            categories[14 - i] = targetDate
            val win4 =
                GameData.instance.gameDao().getGames(GameSize.SIZE_FOUR.value, targetDate, 1)
            val fail4 =
                GameData.instance.gameDao().getGames(GameSize.SIZE_FOUR.value, targetDate, 0)
            four[14 - i] = getRate(win4, win4 + fail4);
            val win6 =
                GameData.instance.gameDao().getGames(GameSize.SIZE_SIX.value, targetDate, 1)
            val fail6 =
                GameData.instance.gameDao().getGames(GameSize.SIZE_SIX.value, targetDate, 0)
            six[14 - i] = getRate(win4, win4 + fail4);
            val win8 =
                GameData.instance.gameDao().getGames(GameSize.SIZE_EIGHT.value, targetDate, 1)
            val fail8 =
                GameData.instance.gameDao().getGames(GameSize.SIZE_EIGHT.value, targetDate, 0)
            eight[14 - i] = getRate(win4, win4 + fail4);
            val win9 =
                GameData.instance.gameDao().getGames(GameSize.SIZE_NINE.value, targetDate, 1)
            val fail9 =
                GameData.instance.gameDao().getGames(GameSize.SIZE_NINE.value, targetDate, 0)
            nine[14 - i] = getRate(win4, win4 + fail4);
        }
        return AAChartModel()
            .chartType(AAChartType.Line)
            .title(getContext().getString(R.string.recent15day))
            .backgroundColor("#c4c6c9")
            .categories(categories)
            .dataLabelsEnabled(false)
            .series(
                arrayOf(
                    AASeriesElement().name(context.getString(R.string.easy)).data(four),
                    AASeriesElement().name(context.getString(R.string.medium)).data(six),
                    AASeriesElement().name(context.getString(R.string.hard)).data(eight),
                    AASeriesElement().name(context.getString(R.string.expert)).data(nine)
                )
            )
    }


}