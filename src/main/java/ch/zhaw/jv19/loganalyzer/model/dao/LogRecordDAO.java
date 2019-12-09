package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.LogRecord;
import ch.zhaw.jv19.loganalyzer.model.User;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.SQLException;

public interface LogRecordDAO {
    ObservableList<LogRecord> getAllLogRecordsList() throws SQLException;
    TableView<ObservableList> getAllLogRecordsTable();
    void saveLogRecord(LogRecord logRecord);
    void updateLogRecord(LogRecord logRecord);
}