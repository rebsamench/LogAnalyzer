package ch.zhaw.jv19.loganalyzer.util.datatype;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides date conversion methods.
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public final class DateUtil {
    private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();
    private DateUtil() {
    }

    /**
     * Gets current date and time with given pattern.
     * @param pattern pattern to format result
     * @return current date time as formatted string
     */
    public static String getCurrentDateTimeString(String pattern) {
        ZonedDateTime now = ZonedDateTime.now();
        return convertDateTimeToString(now, pattern);
    }

    /**
     * Converts given ZonedDateTime to string with given pattern.
     * @param pattern pattern to format result
     * @return converted ZonedDateTime time as formatted string
     */
    public static String convertDateTimeToString(ZonedDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    /**
     * Gets start of day (e. g. 22.12.2019 00:00:00)  of given LocalDate
     * @param date LocalDate
     * @return ZonedDateTime start of day as formatted string
     */
    public static ZonedDateTime getSystemTimezoneStartOfDayFromDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        return ZonedDateTime.of(startOfDay, SYSTEM_ZONE);
    }

    /**
     * Gets end of day (e. g. 22.12.2019 23:59:59) of given LocalDate
     * @param date LocalDate
     * @return ZonedDateTime end of day as formatted string
     */
    public static ZonedDateTime getSystemTimezoneEndOfDayFromDate(LocalDate date) {
        ZonedDateTime startOfToday = getSystemTimezoneStartOfDayFromDate(date);
        ZonedDateTime startOfTomorrow =
                startOfToday.toLocalDate()
                        .plusDays(1)
                        .atStartOfDay()
                        .atZone(startOfToday.getZone())
                        .withEarlierOffsetAtOverlap();
        return startOfTomorrow.minusSeconds(1);
    }

    /**
     * Gets ZonedDateTime from String with given inputDateTimePattern
     * @param dateTime String representing date and time
     * @return ZonedDateTime with system time zone
     */
    public static ZonedDateTime getZonedDateTimeFromDateTimeString(String dateTime, String inputDateTimePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(inputDateTimePattern);
        return LocalDateTime.parse(dateTime, formatter).atZone(SYSTEM_ZONE);
    }
}
