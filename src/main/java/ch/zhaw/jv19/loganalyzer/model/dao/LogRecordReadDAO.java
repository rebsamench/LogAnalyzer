package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.LogRecord;
import java.util.ArrayList;
import java.util.HashMap;

public interface LogRecordReadDAO {
    ArrayList<LogRecord> getLogRecordsListByConditions(HashMap<String, Object> searchConditions);
    String getCurrentQuery();
}
