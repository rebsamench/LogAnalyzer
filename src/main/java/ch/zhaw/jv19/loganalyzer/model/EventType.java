package ch.zhaw.jv19.loganalyzer.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enum of valid values for log record types
 */
public enum EventType {
    INFO("Info"),
    EVENT("Event"),
    WARNING("Warning");

    private String eventType;
    private static final Map<String, EventType> lookup = new HashMap<>();

    EventType(String eventType) {
        this.eventType = eventType;
    }

    String getEventType() {
        return eventType;
    }

    // https://howtodoinjava.com/java/enum/java-enum-string-example/
    //Populate the lookup table on loading time
    static {
        for (EventType eventType : EventType.values()) {
            lookup.put(eventType.getEventType().toUpperCase(), eventType);
        }
    }

    public static EventType get(String eventType) {
        return lookup.get(eventType.toUpperCase());
    }

    /**
     * Gets list of record types defined in enum as strings.
     *
     * @return ObservableList of record types
     */
    public static ObservableList<EventType> getEventTypeList() {
        ObservableList<EventType> recordTypeList = FXCollections.observableArrayList();
        ArrayList<EventType> itemList = Stream.of(EventType.values())
                .collect(Collectors.toCollection(ArrayList::new));
        recordTypeList.addAll(itemList);
        return recordTypeList;
    }

    @Override
    public String toString() {
        return getEventType();
    }
}
