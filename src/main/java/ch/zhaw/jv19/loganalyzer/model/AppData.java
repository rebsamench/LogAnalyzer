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
    private final SimpleStringProperty message;
    private static AppData instance;

    //Singleton: AppData can only be instantiated once
    private AppData () {
        message = new SimpleStringProperty();
    }

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

    public void addUser(User user) {
        userList.add(user);
    }
}
