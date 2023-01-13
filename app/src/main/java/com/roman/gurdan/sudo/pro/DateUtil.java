package com.roman.gurdan.sudo.pro;

import java.util.Calendar;

public class DateUtil {

    public static String getDate(Calendar c){
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * YYYY-MM-dd
     * @return
     */
    public static String getDate(){
       return getDate(Calendar.getInstance());
    }

    /**
     *  前或后 count  天，  YYYY-MM-dd
     * @param count
     * @return
     */
    public static String getDate(int count){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, count);
        return getDate(c);
    }


}
