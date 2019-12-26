package ch.zhaw.jv19.loganalyzer.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BaseDataUserTableData {

    private SimpleStringProperty createdUser;
    private SimpleStringProperty name;
    private SimpleStringProperty password;
    private SimpleIntegerProperty isadmin;

    // added to create the model from the Person object, which might be data retrieved from a database
    public BaseDataUserTableData(User user) {
        this.createdUser = new SimpleStringProperty(user.getCreatedUser());
        this.name = new SimpleStringProperty(user.getName());
        this.password = new SimpleStringProperty(user.getPassword());
        this.isadmin = new SimpleIntegerProperty(user.getIsadmin());
    }
    public BaseDataUserTableData(final String createdUser, final String name, final String password, final int isadmin) {
        this.createdUser = new SimpleStringProperty(createdUser);
        this.name = new SimpleStringProperty(name);
        this.password = new SimpleStringProperty(password);
        this.isadmin = new SimpleIntegerProperty(isadmin);
    }

    public String getCreatedUser() {
        return createdUser.get();
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser.set(createdUser);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public int getIsadmin() {
        return isadmin.get();
    }

    public void setIsadmin(int isadmin) {
        this.isadmin.set(isadmin);
    }

}
