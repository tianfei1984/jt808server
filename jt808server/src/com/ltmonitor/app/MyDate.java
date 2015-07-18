package com.ltmonitor.app;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Formats dates to sortable UTC strings in compliance with ISO-8601.
 * 
 * @author Adam Matan <adam@matan.name>
 * @see http://stackoverflow.com/questions/11294307/convert-java-date-to-utc-string/11294308
 */
public class MyDate {
    public static String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS zzz";
    public static String LEGACY_FORMAT = "EEE MMM dd hh:mm:ss zzz yyyy";
    private static final TimeZone utc = TimeZone.getTimeZone("UTC");
    private static final SimpleDateFormat legacyFormatter = new SimpleDateFormat(LEGACY_FORMAT);
    private static final SimpleDateFormat isoFormatter = new SimpleDateFormat(ISO_FORMAT);
    static {
        legacyFormatter.setTimeZone(utc);
        isoFormatter.setTimeZone(utc);
    }

    /**
     * Formats the current time in a sortable ISO-8601 UTC format.
     * 
     * @return Current time in ISO-8601 format, e.g. :
     *         "2012-07-03T07:59:09.206 UTC"
     */
    public static String now() {
        return MyDate.toString(new Date());
    }

    /**
     * Formats a given date in a sortable ISO-8601 UTC format.
     * 
     * <pre>
     * <code>
     * final Calendar moonLandingCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
     * moonLandingCalendar.set(1969, 7, 20, 20, 18, 0);
     * final Date moonLandingDate = moonLandingCalendar.getTime();
     * System.out.println("UTCDate.toString moon:       " + PrettyDate.toString(moonLandingDate));
     * >>> UTCDate.toString moon:       1969-08-20T20:18:00.209 UTC
     * </code>
     * </pre>
     * 
     * @param date
     *            Valid Date object.
     * @return The given date in ISO-8601 format.
     * 
     */

    public static String toString(final Date date) {
        return isoFormatter.format(date);
    }

    /**
     * Formats a given date in the standard Java Date.toString(), using UTC
     * instead of locale time zone.
     * 
     * <pre>
     * <code>
     * System.out.println(UTCDate.toLegacyString(new Date()));
     * >>> "Tue Jul 03 07:33:57 UTC 2012"
     * </code>
     * </pre>
     * 
     * @param date
     *            Valid Date object.
     * @return The given date in Legacy Date.toString() format, e.g.
     *         "Tue Jul 03 09:34:17 IDT 2012"
     */
    public static String toLegacyString(final Date date) {
        return legacyFormatter.format(date);
    }

    /**
     * Formats a date in any given format at UTC.
     * 
     * <pre>
     * <code>
     * final Calendar moonLandingCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
     * moonLandingCalendar.set(1969, 7, 20, 20, 17, 40);
     * final Date moonLandingDate = moonLandingCalendar.getTime();
     * PrettyDate.toString(moonLandingDate, "yyyy-MM-dd")
     * >>> "1969-08-20"
     * </code>
     * </pre>
     * 
     * 
     * @param date
     *            Valid Date object.
     * @param format
     *            String representation of the format, e.g. "yyyy-MM-dd"
     * @return The given date formatted in the given format.
     */
    public static String toString(final Date date, final String format) {
        return toString(date, format, "UTC");
    }

    /**
     * Formats a date at any given format String, at any given Timezone String.
     * 
     * 
     * @param date
     *            Valid Date object
     * @param format
     *            String representation of the format, e.g. "yyyy-MM-dd HH:mm"
     * @param timezone
     *            String representation of the time zone, e.g. "CST"
     * @return The formatted date in the given time zone.
     */
    public static String toString(final Date date, final String format, final String timezone) {
        final TimeZone tz = TimeZone.getTimeZone(timezone);
        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(tz);
        return formatter.format(date);
    }
}
