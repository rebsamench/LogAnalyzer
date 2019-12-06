package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.datatype.StringUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;

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
                        conditionSb.append(MySQLConst.GT);
                        conditionSb.append(convertToString(entry.getValue()));
                        break;
                    case "createdUpTo":
                        conditionSb.append(MySQLConst.LT);
                        conditionSb.append(convertToString(entry.getValue()));
                        break;
                    case "loggedTimestampFrom":
                        conditionSb.append(MySQLConst.GT);
                        conditionSb.append(convertToString(entry.getValue()));
                        break;
                    case "loggedTimestampUpTo":
                        conditionSb.append(MySQLConst.LT);
                        conditionSb.append(convertToString(entry.getValue()));
                        //list types -> IN conditions
                        break;
                    case "createdUser":
                    case "site":
                    case "recordType":
                        conditionSb.append(MySQLConst.IN);
                        conditionSb.append(convertToString(entry.getValue()));
                        break;
                    case "messageSubstring":
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
        return querySb.append(MySQLConst.ENDQUERY).toString();
    }

    public static String connectConditions(ArrayList<String> conditionList) {
        Iterator<String> it = conditionList.iterator();
        StringBuilder conditionSb = new StringBuilder();
        // connect first condition with WHERE
        conditionSb.append(MySQLConst.WHERE);
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
