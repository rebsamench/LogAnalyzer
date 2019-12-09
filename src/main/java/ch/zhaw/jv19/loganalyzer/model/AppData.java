package ch.zhaw.jv19.loganalyzer.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

/**
 * Contains data for loganalyzer app. Instance created and data loaded from storage on app startup.
 * Access data only from AppDataController.
 */
public class AppData {
    private ObservableList<User> userList;
    private ObservableList<Site> siteList;
    private ObservableList<Busline> busLineList;
    private SimpleStringProperty message;

    public AppData() {
        message = new SimpleStringProperty();
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

    public ObservableList<User> getUserList() {
        return userList;
    }

    public void addUser(User user) {
        userList.add(user);
    }
}
