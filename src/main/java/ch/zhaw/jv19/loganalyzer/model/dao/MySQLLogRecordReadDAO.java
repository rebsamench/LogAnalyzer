package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.datatype.StringUtil;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

public class MySQLLogRecordReadDAO implements LogRecordReadDAO {
    private String currentQuery;

    @Override
    public TableView<ObservableList> getLogRecordsTableByConditions(HashMap<String, Object> searchConditions) {
        TableView<ObservableList> tableView;
        setCurrentLogRecordQuery(searchConditions);
        tableView = DBUtil.getQueryResultAsTable(currentQuery);
        return tableView;
    }

    @Override
    public String getCurrentQuery() {
        return currentQuery;
    }

    private void setCurrentLogRecordQuery(HashMap<String, Object> searchConditions) {
        StringBuilder querySb = new StringBuilder();
        querySb.append("SELECT * FROM logrecord");
        if(searchConditions.size() > 0) {
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
                conditionSb.append(mapKey(entry.getKey()));
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
                        conditionSb.append(MySQLConst.IN);
                        conditionSb.append(getInConditions((ArrayList<String>) entry.getValue()));
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

    private String mapKey(String key){
        switch (key) {
            case "createdFrom":
            case "createdUpTo": {
                return "created";
            }
            case "loggedTimestampFrom":
            case "loggedTimestampUpTo": {
                return "timestamp";
            }
            default: return key;
        }
    }

    private String convertToString(Object object) {
        String condition = null;
        if (object instanceof ZonedDateTime) {
            condition = DateUtil.convertDateTimeToString((ZonedDateTime) object, MySQLConst.DATETIMEPATTERN);
        } else if (object instanceof ArrayList) {
            ArrayList<String> inConditionList = (ArrayList<String>) object;
            if (inConditionList.size() > 0) {
                condition = StringUtil.addQuotes.apply(getInConditions(inConditionList));
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
        return StringUtil.addBrackets.apply(conditionList.stream()
                .map(StringUtil.addQuotes)
                .collect(Collectors.joining(MySQLConst.SEPARATOR)));
    }

}
