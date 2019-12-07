package ch.zhaw.jv19.loganalyzer.util.db;

import ch.zhaw.jv19.loganalyzer.util.datatype.StringUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

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

    private static void dbDisconnect() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int[] executeUpdateWithPreparedStatement(ArrayList<String[]> conditionValueList, String preparedStatementTemplate) throws SQLException {
        int[] rowsAffected;
        dbConnect();
        int parameterCount = StringUtil.countCharacterOcurrenceInString(preparedStatementTemplate, '?');
        PreparedStatement pstmt = con.prepareStatement(preparedStatementTemplate);
        for (String[] conditionList : conditionValueList) {
            if (conditionList.length == parameterCount) {
                for (int paramNo = 0; paramNo < conditionList.length; paramNo++) {
                    pstmt.setString(paramNo, conditionList[paramNo]);
                    pstmt.addBatch();
                }
            } else {
                System.out.println("Number of parameter placeholders (?) does not match condition number. Expected "
                        + parameterCount + ", given " + conditionList.length + " (" + conditionList.toString() + ").");
            }
        }
        return pstmt.executeBatch();
    }

    public static void executeUpdate(String query) {
        ArrayList<String> queryList = new ArrayList<>(Arrays.asList(query.split("(?<=;)")));
        int[] affectedRows;
        if (queryList.size() > 1) {
            dbExecuteBatchUpdate(queryList);
        } else {
            dbExecuteQuery(queryList.get(0));
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

    public static TableView<ObservableList> getQueryResultAsTable(String query) {
        ObservableList<ObservableList> rowList;
        ResultSet resultSet;
        TableView<ObservableList> resultTable = null;
        try {
            dbConnect();
            Statement statement = con.createStatement();
            PreparedStatement pstmt = con.prepareStatement(query);
            resultSet = statement.executeQuery(query);
            rowList = FXCollections.observableArrayList();
            resultTable = new TableView();
            //check if any row is in resultset, https://stackoverflow.com/questions/867194/java-resultset-how-to-check-if-there-are-any-results/6813771#6813771
            if (resultSet.isBeforeFirst()) {
                for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                    final int j = i;
                    TableColumn<ObservableList, String> col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                    resultTable.getColumns().addAll(col);
                    //System.out.println("Column [" + i + "] ");
                }
                while (resultSet.next()) {
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        //Iterate Column
                        row.add(resultSet.getString(i));
                    }
                    System.out.println("Row [1] added " + row);
                    rowList.add(row);
                }
                resultTable.setItems(rowList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbDisconnect();
        }
        return resultTable;
    }

    public static ResultSet dbExecuteQuery(String query) {
        ResultSet resultSet;
        try {
            dbConnect();
            PreparedStatement pstmt = con.prepareStatement(query);
            resultSet = pstmt.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbDisconnect();
        }
        return null;
    }

    private static int dbExecuteUpdate(String query) throws SQLException {
        int affectedRows;
        dbConnect();
        PreparedStatement pstmt = con.prepareStatement(query);
        affectedRows = pstmt.executeUpdate();
        dbDisconnect();
        return affectedRows;
    }
}

