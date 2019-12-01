package ch.zhaw.javavertriefung.loganalyzer.util.db;

import ch.zhaw.javavertriefung.loganalyzer.util.datatypes.DateUtil;
import ch.zhaw.javavertriefung.loganalyzer.model.SearchCondition;
import javafx.collections.ObservableList;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class QueryBuilder {

    public QueryBuilder() {

    }

    public static String getSQLConditionsFromList(ArrayList<SearchCondition> conditions) {
        Iterator<SearchCondition> it = conditions.iterator();
        StringBuilder querySb = new StringBuilder();
        boolean isFirstCondition = true;
        while (it.hasNext()) {
            SearchCondition entry = it.next();
            if(entry != null) {
                // replace first AND with WHERE
                if (isFirstCondition && entry.getOperator().equals(MySQLKeyword.AND)) {
                    querySb.append(MySQLKeyword.WHERE);
                    isFirstCondition = false;
                } else {
                    querySb.append(entry.getOperator());
                }
                querySb.append(getWhereCondition(entry));
            }
        }
        return querySb.append(MySQLKeyword.ENDQUERY).toString();
    }

    private static String getWhereCondition(SearchCondition condition) {
        StringBuilder conditionString = new StringBuilder();
        conditionString.append(condition.getKey());
        conditionString.append(condition.getComparisonOperator());
        Object valueObject = condition.getValue();
        if (valueObject instanceof ZonedDateTime) {
            conditionString.append(addQuotes.apply(DateUtil.convertDateTimeToString((ZonedDateTime) valueObject,"yyyy-MM-dd hh:mm:ss")));
        } else if (valueObject instanceof ObservableList) {
            if(((ObservableList<ObservableList>) valueObject).size() > 0) {
                conditionString.append(MySQLKeyword.IN);
                conditionString.append(addQuotes.apply(getInConditions((ObservableList<String>) condition.getValue())));
            }
        } else if (valueObject instanceof String) {
            // Wrap in %
            if(condition.getComparisonOperator().equals(MySQLKeyword.LIKE)) {
                valueObject = addPercent.apply((String) valueObject);
            }
            conditionString.append(addQuotes.apply((String) valueObject));
        } else {
            System.out.println("Unerwarteter Typ " + condition.getValue().getClass().getName());
        }
        return conditionString.toString();
    }

    // https://stackoverflow.com/questions/18227938/java-append-quotes-to-strings-in-an-array-and-join-strings-in-an-array
    private static Function<String, String> addQuotes = string -> "'" + string + "'";

    private static Function<String, String> addBrackets = string -> "(" + string + ")";

    private static Function<String, String> addPercent = string -> "%" + string + "%";

    private static String getInConditions(ObservableList<String> conditionList) {
        return addBrackets.apply(conditionList.stream()
                .map(addQuotes)
                .collect(Collectors.joining(MySQLKeyword.SEPARATOR)).toString());
    }
}
