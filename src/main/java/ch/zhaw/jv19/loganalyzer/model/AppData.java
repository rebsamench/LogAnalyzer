package ch.zhaw.jv19.loganalyzer.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides base data for loganalyzer app. Instance created (singleton) and data loaded from storage on app startup.
 * Access data only from {@link AppDataController}.
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public class AppData {
    private ObservableList<User> userList;
    private ObservableList<Site> siteList;
    private ObservableList<Busline> busLineList;
    private final SimpleStringProperty message;
    private static AppData instance;

    //Singleton: AppData can only be instantiated once
    private AppData () {
        message = new SimpleStringProperty();
    }

    /**
     * Enum of valid values for log record types
     */
    public enum RecordType {
        INFO("Info"),
        EVENT("Event"),
        WARNING("Warning");

        private String recordType;

        RecordType(String recordType) {
            this.recordType = recordType;
        }

        public String getRecordType() {
            return recordType;
        }
    }

    /**
     * Gets instance of AppData. AppData is a Singleton.
     * @return Singleton instance of AppData
     */
    public static AppData getInstance () {
        if (AppData.instance == null) {
            AppData.instance = new AppData();
        }
        return AppData.instance;
    }

    public SimpleStringProperty getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message.setValue(message);
    }

    public void setUserList(ObservableList<User> userList) {
        this.userList = userList;
    }

    public void setSiteList(ObservableList<Site> siteList) {
        this.siteList = siteList;
    }

    public void setBusLineList(ObservableList<Busline> busLineList) {
        this.busLineList = busLineList;
    }

    public ObservableList<User> getUserList() {
        return userList;
    }

    public ObservableList<Busline> getBusLineList() {
        return busLineList;
    }

    public ObservableList<Site> getSiteList() {
        return siteList;
    }

    /**
     * Getsa list of record types defined in enum as strings.
     * @return ObservableList of record types
     */
    public ObservableList<String> getRecordTypeList() {
        ObservableList<String> recordTypeList = FXCollections.observableArrayList();
        ArrayList<String> itemList = Stream.of(RecordType.values())
                .map(RecordType::getRecordType)
                .collect(Collectors.toCollection(ArrayList::new));
        recordTypeList.addAll(itemList);
        return recordTypeList;
    }

    public void addUser(User user) {
        userList.add(user);
    }
}
