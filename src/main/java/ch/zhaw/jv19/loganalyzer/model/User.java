package ch.zhaw.jv19.loganalyzer.model;

import java.time.ZonedDateTime;

public class User {
    int id;
    ZonedDateTime created;
    String createdUser;
    String name;
    String password;
    int isadmin;

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

}
