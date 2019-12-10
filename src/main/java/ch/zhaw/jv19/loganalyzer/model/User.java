package ch.zhaw.jv19.loganalyzer.model;

import java.time.ZonedDateTime;

public class User {
    private int id;
    private ZonedDateTime created;
    private String createdUser;
    private String name;
    private String password;
    private int isadmin;

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
}
