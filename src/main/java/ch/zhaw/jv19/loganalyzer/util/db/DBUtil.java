package ch.zhaw.jv19.loganalyzer.util.db;

import ch.zhaw.jv19.loganalyzer.util.properties.PropertyHandler;
import ch.zhaw.jv19.loganalyzer.util.datatype.StringUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Provides methods to interact with database.
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public class DBUtil {
    private static Connection con;
    private static final PropertyHandler propHandler = PropertyHandler.getInstance();

    /**
     * Gets connection from database defined in properties file.
     * @return database connection
     */
    public static Connection getConnection() throws SQLException {
        Connection con = null;
        try {
            Class.forName(propHandler.getValue("db.driver.class"));
            try {
                con = DriverManager.getConnection(propHandler.getValue("db.conn.url"),
                        propHandler.getValue("db.username"), propHandler.getValue("db.password"));
            } catch (SQLException e) {
                throw new SQLException("Failed to connect to " + propHandler.getValue("db.conn.url") + ": " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found.");
        }
        return con;
    }

    private static void dbConnect() throws Exception {
        con = getConnection();
    }

    public static void dbDisconnect() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int[] executeMultipleUpsertWithStatementTemplate(ArrayList<String[]> valueList, String statementTemplate) throws SQLException, Exception {
        int[] rowsAffected;
        dbConnect();
        int parameterCount = StringUtil.countCharacterOcurrenceInString(statementTemplate, '?');
        PreparedStatement pstmt = con.prepareStatement(statementTemplate);
        for (String[] valueSet : valueList) {
            if (valueSet.length == parameterCount) {
                for (int paramNo = 0; paramNo < valueSet.length; paramNo++) {
                    pstmt.setString(paramNo, valueSet[paramNo]);
                    pstmt.addBatch();
                }
            } else {
                System.out.println("Number of parameter placeholders (?) does not match number of values. Expected "
                        + parameterCount + ", given " + valueSet.length + " (" + valueSet.toString() + ").");
            }
        }
        return pstmt.executeBatch();
    }

    public static int executeUpdate(String query) throws SQLException, Exception {
        int affectedRows;
        dbConnect();
        PreparedStatement pstmt = con.prepareStatement(query);
        affectedRows = pstmt.executeUpdate();
        dbDisconnect();
        return affectedRows;
    }

    public static void executeMultipleUpdate(String query) throws Exception {
        ArrayList<String> queryList = new ArrayList<>(Arrays.asList(query.split("(?<=;)")));
        int[] affectedRows;
        if (queryList.size() > 1) {
            dbExecuteBatchUpdate(queryList);
        } else {
            executeUpdate(queryList.get(0));
        }
    }

    private static void dbExecuteBatchUpdate(ArrayList<String> queryList) throws Exception {
        Statement stmt = null;
        try {
            dbConnect();
            stmt = con.createStatement();
            long start = System.currentTimeMillis();
            for (int i = 0; i < queryList.size(); i++) {
                stmt.addBatch(queryList.get(i));
                //execute and commit batch of 1000 queries
                if (i % 1000 == 0) {
                    stmt.executeBatch();
                }
            }
            //commit remaining queries in the batch
            stmt.executeBatch();
            System.out.println("Time Taken=" + (System.currentTimeMillis() - start));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                dbDisconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

