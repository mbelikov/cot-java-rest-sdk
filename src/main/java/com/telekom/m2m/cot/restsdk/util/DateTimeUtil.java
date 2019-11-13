package com.telekom.m2m.cot.restsdk.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;

/**
 * Created by Mikhail Belikov on 06.11.19.
 */
public class DateTimeUtil {
    /** Standard formatter using the representation yyyy-MM-dd'T'HH:mm:ss.SSSZZ */
    private static final DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

    /**
     * Converts the a date to string using the ISO8601 representation.
     * @see <a href="https://en.wikipedia.org/wiki/ISO_8601">https://en.wikipedia.org/wiki/ISO_8601</a>.
     * @param date date to stringify
     * @return string representation following the ISO8601.
     */
    public static String convertDateToString(Date date) {
        return fmt.print( new DateTime(date.getTime()) );
    }

    public static String urlfy(String src) {
        return src.replace("+", "%2B");
    }

    /**
     * Tries to convert a string to {@link Date}. The string should follow the ISO8601 standard.
     * @see <a href="https://en.wikipedia.org/wiki/ISO_8601">https://en.wikipedia.org/wiki/ISO_8601</a>.
     * If the convertion is possible a Date instance will be returned. Otherwise a {@link IllegalArgumentException}
     * will be thrown.
     * @param str some string
     * @return Date
     * @throws IllegalArgumentException if the input string doesn't follow the ISO8601.
     */
    public static Date convertStringToDate(String str) {
        return parseDateTime(str).toDate();
    }

    protected static DateTime parseDateTime(String str) {
        return fmt.parseDateTime(str);
    }
}
