package ch.zhaw.jv19.loganalyzer.model;

import java.time.ZonedDateTime;

public class Busline {
    int id;
    ZonedDateTime created;
    String createdUser;
    String name;
    String bustype;

    public String getName() {
        return name;
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

    public void setCreatedUser(String createduser) {
    }

    public void setId(int id) {
    }

    public void setCreated(ZonedDateTime created) {
    }

    public void setName(String name) {
    }

    public void setBustype(String bustype) {
        this.bustype = bustype;
    }
}
