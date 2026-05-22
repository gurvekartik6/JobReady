package com.jobread.app.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final SimpleDateFormat sDisplayFormat =
            new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    private static final SimpleDateFormat sIsoFormat =
            new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static String formatDisplay(Date date) {
        if (date == null) return "";
        return sDisplayFormat.format(date);
    }

    public static String formatIso(Date date) {
        if (date == null) return "";
        return sIsoFormat.format(date);
    }

    public static boolean isInFuture(Date date) {
        if (date == null) return false;
        return date.after(new Date());
    }

    public static boolean isOlderThanDays(Date date, int days) {
        if (date == null) return false;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -days);
        return date.before(cal.getTime());
    }

    public static Date daysAgo(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -days);
        return cal.getTime();
    }

    public static Date startOfWeekOffset(int weeksAgo) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, -weeksAgo);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static Date endOfWeekOffset(int weeksAgo) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, -weeksAgo);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    public static String getWeekLabel(int weeksAgo) {
        if (weeksAgo == 0) return "This Week";
        if (weeksAgo == 1) return "Last Week";
        return weeksAgo + " Weeks Ago";
    }
}
