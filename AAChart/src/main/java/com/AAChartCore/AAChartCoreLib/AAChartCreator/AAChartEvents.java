package com.AAChartCore.AAChartCoreLib.AAChartCreator;

import com.AAChartCore.AAChartCoreLib.AATools.AAJSStringPurer;

public class AAChartEvents {
    public String load;
    public String selection;


    public AAChartEvents load(String prop) {
        load = AAJSStringPurer.pureAnonymousJSFunctionString(prop);
        return this;
    }

    public AAChartEvents selection(String prop) {
        selection = AAJSStringPurer.pureAnonymousJSFunctionString(prop);
        return this;
    }
}
