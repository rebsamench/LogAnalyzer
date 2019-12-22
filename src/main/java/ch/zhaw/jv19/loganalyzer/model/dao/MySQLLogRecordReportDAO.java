package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import ch.zhaw.jv19.loganalyzer.model.LogRecord;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.datatype.StringUtil;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

public class MySQLLogRecordReportDAO implements LogRecordReadDAO {
    private String currentQuery;
    private final AppDataController controller;

    public MySQLLogRecordReportDAO(AppDataController controller) {
        this.controller = controller;
    }

    @Override
    public ArrayList<LogRecord> getLogRecordsListByConditions(HashMap<String, Object> searchConditions) {
        buildLogRecordQuery(searchConditions);
        return getLogRecordList(currentQuery);
    }

    @Override
    public String getCurrentQuery() {
        return currentQuery;
    }

    /**
     * Gets a list of log records from data base
     * @param query SELECT statement
     * @return ArrayList of LogRecords
     */
    private ArrayList<LogRecord> getLogRecordList(String query) {
        ArrayList<LogRecord> resultList = new ArrayList<>();
        ResultSet resultSet;
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            resultSet = pstmt.executeQuery(query);
            while (resultSet.next()) {
                LogRecord logRecord = extractLogRecordFromResultSet(resultSet);
                resultList.add(logRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect();
        }
        return resultList;
    }

    /* Query Builder methods */

    /**
    * Builds query for log records which meet the given conditions.
     * Assembled query is set as current query of this DAO.
     * @param conditionsMap hash map of search conditions. key = db column,
     *                         value = String value, date or array list of multiple IN conditions (strings)
     */
    private void buildLogRecordQuery(HashMap<String, Object> conditionsMap) {
        StringBuilder querySb = new StringBuilder();
        querySb.append("SELECT * FROM logrecord");
        if (conditionsMap != null && conditionsMap.size() > 0) {
            querySb.append(connectConditions(getConditionListFromMap(conditionsMap)));
        }
        querySb.append(MySQLConst.ENDQUERY);
        currentQuery = querySb.toString();
    }

    /**
     * Creates a list of conditions
     * @param conditionsMap hash map of search conditions. key = db column,
     *                         value = String value, date or array list of multiple IN conditions (strings)
     * @return list of conditions as strings
     */
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
                            conditionSb.append(convertToString(entry.getValue()));
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

    /**
     * Converts input to string based on input type
     * @param object object to convert to string
     * @return Condition as String.
     */
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

    /**
     * Connects Conditions of conditionList with WHERE or AND.
     * @param conditionList list of conditions to connect
     * @return String of conditions (WHERE clauses) connected with WHERE and AND
     */
    private String connectConditions(ArrayList<String> conditionList) {
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

    /**
     * Builds and IN-condition set from a list.
     * @param conditionList list of condition values
     * @return String
     */
    private String getInConditions(ArrayList<String> conditionList) {
        return StringUtil.addBrackets.apply(
                conditionList.stream()
                        .map(StringUtil.addQuotes)
                        .collect(Collectors.joining(MySQLConst.SEPARATOR)
                        )
        );
    }

    /**
     * Extracts log record from Resultset.
     * @param rs result set
     * @return user
     * @throws SQLException database exception
     */
    private LogRecord extractLogRecordFromResultSet(ResultSet rs) throws SQLException {
        LogRecord logRecord = new LogRecord();
        logRecord.setId(rs.getInt("id"));
        logRecord.setCreated(DateUtil.getZonedDateTimeFromDateTimeString(rs.getDate("created") + " " + rs.getTime("created"), MySQLConst.DATETIMEPATTERN));
        logRecord.setLastChanged(DateUtil.getZonedDateTimeFromDateTimeString(rs.getDate("created") + " " + rs.getTime("created"), MySQLConst.DATETIMEPATTERN));
        logRecord.setUser(controller.getUserByName(rs.getString("createduser")));
        logRecord.setUniqueIdentifier(rs.getString("unique_identifier"));
        logRecord.setTimestamp(DateUtil.getZonedDateTimeFromDateTimeString(rs.getDate("timestamp") + " " + rs.getTime("timestamp"), MySQLConst.DATETIMEPATTERN));
        logRecord.setSite(controller.getSiteById(rs.getInt("site")));
        logRecord.setBusline(controller.getBuslineById(rs.getInt("busline")));
        logRecord.setAddress(rs.getInt("address"));
        logRecord.setMilliseconds(rs.getInt("milliseconds"));
        logRecord.setEventType(rs.getString("type"));
        logRecord.setMessage(rs.getString("message"));
        return logRecord;
    }

}
