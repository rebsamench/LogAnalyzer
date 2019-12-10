package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.datatype.StringUtil;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

public class MySQLLogRecordReportDAO implements LogRecordReadDAO {
    private String currentQuery;
    private AppDataController controller;

    public MySQLLogRecordReportDAO(AppDataController controller) {
        this.controller = controller;
    }

    @Override
    public TableView<ObservableList> getLogRecordsTableByConditions(HashMap<String, Object> searchConditions) {
        TableView<ObservableList> tableView;
        buildLogRecordQuery(searchConditions);
        tableView = getQueryResultAsTable(currentQuery);
        return tableView;
    }

    @Override
    public String getCurrentQuery() {
        return currentQuery;
    }

    public static TableView<ObservableList> getQueryResultAsTable(String query) {
        ObservableList<ObservableList> rowList;
        ResultSet resultSet;
        TableView<ObservableList> resultTable = null;
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            resultSet = pstmt.executeQuery(query);
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
            DBUtil.dbDisconnect();
        }
        return resultTable;
    }

    /** Query Builder methods */

    /**
     * Builds query for log records which meet the given conditions
     *
     * @param searchConditions hash map of search conditions. key = db column,
     *                         value = String value, date or array list of multiple IN conditions (strings)
     */
    private void buildLogRecordQuery(HashMap<String, Object> searchConditions) {
        StringBuilder querySb = new StringBuilder();
        querySb.append("SELECT * FROM logrecord");
        if (searchConditions.size() > 0) {
            querySb.append(connectConditions(getConditionListFromMap(searchConditions)));
        }
        querySb.append(MySQLConst.ENDQUERY);
        currentQuery = querySb.toString();
    }

    private ArrayList<String> getConditionListFromMap(HashMap<String, Object> conditionsMap) {
        Iterator<HashMap.Entry<String, Object>> entries = conditionsMap.entrySet().iterator();
        ArrayList<String> conditionsList = new ArrayList<>();
        while (entries.hasNext()) {
            HashMap.Entry<String, Object> entry = entries.next();
            if (entry.getValue() != null) {
                StringBuilder conditionSb = new StringBuilder();
                conditionSb.append(mapKeyToTableColumn(entry.getKey()));
                switch (entry.getKey()) {
                    case "createdFrom":
                    case "loggedTimestampFrom":
                        conditionSb.append(MySQLConst.GT);
                        conditionSb.append(StringUtil.addQuotes.apply(convertToString(entry.getValue())));
                        break;
                    case "createdUpTo":
                    case "loggedTimestampUpTo":
                        conditionSb.append(MySQLConst.LT);
                        conditionSb.append(StringUtil.addQuotes.apply(convertToString(entry.getValue())));
                        break;
                    //list types -> IN conditions
                    case "createdUser":
                    case "site":
                    case "recordType":
                        if(entry.getValue() instanceof ArrayList) {
                            conditionSb.append(MySQLConst.IN);
                            conditionSb.append(convertToString((ArrayList<String>) entry.getValue()));
                        }
                        break;
                    case "message":
                        conditionSb.append(MySQLConst.LIKE);
                        String messageSubstring = entry.getValue().toString();
                        conditionSb.append(StringUtil.addQuotes.apply(StringUtil.addPercent.apply(messageSubstring)));
                        break;
                }
                conditionsList.add(conditionSb.toString());
            }
        }
        return conditionsList;
    }

    /**
     * Maps key to database column. Only needed for keys, which dont
     * represent any db column.
     *
     * @param key as String
     * @return key that represents a table column
     */
    private String mapKeyToTableColumn(String key) {
        switch (key) {
            case "createdFrom":
            case "createdUpTo": {
                return "created";
            }
            case "loggedTimestampFrom":
            case "loggedTimestampUpTo": {
                return "timestamp";
            }
            default:
                return key;
        }
    }

    private String convertToString(Object object) {
        String condition = null;
        if (object instanceof ZonedDateTime) {
            condition = DateUtil.convertDateTimeToString((ZonedDateTime) object, MySQLConst.DATETIMEPATTERN);
        } else if (object instanceof ArrayList) {
            ArrayList<String> inConditionList = (ArrayList<String>) object;
            if (inConditionList.size() > 0) {
                condition = getInConditions(inConditionList);
            }
        } else if (object instanceof String) {
            // Wrap in %
            condition = StringUtil.addQuotes.apply(StringUtil.addPercent.apply((String) object));
        }
        return condition;
    }

    private static String connectConditions(ArrayList<String> conditionList) {
        Iterator<String> it = conditionList.iterator();
        StringBuilder conditionSb = new StringBuilder();
        boolean isFirstCondition = true;
        while (it.hasNext()) {
            // replace first AND with WHERE
            if (isFirstCondition) {
                conditionSb.append(MySQLConst.WHERE);
                isFirstCondition = false;
            } else {
                conditionSb.append(MySQLConst.AND);
            }
            conditionSb.append(it.next());
        }
        return conditionSb.toString();
    }

    private String getInConditions(ArrayList<String> conditionList) {
        return StringUtil.addBrackets.apply(
                conditionList.stream()
                        .map(StringUtil.addQuotes)
                        .collect(Collectors.joining(MySQLConst.SEPARATOR)
                        )
        );
    }

}
