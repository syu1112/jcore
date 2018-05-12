package com.jcore.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期和时间工具包
 * 
 * @author Shunli
 */
public class DateUtil {

    public static final String PATTEM_DATE = "yyyy-MM-dd";
    public static final String PATTEM_TIME = "HH:mm:ss";
    public static final String PATTEM_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static final String PATTEM_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String PATTEM_DAY_BEGIN = "yyyy-MM-dd 00:00:00";
    public static final String PATTEM_DAY_END = "yyyy-MM-dd 23:59:59";
    public static final long ONEDAY_MILLISECONDS = 24 * 60 * 60 * 1000;

    public static final int DATE_COMPARE_BEFORE = -1;
    public static final int DATE_COMPARE_BETWEEN = 0;
    public static final int DATE_COMPARE_AFTER = 1;

    /**
     * 将字符串转换成时间格式
     * 
     * @param value
     *            字符串
     * @param pattem
     *            格式
     * @return 时间类型对象
     */
    public static Date getDate(String value, String pattem) {
        SimpleDateFormat format = null;
        if (pattem != null) {
            format = new SimpleDateFormat(pattem);
        } else {
            format = new SimpleDateFormat(PATTEM_DATE_TIME);
        }
        try {
            return format.parse(value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把字符转换成时间，格式为默认格式
     * 
     * @param value
     *            字符串
     * @return 时间类型对象
     */
    public static Date getDate(String value) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(PATTEM_DATE_TIME);
            return format.parse(value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化时间
     * 
     * @param date
     *            时间
     * @param pattem
     *            格式
     * @return 字符串类型的时间
     */
    public static String formatDate(Date date, String pattem) {
        if (date != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(pattem);
                return format.format(date);
            } catch (Exception e) {
                return null;
            }

        }
        return null;
    }

    /**
     * 以默认格式格式化时间
     * 
     * @param date
     *            时间
     * @return 字符串类型的时间
     */
    public static String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat(PATTEM_DATE_TIME);
            return format.format(date);
        }
        return null;
    }

    /**
     * 获得当前时间的默认格式字符串
     * 
     * @return 字符串类型的时间
     */
    public static String now() {
        return formatDate(new Date());
    }

    /**
     * 获得今天时间日期格式的字符串
     * 
     * @return 字符串类型的日期
     */
    public static String getToday() {
        return formatDate(new Date(), PATTEM_DATE);
    }

    /**
     * 获得明天时间日期格式的字符串
     * 
     * @return 字符串类型的日期
     */
    public static String getTomorrow() {
        long time = new Date().getTime() + ONEDAY_MILLISECONDS;
        return formatDate(new Date(time), PATTEM_DATE);
    }

    /**
     * 获得昨时间日期格式的字符串
     * 
     * @return 字符串类型的日期
     */
    public static String getYesterday() {
        long time = new Date().getTime() - ONEDAY_MILLISECONDS;
        return formatDate(new Date(time), PATTEM_DATE);
    }

    /**
     * 获得今天开始时间点 如：2011-09-09 00:00:00
     * 
     * @return 今天开始时间
     */
    public static String getTodayBegin() {
        return formatDate(new Date(), PATTEM_DAY_BEGIN);
    }

    /**
     * 获得今天的结束时间 如：2011-09-09 23:59:59
     * 
     * @return 今天的结束时间
     */
    public static String getTodayEnd() {
        return formatDate(new Date(), PATTEM_DAY_END);
    }

    /**
     * 获得某天的开始时间
     * 
     * @param dateStr
     *            字符类型的时间
     * @return 某天的开始时间
     */
    public static String getDayBegin(String dateStr) {
        if (StringUtil.isEmpty(dateStr) || dateStr.length() < 10)
            return null;
        return dateStr.substring(0, 10) + " 00:00:00";
    }

    /**
     * 获得某天的开始时间
     * 
     * @param date
     *            时间
     * @return 某天的开始时间
     */
    public static String getDayBegin(Date date) {
        if (date == null)
            return null;
        return formatDate(date, PATTEM_DAY_BEGIN);
    }

    /**
     * 获得某天的结束时间
     * 
     * @param dateStr
     *            字符类型的时间
     * @return 某天的结束时间
     */
    public static String getDayEnd(String dateStr) {
        if (StringUtil.isEmpty(dateStr) || dateStr.length() < 10)
            return null;
        return dateStr.substring(0, 10) + " 23:59:59";
    }

    /**
     * 获得某天的结束时间
     * 
     * @param date
     *            时间
     * @return 某天的结束时间
     */
    public static String getDayEnd(Date date) {
        if (date == null)
            return null;
        return formatDate(date, PATTEM_DAY_END);
    }

    /**
     * 当前时间与一段时间进行比较
     * 
     * @param begin
     *            开始时间
     * @param end
     *            结束时间
     * @return 比较结果 -1 在时间段之前，0 在时间段内，1 在时间段后
     */
    public static int compareTimeByNow(Date begin, Date end) {
        long now = new Date().getTime();
        if (now < begin.getTime())
            return DATE_COMPARE_BEFORE;
        if (now > end.getTime())
            return DATE_COMPARE_AFTER;

        return DATE_COMPARE_BETWEEN;
    }

    /**
     * 当前时间与一段时间进行比较
     * 
     * @param begin
     *            开始时间
     * @param end
     *            结束时间
     * @return 比较结果 -1 在时间段之前，0 在时间段内，1 在时间段后
     */
    public static int compareTimeByNow(String begin, String end) {
        long now = new Date().getTime();
        if (now < getDate(begin).getTime())
            return DATE_COMPARE_BEFORE;
        if (now > getDate(end).getTime())
            return DATE_COMPARE_AFTER;

        return DATE_COMPARE_BETWEEN;
    }

    /**
     * 判断当前时间是否在一段时间内
     * 
     * @param begin
     *            开始时间
     * @param end
     *            结束时间
     * @return 比较结果 true 在时间段内 false 不在时间段内
     */
    public static boolean isBetweenTime(Date begin, Date end) {
        return compareTimeByNow(begin, end) == DATE_COMPARE_BETWEEN;
    }

    /**
     * 判断当前时间是否在一段时间内
     * 
     * @param begin
     *            开始时间
     * @param end
     *            结束时间
     * @return 比较结果 true 在时间段内 false 不在时间段内
     */
    public static boolean isBetweenTime(String begin, String end) {
        return compareTimeByNow(begin, end) == DATE_COMPARE_BETWEEN;
    }

    /**
     * 计算当前时间与给出时间的毫秒数
     * 
     * @param date
     *            时间点
     * @return 毫秒数
     */
    public static long calTimeByNow(Date date) {
        return new Date().getTime() - date.getTime();
    }

    /**
     * 计算当前时间与给出时间的毫秒数
     * 
     * @param date
     *            时间点
     * @return 毫秒数
     */
    public static long calTimeByNow(String date) {
        return new Date().getTime() - getDate(date).getTime();
    }

    /**
     * 在当前时间点上加上/减去一定天数
     * 
     * @param days
     *            需要加/减的天数
     * @return 加上/减去后的时间点
     */
    public static Date addDays(int days) {
        return new Date(new Date().getTime() + days * ONEDAY_MILLISECONDS);
    }

    /**
     * 在当前时间点上加上/减去一定天数 并转成字符串返回
     * 
     * @param days
     *            需要加/减的天数
     * @return 加上/减去后的时间点
     */
    public static String addDaysForStr(int days) {
        return formatDate(addDays(days), PATTEM_DATE);
    }

    /**
     * 在当前时间点上加上/减去一定的毫秒数
     * 
     * @param milliseconds
     *            加上/减去的毫秒数
     * @return 加上/减去后的时间点
     */
    public static Date addMilliseconds(int milliseconds) {
        return new Date(new Date().getTime() + milliseconds);
    }

    /**
     * 在当前时间点上加上/减去一定的毫秒数 并转成字符串返回
     * 
     * @param milliseconds
     *            加上/减去的毫秒数
     * @return 加上/减去后的时间点
     */
    public static String addMillisecondsForStr(int milliseconds) {
        return formatDate(addMilliseconds(milliseconds));
    }

    /**
     * 在当前时间点萨很难过加上/减去一定的秒数
     * 
     * @param seconds
     *            加上/减去的秒数
     * @return 加上/减去后的时间点
     */
    public static Date addSeconds(int seconds) {
        return new Date(new Date().getTime() + 1000 * seconds);
    }

    /**
     * 在当前时间点萨很难过加上/减去一定的秒数 并转成字符串返回
     * 
     * @param seconds
     *            加上/减去的秒数
     * @return 加上/减去后的时间点
     */
    public static String addSecondsForStr(int seconds) {
        return formatDate(addSeconds(seconds));
    }

    public static int getWeek() {
        return getWeek(new Date());
    }

    public static int getWeek(String time) {
        Date t = null;
        if (time != null && time.length() >= 10) {
            t = getDate(time.substring(0, 10), PATTEM_DATE);
        } else {
            return -1;
        }
        return getWeek(t);
    }

    public static int getWeek(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static String showtime(long millis) {
        Calendar now = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        String str = formatDate(cal.getTime());

        if (now.getTimeInMillis() - cal.getTimeInMillis() > 86400000) {
            return str.substring(5, 10);
        }

        if (now.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)) {
            return str.substring(11, 16);
        }
        return str.substring(5, 10);
    }

}
