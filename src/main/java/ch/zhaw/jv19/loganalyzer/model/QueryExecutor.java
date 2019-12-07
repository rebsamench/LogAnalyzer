package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.HashMap;

public class QueryExecutor {
    public static TableView<ObservableList> getLogRecordsTable(HashMap<String, Object> searchConditions) {
        TableView<ObservableList> tableView;
        ArrayList<String> conditionList = LogRecordQueryBuilder.getConditionListFromMap(searchConditions);
        tableView = DBUtil.getQueryResultAsTable(LogRecordQueryBuilder.buildLogRecordQuery(conditionList));
        return tableView;
    }

    public static TableView<ObservableList> getQueryResultTable(String query) {
        TableView<ObservableList> tableView;
        tableView = DBUtil.getQueryResultAsTable(query);
        return tableView;
    }
}
