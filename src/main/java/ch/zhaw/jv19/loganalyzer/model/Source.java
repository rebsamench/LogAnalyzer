package ch.zhaw.jv19.loganalyzer.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enum of valid values for log record sources
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public enum Source {
    INPUT("Input"),
    CONTROLLER("Controller"),
    MODBUS_APP("Modbus App"),
    DEVICE_CONFIGURATION("DeviceConfiguration"),
    IRRELEVANT("Irrelevant");

    private String source;
    private static final Map<String, Source> lookup = new HashMap<>();

    Source(String source) {
        this.source = source;
    }

    String getSource() {
        return source;
    }

    // https://howtodoinjava.com/java/enum/java-enum-string-example/
    //Populate the lookup table on loading time
    static {
        for (Source source : Source.values()) {
            lookup.put(source.getSource().toUpperCase(), source);
        }
    }

    public static Source get(String source) {
        Source identifiedSource = lookup.get(source.toUpperCase());
        if(identifiedSource == null) {
            identifiedSource = lookup.get("IRRELEVANT");
        }
        return identifiedSource;
    }

    /**
     * Gets list of record types defined in enum as strings.
     * @return ObservableList of record types
     */
    public static ObservableList<Source> getSourceList() {
        ObservableList<Source> sourceList = FXCollections.observableArrayList();
        ArrayList<Source> itemList = Stream.of(Source.values())
                .collect(Collectors.toCollection(ArrayList::new));
        sourceList.addAll(itemList);
        return sourceList;
    }

    @Override
    public String toString() {
        return getSource();
    }
}
