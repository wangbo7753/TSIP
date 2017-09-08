package com.future.tsip.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConvertor {

    public static String getTimeString(Date date) {
        return getTimeString(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getTimeString(Date date, String pattern) {
        return DateConvertor.getTimeString(date, pattern, true);
    }

    /**
     * 返回特定的时间字符串。输出的时间格式由pattern决定
     *
     * @param date
     *          时间
     * @param pattern
     *          时间的匹配字符串
     * @param returnCurrIfNull
     *          如果date为空，是否返回当前时间
     * @return 特定的时间字符串
     */
    public static String getTimeString(Date date, String pattern, boolean returnCurrIfNull) {
        if (date == null && !returnCurrIfNull) {
            return "";
        }
        DateFormat sdf = new SimpleDateFormat(pattern);
        if (date == null) {
            date = new Date();
        }
        return sdf.format(date);
    }

}
