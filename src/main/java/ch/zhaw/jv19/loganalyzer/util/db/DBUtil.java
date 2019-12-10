package ch.zhaw.jv19.loganalyzer.util.db;

import ch.zhaw.jv19.loganalyzer.util.datatype.StringUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DBUtil {
    private static String url = "jdbc:mysql://localhost:3306/belimo?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin";
    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static String username = "root";
    private static String password = "zhaw123*";
    private static Connection con;

    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                System.out.println("Datenbank " + url + " nicht erreichbar.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found.");
        }
        return con;
    }

    private static void dbConnect() {
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

    public static int[] executeMultipleUpsertWithStatementTemplate(ArrayList<String[]> valueList, String statementTemplate) throws SQLException {
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


    public static int executeUpdate(String query) throws SQLException {
        int affectedRows;
        dbConnect();
        PreparedStatement pstmt = con.prepareStatement(query);
        affectedRows = pstmt.executeUpdate();
        dbDisconnect();
        return affectedRows;
    }

    public static void executeMultipleUpdate(String query) throws SQLException {
        ArrayList<String> queryList = new ArrayList<>(Arrays.asList(query.split("(?<=;)")));
        int[] affectedRows;
        if (queryList.size() > 1) {
            dbExecuteBatchUpdate(queryList);
        } else {
            executeUpdate(queryList.get(0));
        }
    }

    private static void dbExecuteBatchUpdate(ArrayList<String> queryList) {
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
                stmt.close();
                dbDisconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

