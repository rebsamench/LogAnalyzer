package ch.zhaw.jv19.loganalyzer.util.datatype;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.*;

public class DateUtilTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void getCurrentDateTimeString() {
    }

    @org.junit.Test
    public void convertDateTimeToStringValid() {
        ZonedDateTime inputDateTime = ZonedDateTime.of(2020,1,1,15,59,0,0, ZoneId.systemDefault());
        String expectedResultDateTime = "2020-01-01 15:59:00";
        assertEquals(expectedResultDateTime, DateUtil.convertDateTimeToString(inputDateTime, "yyyy-MM-dd HH:mm:ss"));
    }

    @org.junit.Test (expected = DateTimeException.class)
    public void convertDateTimeToStringInvalidMonth() {
        // month out of range
        ZonedDateTime inputDateTime = ZonedDateTime.of(2020,13,1,15,59,0,0, ZoneId.systemDefault());
        DateUtil.convertDateTimeToString(inputDateTime, "yyyy-MM-dd HH:mm:ss");
    }

    @org.junit.Test (expected = DateTimeException.class)
    public void convertDateTimeToStringInvalidHour() {
        // minutes out of range
        ZonedDateTime inputDateTime = ZonedDateTime.of(2020,1,1,15,60,0,0, ZoneId.systemDefault());
        DateUtil.convertDateTimeToString(inputDateTime, "yyyy-MM-dd HH:mm:ss");
    }

    @org.junit.Test
    public void getSystemTimezoneStartOfDayFromDate() {
        ZonedDateTime expectedResultZonedDateTime = ZonedDateTime.of(2020,1,1,0,0,0,0, ZoneId.systemDefault());
        LocalDate inputDate = LocalDate.of(2020,1,1);
        assertEquals(expectedResultZonedDateTime, DateUtil.getSystemTimezoneStartOfDayFromDate(inputDate));
    }

    @org.junit.Test
    public void getSystemTimezoneEndOfDayFromDate() {
        ZonedDateTime expectedResultZonedDateTime = ZonedDateTime.of(2020,1,1,23,59,59,0, ZoneId.systemDefault());
        LocalDate inputDate = LocalDate.of(2020,1,1);
        assertEquals(expectedResultZonedDateTime, DateUtil.getSystemTimezoneEndOfDayFromDate(inputDate));
    }

    @org.junit.Test
    public void getZonedDateTimeFromDateTimeString() {
        ZonedDateTime expectedResultZonedDateTime = ZonedDateTime.of(2020,1,1,0,1,59,0, ZoneId.systemDefault());
        String inputDate = "01.01.2020 00:01:59";
        assertEquals(expectedResultZonedDateTime, DateUtil.getZonedDateTimeFromDateTimeString(inputDate,"dd.MM.yyyy HH:mm:ss"));
    }
}