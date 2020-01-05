package ch.zhaw.jv19.loganalyzer.util.datatype;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;
/**
 * Provides unit tests for date conversion methods.
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public class DateUtilTest {

    @org.junit.Test
    public void getCurrentDateTimeString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        assertEquals(LocalDateTime.now().format(formatter),DateUtil.getCurrentDateTimeString("yyyy-MM-dd HH:mm:ss"));
    }

    @org.junit.Test
    public void convertDateTimeToStringValid() {
        ZonedDateTime inputDateTime = ZonedDateTime.of(2020,1,1,15,59,0,0, ZoneId.systemDefault());
        String expectedResultDateTime = "2020-01-01 15:59:00";
        assertEquals(expectedResultDateTime, DateUtil.convertDateTimeToString(inputDateTime, "yyyy-MM-dd HH:mm:ss"));
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