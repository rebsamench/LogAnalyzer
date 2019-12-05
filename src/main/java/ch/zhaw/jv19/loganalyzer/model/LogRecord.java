package ch.zhaw.jv19.loganalyzer.model;

public class LogRecord {

    String dateTime;
    String milliSeconds;
    String eventType;
    String source;
    String message;

    public LogRecord(String dateTime, String milliSeconds, String eventType, String source, String message){
        this.dateTime = dateTime;
        this.milliSeconds = milliSeconds;
        this.eventType = eventType;
        this.source = source;
        this.message = message;
    }

    public String getDateTime() {
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
}
