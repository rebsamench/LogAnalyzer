package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.User;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.SQLException;
import java.util.HashMap;

public interface LogRecordReadDAO {
    TableView<ObservableList> getLogRecordsTableByConditions(HashMap<String, Object> searchConditions);
    String getCurrentQuery();
}
