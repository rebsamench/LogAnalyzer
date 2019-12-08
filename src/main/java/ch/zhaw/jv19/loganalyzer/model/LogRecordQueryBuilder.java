package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.datatype.StringUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;
import javafx.collections.ObservableList;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

public class LogRecordQueryBuilder {

    public static ArrayList<String> getConditionListFromMap(HashMap<String, Object> conditionsMap) {
        Iterator<HashMap.Entry<String, Object>> entries = conditionsMap.entrySet().iterator();
        ArrayList<String> conditionsList = new ArrayList<>();
        while (entries.hasNext()) {
            HashMap.Entry<String, Object> entry = entries.next();
            if (entry.getValue() != null) {
                StringBuilder conditionSb = new StringBuilder();
                conditionSb.append(entry.getKey());
                switch (entry.getKey()) {
                    case "createdFrom":
                    case "loggedTimestampFrom":
                        conditionSb.append(MySQLConst.GT);
                        conditionSb.append(convertToString(entry.getValue()));
                        break;
                    case "createdUpTo":
                    case "loggedTimestampUpTo":
                        conditionSb.append(MySQLConst.LT);
                        conditionSb.append(convertToString(entry.getValue()));
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

    private static String convertToString(Object object) {
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

    public static String buildLogRecordQuery(ArrayList conditionList) {
        StringBuilder querySb = new StringBuilder();
        querySb.append("SELECT * FROM logrecord");
        if(conditionList.size() > 0) {
            querySb.append(connectConditions(conditionList));
        }
        querySb.append(MySQLConst.ENDQUERY);
        System.out.println(querySb.toString());
        return querySb.toString();
    }

    public static String connectConditions(ArrayList<String> conditionList) {
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

    private static String getInConditions(ArrayList<String> conditionList) {
        return StringUtil.addBrackets.apply(conditionList.stream()
                .map(StringUtil.addQuotes)
                .collect(Collectors.joining(MySQLConst.SEPARATOR)));
    }

}
