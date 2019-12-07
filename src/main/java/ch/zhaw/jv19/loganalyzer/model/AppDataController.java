package ch.zhaw.jv19.loganalyzer.model;

import javafx.beans.property.SimpleStringProperty;

public class AppDataController {
    AppData appData;

    public AppDataController() {
        appData = new AppData();
    }

    public void setMessage(String message) {
        appData.setMessage(message);
    }

    public SimpleStringProperty getMessage() {
        return appData.getMessage();
    }
}
