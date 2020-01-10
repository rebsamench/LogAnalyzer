package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.properties.ImportFileConst;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LogRecordTest {

    private LogRecord logRecord;
    private Site site;
    private Busline busline;
    private static final String TIMESTAMP = "13.11.2019 17:17:52";
    private static final int ADDRESS = 66;

    @Before
    public void setUp() {
        site = new Site();
        site.setId(42);
        busline = new Busline();
        busline.setId(24);
        logRecord = new LogRecord();
        logRecord.setSite(site);
        logRecord.setBusline(busline);
        logRecord.setTimestamp(DateUtil.getZonedDateTimeFromDateTimeString(TIMESTAMP, ImportFileConst.DATETIMEPATTERNIMPORT));
        logRecord.setMilliseconds(4000);
    }

    @Test
    public void setUniqueIdentifier() {
        logRecord.setUniqueIdentifier(ADDRESS);
        assertEquals("6666201911131717524000", logRecord.getUniqueIdentifier());
    }
}