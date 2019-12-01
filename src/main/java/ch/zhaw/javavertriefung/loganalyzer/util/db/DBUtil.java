package ch.zhaw.javavertriefung.loganalyzer.util.db;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class DBUtil {
    private static String url = "jdbc:mysql://localhost:3306/ek?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin";
    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static String username = "root";
    private static String password = "zhaw123*";
    private static Connection con;

    public static void setUpDb() throws Exception {
        dbExecuteSingleQuery("SET GLOBAL time_zone = '+0:00';");
    }

    public static void dbConnect() {
        //setUpDb();
        try {
            Class.forName(driverName);
            try {

                con = DriverManager.getConnection(url, username, password);
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Datenbank " + url + " nicht erreichbar.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found.");
        }
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

    public static ResultSet dbExecuteSingleQuery(String query) throws Exception {
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet cachedRowSet = factory.createCachedRowSet();
        try {
            dbConnect();
            cachedRowSet.setUrl(url);
            cachedRowSet.setUsername(username);
            cachedRowSet.setPassword(password);
            cachedRowSet.setCommand(query);
            cachedRowSet.execute();
        } catch(SQLSyntaxErrorException e) {
            throw new Exception("Syntaxfehler in " + query);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        }
        finally {
            dbDisconnect();
        }
        return cachedRowSet;
    }
}

