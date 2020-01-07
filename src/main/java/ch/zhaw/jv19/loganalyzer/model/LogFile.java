package ch.zhaw.jv19.loganalyzer.model;

import java.util.ArrayList;
import java.util.List;

public class LogFile {

    private List<LogRecord> recordlist;
    private int address;
    private boolean addressSet = false;

    public LogFile() {
        recordlist = new ArrayList<>();
    }
    
    public void addLogRecord(LogRecord logRecord) {
        recordlist.add(logRecord);
    }

    public List<LogRecord> getRecordList() {
        return recordlist;
    }

    public int getAddress() {
        return address;
    }

    public boolean isAddressSet() {
        return addressSet;
    }

    public void setAddress(String message) {
        this.address = Integer.parseInt(message.substring(34, 37).replace(',', ' ').trim());
        this.addressSet = true;
    }

}

