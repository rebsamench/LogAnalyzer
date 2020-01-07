package ch.zhaw.jv19.loganalyzer.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Wraps a user object in order to provide property fields.
 * This is necessary for editable table views.
 *
 * @autor: Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class UserWrapper {

    private User user;
    private SimpleStringProperty createdUser;
    private SimpleStringProperty name;
    private SimpleStringProperty password;
    private SimpleIntegerProperty isadmin;

    public UserWrapper(User user) {
        this.createdUser = new SimpleStringProperty(user.getCreatedUser());
        this.name = new SimpleStringProperty(user.getName());
        this.password = new SimpleStringProperty(user.getPassword());
        this.isadmin = new SimpleIntegerProperty(user.getIsadmin());
        this.user = user;
    }
    public UserWrapper(final String createdUser, final String name, final String password, final int isadmin) {
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
        user.setCreatedUser(createdUser);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
        user.setName(name);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
        user.setPassword(password);
    }

    public int getIsadmin() {
        return isadmin.get();
    }

    public void setIsadmin(int isadmin) {
        this.isadmin.set(isadmin);
        user.setIsadmin(isadmin);
    }
}
