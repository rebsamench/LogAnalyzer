package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;

import java.time.ZonedDateTime;

public class LogRecord {
    private int id;
    private ZonedDateTime created;
    private ZonedDateTime lastchanged;
    private User user;
    private String uniqueIdentifier;
    private ZonedDateTime timestamp;
    private Site site;
    private Busline busline;
    private int address;
    private int milliseconds;
    private String eventType;
    private String source;
    private String message;

    public LogRecord(){};

    public LogRecord(String timestamp, int milliseconds, String eventType, String source, String message, User user, Site site, Busline busline) {
        this.timestamp = DateUtil.getZonedDateTimeFromDateTimeStringAlt(timestamp, MySQLConst.LOCALDATETIMEPATTERN);
        this.milliseconds = milliseconds;
        this.eventType = eventType;
        this.source = source;
        this.message = message;
        this.user = user;
        this.site = site;
        this.busline = busline;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public void setLastchanged(ZonedDateTime lastchanged) {
        this.lastchanged = lastchanged;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public int getMilliseconds() {
        return milliseconds;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Site getSite() {
        return site;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
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

    public void setSite(Site site) {
        this.site = site;
    }

    public Busline getBusline() {
        return busline;
    }

    public void setBusline(Busline busline) {
        this.busline = busline;
    }

    public int getId() {
        return id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getLastchanged() {
        return lastchanged;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public int getAddress() {
        return address;
    }
}