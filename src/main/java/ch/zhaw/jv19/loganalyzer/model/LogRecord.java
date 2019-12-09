package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;

import java.time.ZonedDateTime;

public class LogRecord {

    ZonedDateTime dateTime;
    String milliSeconds;
    String eventType;
    String source;
    String message;
    User user;
    Site site;
    Busline busline;

    public LogRecord(){}

    public LogRecord(String dateTime, String milliSeconds, String eventType, String source, String message, User user, Site site, Busline busline){
        this.dateTime = DateUtil.getZonedDateTimeFromDateTimeString(dateTime, MySQLConst.DATETIMEPATTERN);
        this.milliSeconds = milliSeconds;
        this.eventType = eventType;
        this.source = source;
        this.message = message;
        this.user = user;
        this.site = site;
        this.busline = busline;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public String getMilliSeconds() {
        return milliSeconds;
    }

    public String getEventType() {
        return eventType;
    }

    public String getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setMilliSeconds(String milliSeconds) {
        this.milliSeconds = milliSeconds;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
