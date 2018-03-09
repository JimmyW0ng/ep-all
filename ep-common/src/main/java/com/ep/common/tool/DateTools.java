package com.ep.common.tool;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间计算工具类
 */
public class DateTools {

    /**
     * the milli second of a day
     */
    public static final long DAYMILLI = 24 * 60 * 60 * 1000;

    /**
     * the milli seconds of an hour
     */
    public static final long HOURMILLI = 60 * 60 * 1000;

    /**
     * the milli seconds of a minute
     */
    public static final long MINUTEMILLI = 60 * 1000;

    /**
     * the milli seconds of a second
     */
    public static final long SECONDMILLI = 1000;

    /**
     * added time
     */
    public static final String TIMETO = " 23:59:59";

    /**
     * flag before
     */
    public static final transient int BEFORE = 1;

    /**
     * flag after
     */
    public static final transient int AFTER = 2;

    /**
     * flag equal
     */
    public static final transient int EQUAL = 3;

    /**
     * date format dd/MMM/yyyy:HH:mm:ss +0900
     */
    public static final String TIME_PATTERN_LONG = "dd/MMM/yyyy:HH:mm:ss +0900";

    /**
     * date format dd/MM/yyyy:HH:mm:ss +0900
     */
    public static final String TIME_PATTERN_LONG2 = "dd/MM/yyyy:HH:mm:ss +0900";


    /**
     * date format YYYY-MM-DD HH24:MI:SS
     */
    public static final String DB_TIME_PATTERN = "YYYY-MM-DD HH24:MI:SS";

    /**
     * date format YYYYMMDDHH24MISS
     */
    public static final String DB_TIME_PATTERN_1 = "YYYYMMDDHH24MISS";

    /**
     * date format dd/MM/yy HH:mm:ss
     */
    public static final String TIME_PATTERN_SHORT = "dd/MM/yy HH:mm:ss";

    /**
     * date format dd/MM/yy HH24:mm
     */
    public static final String TIME_PATTERN_SHORT_1 = "yyyy/MM/dd HH:mm";

    /**
     * date format yyyy年MM月dd日 HH:mm:ss
     */
    public static final String TIME_PATTERN_SHORT_2 = "yyyy年MM月dd日 HH:mm:ss";

    /**
     * date format yyyyMMddHHmmss
     */
    public static final String TIME_PATTERN_SESSION = "yyyyMMddHHmmss";

    /**
     * date format yyyyMMddHHmmssSSS
     */
    public static final String TIME_PATTERN_MILLISECOND = "yyyyMMddHHmmssSSS";

    /**
     * date format yyyyMMdd
     */
    public static final String DATE_FMT_0 = "yyyyMMdd";

    /**
     * date format yyyy/MM/dd
     */
    public static final String DATE_FMT_1 = "yyyy/MM/dd";

    /**
     * date format yyyy/MM/dd hh:mm:ss
     */
    public static final String DATE_FMT_2 = "yyyy/MM/dd hh:mm:ss";

    /**
     * date format yyyy-MM-dd
     */
    public static final String DATE_FMT_3 = "yyyy-MM-dd";

    /**
     * date format yyyy年MM月dd日
     */
    public static final String DATE_FMT_4 = "yyyy年MM月dd日";

    /**
     * date format yyyy-MM-dd HH
     */
    public static final String DATE_FMT_5 = "yyyy-MM-dd HH";

    /**
     * date format yyyy-MM
     */
    public static final String DATE_FMT_6 = "yyyy-MM";

    /**
     * date format MM月dd日 HH:mm
     */
    public static final String DATE_FMT_7 = "MM月dd日 HH:mm";

    /**
     * date format MM月dd日 HH:mm
     */
    public static final String DATE_FMT_8 = "HH:mm:ss";
    /**
     * date format MM月dd日 HH:mm
     */
    public static final String DATE_FMT_9 = "yyyy.MM.dd";

    public static final String DATE_FMT_10 = "HH:mm";

    public static final String DATE_FMT_11 = "yyyy.MM.dd HH:mm:ss";

    /**
     * date format yyyy年MM月dd日
     */
    public static final String DATE_FMT_12 = "MM月dd日";


    public static final String DATE_FMT_13 = "yyyy年MM月dd日HH时mm分";


    public static final String DATE_FMT_14 = "yyyyMM";

    public static final String DATE_FMT_15 = "MM-dd HH:mm:ss";

    public static final String DATE_FMT_16 = "yyyyMMddHHmm";

    public static final String DATE_FMT_17 = "HHmmss";

    public static final String DATE_FMT_18 = "yyyy";

    /**
     * date format yyyy-MM-dd HH:mm:ss
     */
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    static {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(timeZone);
    }

    /**
     * 字符串转Date
     *
     * @param str
     * @return
     */
    public static Date stringToDate(String str, String _format) {
        if (StringTools.isBlank(str) || StringTools.isBlank(_format)) {
            return null;
        }
        DateFormat format = new SimpleDateFormat(_format);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    /**
     * 字符串转Timestamp
     *
     * @param str
     * @return
     */
    public static Timestamp stringToTimestamp(String str, String _format) {
        return dateToTimestamp(stringToDate(str, _format));
    }

    /**
     * date 转 string
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {
        String _str = null;
        DateFormat _format = new SimpleDateFormat(format);
        if (date != null) {
            _str = _format.format(date);
        }
        return _str;
    }

    /**
     * timestamp 转 string
     *
     * @param timestamp
     * @param format
     * @return
     */
    public static String timestampToString(Timestamp timestamp, String format) {
        Date date = timestampToDate(timestamp);
        return dateToString(date, format);
    }

    /**
     * 获取一个月有多少天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getActualMaximum(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DATE, 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = {"日", "一", "二", "三", "四", "五", "六"};
        //String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    /**
     * 获取系统当前时间 del 改用注入ISystemTimeService
     *
     * @param format
     * @return
     */
    public static String getCurrentDate(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
        return df.format(new Date());
    }

    /**
     * 取得到期日期
     *
     * @param start
     * @param unit
     * @param amount
     * @return
     */
    public static Date getEndingDate(Date start, String unit, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        return getEndingDate(cal, unit, amount).getTime();
    }

    /**
     * 取得到期日期
     *
     * @param start  Calendar
     * @param unit   String
     * @param amount int
     */
    public static Calendar getEndingDate(Calendar start, String unit, int amount) {

        int field = -1;
        GregorianCalendar cal = new GregorianCalendar(start.get(Calendar.YEAR),
                start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH),
                start.get(Calendar.HOUR), start.get(Calendar.MINUTE));

        //if (unit.equals(CodeTable.getValue(TERMUNIT, TERMUNIT_S1)))
        if (unit.equals("1"))
            field = Calendar.YEAR;
        //if (unit.equals(CodeTable.getValue(TERMUNIT, TERMUNIT_S2)))
        if (unit.equals("2"))
            field = Calendar.MONTH;
        //if (unit.equals(CodeTable.getValue(TERMUNIT, TERMUNIT_S3)))
        if (unit.equals("3"))
            field = Calendar.DAY_OF_MONTH;
        cal.add(field, amount);

        return cal;
    }

    /**
     * 计算两个日期相差的天数，只考虑年月日，不考虑时分秒(如果加上时分秒，可能导致天数计算错误)
     *
     * @param before
     * @param after
     * @return long 相差的天数
     */
    public static int getDateDiff(Date before, Date after) {
        if (before == null || after == null)
            return 0;
        Calendar calendar1 = new GregorianCalendar();
        Calendar calendar2 = new GregorianCalendar();
        calendar1.setTime(before);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        calendar2.setTime(after);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);
        int diff = (int) ((calendar2.getTime().getTime() - calendar1.getTime().getTime()) / (24 * 60 * 60 * 1000));
        return diff;
    }

    public static long getDateDiffSeconds(Date before, Date after) {
        if (before == null || after == null)
            return 0;
        long diff = ((after.getTime() - before.getTime()) / 1000);
        return diff;
    }

    /**
     * 计算两个日期相差的天数，不考虑日期前后，返回结果>=0
     *
     * @param before
     * @param after
     * @return
     */
    public static int getAbsDateDiff(Date before, Date after) {
        int diff = getDateDiff(before, after);
        return Math.abs(diff);
    }

    /**
     * 比较第一个日期是否早于第二个日期 利用了getDateDiff方法，如果两者相差天数>0,则为true;
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean getDateIsBefore(Date d1, Date d2) {
        if (d1 == null || d2 == null)
            return false;
        int diff = getDateDiff(d1, d2);
        if (diff > 0)
            return true;
        return false;
    }

    /**
     * 比较第一个日期是否晚于第二个日期 利用了getDateDiff方法，如果两者相差天数<0,则为true;
     *
     * @param d1
     * @param d2
     * @return boolean
     */
    public static boolean getDateIsAfter(Date d1, Date d2) {
        if (d1 == null || d2 == null)
            return false;
        int diff = getDateDiff(d1, d2);
        if (diff < 0)
            return true;
        return false;
    }

    /**
     * Timestamp转成Calendar
     *
     * @param timestamp
     * @return Calendar
     */
    public static Calendar timestampToCalendar(Timestamp timestamp) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timestamp.getTime());
        calendar.getTime();// 立即刷新
        return calendar; // 不能使用getInstance,否则变当时状态
    }

    /**
     * Calendar转成Timestamp
     *
     * @param calendar
     * @return Timestamp
     */
    public static Timestamp calendarToTimestamp(Calendar calendar) {
        return new Timestamp(calendar.getTimeInMillis());// 不能使用getInstance,否则变成当时状态

    }

    /**
     * Date转换成Timestamp
     *
     * @param date
     * @return
     */
    public static Timestamp dateToTimestamp(Date date) {
        if (date == null)
            return null;
        return new Timestamp(date.getTime());
    }

    /**
     * Timestamp转换成Date
     *
     * @param timestamp
     * @return
     */
    public static Date timestampToDate(Timestamp timestamp) {
        if (timestamp == null)
            return null;
        return new Date(timestamp.getTime());
    }

    /**
     * 根据期限单位取期限的天数
     *
     * @param termUnitCd 期限单位
     * @param term       期限
     * @return 期限（单位为天）
     * @since <pre>
     * int temp = ContractUtil.intValue(term);
     * if (null != termUnitCd &amp;&amp; null != term) {
     * 	if (CodeTable.getValue(&quot;TermUnit&quot;, &quot;S1&quot;).equals(termUnitCd)) {
     * 		temp = ContractUtil.intValue(term) * 365;
     * 	}
     * 	if (CodeTable.getValue(&quot;TermUnit&quot;, &quot;S2&quot;).equals(termUnitCd)) {
     * 		temp = ContractUtil.intValue(term) * 30;
     * 	}
     * }
     * return temp;
     * </pre>
     */
    public static int calTermDay(String termUnitCd, Integer term) {
        int temp = NumberTools.intValue(term);
        if (null != termUnitCd && null != term) {
            //if (CodeTable.getValue(TERMUNIT, TERMUNIT_S1).equals(termUnitCd)) {
            if ("1".equals(termUnitCd)) {
                temp = NumberTools.intValue(term) * 365;
            }
            //if (CodeTable.getValue(TERMUNIT, TERMUNIT_S2).equals(termUnitCd)) {
            if ("2".equals(termUnitCd)) {
                temp = NumberTools.intValue(term) * 30;
            }
        }
        return temp;
    }

    /**
     * 根据开始日期和结束日期计算两个日期的工作日天数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param holidays  节假日集合 Date of List(yyyy-MM-dd) 也可为空（为空的情况忽略法定节假日）
     * @return int 返回工作天数
     */
    public static int countWorkingDays(Date startDate, Date endDate, List<?> holidays, List<?> weekEndWorkDays) {
        Date date;
        if (getDateIsBefore(startDate, endDate)) {
            date = startDate;
        } else {
            date = endDate;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int count = 0;
        while (!date.after(endDate)) {
            if (isWorkDay(date, holidays, weekEndWorkDays)) {//判断是否为工作日，如果为工作日则加1
                count++;
            }
            cal.add(Calendar.DATE, 1);//日期向后加1天
            date = cal.getTime();
        }

        return count;
    }

    /**
     * 根据传入日期返回星期几
     *
     * @param date 日期
     * @return 1-7 1：星期天,2:星期一,3:星期二,4:星期三,5:星期四,6:星期五,7:星期六
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 根据日期判断是否为工作日
     *
     * @param date            需要判断的日期
     * @param holidays        法定假日集合
     * @param weekEndWorkDays 周末工作日集合
     * @return 是工作日返回true, 不是工作日返回false;
     */
    public static boolean isWorkDay(Date date, List<?> holidays, List<?> weekEndWorkDays) {
        int day_of_week = DateTools.getDayOfWeek(date);
        if (day_of_week == 1 && !isHoliday(date, weekEndWorkDays)) {// 星期六
            return false;
        } else if (day_of_week == 7 && !isHoliday(date, weekEndWorkDays)) {// 星期天
            return false;
        }
        if (holidays != null && holidays.size() > 0) {
            if (isHoliday(date, holidays)) {//判断是否为法定假日
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为法定假日
     *
     * @param date     需要判断的日期
     * @param holidays 假日集合
     * @return true为法定假日，false为工作日
     */
    public static boolean isHoliday(Date date, List<?> holidays) {
        if (holidays != null && holidays.size() > 0) {
            Date holiday;
            for (int j = 0; j < holidays.size(); j++) {
                holiday = (Date) holidays.get(j);
                if (getDateDiff(holiday, date) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 日期年份计算函数
     *
     * @param date     要计算的日期
     * @param addYears 要计算的数字 正数为增加XX年 负数为减少XX年
     * @return 计算后的日期
     */
    public static Date addYear(Date date, Integer addYears) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, addYears);
        return calendar.getTime();
    }

    /**
     * 日期月份计算函数
     *
     * @param date      要计算的日期
     * @param addMonths 要计算的数字 正数为增加XX月 负数为减少XX月
     * @return 计算后的日期
     */
    public static Date addMonth(Date date, Integer addMonths) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, addMonths);
        return calendar.getTime();
    }

    /**
     * 日期秒数计算函数
     *
     * @param date       要计算的日期
     * @param addSeconds 要计算的数字 正数为增加XX秒 负数为减少XX秒
     * @return
     */
    public static Date addSecond(Date date, Integer addSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, addSeconds);
        return calendar.getTime();
    }

    /**
     * 日期分钟计算函数
     *
     * @param date       要计算的日期
     * @param addMinutes 要计算的数字 正数为增加XX分 负数为减少XX分
     * @return
     */
    public static Date addMinute(Date date, Integer addMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, addMinutes);
        return calendar.getTime();
    }

    public static Timestamp addMinuteTimestamp(Date date, Integer addMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, addMinutes);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 日期天数计算函数
     *
     * @param date    要计算的日期
     * @param addDays 要计算的数字 正数为增加XX天 负数为减少XX天
     * @return 计算后的日期
     */
    public static Date addDay(Date date, Integer addDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, addDays);
        return calendar.getTime();
    }

    /**
     * 获取系统时间(系统环境影响时区经过修正)
     *
     * @param format
     * @return
     */
    public static String getSystemDate(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date currTime = new Date();
        return formatter.format(currTime);
    }

    /**
     * 计算所处时区
     *
     * @param timeZone_digital 时区时间差值 格式： -480,480等
     * @return
     */
    public static TimeZone getTimeZone(String timeZone_digital) {
        String gmt = "";
        if (timeZone_digital != null) {
            int length = timeZone_digital.length();
            String f = "";
            if (length == 1) {
                gmt = "GMT";
            } else if (timeZone_digital.startsWith("-")) {
                f = timeZone_digital.substring(1, length);
                gmt = "GMT+" + Integer.parseInt(f) / 60;
            } else {
                gmt = "GMT-" + Integer.parseInt(timeZone_digital) / 60;
            }
        } else {
            // 北京时区   
            return TimeZone.getTimeZone("GMT+8");
        }
        return TimeZone.getTimeZone(gmt);
    }

    public static int getLastMonthDay() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        int lastDay = cal.getActualMaximum(Calendar.DATE);

        return lastDay;
    }

    /**
     * 获取指定日期的当月的月份数
     *
     * @param date
     * @return
     */
    public static int getLastMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int lastMonth = cal.get(Calendar.MONTH); // 上个月月份           

        return lastMonth;

    }

    /**
     * 上月的最后一天
     *
     * @param date
     * @return
     */
    public static Calendar newLastMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int lastMonth = cal.get(Calendar.MONTH); // 上个月月份    
        int thisYear = cal.get(Calendar.YEAR);
        int thisDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 本月结束天数

        if (lastMonth != 12) {
            cal.set(thisYear, lastMonth, thisDay);
        } else {
            cal.set(thisYear - 1, lastMonth, thisDay);
        }

        return cal;

    }

    /**
     * 特定日期的当月第一天
     *
     * @param date
     * @return
     */
    public static Calendar newThisMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int lastMonth = cal.get(Calendar.MONTH); // 上个月月份    
        int thisYear = cal.get(Calendar.YEAR);
        int thisDay1 = cal.getActualMinimum(Calendar.DAY_OF_MONTH); // 本月结束天数
        //int thisDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 本月结束天数

        cal.set(thisYear, lastMonth, thisDay1, 0, 0, 0);  //当月第一天

        return cal;

    }

    /**
     * 特定日期的当旬第一天
     *
     * @param date
     * @return
     */
    public static Calendar newFirstTenDays(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int newDay = 0;
        if (day > 0 && day <= 10)
            newDay = 1;
        if (day > 10 && day <= 20)
            newDay = 11;
        if (day > 20)
            newDay = 21;
        c.set(Calendar.DAY_OF_MONTH, newDay);
        return c;

    }

    /**
     * 特定日期的当年第一天
     *
     * @param date
     * @return
     */
    public static Calendar newThisYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int thisYear = cal.get(Calendar.YEAR);

        cal.set(thisYear, 0, 1, 0, 0, 0);  //1-1

        return cal;

    }

    /**
     * 特定日期的本季度第一天
     *
     * @param date
     * @return
     */
    public static Calendar newThisSeason(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int thisYear = cal.get(Calendar.YEAR);
        int lastMonth = cal.get(Calendar.MONTH); // 上个月月份
        if (1 <= lastMonth && lastMonth < 3) {
            cal.set(thisYear, 0, 1, 0, 0, 0);
        } else if (3 <= lastMonth && lastMonth < 6) {
            cal.set(thisYear, 3, 1, 0, 0, 0);
        } else if (6 <= lastMonth && lastMonth < 9) {
            cal.set(thisYear, 6, 1, 0, 0, 0);
        } else if (9 <= lastMonth && lastMonth < 12) {
            cal.set(thisYear, 9, 1, 0, 0, 0);
        } else if (lastMonth == 12) {
            cal.set(thisYear, 0, 1, 0, 0, 0);
        }


        return cal;

    }

    /**
     * 获取指定日期的上个月的最后一个日期数
     *
     * @param date
     * @return
     */
    public static int getLastMonthDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.MONTH, -1);
        int lastDay = cal.getActualMaximum(Calendar.DATE);

        return lastDay;
    }

    /**
     * 获取指定日期的本月的最后一个日期数
     *
     * @param date
     * @return
     */
    public static int getCurrentMonthDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        /*int lastMonth = cal.get(Calendar.MONTH); // 上个月月份    
        int thisYear = cal.get(Calendar.YEAR); 
        int thisDay1 = cal.getActualMinimum(Calendar.DAY_OF_MONTH);// 起始天数
        int thisDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 结束天数 
        */
        int last = cal.getActualMaximum(Calendar.DATE);

        return last;
    }

    /**
     * 获取指定日期的本旬的最后一个日期数
     *
     * @param date
     * @return
     */
    public static int getTenDaysLastDayCount(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int last = 0;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (day > 0 && day <= 10)
            last = 10;
        if (day > 10 && day <= 20)
            last = 20;
        if (day > 20)
            last = getCurrentMonthDay(date);
        return last;

    }

    /**
     * 获取指定日期的本旬的最后一天
     *
     * @param date
     * @return
     */
    public static Calendar getTenDaysLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int last = 0;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (day > 0 && day <= 10)
            last = 10;
        if (day > 10 && day <= 20)
            last = 20;
        if (day > 20)
            last = getCurrentMonthDay(date);
        cal.set(Calendar.DAY_OF_MONTH, last);
        return cal;

    }

    /**
     * 获取本年最后一天
     *
     * @param date
     * @return
     */
    public static Calendar getThisYearEndDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentYear = c.get(Calendar.YEAR);
        try {
            c.set(Calendar.YEAR, currentYear);
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * 获取本季度最后一天
     *
     * @param date
     * @return
     */
    public static Calendar getThisSeasonEndDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * 如果date小于dateUpper,返回date，否则返回dateUpper
     *
     * @param date
     * @param dateUpper
     * @return
     */
    public static Calendar getUpperData(Date date, Date dateUpper) {
        Calendar c = Calendar.getInstance();
        if (date.getTime() < dateUpper.getTime()) {
            c.setTime(date);
        } else {
            c.setTime(dateUpper);
        }
        return c;
    }

    /**
     * 比较年月是否相同
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public static boolean isMonthCompare(Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            if (dateToString(startDate, "yyyy-MM").equals(dateToString(endDate, "yyyy-MM"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回日期后num个月
     *
     * @param date          日期
     * @param num           月数
     * @param repaymentDate 约定还款日
     * @return
     */
    public static Date getDateYYYYMMDD(Date date, Integer num, Integer repaymentDate) {
        if (date == null || num == null || repaymentDate == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int cnt = DateTools.getActualMaximum(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1 + num);
        if (cnt < repaymentDate.intValue()) {
            repaymentDate = cnt;
        }
        date = getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1 + num, repaymentDate);
        return date;
    }

    /**
     * 字串转成时间
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return
     */
    public static Date getDate(int year, int month, int day) {
        StringBuffer sb = new StringBuffer();
        sb.append(year).append("-");
        if (month < 10 && month > 0) {
            sb.append(0).append(month);
        } else {
            sb.append(month);
        }
        sb.append("-");
        if (day < 10 && day > 0) {
            sb.append(0).append(day);
        } else {
            sb.append(day);
        }
        return DateTools.stringToDate(sb.toString(), "yyyy-MM-dd");
    }

    /**
     * 返回当前时间的下一天
     *
     * @param sysDate
     * @return
     */
    public static Date getNextDateYYYYMMDD(Date sysDate) {
        Calendar calendar = Calendar.getInstance();
        if (sysDate == null) {
            calendar.setTime(new Date());
        } else {
            calendar.setTime(sysDate);
        }
        return getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE) + 1);
    }

    /**
     * 功能描述：拼接日期以及时间
     *
     * @param date
     * @param time
     * @return
     * @author wangpeng
     * @lastModified wangpeng 2015年9月21日 下午2:03:11
     */
    public static Date appendDateAndTime(String date, String time) {
        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuffer ssBuffer = new StringBuffer();
        if (StringTools.isNotBlank(date) && StringTools.isNotBlank(time)) {
            ssBuffer.append(date.trim()).append(" " + time.trim());
        }
        Date dd = null;
        try {
            if (StringTools.isNotBlank(ssBuffer.toString())) {
                dd = ss.parse(ssBuffer.toString());
            }
        } catch (ParseException e) {
            try {
                throw new Exception(e.getMessage(), e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return dd;
    }

    public static Timestamp getCurrentDateTime() {
        long timeInMillis = getCurrentCalendar().getTimeInMillis();
        return new Timestamp(timeInMillis);
    }

    /**
     * return current date
     *
     * @return current date
     */
    public static Date getCurrentDate() {
        return getCurrentCalendar().getTime();
    }


    public static Calendar getCurrentCalendar() {
        Calendar instance = Calendar.getInstance();
        return instance;
    }

    /**
     * 把日期后的时间归0 变成(yyyy-MM-dd 00:00:00:000)
     *
     * @param fullDate Date
     * @return Date
     */
    public static final Date zerolizedTime(Date fullDate) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(fullDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 把日期的时间变成(yyyy-MM-dd 23:59:59:999)
     *
     * @param date
     * @return
     */
    public static final Date getEndTime(Date date) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
//        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 把日期后的时间归0 变成(yyyy-MM-dd 00:00:00:000)
     *
     * @param fullDate Date
     * @return Date
     */
    public static final Timestamp zerolizedTime(Timestamp fullDate) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(fullDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 把日期的时间变成(yyyy-MM-dd 23:59:59:999)
     *
     * @param date
     * @return
     */
    public static final Timestamp getEndTime(Timestamp date) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        timestamp.setNanos(0);
        return timestamp;
    }

    /**
     * 把日期的时间变成(yyyy-MM-dd 23:59:59:999)
     *
     * @param date
     * @return
     */
    public static final Timestamp getEndDate(Date date) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        timestamp.setNanos(0);
        return timestamp;
    }

    /**
     * 计算两个日期间隔的秒数
     *
     * @param firstDate 小者
     * @param lastDate  大者
     * @return int 默认-1
     */
    public static int getTimeIntervalSencond(Date firstDate, Date lastDate) {
        if (null == firstDate || null == lastDate) {
            return -1;
        }
        long intervalMilli = lastDate.getTime() - firstDate.getTime();
        return (int) (intervalMilli / 1000);
    }

    /**
     * 计算当前时间到 午夜 23.59.59.999 的秒数
     *
     * @return
     */
    public static int calculateCurrentToEndTime() {
        Date current = getCurrentDate();
        Date end = getEndTime(getCurrentDate());
        int timeIntervalSencond = getTimeIntervalSencond(current, end);
        return timeIntervalSencond;
    }

    /**
     * 倒计时转换为时分秒格式
     *
     * @param time
     * @param format
     * @return
     */
    public static String secToTime(Integer time, String format) {
        Integer hour = 0, minute = 0, second = 0;
        if (time > 0) {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
            }
        }
        String timeStr = format.replaceAll("H", hour.toString());
        timeStr = timeStr.replaceAll("M", minute.toString());
        timeStr = timeStr.replaceAll("S", second.toString());
        return timeStr;
    }

    /**
     * 计算两个时间间隔的天数
     *
     * @param firstDate 小者
     * @param lastDate  大者
     * @return int 默认-1
     */
    public static int getIntervalDays(Date firstDate, Date lastDate) {
        if (null == firstDate || null == lastDate) {
            return -1;
        }

        long intervalMilli = lastDate.getTime() - firstDate.getTime();
        return (int) (intervalMilli / (24 * 60 * 60 * 1000));
    }

    /**
     * 计算两个日期间隔的天数，间隔只关心日期
     *
     * @param firstDate
     * @param lastDate
     * @return
     */
    public static int daysOfTwo(Date firstDate, Date lastDate) {
        return getIntervalDays(firstDate, lastDate);
    }

    /**
     * 计算两个日期间隔的小时数
     *
     * @param firstDate 小者
     * @param lastDate  大者
     * @return int 默认-1
     */
    public static int getTimeIntervalHours(Date firstDate, Date lastDate) {
        if (null == firstDate || null == lastDate) {
            return -1;
        }

        long intervalMilli = lastDate.getTime() - firstDate.getTime();
        return (int) (intervalMilli / (60 * 60 * 1000));
    }

    /**
     * 计算两个日期间隔的分钟数
     *
     * @param firstDate 小者
     * @param lastDate  大者
     * @return int 默认-1
     */
    public static int getTimeIntervalMins(Date firstDate, Date lastDate) {
        if (null == firstDate || null == lastDate) {
            return -1;
        }

        long intervalMilli = lastDate.getTime() - firstDate.getTime();
        return (int) (intervalMilli / (60 * 1000));
    }

    /**
     * 增加天数
     *
     * @param date
     * @param day
     * @return Date
     */
    public static Date addDate(Date date, int day) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + day);
        return calendar.getTime();
    }

    /**
     * 增加月数
     *
     * @param date
     * @param month 需要增加的月数，比如需要增加2个月，就传入2
     * @return
     */

    public static Date addMonth(Date date, int month) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (month != 0) {
            calendar.add(Calendar.MONTH, month);
        }
        return calendar.getTime();
    }

    /**
     * 增加小时
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date addHour(Date date, int hour) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);
        return calendar.getTime();
    }

    /**
     * @param date
     * @param
     * @return
     * @Description:增加秒数
     * @author: wangyanji
     * @time:2015年12月24日 下午5:14:48
     */
    public static Date addSecond(Date date, int seconds) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    public static Timestamp addSecond(Timestamp date, int seconds) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
        timestamp.setNanos(0);
        return timestamp;
    }

    /**
     * change date to string 将日期类型的参数转成String类型
     *
     * @param dt a date
     * @return the format string
     */
    public static String toString(Date dt) {
        return toString(dt, DATE_FMT_0);
    }

    /**
     * change date object to string 将String类型的日期转成Date类型
     *
     * @param dt   date object
     * @param sFmt the date format
     * @return the formatted string
     */
    public static String toString(Date dt, String sFmt) {
        if (null == dt || StringTools.isBlank(sFmt)) {
            return null;
        }

        SimpleDateFormat sdfFrom = null;
        String sRet = null;
        try {
            sdfFrom = new SimpleDateFormat(sFmt);
            sRet = sdfFrom.format(dt).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            sdfFrom = null;
        }

        return sRet;
    }

    /**
     * 字串转为日期
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date getDateFromString(String dateStr, String pattern) {
        if ((pattern == null) || ("".equals(pattern))) {
            pattern = "yyyy-MM-dd";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            date = null;
        }
        return date;
    }

    /**
     * 计算两个日期的月份
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getMonthsBetweenDates(Date startDate, Date endDate) {
        if (startDate.getTime() > endDate.getTime()) {
            Date temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        int yearDiff = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int monthsBetween = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH) + 12 * yearDiff;

        if (endCalendar.get(Calendar.DAY_OF_MONTH) >= startCalendar.get(Calendar.DAY_OF_MONTH))
            monthsBetween = monthsBetween + 1;
        return monthsBetween;

    }

    /**
     * format the date in given pattern 格式化日期
     *
     * @param d       date
     * @param pattern time pattern
     * @return the formatted string
     */
    public static String formatDatetoString(Date d, String pattern) {
        if (null == d || StringTools.isBlank(pattern)) {
            return null;
        }

        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance();

        formatter.applyPattern(pattern);
        return formatter.format(d);
    }


    /**
     * 通过身份证获取生日
     *
     * @param identity
     * @return
     */
    public static Date getBirthdayByIdentity(String identity) {
        if (StringTools.isBlank(identity)) {
            return null;
        }
        Date birthday = null;
        // 18位的身份证取生日
        if (identity.length() == 18) {
            birthday = getDateFromString(identity.substring(6, 14), DATE_FMT_0);
        }
        // 15位的身份证取生日
        if (identity.length() == 15) {
            birthday = getDateFromString("19" + identity.substring(6, 12), DATE_FMT_0);
        }
        return birthday;

    }

    /**
     * 增加天数
     *
     * @param date
     * @param day
     * @return Date
     */
    public static Timestamp addDate(Timestamp date, int day) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + day);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 增加天数 包括当天
     *
     * @param date
     * @param day
     * @return Date
     */
    public static Timestamp addDateIncudeDay(Timestamp date, int day) {
        return addDate(date, day - 1);
    }

    /**
     * 比较两个时间是否同一天
     *
     * @param begin
     * @param end
     * @return
     */
    public static boolean isTheSameDay(Date begin, Date end) {
        String beginDate = formatDatetoString(begin, "yyyy-MM-dd");
        String endDate = formatDatetoString(end, "yyyy-MM-dd");
        return beginDate.equals(endDate);
    }

    /**
     * 比较两个时间大小
     *
     * @param time1
     * @param time2
     * @return 1:第一个比第二个大；0：第一个与第二个相同；-1：第一个比第二个小
     */
    public static int compareTwoTime(Timestamp time1, Timestamp time2) {

        if (time1.getTime() > time2.getTime()) {
            return 1;
        } else if (time1.getTime() == time2.getTime()) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 根据某个时间判断，到当前时间的倒计时.例：-8天13:55:58或10天10:04:01
     *
     * @param time
     * @param format
     */
    public static String getCountDown(Timestamp time, String format) {
        if (null != time) {
            long seconds = (long) (time.getTime() - System.currentTimeMillis()) / 1000;
            String sign = seconds > 0 ? "" : "-";
            Long day = 0L, hour = 0L, minute = 0L, second = 0L;
            long seconds2 = Math.abs(seconds);
            day = seconds2 / (24 * 3600);

            seconds2 = seconds2 - day * 24 * 3600;
            minute = seconds2 / 60;
            if (minute < 60) {
                second = seconds2 % 60;
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = seconds2 - hour * 3600 - minute * 60;
            }
            String timeStr = format.replaceAll("hh", hour.toString()
                    .length() == 1 ? "0" + hour.toString() : hour.toString());
            timeStr = timeStr.replaceAll("mm", minute.toString()
                    .length() == 1 ? "0" + minute.toString() : minute.toString());
            timeStr = timeStr.replaceAll("ss", second.toString()
                    .length() == 1 ? "0" + second.toString() : second.toString());

            return sign + day + "天" + timeStr;
        } else {
            return "";
        }

    }

    public static Timestamp getLastSecondOfDate(Timestamp time) {
        time.setTime(time.getTime() + 24 * 60 * 60 * 1000 - 1000);
        return time;
    }

    /**
     * 比较time1,time2两个时间相差的秒数，如果time1<=time2,返回0
     *
     * @param time1
     * @param time2
     */
    public static long getTwoTimeDiffSecond(Timestamp time1, Timestamp time2) {
        long diffSecond = (time1.getTime() - time2.getTime()) / 1000;
        if (diffSecond > 0) {
            return diffSecond;
        } else {
            return 0;
        }
    }


    /**
     * 判断是否在X天内（当天算一天）  注：当天12点15分 到 23：59：59算一天，到次日00：00：00算第二天
     *
     * @param startDay
     * @param daysLimit
     * @return
     */
    public static boolean isDayNumInLimit(Date startDay, int daysLimit) {
        Date endDate = zerolizedTime(addDate(startDay, daysLimit));
        return startDay.before(endDate);
    }

    /**
     * 判断是否在时间范围内
     *
     * @param startDay
     * @param endDay
     * @return
     */
    public static boolean isDayInRange(Date startDay, Date endDay) {
        Date now = getCurrentDate();
        if (startDay == null) {
            return now.before(endDay);
        } else if (endDay == null) {
            return now.after(startDay);
        }
        return now.after(startDay) && now.before(endDay);
    }

    /**
     * 格式化日期
     *
     * @param str yyyyMMddHHmmss
     * @return
     */
    public static Date formatDate(String str) {
        SimpleDateFormat format1 = new SimpleDateFormat(DateTools.TIME_PATTERN_SESSION);
        SimpleDateFormat format2 = new SimpleDateFormat(DateTools.TIME_PATTERN);
        try {
            String temp = format2.format(format1.parse(str));
            return DateTools.stringToDate(temp, DateTools.TIME_PATTERN);
        } catch (ParseException e) {
            return getCurrentDate();
        }
    }

    public static String dateToString(String dateStr, String format) {
        Date d = formatDate(dateStr);
        return dateToString(d, format);
    }

    public static String dateToString(Long dateStr, String format) {
        String temp = String.valueOf(dateStr);
        return dateToString(temp, format);
    }

    public static String timestampSubstring(String timestampStr) {
        if (StringTools.isBlank(timestampStr)) {
            return null;
        }
        return timestampStr.substring(0, 19);
    }
}