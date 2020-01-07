package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.properties.ImportFileConst;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;

import java.time.ZonedDateTime;

public class LogRecord {
    private int id;
    private ZonedDateTime created;
    private ZonedDateTime lastChanged;
    private User user;
    private String uniqueIdentifier;
    private ZonedDateTime timestamp;
    private Site site;
    private Busline busline;
    private int address;
    private int milliseconds;
    private EventType eventType;
    private Source source;
    private String message;

    /**
     * Holds all the data for a single logRecord and provides getter and setter methods.
     *
     * @autor: Christoph Rebsamen, rebsach1@students.zhaw.ch
     */
    public LogRecord(){}

    public LogRecord(String timestamp, int milliseconds, String eventType, String source, String message, User user, Site site, Busline busline) {
        this.timestamp = DateUtil.getZonedDateTimeFromDateTimeString(timestamp, ImportFileConst.DATETIMEPATTERNIMPORT);
        this.milliseconds = milliseconds;
        this.eventType = EventType.get(eventType);
        this.source = Source.get(source);
        this.message = message;
        this.user = user;
        this.site = site;
        this.busline = busline;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public String getEventType() {
        return eventType.toString();
    }

    public String getSource() {
        return source.toString();
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public Site getSite() {
        return site;
    }

    public Busline getBusline() {
        return busline;
    }

    public int getId() {
        return id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getLastChanged() {
        return lastChanged;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    /**
     * Creates a unique identifier for each logRecord.
     *
     * @param address : bus address. Is unique in a busline.
     */
    public void setUniqueIdentifier(int address) {
        this.uniqueIdentifier = (site.getId() +
                busline.getId() +
                Integer.toString(address) +
                DateUtil.convertDateTimeToString(timestamp, MySQLConst.DATETIMEPATTERN) +
                milliseconds).replaceAll("[^A-Za-z0-9]","");
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLastChanged(ZonedDateTime lastChanged) {
        this.lastChanged = lastChanged;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }

    public void setEventType(String eventType) throws Exception {
        if(EventType.get(eventType) != null) {
            this.eventType = EventType.get(eventType);
        } else {
            throw new Exception("Event type '" + source + "' unknown in EventType enum. Extend EventType enum.");
        }

        this.eventType = EventType.get(eventType);
    }

    public void setSource(String source) throws Exception {
        if(Source.get(source) != null) {
            this.source = Source.get(source);
        } else {
            throw new Exception("Source '" + source + "' unknown in Source enum. Extend source enum.");
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public void setBusline(Busline busline) {
        this.busline = busline;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return (getEventType() + ": " + getMessage());
    }

    /**
     * Overrides comparison of log records. Needed e. g. to find and select log record in table.
     * @param o log record
     * @return true, if unique_identifier is equal
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof LogRecord) {
            //id comparison
            LogRecord logRecord = (LogRecord) o;
            return (logRecord.uniqueIdentifier.equals(uniqueIdentifier));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(uniqueIdentifier);
    }
}
