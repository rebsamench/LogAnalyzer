package ch.zhaw.jv19.loganalyzer.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LogFileTest {

    private LogFile logFile;
    private List<LogRecord> recordList;
    private LogRecord logRecord;

    @Before
    public void setUp() {
        logFile = new LogFile();
        recordList = new ArrayList<>();
        logRecord = new LogRecord();
        logFile.setAddress("initializing modbus with address: 128, baud: 38400, parity: n, stop bits: 2");
    }

    @Test
    public void addLogRecord() {
        recordList.add(logRecord);
        assertEquals(1, recordList.size());
    }

    @Test
    public void getRecordList() {
        assertNotNull("ok", logFile.getRecordList());
    }

    @Test
    public void getAddress() {
        assertEquals(128, logFile.getAddress());
    }

    @Test
    public void isAddressSet() {
        assertTrue(logFile.isAddressSet());
    }

    @Test
    public void setAddress() {
        logFile.setAddress("initializing modbus with address: 4, baud: 38400, parity: n, stop bits: 2");
        assertEquals(4, logFile.getAddress());
        logFile.setAddress("initializing modbus with address: 42, baud: 38400, parity: n, stop bits: 2");
        assertEquals(42, logFile.getAddress());
        logFile.setAddress("initializing modbus with address: 666, baud: 38400, parity: n, stop bits: 2");
        assertEquals(666, logFile.getAddress());
    }

}