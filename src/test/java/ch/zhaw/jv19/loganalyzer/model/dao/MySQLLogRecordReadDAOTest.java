package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.*;
import javafx.collections.FXCollections;
import org.junit.Before;
import org.junit.Test;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class MySQLLogRecordReadDAOTest {
    MySQLLogRecordReadDAO mySQLLogRecordReadDAO;
    User testUser;
    Site testSite;
    Busline testBusLine;
    EventType testEventType;
    Source testSource;

    @Before
    public void setUp() {
        testUser = new User("admin", "test", "password123", 0);
        testSite = new Site("admin", "Site 1", "test street 13", "8152", "Opfikon");
        testSite.setId(1);
        testBusLine = new Busline("admin", "Line 123", "KNX");
        testBusLine.setId(1);
        testEventType = EventType.get("Info");
        testSource = Source.get("Input");
        mySQLLogRecordReadDAO = new MySQLLogRecordReadDAO();
    }

    @Test
    public void getCurrentQuery() {
        mySQLLogRecordReadDAO.getCurrentQuery();
    }

    @Test
    public void buildLogRecordQuery() {
        StringBuilder expectedQuerySb = new StringBuilder();
        // order according to key names after mapping, see MySQLLogRecordDAO.mapKeyToTableColumn
        expectedQuerySb.append("SELECT * FROM logrecord ");
        expectedQuerySb.append("WHERE address = 55 ");
        expectedQuerySb.append("AND busLine IN ('1') ");
        expectedQuerySb.append("AND created >= '1900-01-01 00:00:00' ");
        expectedQuerySb.append("AND created <= '3000-01-01 00:00:00' ");
        expectedQuerySb.append("AND createdUser IN ('test') ");
        expectedQuerySb.append("AND type IN ('Info') ");
        expectedQuerySb.append("AND timestamp >= '1900-01-01 00:00:00' ");
        expectedQuerySb.append("AND timestamp <= '3000-01-01 00:00:00' ");
        expectedQuerySb.append("AND UPPER(message) LIKE '%TEST%' ");
        expectedQuerySb.append("AND site IN ('1') ");
        expectedQuerySb.append("AND source IN ('Input');");
        TreeMap<String, Object> conditionsMap = new TreeMap<>();
        conditionsMap.put("createdFrom", ZonedDateTime.of(1900,1,1,0,0,0,0, ZoneId.systemDefault()));
        conditionsMap.put("createdUpTo", ZonedDateTime.of(3000,1,1,0,0,0,0, ZoneId.systemDefault()));
        conditionsMap.put("loggedTimestampFrom", ZonedDateTime.of(1900,1,1,0,0,0,0, ZoneId.systemDefault()));
        conditionsMap.put("loggedTimestampUpTo", ZonedDateTime.of(3000,1,1,0,0,0,0, ZoneId.systemDefault()));
        conditionsMap.put("createdUser", FXCollections.observableArrayList(testUser));
        conditionsMap.put("eventType", FXCollections.observableArrayList(testEventType));
        conditionsMap.put("site", FXCollections.observableArrayList(testSite));
        conditionsMap.put("busLine", FXCollections.observableArrayList(testBusLine));
        conditionsMap.put("source", FXCollections.observableArrayList(testSource));
        conditionsMap.put("address", 55);
        conditionsMap.put("message", "tEsT");
        mySQLLogRecordReadDAO.buildLogRecordQuery(conditionsMap);
        assertEquals(expectedQuerySb.toString(),mySQLLogRecordReadDAO.getCurrentQuery());
    }
}