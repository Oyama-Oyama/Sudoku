package com.roman.gurdan.sudo.pro.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.AAChartCore.AAChartCoreLib.AAChartCreator.AAChartModel;
import com.AAChartCore.AAChartCoreLib.AAChartCreator.AAChartView;
import com.AAChartCore.AAChartCoreLib.AAChartCreator.AASeriesElement;
import com.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartSymbolStyleType;
import com.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartType;
import com.AAChartCore.AAChartCoreLib.AAOptionsModel.AADataLabels;
import com.AAChartCore.AAChartCoreLib.AAOptionsModel.AAPie;
import com.roman.gurdan.sudo.game.GameSize;
import com.roman.gurdan.sudo.pro.DateUtil;
import com.roman.gurdan.sudo.pro.R;
import com.roman.gurdan.sudo.pro.data.db.GameData;

import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChartView extends CardView {

    private static boolean TEST_MODE = true;
    private Random random = new Random();
    private int chartType;
    private AAChartView chartView;
    private CompositeDisposable disposables = new CompositeDisposable();

    public ChartView(@NonNull Context context) {
        this(context, null);
    }

    public ChartView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ChartView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ChartView);
        chartType = array.getInt(R.styleable.ChartView_chartType, 0);
        array.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            View.inflate(getContext(), R.layout.view_data_chart, this);
            chartView = findViewById(R.id.chartView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            disposables.add(Observable.just(chartType)
                    .map(new Function<Integer, AAChartModel>() {
                        @Override
                        public AAChartModel apply(Integer integer) throws Throwable {
                            if (integer == 1) {
                                return buildTotalWinRate();
                            } else if (integer == 2) {
                                return buildRecent15DayCount();
                            } else if (integer == 3) {
                                return buildType15DayWinRate();
                            } else if (integer == 4) {
                                return buildWeeklyWinRate();
                            }
                            return buildTotalWinRate();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<AAChartModel>() {
                        @Override
                        public void accept(AAChartModel aaChartModel) throws Throwable {
                            if (chartView != null && aaChartModel != null) {
                                chartView.aa_drawChartWithChartModel(aaChartModel);
                            }
                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (disposables != null && !disposables.isDisposed()) disposables.dispose();
        super.onDetachedFromWindow();
    }

    private AAChartModel buildTotalWinRate() {
        float win = TEST_MODE ? random.nextFloat() : GameData.of(getContext().getApplicationContext()).gameDao().getGamesResultCount(1);
        float fail = TEST_MODE ? (1 - win) : GameData.of(getContext().getApplicationContext()).gameDao().getGamesResultCount(0);
        AAChartModel aaChartModel = new AAChartModel()
                .chartType(AAChartType.Pie)
                .title("综合胜率")
                .backgroundColor("#c4c6c9")
                .tooltipEnabled(false)
                .dataLabelsEnabled(true)
                .series(new AAPie[]{
                        new AAPie()
                                .innerSize("20%")
                                .size(180)
                                .allowPointSelect(true)
                                .dataLabels(new AADataLabels()
                                        .enabled(true)
                                        .useHTML(true)
                                        .distance(5)
                                        .format("<b>{point.name}</b>: <br> {point.percentage:.1f} %"))
                                .data(new Object[][]{
                                {"Win", win},
                                {"Fail", fail}
                        })
                });
        return aaChartModel;
    }

    private AAChartModel buildRecent15DayCount() {
        String[] categories = new String[15];
        Object[] four = new Object[15];
        Object[] six = new Object[15];
        Object[] eight = new Object[15];
        Object[] nine = new Object[15];
        for (int i = 14; i >= 0; i--) {
            String targetDate = DateUtil.getDate(-1 * i);
            categories[14 - i] = targetDate;
            four[14 - i] = TEST_MODE ? random.nextInt(100) : GameData.of(getContext().getApplicationContext()).gameDao().getGames(GameSize.SIZE_FOUR.getValue(), targetDate);
            six[14 - i] = TEST_MODE ? random.nextInt(200) : GameData.of(getContext().getApplicationContext()).gameDao().getGames(GameSize.SIZE_SIX.getValue(), targetDate);
            eight[14 - i] = TEST_MODE ? random.nextInt(120) : GameData.of(getContext().getApplicationContext()).gameDao().getGames(GameSize.SIZE_EIGHT.getValue(), targetDate);
            nine[14 - i] = TEST_MODE ? random.nextInt(250) : GameData.of(getContext().getApplicationContext()).gameDao().getGames(GameSize.SIZE_NINE.getValue(), targetDate);
        }

        return new AAChartModel()
                .chartType(AAChartType.Line)
                .title("最近15天游戏次数")
                .backgroundColor("#c4c6c9")
                .categories(categories)
                .dataLabelsEnabled(false)
                .series(new AASeriesElement[]{
                        new AASeriesElement()
                                .name("Easy")
                                .data(four),
                        new AASeriesElement()
                                .name("Medium")
                                .data(six),
                        new AASeriesElement()
                                .name("Hard")
                                .data(eight),
                        new AASeriesElement()
                                .name("Expert")
                                .data(nine),
                });
    }

    private float getRate(int has, int count) {
        if (TEST_MODE) return random.nextFloat() * 100;
        if (count <= 0) return 0;
        return has * 1.0f / count * 100.0f;
    }

    private AAChartModel buildType15DayWinRate() {
        String[] categories = new String[15];
        Object[] four = new Object[15];
        Object[] six = new Object[15];
        Object[] eight = new Object[15];
        Object[] nine = new Object[15];
        for (int i = 14; i >= 0; i--) {
            String targetDate = DateUtil.getDate(-1 * i);
            categories[14 - i] = targetDate;
            int win4 = GameData.of(getContext().getApplicationContext()).gameDao().getGames(GameSize.SIZE_FOUR.getValue(), targetDate, 1);
            int fail4 = GameData.of(getContext().getApplicationContext()).gameDao().getGames(GameSize.SIZE_FOUR.getValue(), targetDate, 0);
            four[14 - i] = getRate(win4, win4 + fail4);
            int win6 = GameData.of(getContext().getApplicationContext()).gameDao().getGames(GameSize.SIZE_SIX.getValue(), targetDate, 1);
            int fail6 = GameData.of(getContext().getApplicationContext()).gameDao().getGames(GameSize.SIZE_SIX.getValue(), targetDate, 0);
            six[14 - i] = getRate(win6, win6 + fail6);
            int win8 = GameData.of(getContext().getApplicationContext()).gameDao().getGames(GameSize.SIZE_EIGHT.getValue(), targetDate, 1);
            int fail8 = GameData.of(getContext().getApplicationContext()).gameDao().getGames(GameSize.SIZE_EIGHT.getValue(), targetDate, 0);
            eight[14 - i] = getRate(win8, win8 + fail8);
            int win9 = GameData.of(getContext().getApplicationContext()).gameDao().getGames(GameSize.SIZE_NINE.getValue(), targetDate, 1);
            int fail9 = GameData.of(getContext().getApplicationContext()).gameDao().getGames(GameSize.SIZE_NINE.getValue(), targetDate, 0);
            nine[14 - i] = getRate(win9, win9 + fail9);
        }
        return new AAChartModel()
                .chartType(AAChartType.Line)
                .title("最近15天胜率")
                .backgroundColor("#c4c6c9")
                .categories(categories)
                .dataLabelsEnabled(false)
                .series(new AASeriesElement[]{
                        new AASeriesElement()
                                .name("Easy")
                                .data(four),
                        new AASeriesElement()
                                .name("Medium")
                                .data(six),
                        new AASeriesElement()
                                .name("Hard")
                                .data(eight),
                        new AASeriesElement()
                                .name("Expert")
                                .data(nine),
                });
    }

    private AAChartModel buildWeeklyWinRate() {
        String[] categories = new String[15];
        Object[] rate = new Object[15];
        Object[][] range = new Object[15][2];
        for (int i = 14; i >= 0; i--) {
            String targetDate = DateUtil.getDate(-1 * i);
            categories[14 - i] = targetDate;
            int win = GameData.of(getContext().getApplicationContext()).weekDao().getWeekly(targetDate, 1);
            int fail = GameData.of(getContext().getApplicationContext()).weekDao().getWeekly(targetDate, 1);
            float _rate = getRate(win, win + fail);
            rate[14 - i] = _rate;
            range[14 - i] = new Object[]{(_rate - 20), (_rate + 20)};
        }
        return new AAChartModel()
                .chartType(AAChartType.Spline)
                .title("挑战胜率")
                .markerSymbolStyle(AAChartSymbolStyleType.BorderBlank)
                .backgroundColor("#c4c6c9")
                .tooltipEnabled(false)
                .categories(categories)
                .dataLabelsEnabled(false)
                .series(new AASeriesElement[]{
                        new AASeriesElement()
                                .name("挑战胜率")
                                .type(AAChartType.Columnrange)
                                .data(range),
                        new AASeriesElement()
                                .name("挑战胜率趋势")
                                .allowPointSelect(true)
                                .borderRadius(1)
                                .data(rate)
                });
    }

}