package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.*;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MySQLLogRecordWriteDAOTest {

    MySQLLogRecordWriteDAO mySQLLogRecordWriteDAO = new MySQLLogRecordWriteDAO();

    List<LogRecord> logRecordLIst;
    LogRecord logRecord1;
    LogRecord logRecord2;
    User testUser;
    Site testSite;
    Busline testBusline;

    @Before
    public void setUp() throws Exception{
        // String createdUser, String name, String password, int isadmin
        testUser = new User("Hans", "RuediTest", "password", 1);
        // String createdUser, String name, String street, String zipCode, String city
        testSite = new Site("RuediTest", "Leutschenbach", "Strasse", "8000", "xy");
        testSite.setId(1);
        // String createdUser, String name, String bustype
        testBusline = new Busline("RuediTest", "Line 1", "Testbus");
        testBusline.setId(1);
        // String timestamp, int milliseconds, String eventType, String source, String message, User user, Site site, Busline busline
        logRecord1 = new LogRecord("13.11.2019 17:17:51", 3000, "Info", "Input", "Output changed to 0", testUser, testSite, testBusline);
        logRecord1.setAddress(1);
        logRecord1.setUniqueIdentifier(logRecord1.getAddress());
        logRecord2 = new LogRecord("13.11.2019 17:18:08", 3001, "Info", "Input", "Output changed to 0", testUser, testSite, testBusline);
        logRecord2.setAddress(2);
        logRecord2.setUniqueIdentifier(logRecord2.getAddress());
        logRecordLIst = new ArrayList<>();
        logRecordLIst.add(logRecord1);
        logRecordLIst.add(logRecord2);
    }

    @Test
    public void insertLogRecords() throws Exception {
//        int[] result = mySQLLogRecordWriteDAO.insertLogRecords(logRecordLIst);
//        System.out.println(result);
//        System.out.println(Statement.SUCCESS_NO_INFO);
        assertEquals(-2 ,mySQLLogRecordWriteDAO.insertLogRecords(logRecordLIst));
    }

    @After
    public void deleteLogRecord() {
        try {
            Connection connection = DBUtil.getConnection();
            String deleteStatement = "delete from logrecord where createduser = 'RuediTest'";
            DBUtil.executeUpdate(deleteStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}