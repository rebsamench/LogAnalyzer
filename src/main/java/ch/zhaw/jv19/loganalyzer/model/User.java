package ch.zhaw.jv19.loganalyzer.model;

import java.time.ZonedDateTime;

/**
 * Holds all the data for a single user and provides getter and setter methods to alter data.
 *
 * @author: Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class User {
    private int id;
    private ZonedDateTime created;
    private String createdUser;
    private String name;
    private String password;
    private int isadmin;

    public User() {}

    public User(String createdUser, String name, String password, int isadmin) {
        this.createdUser = createdUser;
        this.name = name;
        this.password = password;
        this.isadmin = isadmin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsadmin(int isadmin) {
        this.isadmin = isadmin;
    }

    public int getId() {
        return id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getIsadmin() {
        return isadmin;
    }

    @Override
    public String toString() {
        return (getName());
    }

    public int hashCode() {
        int ergebnis = 17;
        ergebnis = 37 * ergebnis + name.hashCode();
        ergebnis = 37 * ergebnis + password.hashCode();
        return ergebnis;
    }

    public boolean equals(Object object) {
        if(this == object) {
            return true;
        }

        if(!(object instanceof User)) {
            return false;
        }

        User anderer = (User) object;
        return name.equals(anderer.name) && password == anderer.password;
    }

}
