package ch.zhaw.jv19.loganalyzer.model;

import java.time.ZonedDateTime;

public class Busline {
    int id;
    ZonedDateTime created;
    String createdUser;
    String name;
    String bustype;

    public Busline() {}

    public Busline(String createdUser, String name, String bustype) {
        this.createdUser = createdUser;
        this.name = name;
        this.bustype = bustype;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getBustype() {
        return bustype;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    @Override
    public String toString() {
        return (getName() + ", " +
                getBustype());
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

    public void setBustype(String bustype) {
        this.bustype = bustype;
    }
}
