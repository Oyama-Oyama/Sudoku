package com.roman.gurdan.sudo.pro.util;

import com.tencent.mmkv.MMKV;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateUtil {

    public static String getDate(Calendar c) {
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * YYYY-MM-dd
     *
     * @return
     */
    public static String getDate() {
        return getDate(Calendar.getInstance());
    }

    /**
     * 前或后 count  天，  YYYY-MM-dd
     *
     * @param count
     * @return
     */
    public static String getDate(int count) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, count);
        return getDate(c);
    }

    public static String[][] getWeek() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK) - 1;
        c.add(Calendar.DAY_OF_MONTH, -1 * day);
        String[][] result = new String[7][2];
        for (int i = 0; i < 7; i++) {
            int _day = c.get(Calendar.DAY_OF_MONTH);
            String date = getDate(c);
            result[i] = new String[]{date, _day > 9 ? String.valueOf(_day) : ("0" + _day)};
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        return result;
    }

    public static int getWeeklyTag() {
        long preRecord = MMKV.defaultMMKV().getLong(CacheId.WEEKLY_START_POINT, -1);
        GregorianCalendar current = new GregorianCalendar();
        GregorianCalendar previous = new GregorianCalendar();
        previous.setTimeInMillis(preRecord);
        int tag = (int) ((current.getTimeInMillis() - previous.getTimeInMillis()) / (1000 * 3600 * 24) / 7 + 1);
        return tag;
    }

    public static void resetWeekTag() {
        long preRecord = MMKV.defaultMMKV().getLong(CacheId.WEEKLY_START_POINT, -1);
        boolean hasStarted = preRecord > 0;
        if (!hasStarted) {
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_WEEK) - 1;
            c.add(Calendar.DAY_OF_MONTH, -1 * day);
            MMKV.defaultMMKV().putLong(CacheId.WEEKLY_START_POINT, c.getTimeInMillis());
        }
    }

    public static String millSecondToDate(long millSecond) {
        long seconds = millSecond / 1000;
        long second = seconds % 60;
        long minutes = seconds / 60 % 60;
        long hours = seconds / 60 / 60;
        return (hours >= 10 ? hours : "0" + hours) + ":" + (minutes >= 10 ? minutes : "0" + minutes) + ":" + (second >= 10 ? second : "0" + second);
    }


}
