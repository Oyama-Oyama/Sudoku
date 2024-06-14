package com.AAChartCore.AAChartCoreLib.AAOptionsModel;

import com.AAChartCore.AAChartCoreLib.AATools.AAJSStringPurer;

public class AASeriesEvents {
    public String legendItemClick;

    public AASeriesEvents legendItemClick(String prop) {
        legendItemClick = AAJSStringPurer.pureAnonymousJSFunctionString(prop);
        return this;
    }

}
