package ch.zhaw.jv19.loganalyzer.util.db;

import ch.zhaw.jv19.loganalyzer.util.properties.PropertyHandler;

import java.sql.*;

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
            System.out.println("Driver " + propHandler.getValue("db.driver.class") + " not found.");
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

    public static int executeUpdate(String query) throws Exception {
        int affectedRows;
        dbConnect();
        PreparedStatement pstmt = con.prepareStatement(query);
        affectedRows = pstmt.executeUpdate();
        dbDisconnect();
        return affectedRows;
    }
}

