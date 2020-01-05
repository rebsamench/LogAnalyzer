package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.LogRecord;
import java.util.ArrayList;
import java.util.TreeMap;

public interface LogRecordReadDAO {
    ArrayList<LogRecord> getLogRecordsListByConditions(TreeMap<String, Object> searchConditions);
    String getCurrentQuery();
}
