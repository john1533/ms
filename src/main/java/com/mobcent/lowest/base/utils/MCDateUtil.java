package com.mobcent.lowest.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MCDateUtil {
    public static Date getTimestampByTimeString(String time) {
        try {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getGenericCurrentTime() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime());
    }

    public static String getFormatTime(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Long.valueOf(time));
    }

    public static String getFormatTimeExceptHourAndSecond(long time) {
        return new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(time));
    }

    public static String getFormatShortTime(long time) {
        return new SimpleDateFormat("MM-dd HH:mm").format(Long.valueOf(time));
    }

    public static String getFormatTimeByYear(long time) {
        String timeStr = "";
        if (Calendar.getInstance().get(1) == new Integer(new SimpleDateFormat("yyyy").format(Long.valueOf(time))).intValue()) {
            return new SimpleDateFormat("MM-dd HH:mm").format(Long.valueOf(time));
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(time));
    }

    public static String getFormatTimeToSecond(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(time));
    }

    public static long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String getFormatTimeByWord(MCResource resource, long time, String pattern) {
        Date createdAt = new Date(time);
        long l = new Date().getTime() - createdAt.getTime();
        long day = l / 86400000;
        long hour = (l / 3600000) - (24 * day);
        long min = ((l / 60000) - ((24 * day) * 60)) - (60 * hour);
        long s = (((l / 1000) - (((24 * day) * 60) * 60)) - ((60 * hour) * 60)) - (60 * min);
        if (day > 0) {
            return new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(time));
        }
        if (hour > 0) {
            return new StringBuilder(String.valueOf(hour)).append(resource.getString("mc_forum_hour_before")).toString();
        }
        if (min > 0) {
            return new StringBuilder(String.valueOf(min)).append(resource.getString("mc_forum_minute_before")).toString();
        }
        if (s < 0) {
            return new SimpleDateFormat(pattern).format(createdAt);
        }
        return new StringBuilder(String.valueOf(s)).append(resource.getString("mc_forum_mill_before")).toString();
    }

    public static String getFormatTimeByPM(long time) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(1);
        int month = cal.get(2) + 1;
        int day = cal.get(5);
        SimpleDateFormat simpleYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat simpleMonth = new SimpleDateFormat("MM");
        SimpleDateFormat simpleDay = new SimpleDateFormat("dd");
        int timeYear = new Integer(simpleYear.format(Long.valueOf(time))).intValue();
        int timeMonth = new Integer(simpleMonth.format(Long.valueOf(time))).intValue();
        int timeDay = new Integer(simpleDay.format(Long.valueOf(time))).intValue();
        String timeStr = "";
        if (year > timeYear) {
            return new SimpleDateFormat("yyyy-MM-dd ahh:mm").format(Long.valueOf(time));
        }
        if (month == timeMonth && day == timeDay) {
            return new SimpleDateFormat("ahh:mm").format(Long.valueOf(time));
        }
        return new SimpleDateFormat("MM-dd ahh:mm").format(Long.valueOf(time));
    }
}
