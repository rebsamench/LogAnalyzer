package ch.zhaw.jv19.loganalyzer.model;

import java.util.ArrayList;
import java.util.List;

public class LogFile {

    private List<LogRecord> recordlist;
    private int address;

    public LogFile() {
        recordlist = new ArrayList<>();
    }

    public void addLogRecord(LogRecord logRecord) {
        recordlist.add(logRecord);
    }

    public List<LogRecord> getRecordList() {
        return recordlist;
    }

    private void createUniqueIdentifier() {

    }

    public void enrichUniqueIdentifier() {

    }

    private void findAddress() {

    }

    public void enrichAddress() {

    }
}

