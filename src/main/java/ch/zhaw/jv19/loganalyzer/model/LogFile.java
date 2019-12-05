package ch.zhaw.jv19.loganalyzer.model;

import java.util.ArrayList;
import java.util.List;

public class LogFile {

    private List<LogRecord> recordList;

    public LogFile() {
        recordList = new ArrayList<>();
    }

    public void creatRecordList(LogRecord logRecord) {
        recordList.add(logRecord);
    }

    public List getLogfile() {
        return recordList;
    }
}
