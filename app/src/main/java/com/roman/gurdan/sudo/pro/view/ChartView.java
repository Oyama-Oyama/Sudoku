package com.roman.gurdan.sudo.pro.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel;
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType;
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView;
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement;
import com.roman.gurdan.sudo.pro.R;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChartView extends CardView {

    private int chartType;
    private AAChartView chartView;

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
            Observable.just(chartType)
                    .map(new Function<Integer, AAChartModel>() {
                        @Override
                        public AAChartModel apply(Integer integer) throws Throwable {
                            AAChartModel aaChartModel = new AAChartModel()
                                    .chartType(AAChartType.Area)
                                    .title("THE HEAT OF PROGRAMMING LANGUAGE")
                                    .subtitle("Virtual Data")
                                    .backgroundColor("#4b2b7f")
                                    .categories(new String[]{"Java", "Swift", "Python", "Ruby", "PHP", "Go", "C", "C#", "C++"})
                                    .dataLabelsEnabled(false)
                                    .yAxisGridLineWidth(0f)
                                    .series(new AASeriesElement[]{
                                            new AASeriesElement()
                                                    .name("Tokyo")
                                                    .data(new Object[]{7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6}),
                                            new AASeriesElement()
                                                    .name("NewYork")
                                                    .data(new Object[]{0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5}),
                                            new AASeriesElement()
                                                    .name("London")
                                                    .data(new Object[]{0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0}),
                                            new AASeriesElement()
                                                    .name("Berlin")
                                                    .data(new Object[]{3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8})
                                    });
                            return aaChartModel;
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
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AAChartModel buildTotalWinRate(){
        AAChartModel aaChartModel = new AAChartModel()
                .chartType(AAChartType.Area)
                .title("综合总胜率")
                .subtitle("Virtual Data")
                .backgroundColor("#4b2b7f")
                .categories(new String[]{"Java", "Swift", "Python", "Ruby", "PHP", "Go", "C", "C#", "C++"})
                .dataLabelsEnabled(false)
                .yAxisGridLineWidth(0f)
                .series(new AASeriesElement[]{
                        new AASeriesElement()
                                .name("Tokyo")
                                .data(new Object[]{7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6}),
                        new AASeriesElement()
                                .name("NewYork")
                                .data(new Object[]{0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5}),
                        new AASeriesElement()
                                .name("London")
                                .data(new Object[]{0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0}),
                        new AASeriesElement()
                                .name("Berlin")
                                .data(new Object[]{3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8})
                });
        return aaChartModel;
    }


}
