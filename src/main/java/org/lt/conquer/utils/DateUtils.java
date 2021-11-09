package org.lt.conquer.utils;

import java.util.Date;

public class DateUtils
{
    private static Date CurrentDate;

    public static Date getTodayDate()
    {
        CurrentDate = new Date(System.currentTimeMillis());
        return CurrentDate;
    }

    public static Date getCurrentDate()
    {
        return CurrentDate;
    }

    public static String getCurrentDateString()
    {
        return getCurrentDate().toString();
    }

    public static String getCurrentDateString(String format)
    {
        return new java.text.SimpleDateFormat(format).format(getCurrentDate());
    }

    public static String toString(Date date, String format)
    {
        return new java.text.SimpleDateFormat(format).format(date);
    }
}
