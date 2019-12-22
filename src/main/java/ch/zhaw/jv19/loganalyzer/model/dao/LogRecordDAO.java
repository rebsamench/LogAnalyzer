package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.LogRecord;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.SQLException;

public interface LogRecordDAO {
    ObservableList<LogRecord> getAllLogRecordsList() throws SQLException;
    TableView<LogRecord> getAllLogRecordsTable();
    void saveLogRecord(LogRecord logRecord);
    void updateLogRecord(LogRecord logRecord);
}