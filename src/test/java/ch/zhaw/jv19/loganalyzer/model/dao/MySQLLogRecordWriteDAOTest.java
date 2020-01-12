package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.*;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MySQLLogRecordWriteDAOTest {

    private final MySQLLogRecordWriteDAO mySQLLogRecordWriteDAO = new MySQLLogRecordWriteDAO();

    private List<LogRecord> logRecordList;
    private LogRecord logRecord1;
    private LogRecord logRecord2;
    private User testUser;
    private Site testSite;
    private BusLine testBusLine;

    @Before
    public void setUp() {
        // String createdUser, String name, String password, int isAdmin
        testUser = new User("admin", "admin", "password", 1);
        // String createdUser, String name, String street, String zipCode, String city
        testSite = new Site("admin", "Leutschenbach", "Strasse", "8000", "xy");
        testSite.setId(1);
        // String createdUser, String name, String bustype
        testBusLine = new BusLine("admin", "Line 1", "Testbus");
        testBusLine.setId(1);
        // String timestamp, int milliseconds, String eventType, String source, String message, User user, Site site, BusLine busLine
        logRecord1 = new LogRecord("01.01.1900 00:00:01", 3000, "Info", "Input", "Output changed to 0", testUser, testSite, testBusLine);
        logRecord1.setAddress(1);
        logRecord1.setUniqueIdentifier(logRecord1.getAddress());
        logRecord2 = new LogRecord("01.01.1900 00:00:02", 3001, "Info", "Input", "Output changed to 0", testUser, testSite, testBusLine);
        logRecord2.setAddress(2);
        logRecord2.setUniqueIdentifier(logRecord2.getAddress());
        logRecordList = new ArrayList<>();
        logRecordList.add(logRecord1);
        logRecordList.add(logRecord2);
    }

    @Test
    public void insertLogRecords() throws Exception {
        int[] insertedRowsList = mySQLLogRecordWriteDAO.insertLogRecords(logRecordList);
        int insertedRows = Arrays.stream(insertedRowsList).sum();
        assertEquals(2 , insertedRows);
    }

    @After
    public void deleteLogRecord() {
        try {
            String deleteStatement = "DELETE FROM logrecord WHERE unique_identifier IN (" +
                    logRecord1.getUniqueIdentifier() +
                    MySQLConst.SEPARATOR +
                    logRecord2.getUniqueIdentifier() + ");";
            DBUtil.executeUpdate(deleteStatement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}