package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.properties.ImportFileConst;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.junit.Assert.*;

public class LogRecordTest {

    LogRecord logRecord;
    Site site;
    Busline busline;
    String timestamp = "13.11.2019 17:17:52";
    int address = 66;

    @Before
    public void setUp() throws Exception {
        site = new Site();
        site.setId(42);
        busline = new Busline();
        busline.setId(24);
        logRecord = new LogRecord();
        logRecord.setSite(site);
        logRecord.setBusline(busline);
        logRecord.setTimestamp(DateUtil.getZonedDateTimeFromDateTimeString(timestamp, ImportFileConst.DATETIMEPATTERNIMPORT));
        logRecord.setMilliseconds(4000);
    }

    @Test
    public void setUniqueIdentifier() {
        logRecord.setUniqueIdentifier(address);
        assertEquals("6666201911131717524000", logRecord.getUniqueIdentifier());
    }
}