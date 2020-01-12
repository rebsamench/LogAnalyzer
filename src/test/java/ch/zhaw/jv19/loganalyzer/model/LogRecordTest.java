package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.properties.ImportFileConst;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LogRecordTest {
    /**
     * Provides unit tests for LogRecord.
     * @author Christoph Rebsamen, rebsach1@students.zhaw.ch
     */
    private LogRecord logRecord;
    private Site site;
    private BusLine busLine;
    private static final String TIMESTAMP = "13.11.2019 17:17:52";
    private static final int ADDRESS = 66;

    @Before
    public void setUp() {
        site = new Site();
        site.setId(42);
        busLine = new BusLine();
        busLine.setId(24);
        logRecord = new LogRecord();
        logRecord.setSite(site);
        logRecord.setBusLine(busLine);
        logRecord.setTimestamp(DateUtil.getZonedDateTimeFromDateTimeString(TIMESTAMP, ImportFileConst.DATETIMEPATTERNIMPORT));
        logRecord.setMilliseconds(4000);
    }

    @Test
    public void setUniqueIdentifier() {
        logRecord.setUniqueIdentifier(ADDRESS);
        assertEquals("6666201911131717524000", logRecord.getUniqueIdentifier());
    }
}