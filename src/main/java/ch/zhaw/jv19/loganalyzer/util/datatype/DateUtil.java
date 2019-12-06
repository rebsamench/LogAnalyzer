package ch.zhaw.jv19.loganalyzer.util.datatype;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    private static ZoneId systemZone = ZoneId.systemDefault();
    private DateUtil() {
    }

    public static String getCurrentDateTimeString(String pattern) {
        ZonedDateTime now = ZonedDateTime.now();
        return convertDateTimeToString(now, pattern);
    }

    public static String convertDateTimeToString(ZonedDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    public static String convertDateTimeToUtcString(ZonedDateTime dateTime, String pattern) {
        ZonedDateTime utcDateTime = ZonedDateTime.ofInstant(dateTime.toInstant(), ZoneId.of("UTC"));
        return convertDateTimeToString(utcDateTime,pattern);
    }

    public static ZonedDateTime convertDateTimeToUtc(ZonedDateTime dateTime) {
        return ZonedDateTime.ofInstant(dateTime.toInstant(), ZoneId.of("UTC"));
    }

    public static ZonedDateTime getUtcHighDate() {
        return ZonedDateTime.of(3000, 1, 01, 00, 00, 00, 0000, ZoneId.of("UTC"));
    }

    public static ZonedDateTime getUtcLowDate() {
        return ZonedDateTime.of(1900, 01, 01, 00, 00, 00, 0000, ZoneId.of("UTC"));
    }

    public static ZonedDateTime getUtcStartOfDayFromDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        return ZonedDateTime.of(startOfDay, systemZone);
    }

    public static ZonedDateTime getUtcEndOfDayFromDate(LocalDate date) {
        ZonedDateTime startOfToday = getUtcStartOfDayFromDate(date);
        ZonedDateTime startOfTomorrow =
                startOfToday.toLocalDate()
                        .plusDays(1)
                        .atStartOfDay()
                        .atZone(startOfToday.getZone())
                        .withEarlierOffsetAtOverlap();
        return startOfTomorrow.minusSeconds(1);
    }

    public static ZonedDateTime getZonedDateTimeFromDateString(String date, String inputDatePattern) {
        String validZonedDateTimePattern = inputDatePattern + "hh:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(validZonedDateTimePattern);
        return ZonedDateTime.parse(date + "00:00" + ZoneId.systemDefault(), formatter);
    }

}
