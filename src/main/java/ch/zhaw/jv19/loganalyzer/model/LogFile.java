package ch.zhaw.jv19.loganalyzer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Hols all the logRecords for a single field device.
 * A field device has an address that is unique within a busline. Therefore it is absolutely necessary
 * to have such a data structure in order to set the address for each logRecord.
 *
 * @author: Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class LogFile {

    private final List<LogRecord> recordlist;
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

    /**
     * Indicates if an address for a LogFile has been set, or not.
     *
     * @return : Returns true if an address for a logfile is set.
     */
    public boolean isAddressSet() {
        return addressSet;
    }

    public void setAddress(String message) {
        this.address = Integer.parseInt(message.substring(34, 37).replace(',', ' ').trim());
        this.addressSet = true;
    }

}

