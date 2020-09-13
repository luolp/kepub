package com.keyue.utils;

import com.github.pagehelper.StringUtil;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    public static final String DB_DATE_FORMAT_STRING = "yyyyMMdd";

    public static final String FRONT_DATE_FORMAT_STRING = "yyyy-MM-dd";

    public static final String DATA_TIME_PATTERN_1 = "yyyy-MM-dd HH:mm:ss";

    public static final String DATA_TIME_PATTERN_2 = "yyyy-MM-dd HH:mm";

    public static final String DATA_TIME_PATTERN_3 = "yyyyMMDDhhmmss";

    public static final String DATA_TIME_PATTERN_4 = "yyyyMMDDhhmmss";

    public static final String DATA_TIME_PATTERN_5 = "yyyyMMddHHmmssSSS";

    public static final String DATA_TIME_PATTERN_6 = "yyyy年MM月dd日";

    public static final String DATA_TIME_PATTERN_7 = "yyyy年MM月dd日 ahh:mm:ss";

    public static final String DATA_TIME_PATTERN_8 = "yyyy-MM-dd HH:mm";

    public static final String YMD_HM_NUM = "yyyyMMddHHmm";

    public static final String UTC_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String TIME_BEGIN = " 00:00:00";

    public static final String TIME_MIDDLE = " 12:00:00";

    public static final String TIME_END = " 23:59:59";

    public static Date getStartDatetime(String startDate) {
        Date repayDatetime = DateUtil.strToDate(startDate + DateUtil.TIME_BEGIN,
            DateUtil.DATA_TIME_PATTERN_1);
        return repayDatetime;
    }

    public static Date getEndDatetime(String endDate) {
        Date repayDatetime = DateUtil.strToDate(endDate + DateUtil.TIME_END,
            DateUtil.DATA_TIME_PATTERN_1);
        return repayDatetime;
    }

    public static Date getRelativeDateOfSecond(Date startDate, int second) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(startDate);
            calendar.add(Calendar.SECOND, second);
            return calendar.getTime();
        } catch (Exception e) {
            return startDate;
        }
    }

    public static Date getRelativeDateOfMinute(Date startDate, int minute) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(startDate);
            calendar.add(Calendar.MINUTE, minute);
            return calendar.getTime();
        } catch (Exception e) {
            return startDate;
        }
    }

    public static Date getRelativeDateOfHour(Date startDate, Double hour) {
        Calendar calendar = Calendar.getInstance();
        try {
            Integer minute = (int) (hour * 60);
            calendar.setTime(startDate);
            calendar.add(Calendar.MINUTE, minute);
            return calendar.getTime();
        } catch (Exception e) {
            return startDate;
        }

    }

    public static Date getRelativeDateOfDays(Date startDate, int days) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(startDate);
            calendar.add(Calendar.SECOND, days * 3600 * 24);
            return calendar.getTime();
        } catch (Exception e) {
            return startDate;
        }
    }

    /**
     * Date按格式pattern转String
     * @param date
     * @param pattern
     * @return
     * @create: 2015-4-18 下午11:02:34 miyb
     * @history:
     */
    public static String dateToStr(Date date, String pattern) {
        String str = null;
        SimpleDateFormat formater = new SimpleDateFormat(pattern);
        try {
            str = formater.format(date);
        } catch (Exception e) {
        }
        return str;
    }

    /**
     * 获取当天开始时间
     * @return
     * @create: 2014-10-14 下午4:24:57 miyb
     * @history:
     */
    public static Date getTodayStart() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        return (Date) currentDate.getTime().clone();
    }

    /**
     * 获取当天结束时间
     * @return
     * @create: 2014-10-14 下午4:24:57 miyb
     * @history:
     */
    public static Date getTodayEnd() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return (Date) currentDate.getTime().clone();
    }

    /**
     * 相对参数today的明日起始时刻。比如今天是11日23点，明日起始时刻为12日0点0分0秒
     * @param today
     * @return
     * @create: 2015年11月16日 上午11:49:51 myb858
     * @history:
     */
    public static Date getTomorrowStart(Date today) {
        String str = dateToStr(today, FRONT_DATE_FORMAT_STRING);
        Date tommrow = getRelativeDateOfSecond(
            strToDate(str, FRONT_DATE_FORMAT_STRING), 24 * 3600);
        return tommrow;
    }

    /**
     * String 按格式pattern转Date
     * @param str
     * @param pattern
     * @return
     * @create: 2015-4-18 下午11:02:34 miyb
     * @history:
     */
    public static Date strToDate(String str, String pattern) {
        Date date = null;
        SimpleDateFormat formater = new SimpleDateFormat(pattern);
        try {
            date = formater.parse(str);
        } catch (Exception e) {
        }
        return date;
    }

    /**
     * 删除—
     * @param pattern
     * @return
     * @create: 2015年10月27日 下午7:59:41 myb858
     * @history:
     */
    public static String remove_(String strDate) {
        String string = null;
        try {
            string = strDate.replace("-", "");
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return string;
    }

    /**
     * 按格式获取当前时间
     * @param pattern
     * @return
     * @create: 2015-5-7 上午11:22:04 miyb
     * @history:
     */
    public static String getToday(String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
    }

    /**
     * 按格式获取时间
     * @param pattern
     * @return
     * @create: 2015-5-7 上午11:22:04 miyb
     * @history:
     */
    public static String getDate(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     *
     * @param date
     * @param addOneDay 是否加1天
     * @return
     * @create: 2015-5-7 上午11:25:23 miyb
     * @history:
     */
    public static Date getFrontDate(String date, boolean addOneDay) {
        Date returnDate = null;
        try {
            returnDate = new SimpleDateFormat(FRONT_DATE_FORMAT_STRING)
                .parse(date);
            if (addOneDay) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(returnDate);
                calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
                calendar.add(calendar.SECOND, -1);// 变成23：59：59
                returnDate = calendar.getTime(); // 这个时间就是日期往后推一天的结果
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return returnDate;
    }

    /**
     * 统计两个时间差，返回的是天数(即24小时算一天，少于24小时就为0，用这个的时候最好把小时、分钟等去掉)
     * @param beginStr 开始时间
     * @param endStr 结束时间
     * @param format 时间格式
     * @return
     */
    public static int daysBetween(String beginStr, String endStr,
            String format) {
        Date end = strToDate(endStr, format);
        Date begin = strToDate(beginStr, format);
        long times = end.getTime() - begin.getTime();
        return (int) (times / 60 / 60 / 1000 / 24);
    }

    /**
     * 统计两个时间差，返回的是天数(即24小时算一天，少于24小时就为0，用这个的时候最好把小时、分钟等去掉)
     * @param beginDate
     * @param endDate
     * @return
     * @create: 2015年11月16日 上午11:20:51 myb858
     * @history:
     */
    public static int daysBetween(Date beginDate, Date endDate) {
        long times = endDate.getTime() - beginDate.getTime();
        return (int) (times / 60 / 60 / 1000 / 24);
    }

    /**
     * 统计两个日期天数差
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return
     */
    public static int daysDiff(Date beginDate, Date endDate) {
        beginDate = strToDate(dateToStr(beginDate, DateUtil.FRONT_DATE_FORMAT_STRING),
                DateUtil.FRONT_DATE_FORMAT_STRING);
        endDate = strToDate(dateToStr(endDate, DateUtil.FRONT_DATE_FORMAT_STRING),
                DateUtil.FRONT_DATE_FORMAT_STRING);

        long times = endDate.getTime() - beginDate.getTime();
        return (int) (times / 60 / 60 / 1000 / 24);
    }

    /**
     * 统计两个时间差，返回年数
     * @param beginDate
     * @param endDate
     * @return
     * @create: Oct 9, 2018 9:49:02 PM silver
     * @history:
     */
    public static int yearsBetween(Date beginDate, Date endDate) {
        long times = endDate.getTime() - beginDate.getTime();
        return (int) (times / 60 / 60 / 1000 / 24 / 365) + 1;
    }

    public static Date getCurrentMonthFirstDay() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.DATE, 1);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        return (Date) currentDate.getTime().clone();
    }

    public static Date getCurrentMonthLastDay() {
        Date date = new Date();
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(date);
        do {
            currentDate.add(Calendar.DATE, 1);
        } while (currentDate.get(Calendar.DATE) != 1);
        currentDate.add(Calendar.DATE, -1);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return currentDate.getTime();
    }

    public static Date TimeStamp2Date(String timestampString, String formats) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats)
            .format(new Date(timestamp));
        return strToDate(date, formats);
    }

    // 获取本周的开始时间
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    // 获取本周的结束时间
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    // 获取当前日期是星期几(星期一为1)
    public static int getWeekOfDate(Date date) {
        int week = getWeekOfDateSunday(date);
        return ((week + 5) % 7) + 1;
    }

    // 获取当前日期是星期几(星期天为1)
    public static int getWeekOfDateSunday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    // 获取某个日期的开始时间
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    // 获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Date getHourStart() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        return (Date) currentDate.getTime().clone();
    }

    public static Boolean isNowBetween(Date startDate, Date endDate) {
        Boolean isBetween = false;
        Date now = new Date();

        endDate = getDayEndTime(endDate);

        if (startDate.before(now) && now.before(endDate)) {
            isBetween = true;
        }

        return isBetween;
    }

    public static Boolean isToday(String d) {
        Boolean isToday = false;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        String today = fmt.format(new Date()).toString();
        String date = fmt.format(strToDate(d, FRONT_DATE_FORMAT_STRING))
            .toString();

        if (date.equals(today)) {
            isToday = true;
        }

        return isToday;
    }

    /**
     * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm:ss"<br />
     * 如果获取失败，返回null
     * @return
     */
    public static String getUTCTimeStr() {
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance();
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        Date utc = new Date(cal.getTimeInMillis());
        return dateToStr(utc, UTC_TIME_FORMAT);
    }

    /**
     * 获取一周前的时间
     * @return
     */
    public static Date getAWeekAgo() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -6);
        return getDayStartTime(cal.getTime());
    }


    /**
     * 获取一月前的时间
     * @return
     */
    public static Date getAMonthAgo() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        cal.add(Calendar.DATE, 1);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取n月前的时间（当月1日0时0分0秒）
     * @param d
     * @param n
     * @return
     */
    public static Date getNMonthAgo(Date d, int n) {
        Calendar cal = Calendar.getInstance();
        if (null != d)
            cal.setTime(d);
        cal.add(Calendar.MONTH, -1 * n);
        cal.set(Calendar.DATE, 1);
        return getDayStartTime(cal.getTime());
    }


    /**
     * 获取两个时间之间的月份
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<String> getMonthList(Date beginDate, Date endDate){
        List<String> monthList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        Date cDate = beginDate;
        for(int i = 0; cDate.before(endDate); i++){
            String monthStr = dateToStr(cDate, "yyyy-MM");
            monthList.add(monthStr);
            cal.add(Calendar.MONTH, 1);
            cDate = cal.getTime();
        }
        return monthList;
    }

    /**
     * 获取两个时间之间的日期
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<String> getDateList(Date beginDate, Date endDate){
        List<String> monthList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date cDate = beginDate;
        for(int i = 0; !cDate.after(endDate); i++){
            String monthStr = dateToStr(cDate, "yyyy-MM-dd");
            monthList.add(monthStr);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            cDate = cal.getTime();
        }
        return monthList;
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getNowTime()
    {
        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateformat.format(date);
        return time;
    }

    public static String getDayStartStr(String ymd) {
        if(StringUtil.isNotEmpty(ymd)) {
            return ymd + " 00:00:00";
        }
        return null;
    }

    public static String getDayEndStr(String ymd) {
        if(StringUtil.isNotEmpty(ymd)) {
            return ymd + " 23:59:59";
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getWeekOfDate(new Date()));
        System.out.println(getWeekOfDateSunday(new Date()));

//        System.out.println(String.format(SysConstants.DO_ADD_USER_CN,
//            PhoneUtil.hideMobile("13454146415"), "123456"));
//        System.out.println(getAWeekAgo());
//        System.out.println(getAMonthAgo());
//        System.out.println(getTodayEnd());
//        System.out.println(getCurrentMonthFirstDay());
//        String endDate = "2018-06-06";
//        System.out.println(getNMonthAgo(new Date(), 11));
//        System.out.println(getNMonthAgo(strToDate(endDate, DateUtil.FRONT_DATE_FORMAT_STRING), -12));

//        String startDateStr = "2018-06-06";
//        Date startDate = strToDate(startDateStr, DateUtil.FRONT_DATE_FORMAT_STRING);
//        Date endDate = new Date();
//        System.out.println(getMonthList(startDate, endDate));

        Base64.Encoder encoder = Base64.getEncoder();

        try {
            System.out.println(new String(encoder.encode("罗".getBytes("UTF-8"))));
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        Base64.Decoder decoder = Base64.getDecoder();

        try {
            System.out.println(new String(decoder.decode("572X")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

//        try {
//            String s = new String("罗".getBytes("UTF-8"),"UTF-8");
//            System.out.println(s);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }


}
