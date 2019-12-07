package ch.zhaw.jv19.loganalyzer.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class AppData {
    private ObservableList<User> userList;
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
}
